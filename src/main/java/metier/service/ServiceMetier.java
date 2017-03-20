/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import com.google.maps.model.LatLng;
import dao.ClientDAO;
import dao.CommandeDAO;
import dao.JpaUtil;
import dao.LivreurDAO;
import dao.ProduitDAO;
import dao.RestaurantDAO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import metier.modele.Client;
import metier.modele.Commande;
import metier.modele.Drone;
import metier.modele.Employe;
import metier.modele.Livreur;
import metier.modele.Produit;
import metier.modele.Restaurant;
import util.GeoTest;

/**
 *
 * @author Anthony
 */
public class ServiceMetier {
    
    /**
     * 
     * @param adresse : Adresse Email du client
     * @return Si l'email n'existe pas dans la base : return null
     *          Sinon : return l'objet Client 
     */
    public Client connect(String adresse){
        ClientDAO cdao = new ClientDAO();
        JpaUtil.creerEntityManager();
        Client c = cdao.findByEmail(adresse);
        JpaUtil.fermerEntityManager();
        return c;
    }
    
    /**
     * Créer la commande du client
     * @param hm : HashMap Objet Produit / quantité de produit
     * @param c : Objet Client
     * @param r : Objet Restaurant
     * @return La commande créer
     */
    public Commande submitCommande(HashMap<Produit,Integer> hm, Client c, Restaurant r) throws Exception{
        CommandeDAO cdao = new CommandeDAO();
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        List<Livreur> ll = getAvailableLivreur();
        LatLng d_latlng = new LatLng(c.getLatitude(), c.getLongitude());
        double duree_min = -2;
        Livreur selectLivreur = null;
        double poids = getPoids(hm);
        for(int i=0;i<ll.size();i++){
            if(ll.get(i).getMax_transport() <= poids + ll.get(i).getMax_transport()){ // Poids OK
                LatLng s_latlng = new LatLng(ll.get(i).getLatitude(), ll.get(i).getLongitude());
                double duree = -1;
                if(ll.get(i) instanceof Employe){
                    duree = GeoTest.getTripDurationByBicycleInMinute(s_latlng, d_latlng);
                }
                else if(ll.get(i) instanceof Drone){
                    Drone d = (Drone)ll.get(i);
                    duree = GeoTest.getFlightDistanceInKm(s_latlng, s_latlng) / d.getVitesse();
                }
                if(duree != -1 && duree < duree_min ){
                    duree_min = duree;
                    selectLivreur = ll.get(i);
                }
            }
        }
        
        LivreurDAO ldao = new LivreurDAO();
        ldao.setStatus(selectLivreur, 1);
        Commande commande = cdao.createCommande(hm,c,r,selectLivreur,duree_min);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
        return commande;
    }
    
    public double getPoids(HashMap<Produit,Integer> hm){
        double poids = 0.0;
        for (Produit key: hm.keySet()) {
            poids += key.getPoids() * hm.get(key);
        }
        return poids;
    }
    
    /**
     * Permet d'inscrire un utilisateur et de controler si email disponible 
     * @return null si inscription échoue, le nouveau client si inscription est un succès
     */
  public Client submitSubscription(String nom, String prenom,String mail,String adresse) throws Exception{
      ClientDAO cdao = new ClientDAO();
      if(!cdao.isTaken(mail)){
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        LatLng latlng = GeoTest.getLatLng(adresse);
        Client newclient = cdao.createClient(nom, prenom, mail, adresse,latlng);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
        return newclient;
      }
      else{
        return null;
      }
      
  }
    /** 
     * Retourne liste de restaurant
     * @return liste de restaurant
     * @throws Exception si operation echoue 
     */
    public List<Restaurant> getRestaurantsList() throws Exception{
        RestaurantDAO rdao = new RestaurantDAO();
        JpaUtil.creerEntityManager();
        List<Restaurant> restolist = rdao.findAll();
        JpaUtil.fermerEntityManager();
        return restolist;
    }
    
    public List<Livreur> getLivreurList() throws Exception{
        LivreurDAO ldao = new LivreurDAO();
        JpaUtil.creerEntityManager();
        List<Livreur> livreurlist = ldao.findAll();
        JpaUtil.fermerEntityManager();
        return livreurlist;
    }
    
    public List<Livreur> getAvailableLivreur(){
        LivreurDAO ldao = new LivreurDAO();
        JpaUtil.creerEntityManager();
        List<Livreur> livreurlist = ldao.findByStatut(0); // Livreur libre
        JpaUtil.fermerEntityManager();
        return livreurlist;
    }
    
    public Commande getCommande(long id) throws Exception{
        JpaUtil.creerEntityManager();
        CommandeDAO cdao =new CommandeDAO();
        Commande c = cdao.findById(id);
        JpaUtil.fermerEntityManager();
        return c;
    }
    
    public Restaurant getRestaurant(long id) throws Exception{
        JpaUtil.creerEntityManager();
        RestaurantDAO rdao = new RestaurantDAO();
        Restaurant r = rdao.findById(id);
        JpaUtil.fermerEntityManager();
        return r;
    }
    
    public Commande confirmCommande(Commande c) throws Exception{
        CommandeDAO cdao = new CommandeDAO();
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        Commande commande = cdao.findById(c.getId());
        cdao.setEtat(c, CommandeDAO.Etat.EN_COURS);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
        // Appelle d'un service pour assigner livreur ?
        return commande;
    }
    
    public void termineCommande(Commande c) throws Exception{
        CommandeDAO cdao = new CommandeDAO();
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        Commande commande = cdao.findById(c.getId());
        LivreurDAO ldao = new LivreurDAO();
        ldao.setStatus(commande.getLivreur(), 0);
        cdao.setDateReception(c, new Date());
        cdao.setEtat(c, CommandeDAO.Etat.TERMINE);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }
    
    public void annuleCommande(Commande c) throws Exception{
        CommandeDAO cdao = new CommandeDAO();
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        cdao.setEtat(c, CommandeDAO.Etat.ANNULE);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }
    
    public List<Commande> getLivraisonsEnCours() throws Exception{
        CommandeDAO cdao = new CommandeDAO();
        List<Commande> lc = cdao.findByEtat(CommandeDAO.Etat.EN_COURS.ordinal());
        return lc;
    }
    
    public double getPrixTot(Commande lc){
        double prix = 0.0;
        for (Produit key: lc.getListeProduit().keySet()) {
            prix +=key.getPrix() * lc.getListeProduit().get(key);
        }
        return prix;
    }
    
    private Employe createEmploye(String nom,String prenom,String email,String adresse, double max_transport){
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        Employe e = new Employe(nom, prenom, email,adresse,max_transport);
        LivreurDAO ldao = new LivreurDAO();
        ldao.createEmploye(e);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
        return e;
    }
    
    private Drone createDrone(String numero,String adresse,double max_transport){
        JpaUtil.creerEntityManager();
        Drone d = new Drone(numero,adresse,max_transport);
        LivreurDAO ldao = new LivreurDAO();
        ldao.createDrone(d);
        JpaUtil.fermerEntityManager();
        return d;
    }
    
     public void init(){
        for(int i=0; i<50; i++){
            
            if(i%5==0){
                createDrone(""+i,i+" Cours Emile Zola, Villeurbanne",i*(2.5));
            }
            else{
                createEmploye("EmployeNom"+i,"Prenom"+i,i+"@mail.fr",i+" Cours de la République, Lyon",i*3);
            
            }
            
            
        
        }
        
    }

}
