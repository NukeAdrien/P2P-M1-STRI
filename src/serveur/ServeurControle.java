package serveur;

import envoie.reception.*;
import systeme.fichiers.*;

public class ServeurControle {
	GestionFichier gestionFichier;

	public ServeurControle(GestionFichier gf) {
		gestionFichier = gf;
	}

	public PDU TSF(PDU requete) {
		PDU reponse = null;
		Fichier fichier;
		reponse = new PDU("CTRL","TSF","Fichier introuvable",null);
		return reponse;
		/*fichier = gestionFichier.RechercheFichier(requete.getDonnees());
		if (fichier == null) {
			reponse = new PDU("CTRL","TSF","Fichier introuvable",null);
		}else {
			if(gestionFichier.EtatFichier(requete.getDonnees()) == 1) {
				reponse = new PDU("CTRL","TSF","Fichier disponible",fichier);
			}else {
				reponse = new PDU("CTRL","TSF","Fichier en cour de téléchargement sur le serveur",null);
			}
		}
		return reponse;*/
	}
}
