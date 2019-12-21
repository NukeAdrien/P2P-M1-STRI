package serveur;
import java.util.Date;

public class GestionProtocole {
	BanqueSimple banqueS;
	
	public GestionProtocole(BanqueSimple b) {
		banqueS = b;
	}
	
	public String gestion (String requete) {
		 final String SEPARATEUR = " ";
		String commande[] = requete.split(SEPARATEUR);
		String reponse;
		
		switch (commande[0]) {
		case "CREATION" :
			banqueS.creerCompte(commande[1],  Double.parseDouble(commande[2]));
			reponse = "OK " + commande[0];
			break;
		case "POSITION" :
			double solde = banqueS.getSolde(commande[1]);
			Date date = banqueS.getDerniereOperation(commande[1]);
			reponse = commande[1] + " -> " + solde + " " + date;
			break;
		case "AJOUT" :
			banqueS.ajouter(commande[1], Double.parseDouble(commande[2]));
			reponse = "OK " + commande[0];
			break;
		case "RETRAIT" :
			banqueS.retirer(commande[1], Double.parseDouble(commande[2]));
			reponse = "OK " + commande[0];
			break;
		default :
			reponse = "ERREUR " + commande[0] + " commande introuvable";
		}
		return reponse;
		
	}
		

}
