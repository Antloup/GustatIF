/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import com.google.maps.model.LatLng;
import dao.CommandeDAO;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import util.GeoTest;

/**
 *
 * @author Anthony
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Livreur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany
    private Set<Commande> commandes;
    private double max_transport;
    private String adresse;
    private int status;
    private Double longitude;
    private Double latitude;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getMax_transport() {
        return max_transport;
    }

    public void setMax_transport(double max_transport) {
        this.max_transport = max_transport;
    }

    /**
     *
     * @return Duree totale des livraisons du livreurs
     */
    /*
    public double getDuree() {
        double duree = 0.0;
        if (this.commandes != null) {
            Iterator<Commande> iter = this.commandes.iterator();
            while (iter.hasNext()) {
                duree += iter.next().getDuree();
            }
        }
        return duree;
    }
    */

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        LatLng ll = GeoTest.getLatLng(adresse);
        this.longitude = ll.lng;
        this.latitude = ll.lat;
        this.adresse = adresse;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Livreur() {
    }

    public Livreur(Set<Commande> commandes, String adresse, double max_transport, int status) {
        this.commandes = commandes;
        this.status = status;
        this.max_transport = max_transport;
        setAdresse(adresse);
    }

    public Set<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }
    
    public Commande getCommandeEnCours(){
        Iterator<Commande> iter = this.commandes.iterator();
        while (iter.hasNext()) {
            Commande c = iter.next();
            if(c.getEtat() == CommandeDAO.Etat.EN_COURS.ordinal()){
                return c;
            }
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Livreur)) {
            return false;
        }
        Livreur other = (Livreur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "metier.modele.Livreur[ id=" + id + " ]";
    }

}
