/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;


import dao.*;
import java.util.List;
import metier.modele.*;
/**
 *
 * @author Anthony
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        JpaUtil.init();
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        
        ClientDAO cdao = new ClientDAO();
        System.out.println(cdao.findByEmail("adresse@insa.com"));
        System.out.println(cdao.findByEmail("nolmeadamarais1551@gmail.com"));
        if(cdao.isTaken("nolmeadamarais1551@gmail.com")){
            System.out.println("Il faut utiliser une autre adresse email");
        }
        else{
            System.out.println("Adresse email valide");
        }
        
        JpaUtil.validerTransaction();
        JpaUtil.destroy();
    }
    
    private void afficherListe(List<Object> lo) throws Exception{
        ClientDAO cdao = new ClientDAO();
        List<Client> listeClient = cdao.findAll();
        for (int i = 0; i < listeClient.size(); i++) {
            System.out.println(listeClient.get(i));
        }
        
        ProduitDAO pdao = new ProduitDAO();
        List<Produit> listeProduit = pdao.findAll();
        for (int i = 0; i < listeProduit.size(); i++) {
            System.out.println(listeProduit.get(i));
        }

        RestaurantDAO rdao = new RestaurantDAO();
        List<Restaurant> listeRestaurant = rdao.findAll();
        for (int i = 0; i < listeRestaurant.size(); i++) {
            System.out.println(listeRestaurant.get(i));
        }
    }
    
}
