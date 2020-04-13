package serveur;

import envoie.reception.*;
import systeme.fichiers.*;

/*
 * Classe ServeurDonnees --> Cette classe permet de gérer l'envoi des données contenues dans une PDU
 */

public class ServeurDonnees {
	/* Déclaration de variables */
	GestionFichier sysFichier;

	/*
	 * Constructeur ServeurDonnees --> Ce constructeur prend en paramétre un gestionnaire de fichiers
	 * Ce constructeur permet de créer un nouveau ServeurDonnees.
	 */
	public ServeurDonnees(GestionFichier g) {
		sysFichier = g;
	}

	/*
	 * Méthode Upload : Méthode permettant de charger les données dans les blocs dans la PDU avant son envoi
	 * @param : la requéte à traiter
	 * @return : la PDU une fois les données chargées
	 */
	public PDU Upload(PDUDonnees requete) {
		/* Déclaration des variables */
		PDUDonnees reponse = null;
		byte[] bloc = null;
		
		/*On recupére les données du fichier */
		Fichier fichier = sysFichier.getListFichier().get(requete.getDonnees());
		/* On charge les données du fichier dans un des blocs de la PDU*/
		bloc = sysFichier.Lire(fichier, requete.getIndex());
		/* Si la variable bloc est nulle */
		if (bloc != null) {
			/*On encapsule les données lues dans une PDUDonnees pour pouvoir les envoyer par la suite */
			reponse = new PDUDonnees("DATA",null, requete.getIndex(), bloc);
			sysFichier.nbUploadInc();
			/* On incrémente le nombre d'upload */
		}else {
			/* Affichage d'une requéte d'erreur */
			reponse = new PDUDonnees("ERR","Erreur lors de la lecture du bloc : "+requete.getIndex(), requete.getIndex(),null);
		}
		/* On retourne la PDU */
		return reponse;
	}
}
