package serveur;

import envoie.reception.*;
import systeme.fichiers.*;

/*
 * Classe ServeurControle --> Cette classe permet de cr�er un serveur de contr�le
 */
public class ServeurControle {
	
	/* D�claration de variables */
	GestionFichier gestionFichier;

	
	/*
	 * Constructeur ServeuControle --> Ce constructeur prend en param�tre un gestionnaire de fichiers
	 * Ce constructeur permet d'instancier un nouveau ServeurControle .
	 */
	public ServeurControle(GestionFichier gf) {
		gestionFichier = gf;
	}

	/*
	 * M�thode TSF (Transfert simple de fichier) : M�thode permettant de t�l�charger un fichier se trouvant sur un serveur
	 * @param : la PDU � traiter
	 * @return : la PDU une fois trait�e
	 */
	public PDU TSF(PDU requete) {
		/* D�claration de variables */
		PDU reponse = null;
		Fichier fichier;
		/* On recherche si les fichier existe */
		fichier = gestionFichier.RechercheFichier(requete.getDonnees());
		/* Si le fichier n'existe pas */
		if (fichier == null) {
			/* On cr�e une nouvelle PDU en indiquant que le fichier n'existe pas */
			reponse = new PDUControle("CTRL","TSF","Fichier introuvable",null);
		}else {
			/* Si le fichier existe et qu'il est disponible */
			if(gestionFichier.EtatFichier(requete.getDonnees()) == 1) {
				/* On cr�e une nouvelle PDU en indiquant que le fichier est disponible */
				reponse = new PDUControle("CTRL","TSF","Fichier disponible",fichier);
			}else {
				/* Si le fichier existe mais n'est pas disponible */
				/* On cr�e une nouvelle PDU en indiquant que le fichier n'est pas disponible */
				reponse = new PDUControle("CTRL","TSF","Fichier en cours de t�l�chargement sur le serveur",null);
			}
		}
		/* On retourne la PDU trait�e */
		return reponse;
	}
	
	/*
	 * M�thode TPF (Transfert Parallele de fichiers) : M�thode permettant de t�l�charger un fichier se trouvant sur plusieurs serveurs
	 * @param : la PDU � traiter
	 * @return : la PDU une fois trait�e
	 */
	
	public PDU TPF(PDU requete) {
		/* D�claration de variables */
		PDU reponse = null;
		Fichier fichier;
		/* On recherche si le fichier existe */
		fichier = gestionFichier.RechercheFichier(requete.getDonnees());
		/* Si le fichier n'existe pas */
		if (fichier == null) {
			/* On cr�e une nouvelle PDU en indiquant que le fichier n'existe pas */
			reponse = new PDUControle("CTRL","TPF","Fichier introuvable",null);
		}else {
			/* Si le fichier existe */
			/* On cr�e une nouvelle PDU en indiquant que le fichier est disponible */
			reponse = new PDUControle("CTRL","TPF","Fichier disponible",fichier);
		}
		/* On retourne la PDU trait�e */
		return reponse;
	}
}
