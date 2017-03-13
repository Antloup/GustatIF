/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import metier.modele.Livreur;
import static dao.JpaUtil.obtenirEntityManager;
import javax.persistence.Query;
/**
 *
 * @author yanis
 */
public class LivreurDAO {
    
    public List<Livreur> findLivreur(int statut){
        
        EntityManager em = obtenirEntityManager();
        List<Livreur> Livreurs = null; 
        try{
            Query q = em.createQuery("SELECT l FROM Livreur l where l.status=:statut ");
            q.setParameter(0, statut);
            Livreurs= (List<Livreur>) q.getResultList();
            if(!Livreurs.isEmpty())
            return Livreurs;
            else{
            return null;
            }
        }
        catch(Exception e){
        throw e;
        }        
        
    }
    
}