package client;

import java.util.HashMap;
import java.util.Map;

import envoie.reception.PDU;
import envoie.reception.PDUControle;
import socket.SocketClient;
import systeme.fichiers.Fichier;
import systeme.fichiers.GestionFichier;
import systeme.fichiers.HeaderBloc;


/*
 * Classe ClientControleThread --> Classe permettant de cr�er un ClientControleThread
 */

public class ClientControleThread implements Runnable {
	/* D�claration de variables */
	String transport;
	GestionFichier sysFichiers;
	String nomFichier;
	String ip;
	int port;
	
	/*
	 * Constructeur ClientControleThread --> Ce constructeur prend en param�tre le protocole de transport choisie 
	 * un GestionFichier, le nom du fichier, l'adresse ip et le port
	 * Ce constructeur permet de cr�er un nouveau ClientControleThread.
	 */

	public ClientControleThread(String t, GestionFichier g,String i,int p,String nf) {
		this.transport = t;
		this.sysFichiers = g;
		this.nomFichier = nf;
		this.ip = i;
		this.port=p;
	}
	
	/* M�thode run : m�thode d'ex�cution du thread */

	public void run() {
		/* D�claration de variables */
		Fichier fichierDl;
		/* On Cree un objet PDU pour l'envoyer au serveur */
		PDUControle requete = new PDUControle("CTRL","TPF", nomFichier, null);
		/* On Cree le socket en indiquant le mode de transport (TCP ou UDP) */
		SocketClient serveur = new SocketClient(transport);
		/* Si il y a un probl�me avec l'initialisation du socket, l'adresse IP et le port du destinataire */
		if (serveur.InitialisationSocket(ip, port) != 0) {
			/* Affichage d'un message d'erreur */
			System.out.println("Impossible de joindre le serveur");
			return;
		}
		/* Si il y un probl�me avec l'envoie de la PDU au serveur */
		if(serveur.EnvoiePDU(requete) != 0) {
			/* On ferme le socket */
			serveur.FermerSocket();
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur lors de l'envoie de la requete");
			return;
		}
		/* On initialise la variable */
		PDU reponse = null;
		/* On r�cup�re la PDU recu */
		reponse  = serveur.RecevoirPDU();
		/* Si la PDU est pas nulle*/
		if (reponse == null) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de connexion avec le serveur "+this.ip+":"+this.port);
			return;
			/* Si la reponse est une instance de PDU controle */
		}else if(reponse instanceof PDUControle) {
			/* On r�cup�re la PDUControle */
			requete = (PDUControle) reponse;
		}else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de PDU");
			/* On ferme la socket */
			serveur.FermerSocket();
			return;
		}
		/* V�rification de la reponse */
		/* Si la commande correspond � celle de TSF */

		if (requete.getCommande().compareTo("TSF") == 0) {
			/* Si le fichier existe */
			if (requete.getFichier() != null) { 
				/* On r�cup�re le fichier */
				fichierDl = requete.getFichier();
				/* Si le fichier n'existe pas dans GestionFichier */
				if(sysFichiers.RechercheFichier(nomFichier) == null) {
					/* On change l'emplacement du fichier pour le mettre avec les autres fichiers t�l�charg�s */
					fichierDl.setEmplacement(sysFichiers.getChemin()+fichierDl.getNomFichier());
					/* On ajoute le fichier dans GestionFichier */
					sysFichiers.AjouterFichier(requete.getFichier());
					/* On parcourt les headers blocs contenues dans le fichier */
					for(Map.Entry<Integer,HeaderBloc> headerbloc : fichierDl.getListHeaderBlocs().entrySet()) {
						/* On indique que les blocs ne sont pas disponible */
						fichierDl.setDisponible(headerbloc.getKey(), -1);
					}
				}
				/* On cr�e une liste d'headers blocs */
				HashMap<Integer, HeaderBloc> listHeaderBlocs = new HashMap<Integer, HeaderBloc>(); 
				/* On parcourt les headers blocs*/
				for(Map.Entry<Integer,HeaderBloc> headerbloc : sysFichiers.getListFichier().get(nomFichier).getListHeaderBlocs().entrySet()) {
					/* Si le bloc n'est pas disponible mais que le fichier contient des blocs qui sont disponibles */
					if(headerbloc.getValue().getDisponible() == -1 && requete.getFichier().getListHeaderBlocs().get(headerbloc.getKey()).getDisponible() == 1) {
						/* On indique que le fichier est en cours de t�l�chargement */
						sysFichiers.getListFichier().get(nomFichier).getListHeaderBlocs().get(headerbloc.getKey()).setDisponible(0);
						/* On ajoute ces blocs dans la hashMap */
						listHeaderBlocs.put(headerbloc.getKey(), headerbloc.getValue());
					}
				}
				/*Si il n'y a pas de blocs */
				if(listHeaderBlocs.size() == 0) {
					/* Affichage d'un message d'erreur */
					System.out.println(ip+":"+port+" Ne possede aucun bloc du fichier");
					return;
				}
				/* Cr�ation d'un objet ClientDonnees */
				ClientDonnees transfert = new ClientDonnees(sysFichiers,serveur);
				/* T�l�chargement du fichier */
				transfert.Dowload(fichierDl, null);
				/* Fermeture du socket */
				serveur.FermerSocket();
				/*Si le fichier n'existe pas */
			} else if (requete.getFichier() == null) {
				/* On affiche les donn�es du fichier */
				System.out.println(requete.getDonnees());
			} else {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur de commande");
			}
		}
		
	}

}
