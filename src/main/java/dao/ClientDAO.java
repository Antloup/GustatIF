package dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
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
        
        List<Client> clients = null;
        try {
            JpaUtil.ouvrirTransaction();
            //Query q = em.createQuery("SELECT c FROM Client c WHERE c.mail = :mail");
            Query q = em.createQuery("SELECT c FROM Client c");
            q.setFirstResult(0);
            q.setMaxResults(1);
            q.setParameter("mail", adresse);
            clients = (List<Client>) q.getResultList();
            JpaUtil.validerTransaction();
        }
        catch(Exception e) {
            if(e instanceof NullPointerException){
                return null;
            }
            else{
                throw e;
            }
        }
        if(clients.isEmpty()){
            return null;
        }
        else{
            return clients.get(0);
        }
    }
    
    public Client createClient(String nom, String prenom,String mail,String adresse){
        Client c = new Client(nom,prenom,mail,adresse);
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(c);
        return c;
    }
 
}
