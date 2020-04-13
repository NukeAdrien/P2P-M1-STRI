package client;

import java.util.HashMap;
import java.util.Map;

import envoie.reception.PDU;
import envoie.reception.PDUDonnees;
import socket.SocketClient;
import systeme.fichiers.*;

/*
 * Classe ClientDonnees --> Cette classe permet de gérer l'arrivée des données contenues dans une PDU
 */

public class ClientDonnees {
	
	/* Déclaration de variables */
	SocketClient serveur;
	GestionFichier sysFichiers;

	/*
	 * Constructeur ClientDonnees --> Ce constructeur prend en paramètre un SocketClient et un gestion de fichier
	 * Ce constructeur permet de créer un nouveau ClientDonnees.
	 */
	public ClientDonnees(GestionFichier g, SocketClient s) {
		sysFichiers = g;
		serveur = s;
	}

	/*
	 * Constructeur ClientDonnees --> Ce constructeur prend en paramètre un gestion de fichier
	 * Ce constructeur permet de créer un nouveau ClientDonnees.
	 */
	public ClientDonnees(GestionFichier g) {
		sysFichiers = g;
	}

	/*
	 * Méthode InitialiserConnexion : Méthode permettant de tester la connexion client/serveur
	 * @param : L'adresse IP et le numéro de port à lier au socket
	 * @return : 0 si ça s'est passée, 1 sinon
	 */
	public Integer InitialiserConnexion(String ip, Integer port) {
		/* Si il y a un problème lors de l'initialisation du socket */
		if (serveur.InitialisationSocket(ip, 4444) != 0) {
			/*Affichage d'un message d'erreur */
			System.out.println("Impossible de joindre le serveur");
			return 1;
		} else {
			return 0;
		}
	}

	/*
	 * Méthode Download : Méthode permettant au client de télécharger le fichier est ses blocs de données associés.
	 * @param : Le nom du fichier et sa liste des blocs
	 * @return : 0 si ça s'est passée, 1 sinon
	 */
	
	public Integer Dowload(Fichier fichier, HashMap<Integer, HeaderBloc> listHeaderBlocs) {
		/* Si il n'y a pas de blocs encore présents */
		if (listHeaderBlocs == null) {
			/* Alors on récupère les blocs présents dans le fichier*/
			listHeaderBlocs = fichier.getListHeaderBlocs();
		}
		/*On parcourt tous les blocs*/
		for (Map.Entry<Integer,HeaderBloc> headerbloc : listHeaderBlocs.entrySet()) {
			/*On instancie une PDUDonnees*/
			PDUDonnees requete = new PDUDonnees("DATA",fichier.getNomFichier() ,(int)headerbloc.getKey(), null);
			// Envoie de la PDU au serveur
			/*Si il y a un problème lors de l'envoie de la PDU au client */
			if (serveur.EnvoiePDU(requete) != 0) {
				/*On ferme la Socket*/
				serveur.FermerSocket();
				/*Affichage d'un message d'erreur*/
				System.out.println("Erreur lors de l'envoi de la requête");
				return 1;
			}
			/* Déclaration de variables de type PDU et de PDUDonnees */
			PDU reponsePDU = null;
			PDUDonnees reponse = null;
			/* On Récupère la PDU recu */
			reponsePDU = serveur.RecevoirPDU();
			/* Si il y a un problème lors de la réception de la PDU */
			if (reponsePDU == null) {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur de connexion avec le serveur");
				return 1;
				/* Si reponsePDU est de l'instance PDUDonnees*/
			}else if(reponsePDU instanceof PDUDonnees) {
				/*On caste reponsePDU pour pouvoir récupérer les données*/
				reponse = (PDUDonnees) reponsePDU;
			}else {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur de PDU");
				serveur.FermerSocket();
				/*On ferme la socket*/
				return 1;
			}
			/* Si la variable reponse a un type de PDU correspondant à DATA */
			if (reponse.getType().compareTo("DATA") == 0) {
				/* Si il y a un problème lors de l'écriture du bloc*/
				if(sysFichiers.Ecrire(fichier,(int) headerbloc.getKey(),reponse.getBloc()) != 0) {
					/*On indique que ce bloc n'est pas disponible*/
					sysFichiers.setDisponible(fichier.getNomFichier(), (int) headerbloc.getKey(), -1);
				}
				/* Si il y a pas eu de problèmes, on indique que ce bloc est disponible*/
				sysFichiers.setDisponible(fichier.getNomFichier(), (int) headerbloc.getKey(), 1);
				/* On Incrémente le nombre de téléchargement*/
				sysFichiers.nbDowloadInc();
			/* Si la variable reponse a un type de PDU correspondant à ERR */
			} else if (reponse.getType().compareTo("ERR") == 0) {
				/* On récupère les données qu'elle a pu recevoir */
				System.out.println(reponse.getDonnees());
			} else {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur de type de la PDU");
				return 1;
			}

		}
		return 0;
	}

}
