package serveur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import envoie.reception.PDU;
import envoie.reception.PDUAnnuaire;
import envoie.reception.PDUControle;
import systeme.fichiers.Fichier;
import systeme.fichiers.GestionFichier;
import systeme.fichiers.HeaderBloc;

public class ServeurAnnuaire {
	/* Déclaration de variables */
	GestionFichier gestionFichier;
	HashMap<String, GestionFichier> listServeurs;
	PDUAnnuaire reponse;

	/*
	 * Constructeur ServeuControle --> Ce constructeur prend en paramétre un gestion
	 * de fichier Ce constructeur permet de créer un nouveau ServeurControle .
	 */
	public ServeurAnnuaire(GestionFichier gf) {
		gestionFichier = gf;
	}

	public PDUAnnuaire Inscription(PDUAnnuaire pdu, String adresse) {
		GestionFichier t;
		if (listServeurs.get(adresse) == null) {
			t = listServeurs.put(adresse, pdu.getSysFichiers());
			if (t == null) {
				reponse = new PDUAnnuaire("ANN", "REGISTRATION", null, "NOK",null);
			} else {
				reponse = new PDUAnnuaire("ANN", "REGISTRATION", null, "OK",null);
			}
		} else {
			listServeurs.get(adresse).setListFichier(pdu.getSysFichiers().getListFichier());
			reponse = new PDUAnnuaire("ANN", "REGISTRATION", this.listServeurs.get(adresse), "MAJ",null);
		}
		return reponse;
	}

	public PDUAnnuaire Search(PDUAnnuaire pdu) {

		return null;
	}

	public PDUAnnuaire Dowload(PDUAnnuaire pdu) {
		/* Déclaration de variables */
		PDUAnnuaire reponse = null;
		String nomFichier;
		int i = 0;
		Fichier fichier = null;
		Fichier fichierFinal = null;
		/* On parourt la liste des adresses ip's */
		List<String> listServeurDispo = new ArrayList<String>();
		/* On parcourt les headers blocs */
		for (Map.Entry<String, GestionFichier> listServeur : listServeurs.entrySet()) {
			fichier = listServeur.getValue().RechercheFichier(pdu.getDonnees());
			if (fichier != null) {
				if (fichierFinal == null) {
					fichierFinal = (Fichier) fichier.clone();
				} else {
					for (Map.Entry<Integer, HeaderBloc> headerbloc : fichier.getListHeaderBlocs().entrySet()) {
						if (headerbloc.getValue().getDisponible() == 1) {
							fichierFinal.getListHeaderBlocs().get(headerbloc.getKey()).setDisponible(1);
							i++;
						}
					}
				}

			}
			if (i != 0) {
				listServeurDispo.add(listServeur.getKey());
			}
			fichier = null;
			i = 0;
		}
		if(gestionFichier.EtatFichier(fichierFinal)==1) {
			reponse = new PDUAnnuaire("ANN", "TELECHARGEMENT", null, "OK",listServeurDispo);
		}
		else {
			reponse = new PDUAnnuaire("ANN", "TELECHARGEMENT", null, "Impossible de télécharger le fichier en entier",null);
		}
		/* On retourne la PDU traitée */
		return reponse;
	}
}
