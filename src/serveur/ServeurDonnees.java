package serveur;

import envoie.reception.PDU;
import systeme.fichiers.*;

public class ServeurDonnees {
	GestionFichier sysFichier;

	public ServeurDonnees(GestionFichier g) {
		sysFichier = g;
	}

	public PDU Upload(PDU requete) {
		PDU reponse = null;
		Byte bloc = null;
		Fichier fichier = requete.getFichier();
		Integer index = Integer.parseInt(requete.getDonnees());
		bloc = sysFichier.Lire(fichier, index);
		if (bloc != null) {
			fichier.setDonnees(index, bloc);
			reponse = new PDU("DATA", null, null, fichier);
		}else {
			reponse = new PDU("ERR", null,"Erreur lors de la lecture du bloc : "+requete.getDonnees(), null);
		}
		return reponse;
	}
}
