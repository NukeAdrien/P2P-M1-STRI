package serveur;

import envoie.reception.*;

/*
 * Classe GestionProtocole --> Cette classe permet de gérer un protocole
 */


public class GestionProtocole {
	/* Déclaration de variables */
	ServeurAnnuaire annuaire;
	ServeurControle controle;
	ServeurDonnees donnees;
	PDU reponse;

	/*
	 * Constructeur GestionProtocole --> Ce constructeur prend en paramètres un ServeurControle et ServeurDonnees
	 * Ce constructeur permet de créer un nouveau GestionProtocole.
	 */
	public GestionProtocole(ServeurControle sc, ServeurDonnees sd) {
		controle = sc;
		donnees = sd;
	}
	
	/*
	 * Constructeur GestionProtocole --> Ce constructeur prend en paramètres un ServeurControle, un ServeurDonnees
	 * Ce constructeur permet de créer un nouveau GestionProtocole.
	 */
	public GestionProtocole(ServeurControle sc, ServeurDonnees sd, ServeurAnnuaire sa) {
		annuaire = sa;
		controle = sc;
		donnees = sd;
	}
	
	
	/*
	 * Méthode gestionRequete : Méthode permettant de gérer la requête traitée
	 * @param : la requête PDU à traiter et l'adresse Ip provenant de la PDU
	 * @return : la PDU une fois la requête traitée
	 */
	public PDU gestionRequete(PDU requetePDU, String adresse) {
		/* Si la variable requetePDU est une instance de PDUControle*/
		if (requetePDU instanceof PDUControle) {
			/* On caste la variable en PDUControle*/
			PDUControle requete = (PDUControle)requetePDU;
			/* Suivant la commande contenue dans la variable requete, casté en PDUControle*/
			switch (requete.getCommande()) {
			/* Si c'est TSF */
			case "TSF":
				/* Alors on applique la méthode TSF indiquant que les blocs sont disponibles*/
				reponse = controle.TSF(requete);
				return reponse;
				/* Si c'est TPF */
			case "TPF":
				/* Alors on applique la méthode TPF indiquant que les blocs sont disponibles*/
				reponse = controle.TPF(requete);
				return reponse;
			case "PING":
				/* On crée une PDU permettant de faire le PING */
				return reponse = new PDUControle("CTRL", "PING", null, null);
			default:
				/*Affichage d'un message d'erreur*/
				System.out.println("Erreur requéte inexistante");
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
			/* Suivant la commande contenue dans la variable requete, casté en PDUControle*/
			switch (requete.getMethode()) {
			/* Si c'est REGISTRATION */
			case "REGISTRATION":
				/* Alors on applique méthode Inscription permettant l'ajout d'un nouveau serveur dans l'annuaire */
				reponse = annuaire.Inscription(requete,adresse);
				return reponse;
				/* Si c'est SEARCH */
			case "SEARCH":
				/* Alors on applique méthode Search permettant la recherche d'un élément dans l'annuaire */
				reponse = annuaire.Search(requete);
				return reponse;
				/* Si c'est DOWLOAD */
			case "DOWLOAD":
				/* Alors on applique méthode Dowload permettant le téléchargement d'un élément depuis plusieurs serveurs dans l'annuaire */
				reponse = annuaire.Dowload(requete,adresse);
				return reponse;
			default:
				/*Affichage d'un message d'erreur*/
				System.out.println("Erreur requête inexistante");
				return null;
			}
			
		}else {
			return null;
		}
	}

}
