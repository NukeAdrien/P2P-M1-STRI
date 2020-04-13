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
	/* D�claration de variables */
	GestionFichier gestionFichier;
	HashMap<String, GestionFichier> listServeurs;
	PDUAnnuaire reponse;
	
	/*
	 * Constructeur ServeuControle --> Ce constructeur prend en param�tre un gestionnaire
	 * de fichiers. Ce constructeur permet de cr�er un nouveau ServeurAnnuaire .
	 */
	
	public ServeurAnnuaire(GestionFichier gf) {
		gestionFichier = gf;
		listServeurs = new HashMap<String, GestionFichier>();
	}

	/*
	 * Methode Inscription : Permet de s'inscrire aupr�s d'un serveur annuaire
	 * @param : La PDUAnnuaire � traiter ainsi que l'adresse IP du serveur � ajouter
	 * @return : La PDUAnnuaire trait�e
	 */
	
	public PDUAnnuaire Inscription(PDUAnnuaire pdu, String adresse) {
		/* Recup�ration des donn�es de la PDU */
		adresse = adresse+":"+pdu.getDonnees();
		/* Si l'adresse n'est pas encore pr�sente dans la liste */
		if (listServeurs.get(adresse) == null) {
			/* On ajoute cette nouvelle adresse */
			listServeurs.put(adresse, pdu.getSysFichiers());
			/* Si il un probl�me lors de l'enregistrement */
			if (listServeurs.get(adresse) == null) {
				/* On envoie une PDU NOK*/
				reponse = new PDUAnnuaire("ANN", "REGISTRATION", null, "NOK", null);
			} else {
				/*Sinon on envoie une PDU OK */
				reponse = new PDUAnnuaire("ANN", "REGISTRATION", null, "OK", null);
			}
			/* Si l'adresse existe d�j� */
		} else {
			/* On modifie la liste des fichiers existants par celui du syst�me de fichier pr�c�demment cr��e */
			listServeurs.get(adresse).setListFichier(pdu.getSysFichiers().getListFichier());
			/* Puis on effectue la MAJ des fichiers */
			reponse = new PDUAnnuaire("ANN", "REGISTRATION", this.listServeurs.get(adresse), "MAJ", null);
		}
		return reponse;
	}
	
	/*
	 * M�thode Search : Permet la recherche d'un �l�ment dans l'annuaire
	 * @param : La PDU � traiter
	 * @return : La PDU trait�e
	 */

	public PDUAnnuaire Search(PDUAnnuaire pdu) {

		return null;
	}

	/*
	 * M�thode Dowload : Permet le t�l�chargement d'un �l�ment dans l'annuaire
	 * @param : La PDU � traiter ainsi que l'adresse du serveur sur lequel se trouve le fichier
	 * @return : La PDU trait�e
	 */
	
	public PDUAnnuaire Dowload(PDUAnnuaire pdu, String adresse) {
		adresse = adresse+":"+pdu.getListServeurs().get(0);
		/* D�claration de variables */
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
			/* On recherche la pr�sence de fichiers */
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
						/* Alors chaque bloc constituant le ficher est disponible �galement */
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
			/* On r�initialise les param�tres */
			fichier = null;
			i = 0;
		}
		/* Si le fichier est introuvable */
		if(fichierFinal == null) {
			/* On cr�e une PDU indiquant la situation */
			reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Fichier inconnu des autres serveurs", null);
		}
		/* Si le fichier est disponible */
		else if (gestionFichier.EtatFichier(fichierFinal) == 1) {
			/* On calcule le ratio */
			ratio  = ((nbU+625)/nbD)*100;
			/* Si le ratio est sup�rieur � 50 */
			if(ratio >= 50) {
				/* Le t�l�chargement peut d�buter */
				reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Le fichier va etre telecharge", listServeurDispo);
			}
			/* Si le ratio est sup�rieur � 25 */
			else if (ratio >= 25) {
				/* Le t�l�chargement pourra d�buter dans 30 secs */
				reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Le fichier va etre telecharge dans 30 secondes", listServeurDispo);
			}
			else {
				/* Sinon annulation du t�l�chargement car son ratio est trop bas */
				reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Vous ne pouvez pas t�l�charg� pour l'instant car votre ratio de Uplaod/T�l�chargement est inferieur a 25% ", null);
			}
		} else {
			/* Si le fichier n'est pas disponible ou partiellement disponible */
			reponse = new PDUAnnuaire("ANN", "DOWLOAD", null, "Impossible de t�l�charger le fichier en entier", null);
		}
		/* On retourne la PDU trait�e */
		return reponse;
	}
}