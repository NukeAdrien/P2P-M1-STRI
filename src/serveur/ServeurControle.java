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
		fichier = gestionFichier.RechercheFichier(requete.getDonnees());
		if (fichier == null) {
			reponse = new PDUControle("CTRL","TSF","Fichier introuvable",null);
		}else {
			if(gestionFichier.EtatFichier(requete.getDonnees()) == 1) {
				reponse = new PDUControle("CTRL","TSF","Fichier disponible",fichier);
			}else {
				reponse = new PDUControle("CTRL","TSF","Fichier en cour de téléchargement sur le serveur",null);
			}
		}
		return reponse;
	}
}
