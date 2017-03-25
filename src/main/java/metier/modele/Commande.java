/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Anthony
 */
@Entity
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int etat;
    private double duree;
    private HashMap<Produit,Integer> listeProduit;
    @Temporal(TemporalType.DATE)
    private Date dateCommande;
    @Temporal(TemporalType.DATE)
    private Date dateReception;
    @ManyToOne
    private Restaurant restaurant;
    @ManyToOne
    private Livreur livreur;
    @ManyToOne
    private Client client;

    public Commande() {
    }

    public Commande(int etat, HashMap<Produit, Integer> listeProduit, Date dateCommande, Double duree, Restaurant restaurant, Livreur livreur, Client client) {
        this.etat = etat;
        this.listeProduit = listeProduit;
        this.dateCommande = dateCommande;
        this.dateReception = null;
        this.duree = duree;
        this.restaurant = restaurant;
        this.livreur = livreur;
        this.client = client;
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    /**
     * Get the value of dateCommande
     *
     * @return the value of dateCommande
     */
    public Date getDateCommande() {
        return dateCommande;
    }

    /**
     * Set the value of dateCommande
     *
     * @param dateCommande new value of dateCommande
     */
    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }


    /**
     * Get the value of listeProduit
     *
     * @return the value of listeProduit
     */
    public HashMap<Produit,Integer> getListeProduit() {
        return listeProduit;
    }

    /**
     * Set the value of listeProduit
     *
     * @param listeProduit new value of listeProduit
     */
    public void setListeProduit(HashMap<Produit,Integer> listeProduit) {
        this.listeProduit = listeProduit;
    }

    /**
     * Get the value of duree
     *
     * @return the value of duree
     */
    public double getDuree() {
        return duree;
    }

    /**
     * Set the value of duree
     *
     * @param duree new value of duree
     */
    public void setDuree(double duree) {
        this.duree = duree;
    }

    /**
     * Get the value of client
     *
     * @return the value of client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Set the value of client
     *
     * @param client new value of client
     */
    public void setClient(Client client) {
        this.client = client;
    }


    /**
     * Get the value of livreur
     *
     * @return the value of livreur
     */
    public Livreur getLivreur() {
        return livreur;
    }

    /**
     * Set the value of livreur
     *
     * @param livreur new value of livreur
     */
    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
    }


    /**
     * Get the value of restaurant
     *
     * @return the value of restaurant
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Set the value of restaurant
     *
     * @param restaurant new value of restaurant
     */
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * Get the value of etat
     *
     * @return the value of etat
     */
    public int getEtat() {
        return etat;
    }

    /**
     * Set the value of etat
     *
     * @param etat new value of etat
     */
    public void setEtat(int etat) {
        this.etat = etat;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     *
     * @return Poids totale d'une commande
     */
    public double getPoids() {
        double poids = 0.0;
        HashMap<Produit, Integer> hm = this.getListeProduit();
        for (Produit key : hm.keySet()) {
            poids += key.getPoids() * hm.get(key);
        }
        return poids;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Commande)) {
            return false;
        }
        Commande other = (Commande) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "metier.modele.Commande[ id=" + id + " ]";
    }
    
}
