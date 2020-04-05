package serveur;

import envoie.reception.*;

/*
 * Classe GestionProtocole --> Cette classe permet de g�rer un protocole
 */


public class GestionProtocole {
	/* D�claration de variables */
	ServeurAnnuaire annuaire;
	ServeurControle controle;
	ServeurDonnees donnees;
	PDU reponse;

	/*
	 * Constructeur GestionProtocole --> Ce constructeur prend en param�tre un ServeurControle et ServeurDonnees
	 * Ce constructeur permet de cr�er un nouveau GestionProtocole.
	 */
	public GestionProtocole(ServeurControle sc, ServeurDonnees sd) {
		controle = sc;
		donnees = sd;
	}
	
	/*
	 * Constructeur GestionProtocole --> Ce constructeur prend en param�tre un ServeurControle et ServeurDonnees
	 * Ce constructeur permet de cr�er un nouveau GestionProtocole.
	 */
	public GestionProtocole(ServeurControle sc, ServeurDonnees sd, ServeurAnnuaire sa) {
		annuaire = sa;
		controle = sc;
		donnees = sd;
	}
	
	
	/*
	 * M�thode gestionRequete : M�thode permettant de g�rer la requ�te trait�e
	 * @param : la requ�te PDU � traiter
	 * @return : la PDU une fois la requ�te trait�e
	 */
	public PDU gestionRequete(PDU requetePDU, String adresse) {
		/* Si la variable requetePDU est une instance de PDUControle*/
		if (requetePDU instanceof PDUControle) {
			/* On caste la variable en PDUControle*/
			PDUControle requete = (PDUControle)requetePDU;
			/* Suivant la commande contenue dans la variable requete, cast� en PDUControle*/
			switch (requete.getCommande()) {
			/* Si c'est TSF */
			case "TSF":
				/* Alors on applique la m�thode TSF indiquant que les blocs sont disponibles*/
				reponse = controle.TSF(requete);
				return reponse;
			case "TPF":
				reponse = controle.TPF(requete);
				return reponse;
			case "PING":
				return reponse = new PDUControle("CTRL", "PING", null, null);
			default:
				/*Affichage d'un message d'erreur*/
				System.out.println("Erreur requete inexistante");
				return null;
			}
			/* Si la variable requetePDU est une instance de PDUDonnees*/
		} else if (requetePDU instanceof PDUDonnees) {
			/* On caste la variable en PDUDonnees*/
			PDUDonnees requete = (PDUDonnees )requetePDU;
			/*On retourne les donnees lues*/
			return donnees.Upload(requete);
			
		} else if (requetePDU instanceof PDUAnnuaire) {
			/* On caste la variable en PDUDonnees*/
			PDUAnnuaire requete = (PDUAnnuaire)requetePDU;
			/* Suivant la commande contenue dans la variable requete, cast� en PDUControle*/
			switch (requete.getMethode()) {
			/* Si c'est TSF */
			case "REGISTRATION":
				/* Alors on applique la m�thode TSF indiquant que les blocs sont disponibles*/
				reponse = annuaire.Inscription(requete,adresse);
				return reponse;
			case "SEARCH":
				reponse = annuaire.Search(requete);
				return reponse;
			case "DOWLOAD":
				reponse = annuaire.Dowload(requete);
				return reponse;
			default:
				/*Affichage d'un message d'erreur*/
				System.out.println("Erreur requete inexistante");
				return null;
			}
			
		}else {
			return null;
		}
	}

}
