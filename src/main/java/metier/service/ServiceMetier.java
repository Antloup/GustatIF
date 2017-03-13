/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import dao.ClientDAO;
import dao.CommandeDAO;
import dao.ProduitDAO;
import java.util.HashMap;
import java.util.List;
import metier.modele.Client;
import metier.modele.Commande;
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
    public Commande submitMeal(HashMap<Produit,Integer> hm, Client c, Restaurant r){
        CommandeDAO cdao = new CommandeDAO();
        return cdao.createCommande(hm,c,r);
    }
    

}
