package util;

import dao.CommandeDAO;
import dao.JpaUtil;
import dao.LivreurDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.modele.Client;
import metier.modele.Commande;
import metier.modele.Drone;
import metier.modele.Employe;
import metier.modele.Livreur;
import metier.modele.Produit;
import metier.modele.Restaurant;
import metier.service.ServiceMetier;
import metier.service.ServiceTechnique;
import static metier.service.ServiceTechnique.sendConfirmInscription;

/**
 *
 * @author DASI Team
 */
public class Saisie {

    public static String lireChaine(String invite) {
        String chaineLue = null;
        System.out.print(invite);
        try {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            chaineLue = br.readLine();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        return chaineLue;

    }

    public static Integer lireInteger(String invite) {
        Integer valeurLue = null;
        while (valeurLue == null) {
            try {
                valeurLue = Integer.parseInt(lireChaine(invite));
            } catch (NumberFormatException ex) {
                System.out.println("/!\\ Erreur de saisie - Nombre entier attendu /!\\");
            }
        }
        return valeurLue;
    }

    public static Integer lireInteger(String invite, List<Integer> valeursPossibles) {
        Integer valeurLue = null;
        while (valeurLue == null) {
            try {
                valeurLue = Integer.parseInt(lireChaine(invite));
            } catch (NumberFormatException ex) {
                System.out.println("/!\\ Erreur de saisie - Nombre entier attendu /!\\");
            }
            if (!(valeursPossibles.contains(valeurLue))) {
                System.out.println("/!\\ Erreur de saisie - Valeur non-autorisée /!\\");
                valeurLue = null;
            }
        }
        return valeurLue;
    }

    public static void pause() {
        lireChaine("--PAUSE--");
    }

    public static void main(String[] args) {
        JpaUtil.init();

        System.out.println("GustatIF textuel !");
        Integer action = -1;
        ServiceMetier sm = new ServiceMetier();
        try {
            if(sm.getLivreurList().isEmpty()){
                sm.init();
            }
        } catch (Exception ex) {
            Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
        }

        Client client = null;

        while (action != 0) {
            afficherMenuConnexion();
            action = Saisie.lireInteger("Votre choix : ", Arrays.asList(0, 1, 2, 3, 4));
            switch (action) {
                case 0:
                    System.out.println("Au revoir !");
                    break;
                case 1: // S'inscrire
                    String nom = Saisie.lireChaine("Nom : ");
                    String prenom = Saisie.lireChaine("Prenom : ");
                    String mail = Saisie.lireChaine("Mail : ");
                    String adresse = Saisie.lireChaine("Adresse : ");
                    Client c = null;
                    try {
                        c = sm.submitSubscription(nom, prenom, mail, adresse);
                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (c != null) {
                        System.out.println("Vous etes inscrit ! Objet Client créer :");
                        System.out.println(c.toString());
                        System.out.println(sendConfirmInscription(c,false));
                    } else {
                        System.out.println("Erreur lors de l'inscription : Email déja utilisée ou Adresse inconnu");
                        System.out.println(sendConfirmInscription(c,true));

                    }
                    break;
                case 2: // Se connecter client
                    String identifiant = Saisie.lireChaine("Adresse : ");
                    client = sm.connectClient(identifiant);
                    if (client == null) {
                        System.out.println("Adresse invalide");
                    } else {
                        System.out.println("Connecte en tant que :");
                        System.out.println(client.toString());
                        menuCommande(client);
                    }
                    break;
                case 3: // Se connecter livreur
                    String mail_livreur = Saisie.lireChaine("Adresse : ");
                    Livreur livreur = sm.connectLivreur(mail_livreur);
                    if (livreur == null) {
                        System.out.println("Adresse invalide");
                    } else {
                        System.out.println("Connecte en tant que :");
                        System.out.println(livreur.toString());
                        menuLivreur(livreur);
                    }
                    break;
                case 4:
                    menuGestionnaire();
                    break;
                    /*
                case 8:
                    try {
                        List<Commande> lc = sm.getLivraisonsEnCours();
                        if (lc != null) {
                            System.out.println("Commande en cours :");
                            for (int i = 0; i < lc.size(); i++) {
                                System.out.println(lc.get(i));
                            }
                        }
                        
                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;

                case 9:
                    int c_id = Saisie.lireInteger("ID de la commande a terminer :");

                    try {
                        Commande commande = sm.getCommande(c_id);
                        sm.termineCommande(commande);
                        
                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;
*/
                default:
                    System.out.println("Wat ?");
            }
        }

        JpaUtil.destroy();
    }

    public static void afficherMenuConnexion() {
        System.out.println("--- MENU CONNEXION ---");
        System.out.println("1 : S'inscrire");
        System.out.println("2 : Se connecter en tant que client");
        System.out.println("3 : Se connecter en tant que livreur");
        System.out.println("4 : Se connecter en tant que gestionnaire");
        System.out.println("0 : Quitter");
    }
    
    public static void afficherMenuCommande(){
        System.out.println("--- MENU COMMANDE ---");
        System.out.println("1 : Voir les restaurants");
        System.out.println("2 : Commander : (Voir produits restaurant par ID)");
        System.out.println("0 : Quitter");
    }
    
   public static void menuCommande(Client client){
        ServiceMetier sm = new ServiceMetier();
        Integer action = -1;
        while (action != 0) {
            afficherMenuCommande();
            action = Saisie.lireInteger("Votre choix : ", Arrays.asList(0, 1, 2));
            switch(action){
                case 1:
                    try {

                        List<Restaurant> lr = sm.getRestaurantsList();
                        for (int i = 0; i < lr.size(); i++) {
                            System.out.println("Restaurant id:" + lr.get(i).getId());
                            System.out.println("Nom:" + lr.get(i).getDenomination());
                            System.out.println("Adresse :" + lr.get(i).getAdresse());
                            System.out.println("Description:" + lr.get(i).getDescription());
                            System.out.println("----------");
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                    
                case 2:
                    int restoID = Saisie.lireInteger("ID du resto : ");
                    Restaurant r = null;
                    List<Produit> lp = null;
                    HashMap<Produit, Integer> hm = new HashMap<Produit, Integer>();
                    try {
                        r = sm.getRestaurant(restoID);
                        lp = r.getProduits();

                        for (int i = 0; i < lp.size(); i++) {
                            System.out.println("Produit ID:" + lp.get(i).getId());
                            System.out.println("Nom:" + lp.get(i).getDenomination());
                            System.out.println("Description:" + lp.get(i).getDescription());
                            System.out.println("Poids :" + lp.get(i).getPoids());
                            System.out.println("Prix :" + lp.get(i).getPrix());
                            int qte = Saisie.lireInteger("Quantité:");
                            if (qte > 0) {
                                hm.put(lp.get(i), qte);
                            }
                            System.out.println("----------");
                        }

                        Commande commande = sm.submitCommande(hm, client, r);
                        if (commande != null) {
                            System.out.println("Commande accepté");
                            System.out.println("Resumé :");
                            System.out.println("Livreur : "+ commande.getLivreur());
                            System.out.println("Durée : "+commande.getDuree());
                            System.out.println("Details commande :");
                            for(Entry<Produit, Integer> e : commande.getListeProduit().entrySet()) {
                                System.out.println("Produit :"+e.getKey());
                                System.out.println("Quantité :"+e.getValue());
                            }
                            System.out.println("Prix total : "+sm.getPrixTot(commande));
                            int confirm = Saisie.lireInteger("1 : Confirmer 2: Annuler :");
                            if(confirm == 1){
                                sm.confirmCommande(commande);
                                
                                if(commande.getLivreur() instanceof Employe ){
                                System.out.println("Email recu par le livreur :");
                                System.out.println(ServiceTechnique.sendEmail(commande.getLivreur()));
                                System.out.println(" Un livreur est en route ");}
                                else{
                                   System.out.println(" Un drone est en route pour votre commande");
                                }
                            }
                            else{
                                sm.annuleCommande(commande);
                            }
                        } else {
                            System.out.println("Erreur dans l'insertion de la commande");
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                
                case 0:
                   break;
            }
           
        }
   }
   
   public static void afficherMenuLivreur(){
       System.out.println("--- MENU LIVREUR ---");
       System.out.println("1 : Voir la commande en cours");
       System.out.println("2 : Valider la commande en cours");
       System.out.println("0 : Quitter");
   }
   public static void menuLivreur(Livreur livreur){
        ServiceMetier sm = new ServiceMetier();
        Integer action = -1;
        while (action != 0) {
            afficherMenuLivreur();
            action = Saisie.lireInteger("Votre choix : ", Arrays.asList(0, 1, 2));
            
            switch(action){
                case 1:
                {
                    Commande commande = livreur.getCommandeEnCours();
                    if(commande != null){
                        System.out.println(commande);
                        System.out.println("Prix tot : "+sm.getPrixTot(commande));
                        System.out.println("Client : " + commande.getClient());
                    }
                    else{
                        System.out.println("Pas de commande en cours");
                    }
                    
                    break;
                }
                case 2:
                {
                    Set<Commande> lc = livreur.getCommandes();
                    Iterator<Commande> iter = lc.iterator();
                    while (iter.hasNext()) {
                        if(iter.next().getEtat() == CommandeDAO.Etat.EN_COURS.ordinal()){
                            try {
                                sm.termineCommande(iter.next());
                                System.out.println("Commande validee");
                            } catch (Exception ex) {
                                Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    break;
                }
                case 0:
                    break;
            }
        }
       
   }
   
   public static void afficherMenuGestionnaire(){
       System.out.println("--- MENU GESTIONNAIRE ---");
       System.out.println("1 : Voir la liste des livreurs + leurs commandes en cours");
       System.out.println("2 : Coordonnees des clients,livreurs,restaurant");
       System.out.println("3 : Voir commande drone");
       System.out.println("4 : Valider commande drone");
       System.out.println("0 : Quitter");
   }
   
   public static void menuGestionnaire(){
       ServiceMetier sm = new ServiceMetier();
        Integer action = -1;
        while (action != 0) {
            afficherMenuGestionnaire();
            action = Saisie.lireInteger("Votre choix : ", Arrays.asList(0, 1, 2,3,4));
            
            switch(action){
                case 1:
                    List<Livreur> ll = null;
                    try {
                        ll = sm.getLivreurList();
                        for(int i=0;i<ll.size();i++){
                            if(ll.get(i) instanceof Employe){
                                Employe e = (Employe)ll.get(i);
                                System.out.println("Livreur : "+ e.getNom() + " "+e.getPrenom());
                            }
                            else if(ll.get(i) instanceof Drone){
                                Drone e = (Drone)ll.get(i);
                                System.out.println("Num livreur : "+ e.getNumero());
                            }
                            System.out.println("Status :"+ll.get(i).getStatus());
                            if(ll.get(i).getCommandeEnCours() != null){
                                System.out.println("Commande en cours : "+ll.get(i).getCommandeEnCours());
                                System.out.println("Client : "+ll.get(i).getCommandeEnCours().getClient());
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 2:
                    System.out.println("Coordonnées client : ");
                    
                    try {
                        List<Client> lc = sm.getClientsList();
                        for(int i =0;i<lc.size();i++){
                            System.out.println(lc.get(i).getNom() + " "+ lc.get(i).getPrenom());
                            System.out.println("Latitude : "+lc.get(i).getLatitude());
                            System.out.println("Longitude : "+lc.get(i).getLongitude());
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    System.out.println("Coordonnées Livreur : ");
                    
                    try {
                        List<Livreur> livreurlist = sm.getLivreurList();
                        for(int i =0;i<livreurlist.size();i++){
                            System.out.println(livreurlist.get(i).getId());
                            System.out.println("Latitude : "+livreurlist.get(i).getLatitude());
                            System.out.println("Longitude : "+livreurlist.get(i).getLongitude());
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    System.out.println("Coordonnées Restaurant : ");
                    
                    try {
                        List<Restaurant> lr = sm.getRestaurantsList();
                        for(int i =0;i<lr.size();i++){
                            System.out.println(lr.get(i).getId());
                            System.out.println("Latitude : "+lr.get(i).getLatitude());
                            System.out.println("Longitude : "+lr.get(i).getLongitude());
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    break;
                case 3:
                    List<Livreur> ld = null;
                    try {
                        ld = sm.getLivreurList();
                        for(int i=0;i<ld.size();i++){
                            if(ld.get(i) instanceof Drone && ld.get(i).getCommandeEnCours() != null){
                                System.out.println("Livreur : "+ ld.get(i));
                                System.out.println("Commande en cours : "+ld.get(i).getCommandeEnCours());
                                System.out.println("Client : "+ld.get(i).getCommandeEnCours().getClient());
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 4:
                    int id_drone = Saisie.lireInteger("ID du drone :");
                    try {
                        Livreur drone = sm.getLivreurById(new Long(id_drone));
                        sm.termineCommande(drone.getCommandeEnCours());
                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                    
                    break;
                case 0:
                    break;
            }
        }
   }
}
