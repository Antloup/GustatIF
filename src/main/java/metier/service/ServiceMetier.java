/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;
import metier.modele.Client;
import dao.ClientDAO;
import dao.RestaurantDAO;
import java.util.List;
import metier.modele.Commande;
import metier.modele.Restaurant;

/**
 *
 * @author Anthony
 */
public class ServiceMetier {
    
    /**
     * Permet d'inscrire un utilisateur et de controler si email disponible 
     * @return null si inscription échoue, le nouveau client si inscription est un succès
     */
  public Client submitSubscription(String nom, String prenom,String mail,String adresse) throws Exception{
      
      ClientDAO cdao = new ClientDAO();
      if(!cdao.isTaken(mail)){
          
          Client newclient = cdao.createClient(nom, prenom, mail, adresse);
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
  
}
