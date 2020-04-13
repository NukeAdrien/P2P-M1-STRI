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
	 * Constructeur GestionProtocole --> Ce constructeur prend en param�tre un ServeurControle, un ServeurDonnees
	 * Ce constructeur permet de cr�er un nouveau GestionProtocole.
	 */
	public GestionProtocole(ServeurControle sc, ServeurDonnees sd, ServeurAnnuaire sa) {
		annuaire = sa;
		controle = sc;
		donnees = sd;
	}
	
	
	/*
	 * M�thode gestionRequete : M�thode permettant de g�rer la requ�te trait�e
	 * @param : la requ�te PDU � traiter et l'adresse Ip provenant de la PDU
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
				/* Si c'est TPF */
			case "TPF":
				/* Alors on applique la m�thode TPF indiquant que les blocs sont disponibles*/
				reponse = controle.TPF(requete);
				return reponse;
			case "PING":
				/* On cr�e une PDU permettant de faire le PING */
				return reponse = new PDUControle("CTRL", "PING", null, null);
			default:
				/*Affichage d'un message d'erreur*/
				System.out.println("Erreur requ�te inexistante");
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
			/* Si c'est REGISTRATION */
			case "REGISTRATION":
				/* Alors on applique m�thode Inscription permettant l'ajout d'un nouveau serveur dans l'annuaire */
				reponse = annuaire.Inscription(requete,adresse);
				return reponse;
				/* Si c'est SEARCH */
			case "SEARCH":
				/* Alors on applique m�thode Search permettant la recherche d'un �l�ment dans l'annuaire */
				reponse = annuaire.Search(requete);
				return reponse;
				/* Si c'est DOWLOAD */
			case "DOWLOAD":
				/* Alors on applique m�thode Dowload permettant le t�l�chargement d'un �l�ment depuis plusieurs serveurs dans l'annuaire */
				reponse = annuaire.Dowload(requete,adresse);
				return reponse;
			default:
				/*Affichage d'un message d'erreur*/
				System.out.println("Erreur requ�te inexistante");
				return null;
			}
			
		}else {
			return null;
		}
	}

}
