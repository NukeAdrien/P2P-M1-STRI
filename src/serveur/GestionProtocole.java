package serveur;

import envoie.reception.PDU;

public class GestionProtocole {
	ServeurControle controle;
	ServeurDonnees donnees;
	PDU reponse;

	public GestionProtocole(ServeurControle sc, ServeurDonnees sd) {
		controle = sc;
		donnees = sd;
	}

	public PDU gestionRequete(PDU requete) {
		if (requete.getType().compareTo("CTRL") == 0) {
			switch (requete.getCommande()) {
			case "TSF":
				reponse = controle.TSF(requete);
				return reponse;
			default:
				System.out.println("Erreur requete inexistante");
				return null;
			}

		} else if (requete.getType().compareTo("DATA") == 0) {
			switch (requete.getCommande()) {
			case "TSF":
				reponse = donnees.Upload(requete);
				return reponse;
			default:
				System.out.println("Erreur requete inexistante");
				return null;
			}

		} else if (requete.getType().compareTo("ERR") == 0) {

		} else {
			return null;
		}

		return null;

	}

}
