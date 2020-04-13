package serveur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import envoie.reception.PDUAnnuaire;
import systeme.fichiers.Fichier;
import systeme.fichiers.GestionFichier;
import systeme.fichiers.HeaderBloc;

/*
 * Classe ServeurAnnuaire : Permet d'instancier un nouveau serveur Annuaire
 */

public class ServeurAnnuaire {
	/* Déclaration de variables */
	GestionFichier gestionFichier;
	HashMap<String, GestionFichier> listServeurs;
	PDUAnnuaire reponse;
	
	/*
	 * Constructeur ServeuControle --> Ce constructeur prend en paramètre un gestionnaire
	 * de fichiers. Ce constructeur permet de créer un nouveau ServeurAnnuaire .
	 */
	
	public ServeurAnnuaire(GestionFichier gf) {
		gestionFichier = gf;
		listServeurs = new HashMap<String, GestionFichier>();
	}

	/*
	 * Methode Inscription : Permet de s'inscrire auprès d'un serveur annuaire
	 * @param : La PDUAnnuaire à traiter ainsi que l'adresse IP du serveur à ajouter
	 * @return : La PDUAnnuaire traitée
	 */
	
	public PDUAnnuaire Inscription(PDUAnnuaire pdu, String adresse) {
		/* Recupération des données de la PDU */
		adresse = adresse+":"+pdu.getDonnees();
		/* Si l'adresse n'est pas encore présente dans la liste */
		if (listServeurs.get(adresse) == null) {
			/* On ajoute cette nouvelle adresse */
			listServeurs.put(adresse, pdu.getSysFichiers());
			/* Si il un problème lors de l'enregistrement */
			if (listServeurs.get(adresse) == null) {
				/* On envoie une PDU NOK*/
				reponse = new PDUAnnuaire("ANN", "REGISTRATION", null, "NOK", null);
			} else {
				/*Sinon on envoie une PDU OK */
				reponse = new PDUAnnuaire("ANN", "REGISTRATION", null, "OK", null);
			}
			/* Si l'adresse existe déjà */
		} else {
			/* On modifie la liste des fichiers existants par celui du système de fichier précédemment créée */
			listServeurs.get(adresse).setListFichier(pdu.getSysFichiers().getListFichier());
			/* Puis on effectue la MAJ des fichiers */
			reponse = new PDUAnnuaire("ANN", "REGISTRATION", this.listServeurs.get(adresse), "MAJ", null);
		}
		return reponse;
	}
	
	/*
	 * Méthode Search : Permet la recherche d'un élément dans l'annuaire
	 * @param : La PDU à traiter
	 * @return : La PDU traitée
	 */

	public PDUAnnuaire Search(PDUAnnuaire pdu) {

		return null;
	}

	/*
	 * Méthode Dowload : Permet le téléchargement d'un élément dans l'annuaire
	 * @param : La PDU à traiter ainsi que l'adresse du serveur sur lequel se trouve le fichier
	 * @return : La PDU traitée
	 */
	
	public PDUAnnuaire Dowload(PDUAnnuaire pdu, String adresse) {
		adresse = adresse+":"+pdu.getListServeurs().get(0);
		/* Déclaration de variables */
		PDUAnnuaire reponse = null;
		int i = 0;
		Fichier fichier = null;
		Fichier fichierFinal = null;
		
		double ratio;
		double nbU,nbD;
		nbD = this.listServeurs.get(adresse).getNbDowload();
		if(nbD > pdu.getSysFichiers().getNbDowload()) {
			reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Vous trichez !", null);
			return reponse;
		}else {
			listServeurs.get(adresse).setListFichier(pdu.getSysFichiers().getListFichier());
			nbU = pdu.getSysFichiers().getNbUpload();
			nbD = pdu.getSysFichiers().getNbDowload();
			this.listServeurs.get(adresse).setNbUpload((int)nbU);
			this.listServeurs.get(adresse).setNbDowload((int)nbD);
		}
		
		/* On parcourt la liste des adresses ip's */
		List<String> listServeurDispo = new ArrayList<String>();
		/* On parcourt les headers blocs */
		for (Map.Entry<String, GestionFichier> listServeur : listServeurs.entrySet()) {
			/* On recherche la présence de fichiers */
			fichier = listServeur.getValue().RechercheFichier(pdu.getDonnees());
			/* Si le fichier n'existe pas */
			if (fichier != null) {
				if (fichierFinal == null) {
					/* On clone le fichier existant */
					fichierFinal = (Fichier) fichier.clone();
				}
				/* On parcourt les headers blocs */
				for (Map.Entry<Integer, HeaderBloc> headerbloc : fichier.getListHeaderBlocs().entrySet()) {
					/* Si le bloc est disponible */
					if (headerbloc.getValue().getDisponible() == 1) {
						/* Alors chaque bloc constituant le ficher est disponible également */
						fichierFinal.getListHeaderBlocs().get(headerbloc.getKey()).setDisponible(1);
						i++;
					}
				}
			}
			/* 
			 * On ajoute tous les fichiers dispos dans une autre liste de serveurs
			 * Il contient donc tous les serveurs dnas lesquels un fichier est disponible
			 */
			if (i != 0) {
				listServeurDispo.add(listServeur.getKey());
			}
			/* On réinitialise les paramètres */
			fichier = null;
			i = 0;
		}
		/* Si le fichier est introuvable */
		if(fichierFinal == null) {
			/* On crée une PDU indiquant la situation */
			reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Fichier inconnu des autres serveurs", null);
		}
		/* Si le fichier est disponible */
		else if (gestionFichier.EtatFichier(fichierFinal) == 1) {
			/* On calcule le ratio */
			ratio  = ((nbU+625)/nbD)*100;
			/* Si le ratio est supérieur à 50 */
			if(ratio >= 50) {
				/* Le téléchargement peut débuter */
				reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Le fichier va etre telecharge", listServeurDispo);
			}
			/* Si le ratio est supérieur à 25 */
			else if (ratio >= 25) {
				/* Le téléchargement pourra débuter dans 30 secs */
				reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Le fichier va etre telecharge dans 30 secondes", listServeurDispo);
			}
			else {
				/* Sinon annulation du téléchargement car son ratio est trop bas */
				reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Vous ne pouvez pas téléchargé pour l'instant car votre ratio de Uplaod/Téléchargement est inferieur a 25% ", null);
			}
		} else {
			/* Si le fichier n'est pas disponible ou partiellement disponible */
			reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Impossible de télécharger le fichier en entier", null);
		}
		/* On retourne la PDU traitée */
		return reponse;
	}
}