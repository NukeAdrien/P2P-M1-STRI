package serveur;

import java.util.Date;

import client.ClientControle;
import envoie.reception.PDU;
import systeme.fichiers.GestionFichier;

public class GestionProtocole {
	ServeurControle controle;
	ServeurDonnees donnees;

	public GestionProtocole(ServeurControle sc, ServeurDonnees sd) {
		controle = sc;
		donnees = sd;
	}

	public PDU gestionRequete(PDU requete) {
		if (requete.getType() == "CTRL") {
			switch (requete.getCommande()) {
			case "TSF":
				return controle.TSF(requete);
			default:
				System.out.println("Erreur requete inexistante");
				return null;
			}

		} else if (requete.getType() == "DATA") {

		} else if (requete.getType() == "ERR") {

		} else {
			return null;
		}

		return null;

	}

}
