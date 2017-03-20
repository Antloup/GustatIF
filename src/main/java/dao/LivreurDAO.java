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
import java.util.Set;
import javax.persistence.Query;
import metier.modele.Commande;
import metier.modele.Drone;
import metier.modele.Employe;

/**
 *
 * @author yanis
 */
public class LivreurDAO {

    public List<Livreur> findByStatut(int statut) {

        EntityManager em = obtenirEntityManager();
        List<Livreur> Livreurs = null;
        try {
            Query q = em.createQuery("SELECT l FROM Livreur l where l.status = 0");
            //q.setParameter(0, statut);
            Livreurs = (List<Livreur>) q.getResultList();
            if (!Livreurs.isEmpty()) {
                return Livreurs;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw e;
        }

    }

    public Livreur findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Livreur livreur = null;
        try {
            livreur = em.find(Livreur.class, id);
        } catch (Exception e) {
            throw e;
        }
        return livreur;
    }

    public List<Livreur> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Livreur> livreurs = null;
        try {
            Query q = em.createQuery("SELECT l FROM Livreur l");
            livreurs = (List<Livreur>) q.getResultList();
        } catch (Exception e) {
            throw e;
        }

        return livreurs;
    }

    public void setStatus(Livreur l, int status) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        l.setStatus(status);
        em.merge(l);

    }

    public void createEmploye(Employe e) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(e);
    }

    public void createDrone(Drone d) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(d);
    }

}
