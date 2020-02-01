package client;

import java.util.List;

import envoie.reception.*;
import socket.SocketClient;
import systeme.fichiers.Fichier;
import systeme.fichiers.GestionFichier;

/*
 * Classe Client --> Classe permettant de cr�er un ClientControle
 */

public class ClientControle {
	/* D�claration de variables */
	String transport;
	GestionFichier sysFichiers;

	/*
	 * Constructeur ClientControle --> Ce constructeur prend en param�tre le protocole de transport choisie et un GestionFichier
	 * Ce constructeur permet de cr�er un nouveau ClientControle.
	 */

	public ClientControle(String t, GestionFichier g) {
		transport = t;
		sysFichiers = g;
	}

	/*
	 * M�thode TelechargementSimple : M�thode permettant de r�aliser un simple t�l�chargement
	 * @param : le nom du fichier, l'adresse ip et le num�ro de port
	 */

	public void TelechargementSimple(String nomFichier, String ip, int port) {
		/* D�claration de variables */
		Fichier fichierDl;
		/* Cree un objet PDU pour l'envoyer au serveur */
		PDUControle simpleTel = new PDUControle("CTRL", "TSF", nomFichier, null);
		/* Cree le socket en indiquant le mode de transport (TCP ou UDP) */
		SocketClient serveur = new SocketClient(transport);
		/* Si il y a un probl�me avec l'initialisation avec le socket, l'adresse IP et le port du destinataire */
		if (serveur.InitialisationSocket(ip, port) != 0) {
			/* Affichage d'un message d'erreur */
			System.out.println("Impossible de joindre le serveur");
			return;
		}
		/* Si il y un probl�me avec l'envoie de la PDU au serveur */
		if (serveur.EnvoiePDU(simpleTel) != 0) {
			/* On ferme le socket */
			serveur.FermerSocket();
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur lors de l'envoie de la requete");
			return;
		}
		/* On initialise la variable */
		PDU reponse = null;
		/* On r�cup�re la PDU recu */
		reponse = serveur.RecevoirPDU();
		/* Si la PDU est pas nulle*/
		if (reponse == null) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de connexion avec le serveur");
			return;
			/* Si la reponse est une instance de PDU controle */
		} else if (reponse instanceof PDUControle) {
			/* On r�cup�re la PDUControle */
			simpleTel = (PDUControle) reponse;
		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de PDU");
			/* On ferme la socket */
			serveur.FermerSocket();
			return;
		}
		/* On v�rifie la reponse */
		/* Si la commande correspond � celle de TSF */
		if (simpleTel.getCommande().compareTo("TSF") == 0) {
			/* Si le fichier existe */
			if (simpleTel.getFichier() != null) {
				/* On r�cup�re le fichier */
				fichierDl = simpleTel.getFichier();
				/* On change l'emplacement du fichier pour le mettre avec les autres fichiers t�l�charg�s */
				fichierDl.setEmplacement(sysFichiers.getChemin() + fichierDl.getNomFichier());
				/* On ajoute le fichier dans GestionFichier */
				sysFichiers.AjouterFichier(simpleTel.getFichier());
				/* On cr�e un objet ClientDonnees pour signaler le t�l�chargement */
				ClientDonnees transfert = new ClientDonnees(sysFichiers, serveur);
				/* On t�l�charge le fichier */
				transfert.Dowload(fichierDl, null);
				/* On ferme le socket */
				if (sysFichiers.EtatFichier(nomFichier) != 1) {
					System.out.println("Erreur lors du telechargement du fichier");
					System.out.println("Nouvelle tentative");
					transfert.Dowload(fichierDl, null);
					if (sysFichiers.EtatFichier(nomFichier) != 1) {
						System.out.println("Erreur lors du telechargement du fichier");
						System.out.println("Impossible de t�l�charger le fichier");
					} else {
						System.out.println("Fichier t�l�charg�");
					}
				} else {
					System.out.println("Fichier t�l�charg�");
				}
				simpleTel = new PDUControle("CTRL", "TSF", "FIN", null);
				serveur.EnvoiePDU(simpleTel);
				serveur.FermerSocket();
				/* Si le fichier n'existe pas */
			} else if (simpleTel.getFichier() == null) {
				/* On affiche les donn�es */
				System.out.println(simpleTel.getDonnees());
			} else {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur de commande");
			}
		}
		return;
	}

	/*
	 * M�thode TelechargementParallele : M�thode permettant de r�aliser un t�l�chargement sur plusieurs serveurs
	 * @param : le nom du fichier, les adresses ip's et les num�ros de ports
	 */
	
	public void TelechargementParallele(String nomFichier, List<String> ip, List<Integer> port) {
		/* D�claration de variables */
		int i;
		/* On parourt la liste des adresses ip's */
		for (i = 0; i < ip.size(); i++) {
			/* On cr�e un objet ClientControleThread */
			ClientControleThread cct = new ClientControleThread(this.transport, this.sysFichiers, ip.get(i),
					port.get(i), nomFichier);
			/* On cr�e un Thread */
			Thread thread = new Thread(cct);
			/* On lance un Thread */
			thread.start();
		}
		if (sysFichiers.EtatFichier(nomFichier) != 1) {
			System.out.println("Erreur lors du t�l�chargement du fichier");
		}
		return;
	}

}
