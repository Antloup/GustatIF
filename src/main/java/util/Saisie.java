package util;

import dao.JpaUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.modele.Client;
import metier.modele.Commande;
import metier.modele.Produit;
import metier.modele.Restaurant;
import metier.service.ServiceMetier;

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

//    public static void main(String[] args) {
//        
//        System.out.println("Bonjour !");
//        
//        String nom = Saisie.lireChaine("Entrez votre nom: ");
//        System.out.println("Bonjour, " + nom + " !");
//        
//        Integer age = Saisie.lireInteger("Entrez votre âge: ");
//        System.out.println("Vous avez " + age + " ans.");
//        
//        Integer annee = Saisie.lireInteger("Entrez votre année au Département IF (3,4,5): ", Arrays.asList(3,4,5));
//        System.out.println("Vous êtes en " + annee + "IF.");
//
//        Saisie.pause();
//        
//        System.out.println("Au revoir !");
//    }
    
    
    public static void main(String[] args) {
        JpaUtil.init();
        
        System.out.println("GustatIF textuel !");
        Integer action = -1;
        ServiceMetier sm = new ServiceMetier();
        Client client = null;
        
        while(action != 0){
            afficherMenu(client);
            action = Saisie.lireInteger("Votre choix : ",Arrays.asList(0,1,2,3,4,5));
            switch(action){
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
                        sm.submitSubscription(nom, prenom, mail, adresse);
                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(c != null){
                        System.out.println("Vous etes inscrit ! Objet Client créer :");
                        System.out.println(c.toString());
                    }
                    else{
                        System.out.println("Email déja utilisée !");
                    }
                    break;
                case 2: // Se connecter
                    String identifiant = Saisie.lireChaine("Adresse : ");
                    client = sm.connect(identifiant);
                    if(client == null){
                        System.out.println("Adresse invalide");
                    }
                    else{
                        System.out.println("Connecte en tant que :");
                        System.out.println(client.toString());
                    }
                    break;
                case 3: // List restaurants
                    try {
                        
                        List<Restaurant> lr = sm.getRestaurantsList();
                        for(int i=0;i<lr.size();i++){
                            System.out.println("Restaurant id:"+lr.get(i).getId());
                            System.out.println("Nom:"+lr.get(i).getDenomination());
                            System.out.println("Adresse :"+lr.get(i).getAdresse());
                            System.out.println("Description:"+lr.get(i).getDescription());
                            System.out.println("----------");
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    break;
                case 4:
                    int restoID = Saisie.lireInteger("ID du resto : ");
                    Restaurant r = null;
                    List<Produit> lp = null;
                    HashMap<Produit,Integer> hm = new HashMap<Produit,Integer>();
                    try {
                        r = sm.getRestaurant(restoID);
                        lp = r.getProduits();
                        
                        for(int i=0;i<lp.size();i++){
                            System.out.println("Produit ID:"+lp.get(i).getId());
                            System.out.println("Nom:"+lp.get(i).getDenomination());
                            System.out.println("Description:"+lp.get(i).getDescription());
                            System.out.println("Poids :"+lp.get(i).getPoids());
                            System.out.println("Prix :"+lp.get(i).getPrix());
                            int qte = Saisie.lireInteger("Quantité:");
                            if(qte > 0){
                                hm.put(lp.get(i), qte);
                            }
                            System.out.println("----------");
                        }
                        
                        sm.submitCommande(hm, client, r);
                    } catch (Exception ex) {
                        Logger.getLogger(Saisie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 5:
                    
                    break;
                default:
                    System.out.println("Wat ?");
            }
        }
        
        JpaUtil.destroy();
    }
    
    public static void afficherMenu(Client c){
        
        System.out.println("--- MENU ---");
        if(c==null){
            System.out.println("1 : S'inscrire");
            System.out.println("2 : Se connecter");
        }
        else{
            System.out.println("3 : Voir les restaurants");
            System.out.println("4 : Commander : (Voir produits restaurant par ID)");
            System.out.println("5 : ...");
        }
        System.out.println("0 : Quitter");
    }
}
