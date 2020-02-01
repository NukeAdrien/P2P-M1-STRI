package serveur;

import envoie.reception.*;
import systeme.fichiers.*;

/*
 * Classe ServeurControle --> Cette classe permet de décider quel transfert de fichier est le mieux adapté
 */
public class ServeurControle {
	
	/* Déclaration de variables */
	GestionFichier gestionFichier;

	
	/*
	 * Constructeur ServeuControle --> Ce constructeur prend en paramètre un gestion de fichier
	 * Ce constructeur permet de créer un nouveau ServeurControle .
	 */
	public ServeurControle(GestionFichier gf) {
		gestionFichier = gf;
	}

	/*
	 * Méthode TSF (Transfert simple de fichier) : Méthode permettant de télécharger un fichier se trouvant sur un serveur
	 * @param : la PDU à traiter
	 * @return : la PDU une fois traitée
	 */
	public PDU TSF(PDU requete) {
		/* Déclaration de variables */
		PDU reponse = null;
		Fichier fichier;
		/* On recherche si les fichier existe mais plus précisément on regarde si le fichier contient des données*/
		fichier = gestionFichier.RechercheFichier(requete.getDonnees());
		/* Si le fichier n'existe pas */
		if (fichier == null) {
			/* On crée une nouvelle PDU en indiquant que le fichier n'existe pas */
			reponse = new PDUControle("CTRL","TSF","Fichier introuvable",null);
		}else {
			/* Si le fichier existe */
			if(gestionFichier.EtatFichier(requete.getDonnees()) == 1) {
				/* On crée une nouvelle PDU en indiquant que le fichier est disponible */
				reponse = new PDUControle("CTRL","TSF","Fichier disponible",fichier);
			}else {
				/* Si le fichier existe mais n'est pas disponible */
				/* On crée une nouvelle PDU en indiquant que le fichier n'est pas disponible */
				reponse = new PDUControle("CTRL","TSF","Fichier en cours de téléchargement sur le serveur",null);
			}
		}
		/* On retourne la PDU traitée */
		return reponse;
	}
	
	/*
	 * Méthode TPF (Transfert Parallele de fichiers) : Méthode permettant de télécharger un fichier se trouvant sur plusieurs serveurs
	 * @param : la PDU à traiter
	 * @return : la PDU une fois traitée
	 */
	
	public PDU TPF(PDU requete) {
		/* Déclaration de variables */
		PDU reponse = null;
		Fichier fichier;
		/* On recherche si les fichier existe mais plus précisément on regarde si le fichier contient des données*/
		fichier = gestionFichier.RechercheFichier(requete.getDonnees());
		/* Si le fichier n'existe pas */
		if (fichier == null) {
			/* On crée une nouvelle PDU en indiquant que le fichier n'existe pas */
			reponse = new PDUControle("CTRL","TSF","Fichier introuvable",null);
		}else {
			/* Si le fichier existe */
			if(gestionFichier.EtatFichier(requete.getDonnees()) == 1) {
				/* On crée une nouvelle PDU en indiquant que le fichier est disponible */
				reponse = new PDUControle("CTRL","TSF","Fichier disponible",fichier);
			}else {
				/* Si le fichier existe mais n'est pas disponible */
				/* On crée une nouvelle PDU en indiquant que le fichier n'est pas disponible */
				reponse = new PDUControle("CTRL","TSF","Fichier en cours de téléchargement sur le serveur",null);
			}
		}
		/* On retourne la PDU traitée */
		return reponse;
	}
}
