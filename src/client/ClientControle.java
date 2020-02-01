package client;

import java.util.List;

import envoie.reception.*;
import socket.SocketClient;
import systeme.fichiers.Fichier;
import systeme.fichiers.GestionFichier;

/*
 * Classe Client --> Classe permettant de créer un ClientControle
 */

public class ClientControle {
	/* Déclaration de variables */
	String transport;
	GestionFichier sysFichiers;

	/*
	 * Constructeur ClientControle --> Ce constructeur prend en paramètre le protocole de transport choisie et un GestionFichier
	 * Ce constructeur permet de créer un nouveau ClientControle.
	 */

	public ClientControle(String t, GestionFichier g) {
		transport = t;
		sysFichiers = g;
	}

	/*
	 * Méthode TelechargementSimple : Méthode permettant de réaliser un simple téléchargement
	 * @param : le nom du fichier, l'adresse ip et le numéro de port
	 */

	public void TelechargementSimple(String nomFichier, String ip, int port) {
		/* Déclaration de variables */
		Fichier fichierDl;
		/* Cree un objet PDU pour l'envoyer au serveur */
		PDUControle simpleTel = new PDUControle("CTRL", "TSF", nomFichier, null);
		/* Cree le socket en indiquant le mode de transport (TCP ou UDP) */
		SocketClient serveur = new SocketClient(transport);
		/* Si il y a un problème avec l'initialisation avec le socket, l'adresse IP et le port du destinataire */
		if (serveur.InitialisationSocket(ip, port) != 0) {
			/* Affichage d'un message d'erreur */
			System.out.println("Impossible de joindre le serveur");
			return;
		}
		/* Si il y un problème avec l'envoie de la PDU au serveur */
		if (serveur.EnvoiePDU(simpleTel) != 0) {
			/* On ferme le socket */
			serveur.FermerSocket();
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur lors de l'envoie de la requete");
			return;
		}
		/* On initialise la variable */
		PDU reponse = null;
		/* On récupère la PDU recu */
		reponse = serveur.RecevoirPDU();
		/* Si la PDU est pas nulle*/
		if (reponse == null) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de connexion avec le serveur");
			return;
			/* Si la reponse est une instance de PDU controle */
		} else if (reponse instanceof PDUControle) {
			/* On récupère la PDUControle */
			simpleTel = (PDUControle) reponse;
		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de PDU");
			/* On ferme la socket */
			serveur.FermerSocket();
			return;
		}
		/* On vérifie la reponse */
		/* Si la commande correspond à celle de TSF */
		if (simpleTel.getCommande().compareTo("TSF") == 0) {
			/* Si le fichier existe */
			if (simpleTel.getFichier() != null) {
				/* On récupère le fichier */
				fichierDl = simpleTel.getFichier();
				/* On change l'emplacement du fichier pour le mettre avec les autres fichiers téléchargés */
				fichierDl.setEmplacement(sysFichiers.getChemin() + fichierDl.getNomFichier());
				/* On ajoute le fichier dans GestionFichier */
				sysFichiers.AjouterFichier(simpleTel.getFichier());
				/* On crée un objet ClientDonnees pour signaler le téléchargement */
				ClientDonnees transfert = new ClientDonnees(sysFichiers, serveur);
				/* On télécharge le fichier */
				transfert.Dowload(fichierDl, null);
				/* On ferme le socket */
				serveur.FermerSocket();
				/* Si le fichier n'existe pas */
			} else if (simpleTel.getFichier() == null) {
				/* On affiche les données */
				System.out.println(simpleTel.getDonnees());
			} else {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur de commande");
			}
		}
		return;
	}

	/*
	 * Méthode TelechargementParallele : Méthode permettant de réaliser un téléchargement sur plusieurs serveurs
	 * @param : le nom du fichier, les adresses ip's et les numéros de ports
	 */
	
	public void TelechargementParallele(String nomFichier, List<String> ip, List<Integer> port) {
		/* Déclaration de variables */
		int i;
		/* On parourt la liste des adresses ip's */
		for (i = 0; i > ip.size(); i++) {
			/* On crée un objet ClientControleThread */
			ClientControleThread cct = new ClientControleThread(this.transport, this.sysFichiers, ip.get(i),
					port.get(i), nomFichier);
			/* On crée un Thread */
			Thread thread = new Thread(cct);
			/* On lance un Thread */
			thread.start();
		}
		/* On réinitialise i */
		i=0;
		/* Tant que le fichier n'est pas dispo ou tant qu'on pas parcouru toute la liste des adresses ip */
		while(sysFichiers.EtatFichier(nomFichier) != 1 && i != ip.size()) {
			try {
				/* On attend */
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/* On incrémente i*/
			i++;
		}
		/* Si le fichier n'est pas disponible */
		if(sysFichiers.EtatFichier(nomFichier) != 1) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur lors du téléchargement du fichier");
		}
		return;
	}

}
