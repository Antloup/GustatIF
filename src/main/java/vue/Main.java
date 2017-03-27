/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;


import dao.*;
import java.util.ArrayList;
import java.util.List;
import metier.modele.*;
/**
 *
 * @author Anthony
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        JpaUtil.init();
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        
        ClientDAO cdao = new ClientDAO();
        System.out.println(cdao.findByEmail("adresse@insa.com"));
        System.out.println(cdao.findByEmail("nolmeadamarais1551@gmail.com"));
        if(cdao.isTaken("Adr@mail2.com")){
            System.out.println("Il faut utiliser une autre adresse email");
        }
        else{
            System.out.println("Adresse email valide");
        }
        List<Object> objectList = new ArrayList<Object>(cdao.findAll());
        afficherListe(objectList);
        
        JpaUtil.validerTransaction();
        JpaUtil.destroy();
    }
    
    private static void afficherListe(List<Object> lo){
        for (int i = 0; i < lo.size(); i++) {
            System.out.println(lo.get(i));
        }
    }
    
}
