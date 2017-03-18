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
public class Drone extends Livreur implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String numero;

    public Drone() {
    }
    

    public Drone(String numero) {
        this.numero = numero;
    }
    
    

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    
    
}
