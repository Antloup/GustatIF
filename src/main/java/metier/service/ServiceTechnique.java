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
     * @param c : Client a livrer
     * @param r : restaurant de la commande
     * @param poids : Poids de la livraison
     * @param duree_min : Double de valeur max. En sortie : valeur de la durée de la livraison
     * @return null si aucuns livreurs disponible, sinon, le livreur le mieux positionné 
     */
    public static Livreur selectionnerLivreur(Client c, Restaurant r, double poids,double duree_min){
        List<Livreur> ll = ServiceMetier.getAvailableLivreur();
        LatLng d_latlng = new LatLng(c.getLatitude(), c.getLongitude());
        Livreur selectLivreur = null;
        for (int i = 0; i < ll.size(); i++) {
            if (ll.get(i).getMax_transport() >= poids && ll.get(i).getStatus() == 0) { // Poids OK + Disponible
                LatLng s_latlng = new LatLng(ll.get(i).getLatitude(), ll.get(i).getLongitude());
                double duree = -1;
                LatLng r_latlng = new LatLng(r.getLatitude(),r.getLongitude());
                if (ll.get(i) instanceof Employe) {
                    duree = GeoTest.getTripDurationByBicycleInMinute(s_latlng, r_latlng);
                    duree += GeoTest.getTripDurationByBicycleInMinute(r_latlng, d_latlng);
                } else if (ll.get(i) instanceof Drone) {
                    Drone d = (Drone) ll.get(i);
                    duree = (GeoTest.getFlightDistanceInKm(s_latlng, r_latlng) / d.getVitesse()) * 60;
                    duree += (GeoTest.getFlightDistanceInKm(r_latlng, d_latlng) / d.getVitesse()) * 60;
                }
                if (duree != -1 && duree < duree_min) {
                    duree_min = duree;
                    selectLivreur = ll.get(i);
                }
            }
        }
        return selectLivreur;
    }
    
    /**
     * 
     * @param l : livreur qui doit recevoir un mail pour être notifié de sa commande en cours
     * @return : String contenant l'email
     */
    public static String sendEmail(Livreur l){
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
