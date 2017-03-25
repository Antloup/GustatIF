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
import java.util.Arrays;
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
     * @return Si l'email n'existe pas dans la base : return null Sinon : return
     * l'objet Client
     */
    public Client connectClient(String adresse) {
        ClientDAO cdao = new ClientDAO();
        JpaUtil.creerEntityManager();
        Client c = cdao.findByEmail(adresse);
        JpaUtil.fermerEntityManager();
        return c;
    }
    
    /**
     *
     * @param adresse : Adresse Email du livreur
     * @return Si l'email n'existe pas dans la base : return null Sinon : return
     * l'objet Livreur
     */
    public Livreur connectLivreur(String adresse) {
        LivreurDAO ldao = new LivreurDAO();
        JpaUtil.creerEntityManager();
        Livreur l = ldao.findByEmail(adresse);
        JpaUtil.fermerEntityManager();
        return l;
    }

    /**
     * Créer la commande du client
     *
     * @param hm : HashMap : Produit / quantité de produit
     * @param c : Objet Client
     * @param r : Objet Restaurant
     * @return La commande créer
     */
    public Commande submitCommande(HashMap<Produit, Integer> hm, Client c, Restaurant r) throws Exception {
        double poids = getPoids(hm);
        double duree_min = Double.MAX_VALUE;
        Livreur selectLivreur = ServiceTechnique.selectionnerLivreur(c, r, poids, duree_min);
        Commande commande = null;
        CommandeDAO cdao = new CommandeDAO();
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        LivreurDAO ldao = new LivreurDAO();
        commande = cdao.createCommande(hm, c, r, selectLivreur, 6);
        ldao.addCommande(commande, selectLivreur);
        if(selectLivreur == null){
            cdao.setEtat(commande, CommandeDAO.Etat.ANNULE);
        }
        else{
            cdao.setEtat(commande, CommandeDAO.Etat.EN_ATTENTE);
        }
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
        return commande;

    }

    /**
     * 
     * @param hm : HashMap< Produit,Integer > : Produits de la commande et leurs quantités
     * @return Poids total de la commande
     */
    public double getPoids(HashMap<Produit, Integer> hm) {
        double poids = 0.0;
        for (Produit key : hm.keySet()) {
            poids += key.getPoids() * hm.get(key);
        }
        return poids;
    }

    /**
     * Permet d'inscrire un utilisateur et de controler si email disponible
     *
     * @return null si inscription échoue, le nouveau client si inscription est
     * un succès
     */
    public Client submitSubscription(String nom, String prenom, String mail, String adresse) throws Exception {
        ClientDAO cdao = new ClientDAO();
        if (!cdao.isTaken(mail)) {
            LatLng latlng = GeoTest.getLatLng(adresse);
            if(latlng != null){
                JpaUtil.creerEntityManager();
                JpaUtil.ouvrirTransaction();
                Client newclient = cdao.createClient(nom, prenom, mail, adresse, latlng);
                JpaUtil.validerTransaction();
                JpaUtil.fermerEntityManager();
                return newclient;
            }
            return null;
        } else {
            return null;
        }

    }

    /**
     * Retourne liste de restaurant
     *
     * @return liste de restaurant
     * @throws Exception si operation echoue
     */
    public List<Restaurant> getRestaurantsList() throws Exception {
        RestaurantDAO rdao = new RestaurantDAO();
        JpaUtil.creerEntityManager();
        List<Restaurant> restolist = rdao.findAll();
        JpaUtil.fermerEntityManager();
        return restolist;
    }

    /**
     * 
     * @return liste des livreurs
     * @throws Exception si operation echoue
     */
    public List<Livreur> getLivreurList() throws Exception {
        LivreurDAO ldao = new LivreurDAO();
        JpaUtil.creerEntityManager();
        List<Livreur> livreurlist = ldao.findAll();
        JpaUtil.fermerEntityManager();
        return livreurlist;
    }
    
    /**
     * 
     * @return liste des clients
     * @throws Exception si operation echoue
     */
    public List<Client> getClientsList() throws Exception {
        ClientDAO cdao = new ClientDAO();
        JpaUtil.creerEntityManager();
        List<Client> clientlist = cdao.findAll();
        JpaUtil.fermerEntityManager();
        return clientlist;
    }
    
    /**
     * 
     * @param id : Id du livreur a récupérer
     * @return : Objet Livreur (null si aucun livreur trouvé)
     * @throws Exception si operation echoue
     */
    public Livreur getLivreurById(Long id) throws Exception {
        LivreurDAO ldao = new LivreurDAO();
        JpaUtil.creerEntityManager();
        Livreur livreur = ldao.findById(id);
        JpaUtil.fermerEntityManager();
        return livreur;
    }
    
    /**
     * 
     * @param id : Id du client a récupérer
     * @return : Objet Client (null si aucun client trouvé)
     * @throws Exception si operation echoue
     */
    public Client getClientById(Long id) throws Exception {
        ClientDAO cdao = new ClientDAO();
        JpaUtil.creerEntityManager();
        Client client = cdao.findById(id);
        JpaUtil.fermerEntityManager();
        return client;
    }

    /**
     * 
     * @return Liste des livreurs disponible
     */
    public static List<Livreur> getAvailableLivreur() {
        LivreurDAO ldao = new LivreurDAO();
        JpaUtil.creerEntityManager();
        List<Livreur> livreurlist = ldao.findByStatut(0); // Livreur libre
        JpaUtil.fermerEntityManager();
        return livreurlist;
    }

    /**
     * 
     * @param id : Id de la commande a récupérer
     * @return : Objet Commande (null si aucune commande trouvé)
     * @throws Exception si operation echoue
     */
    public Commande getCommande(long id) throws Exception {
        JpaUtil.creerEntityManager();
        CommandeDAO cdao = new CommandeDAO();
        Commande c = cdao.findById(id);
        JpaUtil.fermerEntityManager();
        return c;
    }

    /**
     * 
     * @param id : Id du restaurant a récupérer
     * @return : Objet Restaurant (null si aucun resturant trouvé)
     * @throws Exception si operation echoue
     */
    public Restaurant getRestaurant(long id) throws Exception {
        JpaUtil.creerEntityManager();
        RestaurantDAO rdao = new RestaurantDAO();
        Restaurant r = rdao.findById(id);
        JpaUtil.fermerEntityManager();
        return r;
    }

    /**
     * 
     * @param c : commande a confirmer : le livreur sera affecter a cette commande
     * @return la commande confirmé
     * @throws Exception si operation echoue
     */
    public Commande confirmCommande(Commande c) throws Exception {
        CommandeDAO cdao = new CommandeDAO();
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        Commande commande = cdao.findById(c.getId());
        cdao.setEtat(c, CommandeDAO.Etat.EN_COURS);
        LivreurDAO ldao = new LivreurDAO();
        ldao.setStatus(c.getLivreur(), 1);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
        return commande;
    }

    /**
     * 
     * @param c : Commande a terminer
     * @throws Exception si operation echoue
     */
    public void termineCommande(Commande c) throws Exception {
        CommandeDAO cdao = new CommandeDAO();
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        Commande commande = cdao.findById(c.getId());
        LivreurDAO ldao = new LivreurDAO();
        ldao.setStatus(commande.getLivreur(), 0);
        ldao.removeCommande(commande); // A tester
        cdao.setDateReception(c, new Date());
        cdao.setEtat(c, CommandeDAO.Etat.TERMINE);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }

    /**
     * 
     * @param c : Commande a annuler
     * @throws Exception si operation echoue
     */
    public void annuleCommande(Commande c) throws Exception {
        CommandeDAO cdao = new CommandeDAO();
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        cdao.setEtat(c, CommandeDAO.Etat.ANNULE);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }

    public List<Commande> getLivraisonsEnCours() throws Exception {
        JpaUtil.creerEntityManager();
        CommandeDAO cdao = new CommandeDAO();
        List<Commande> lc = cdao.findByEtat(CommandeDAO.Etat.EN_COURS.ordinal());
        JpaUtil.fermerEntityManager();
        return lc;
    }

    /**
     * 
     * @param lc : commande
     * @return le prix total de la commande
     */
    public static double getPrixTot(Commande lc) {
        double prix = 0.0;
        for (Produit key : lc.getListeProduit().keySet()) {
            prix += key.getPrix() * lc.getListeProduit().get(key);
        }
        return prix;
    }

    private Employe createEmploye(String nom, String prenom, String email, String adresse, double max_transport) {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        Employe e = new Employe(nom, prenom, email, adresse, max_transport);
        LivreurDAO ldao = new LivreurDAO();
        ldao.createEmploye(e);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
        return e;
    }

    private Drone createDrone(String numero, String adresse, double max_transport) {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        Drone d = new Drone(numero, adresse, max_transport);
        LivreurDAO ldao = new LivreurDAO();
        ldao.createDrone(d);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
        return d;
    }
    
