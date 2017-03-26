/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import com.google.maps.model.LatLng;
import java.util.List;
import java.util.Map;
import metier.modele.Client;
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
public class ServiceTechnique {
    
    
    /**
     * 
     * @param l : livreur qui doit recevoir un mail pour être notifié de sa commande en cours
     * @return : String contenant l'email
     */
    public static String sendEmailCommande(Livreur l){
        String email = "";
        Employe e = (Employe) l;
        email += "Bonjour "+ e.getPrenom() + " "+e.getNom()+"\n";
        email += "Une nouvelle commande vous est attribué, voici les détails :\n";
        email += "Client :" + e.getCommandeEnCours().getClient()+"\n";
        email += "Commande : \n";
        for(Map.Entry<Produit, Integer> commande : e.getCommandeEnCours().getListeProduit().entrySet()) {
            System.out.println("Produit :"+commande.getKey());
            System.out.println("Quantité :"+commande.getValue());
        }
        System.out.println("Prix total : "+ServiceMetier.getPrixTot(e.getCommandeEnCours()));
        
        return email;
        
    }
    
    public static String sendConfirmInscription(Client c, boolean error){
        String email="";
        email = "Mail adressé à "+c.getMail()+" par Service@Gustatif.fr \n";
        if(error==false){
            email += "Bienvenue sur Gustatif votre numéro client est "+c.getId();
        }
        else{
            email +="Votre inscription à échouez veuillez réessayer";
        
        }
    
        return email;
    }
}
