/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Anthony
 */
@Entity
public class Employe extends Livreur implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nom;
    private String prenom;
    private String email;

    public Employe() {
    }

    public Employe(String nom, String prenom,String email,String adresse,double max_transport) {
        super(null, adresse, max_transport, 0);
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }
    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    
}
