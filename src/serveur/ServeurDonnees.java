package serveur;

import envoie.reception.*;
import systeme.fichiers.*;

public class ServeurDonnees {
	GestionFichier sysFichier;

	public ServeurDonnees(GestionFichier g) {
		sysFichier = g;
	}

	public PDU Upload(PDUDonnees requete) {
		PDUDonnees reponse = null;
		byte[] bloc = null;
		Fichier fichier = sysFichier.getListFichier().get(requete.getDonnees());
		bloc = sysFichier.Lire(fichier, requete.getIndex());
		if (bloc != null) {
			reponse = new PDUDonnees("DATA",null, requete.getIndex(), bloc);
		}else {
			reponse = new PDUDonnees("ERR","Erreur lors de la lecture du bloc : "+requete.getIndex(), requete.getIndex(),null);
		}
		return reponse;
	}
}
