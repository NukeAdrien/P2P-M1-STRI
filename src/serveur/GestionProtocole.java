package serveur;

import envoie.reception.*;

public class GestionProtocole {
	ServeurControle controle;
	ServeurDonnees donnees;
	PDU reponse;

	public GestionProtocole(ServeurControle sc, ServeurDonnees sd) {
		controle = sc;
		donnees = sd;
	}

	public PDU gestionRequete(PDU requetePDU) {
		if (requetePDU instanceof PDUControle) {
			PDUControle requete = (PDUControle)requetePDU;
			switch (requete.getCommande()) {
			case "TSF":
				reponse = controle.TSF(requete);
				return reponse;
			case "TPF":
				reponse = controle.TPF(requete);
				return reponse;
			default:
				System.out.println("Erreur requete inexistante");
				return null;
			}
		} else if (requetePDU instanceof PDUDonnees) {
			PDUDonnees requete = (PDUDonnees )requetePDU;
			return donnees.Upload(requete);
		} else {
			return null;
		}
	}

}
