package metier.modele;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import metier.modele.Commande;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-20T14:32:12")
@StaticMetamodel(Client.class)
public class Client_ { 

    public static volatile SetAttribute<Client, Commande> commandes;
    public static volatile SingularAttribute<Client, String> mail;
    public static volatile SingularAttribute<Client, Double> latitude;
    public static volatile SingularAttribute<Client, String> adresse;
    public static volatile SingularAttribute<Client, Long> id;
    public static volatile SingularAttribute<Client, String> nom;
    public static volatile SingularAttribute<Client, String> prenom;
    public static volatile SingularAttribute<Client, Double> longitude;

}