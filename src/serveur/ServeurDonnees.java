package serveur;

import envoie.reception.*;
import systeme.fichiers.*;

/*
 * Classe ServeurDonnees --> Cette classe permet de g�rer l'envoi des donn�es contenues dans une PDU
 */

public class ServeurDonnees {
	/* D�claration de variables */
	GestionFichier sysFichier;

	/*
	 * Constructeur ServeurDonnees --> Ce constructeur prend en param�tre un gestionnaire de fichiers
	 * Ce constructeur permet de cr�er un nouveau ServeurDonnees.
	 */
	public ServeurDonnees(GestionFichier g) {
		sysFichier = g;
	}

	/*
	 * M�thode Upload : M�thode permettant de charger les donn�es dans les blocs dans la PDU avant son envoi
	 * @param : la requ�te � traiter
	 * @return : la PDU une fois les donn�es charg�es
	 */
	public PDU Upload(PDUDonnees requete) {
		/* D�claration des variables */
		PDUDonnees reponse = null;
		byte[] bloc = null;
		
		/*On recup�re les donn�es du fichier */
		Fichier fichier = sysFichier.getListFichier().get(requete.getDonnees());
		/* On charge les donn�es du fichier dans un des blocs de la PDU*/
		bloc = sysFichier.Lire(fichier, requete.getIndex());
		/* Si la variable bloc est nulle */
		if (bloc != null) {
			/*On encapsule les donn�es lues dans une PDUDonnees pour pouvoir les envoyer par la suite */
			reponse = new PDUDonnees("DATA",null, requete.getIndex(), bloc);
			sysFichier.nbUploadInc();
			/* On incr�mente le nombre d'upload */
		}else {
			/* Affichage d'une requ�te d'erreur */
			reponse = new PDUDonnees("ERR","Erreur lors de la lecture du bloc : "+requete.getIndex(), requete.getIndex(),null);
		}
		/* On retourne la PDU */
		return reponse;
	}
}
