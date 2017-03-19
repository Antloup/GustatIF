/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import dao.ClientDAO;
import dao.CommandeDAO;
import dao.JpaUtil;
import dao.LivreurDAO;
import dao.ProduitDAO;
import dao.RestaurantDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import metier.modele.Client;
import metier.modele.Commande;
import metier.modele.Livreur;
import metier.modele.Produit;
import metier.modele.Restaurant;

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
        return cdao.findByEmail(adresse);
    }
    
    /**
     * Créer la commande du client
     * @param hm : HashMap Objet Produit / quantité de produit
     * @param c : Objet Client
     * @param r : Objet Restaurant
     * @return La commande créer
     */
    public Commande submitCommande(HashMap<Produit,Integer> hm, Client c, Restaurant r){
        CommandeDAO cdao = new CommandeDAO();
        JpaUtil.init(); //Commentaire de Yanis : Il ne faut pas mettre l'init ici, non ?
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        Commande commande = cdao.createCommande(hm,c,r);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
        return commande;
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
        Client newclient = cdao.createClient(nom, prenom, mail, adresse);
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
        List<Restaurant> restolist = rdao.findAll();
        return restolist;
    }
    
    public List<Livreur> getLivreurList() throws Exception{
        LivreurDAO ldao = new LivreurDAO();
        List<Livreur> livreurlist = ldao.findAll();
        return livreurlist;
    }
    
    public Commande getCommande(long id) throws Exception{
        CommandeDAO cdao =new CommandeDAO();
        return cdao.findById(id); 
    } 
    
    
    public Commande confirmCommande(Commande c) throws Exception{
        CommandeDAO cdao = new CommandeDAO();
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        Commande commande = cdao.findById(c.getId());
        commande.setEtat(CommandeDAO.Etat.EN_COURS.ordinal());
        cdao.merge(commande);
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
        commande.setEtat(CommandeDAO.Etat.TERMINE.ordinal());
        cdao.merge(commande);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }
        public void annuleCommande(Commande c) throws Exception{
        CommandeDAO cdao = new CommandeDAO();
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        Commande commande = cdao.findById(c.getId());
        commande.setEtat(CommandeDAO.Etat.ANNULE.ordinal());
        cdao.merge(commande);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }
    
//    public Set<Commande> getCommandeByLivreur(Livreur l) throws Exception{
//        LivreurDAO ldao = new LivreurDAO();
//        Set<Commande> commandes = ldao.findById(l.getId()).getCommandes();
//        return commandes;
//    }
    
    public List<Commande> getLivraisonsEnCours() throws Exception{
        CommandeDAO cdao = new CommandeDAO();
        List<Commande> lc = cdao.findByEtat(CommandeDAO.Etat.EN_COURS.ordinal());
        return lc;
    }
    

}
