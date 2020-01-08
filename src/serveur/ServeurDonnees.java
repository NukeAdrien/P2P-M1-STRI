package serveur;

import envoie.reception.PDU;
import systeme.fichiers.GestionFichier;

public class ServeurDonnees {
	GestionFichier sysFichier;
	public ServeurDonnees(GestionFichier g) {
		sysFichier = g;
	}
	public PDU Upload(PDU requete) {
		PDU reponse = null; 
		return reponse;
	}
}
