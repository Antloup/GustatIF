package dao;

import com.google.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import metier.modele.Client;

public class ClientDAO {
    
    public Client findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Client client = null;
        try {
            client = em.find(Client.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return client;
    }
    
    public List<Client> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Client> clients = null;
        try {
            Query q = em.createQuery("SELECT c FROM Client c");
            clients = (List<Client>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return clients;
    }
    
    public boolean isTaken(String adresse){
        if(findByEmail(adresse)!=null)
            return true;
        else
            return false;
    }
    
    public Client findByEmail(String adresse){
        EntityManager em = JpaUtil.obtenirEntityManager();
        
        Client client = null;
        try {
            Query q = em.createQuery("SELECT c FROM Client c WHERE c.mail = :mail");
            q.setParameter("mail", adresse);
            client = (Client) q.getSingleResult();
        }
        catch(Exception e) {
            if(e instanceof NullPointerException){
                return null;
            }
            else if(e instanceof NoResultException){
                return null;
            }
            else{
                throw e;
            }
        }
        return client;
    }
    
    public Client createClient(String nom, String prenom,String mail,String adresse,LatLng latlng){
        Client c = new Client(nom,prenom,mail,adresse,latlng);
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(c);
        return c;
    }
 
}