//    private Restaurant createResto( String denomination ,String description, String adresse){
//       JpaUtil.creerEntityManager();
//        JpaUtil.ouvrirTransaction();
//        Restaurant r = new Restaurant(String denomination, String description, String adresse);
//        RestaurantDAO rdao = new RestaurantDAO();
//        Restaurant.createRestaurant(r);
//        JpaUtil.validerTransaction();
//        JpaUtil.fermerEntityManager();
//    
//    
//    }

    /**
     * Initialise les données des livreurs
     */
    public void init() {
        List<String> places = Arrays.asList("Lyon Boxe, 215 Rue Paul Bert, 69003 Lyon", "325 Rue Paul Bert, 69003 Lyon",
                "11 Rue Lafontaine 69100 Villeurbanne ", "30 Rue de la Cité 69003 Villeurbanne",
                "1 Rue des Dahlias, 69003 Lyon", "3 Rue Fiol, 69003 Lyon-3E-Arrondissement",
                "6 Rue de la Ruche, 69003 Lyon-3E-Arrondissement","6 Rue de la Métallurgie, 69003 Lyon-3E-Arrondissement",
                "30 Rue Paul Verlaine, 69100 Villeurbanne","96 Rue des Charmettes, 69006 Lyon",
                "2 Rue Sully Prudhomme 69100 Villeurbanne", "42 Rue Clément Michut, 69100 Villeurbanne",
                "39 Rue Dr Ollier, 69100 Villeurbanne","27 Rue Paul Lafargue, 69100 Villeurbanne",
                "233 Cours Emile Zola, 69100 Villeurbanne","5 Rue Pierre Loti, 69100 Villeurbanne",
                "30 Rue Jean-Claude Vivant, 69100 Villeurbanne","18 Petite Rue de la Viabert, 69006 Lyon",
                "8 Rue de la Gaité, 69006 Lyon","7 Rue Dedieu, 69100 Villeurbanne",
                "13 Rue Alexandre Boutin, 69100 Villeurbanne","25 Rue Jean-Claude Vivant, 69100 Villeurbanne",
                "9B Rue Sylvestre, 69100 Villeurbanne","31 Rue d'Inkermann, 69100 Villeurbanne",
                "14 Petite Rue de la Viabert, 69006 Lyon","75 Rue des Charmettes, 69100 Villeurbanne",
                "29 Rue Alexandre Boutin, 69100 Villeurbanne","43 Rue Magenta, 69100 Villeurbanne",
                "43 Rue des Alliés, 69100 Villeurbanne","100 Rue Alexis Perroncel, 69100 Villeurbanne",
                "82 Rue Alexis Perroncel, 69100 Villeurbanne","8 Rue Mauvert, 69100 Villeurbanne",
                "7 Rue Philippe Verzier, 69100 Villeurbanne","3 Rue Viret, 69100 Villeurbanne",
                "83 Rue Edouard Vaillant, 69100 Villeurbanne","19 Rue Raspail, 69100 Villeurbanne",
                "4 Rue Benjamin Constant, 69100 Villeurbanne","57 Rue de Fontanières, 69100 Villeurbanne",
                "40 Rue Colonel Klobb, 69100 Villeurbanne","139a Rue Alexis Perroncel, 69100 Villeurbanne",
                "42 Rue de Fontanières, 69100 Villeurbanne"+"113 Rue Alexis Perroncel, 69100 Villeurbanne",
                "38 Rue des Alliés, 69100 Villeurbanne"+"30 Rue des Alliés, 69100 Villeurbanne",
                "30 Rue des Alliés, 69100 Villeurbanne"+"15 Rue de Fontanières, 69100 Villeurbanne",
                "3 Rue Jean-Pierre Brédy, 69100 Villeurbanne"+"5 Rue Octavie, 69100 Villeurbanne",
                "38 Rue Octavie, 69100 Villeurbanne" +"72 Rue René, 69100 Villeurbanne");
        for (int i = 0; i < 20; i++) {
            if (i % 5 == 0) {
                createDrone("" + i, i+" Cours Emile Zola, Villeurbanne", 1000 * (i + 1) * (2.5));
            } else {
                createEmploye("EmployeNom" + i, "Prenom" + i, i + "@mail.fr", places.get(i), 1000 * (i + 1) * 3);
            }
        }
    }
}
