/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Date;
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
public class CommandeDAO {
    
    public enum Etat{
        EN_ATTENTE,EN_COURS,TERMINE
    }
    

    /**
     * 
     * @param hm : HashMap Objet Produit / quantité de produit
     * @param c : Objet client
     * @param r : Objet Restaurant
     * @return La commande créer
     */
    public Commande createCommande(HashMap<Produit,Integer> hm,Client c,Restaurant r) {
        Date today = new Date();
        // Pour la durée : voir API Google MAP
        // Pour le livreur : voir DAO livreur
        return new Commande(Etat.EN_ATTENTE.ordinal(), hm, today, null, r, null, c);
    }
    
}
