package serveur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import envoie.reception.PDUAnnuaire;
import systeme.fichiers.Fichier;
import systeme.fichiers.GestionFichier;
import systeme.fichiers.HeaderBloc;

public class ServeurAnnuaire {
	/* D�claration de variables */
	GestionFichier gestionFichier;
	HashMap<String, GestionFichier> listServeurs;
	PDUAnnuaire reponse;

	/*
	 * Constructeur ServeuControle --> Ce constructeur prend en param�tre un gestion
	 * de fichier Ce constructeur permet de cr�er un nouveau ServeurControle .
	 */
	public ServeurAnnuaire(GestionFichier gf) {
		gestionFichier = gf;
		listServeurs = new HashMap<String, GestionFichier>();
	}

	public PDUAnnuaire Inscription(PDUAnnuaire pdu, String adresse) {
		adresse = adresse+":"+pdu.getDonnees();
		if (listServeurs.get(adresse) == null) {
			listServeurs.put(adresse, pdu.getSysFichiers());
			if (listServeurs.get(adresse) == null) {
				reponse = new PDUAnnuaire("ANN", "REGISTRATION", null, "NOK", null);
			} else {
				reponse = new PDUAnnuaire("ANN", "REGISTRATION", null, "OK", null);
			}
		} else {
			listServeurs.get(adresse).setListFichier(pdu.getSysFichiers().getListFichier());
			reponse = new PDUAnnuaire("ANN", "REGISTRATION", this.listServeurs.get(adresse), "MAJ", null);
		}
		return reponse;
	}

	public PDUAnnuaire Search(PDUAnnuaire pdu) {

		return null;
	}

	public PDUAnnuaire Dowload(PDUAnnuaire pdu) {
		/* D�claration de variables */
		PDUAnnuaire reponse = null;
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
				}
				for (Map.Entry<Integer, HeaderBloc> headerbloc : fichier.getListHeaderBlocs().entrySet()) {
					if (headerbloc.getValue().getDisponible() == 1) {
						fichierFinal.getListHeaderBlocs().get(headerbloc.getKey()).setDisponible(1);
						i++;
					}
				}
			}
			if (i != 0) {
				listServeurDispo.add(listServeur.getKey());
			}
			fichier = null;
			i = 0;
		}
		if(fichierFinal == null) {
			reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Fichier inconnu des autre serveurs", null);
		}
		else if (gestionFichier.EtatFichier(fichierFinal) == 1) {
			reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Le fichier va etre telecharge", listServeurDispo);
		} else {
			reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Impossible de t�l�charger le fichier en entier", null);
		}
		/* On retourne la PDU trait�e */
		return reponse;
	}
}
