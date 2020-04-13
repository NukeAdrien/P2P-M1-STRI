package client;

import java.util.HashMap;
import java.util.Map;

import envoie.reception.PDU;
import envoie.reception.PDUDonnees;
import socket.SocketClient;
import systeme.fichiers.*;

/*
 * Classe ClientDonnees --> Cette classe permet de g�rer l'arriv�e des donn�es contenues dans une PDU
 */

public class ClientDonnees {
	
	/* D�claration de variables */
	SocketClient serveur;
	GestionFichier sysFichiers;

	/*
	 * Constructeur ClientDonnees --> Ce constructeur prend en param�tre un SocketClient et un gestion de fichier
	 * Ce constructeur permet de cr�er un nouveau ClientDonnees.
	 */
	public ClientDonnees(GestionFichier g, SocketClient s) {
		sysFichiers = g;
		serveur = s;
	}

	/*
	 * Constructeur ClientDonnees --> Ce constructeur prend en param�tre un gestion de fichier
	 * Ce constructeur permet de cr�er un nouveau ClientDonnees.
	 */
	public ClientDonnees(GestionFichier g) {
		sysFichiers = g;
	}

	/*
	 * M�thode InitialiserConnexion : M�thode permettant de tester la connexion client/serveur
	 * @param : L'adresse IP et le num�ro de port � lier au socket
	 * @return : 0 si �a s'est pass�e, 1 sinon
	 */
	public Integer InitialiserConnexion(String ip, Integer port) {
		/* Si il y a un probl�me lors de l'initialisation du socket */
		if (serveur.InitialisationSocket(ip, 4444) != 0) {
			/*Affichage d'un message d'erreur */
			System.out.println("Impossible de joindre le serveur");
			return 1;
		} else {
			return 0;
		}
	}

	/*
	 * M�thode Download : M�thode permettant au client de t�l�charger le fichier est ses blocs de donn�es associ�s.
	 * @param : Le nom du fichier et sa liste des blocs
	 * @return : 0 si �a s'est pass�e, 1 sinon
	 */
	
	public Integer Dowload(Fichier fichier, HashMap<Integer, HeaderBloc> listHeaderBlocs) {
		/* Si il n'y a pas de blocs encore pr�sents */
		if (listHeaderBlocs == null) {
			/* Alors on r�cup�re les blocs pr�sents dans le fichier*/
			listHeaderBlocs = fichier.getListHeaderBlocs();
		}
		/*On parcourt tous les blocs*/
		for (Map.Entry<Integer,HeaderBloc> headerbloc : listHeaderBlocs.entrySet()) {
			/*On instancie une PDUDonnees*/
			PDUDonnees requete = new PDUDonnees("DATA",fichier.getNomFichier() ,(int)headerbloc.getKey(), null);
			// Envoie de la PDU au serveur
			/*Si il y a un probl�me lors de l'envoie de la PDU au client */
			if (serveur.EnvoiePDU(requete) != 0) {
				/*On ferme la Socket*/
				serveur.FermerSocket();
				/*Affichage d'un message d'erreur*/
				System.out.println("Erreur lors de l'envoi de la requ�te");
				return 1;
			}
			/* D�claration de variables de type PDU et de PDUDonnees */
			PDU reponsePDU = null;
			PDUDonnees reponse = null;
			/* On R�cup�re la PDU recu */
			reponsePDU = serveur.RecevoirPDU();
			/* Si il y a un probl�me lors de la r�ception de la PDU */
			if (reponsePDU == null) {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur de connexion avec le serveur");
				return 1;
				/* Si reponsePDU est de l'instance PDUDonnees*/
			}else if(reponsePDU instanceof PDUDonnees) {
				/*On caste reponsePDU pour pouvoir r�cup�rer les donn�es*/
				reponse = (PDUDonnees) reponsePDU;
			}else {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur de PDU");
				serveur.FermerSocket();
				/*On ferme la socket*/
				return 1;
			}
			/* Si la variable reponse a un type de PDU correspondant � DATA */
			if (reponse.getType().compareTo("DATA") == 0) {
				/* Si il y a un probl�me lors de l'�criture du bloc*/
				if(sysFichiers.Ecrire(fichier,(int) headerbloc.getKey(),reponse.getBloc()) != 0) {
					/*On indique que ce bloc n'est pas disponible*/
					sysFichiers.setDisponible(fichier.getNomFichier(), (int) headerbloc.getKey(), -1);
				}
				/* Si il y a pas eu de probl�mes, on indique que ce bloc est disponible*/
				sysFichiers.setDisponible(fichier.getNomFichier(), (int) headerbloc.getKey(), 1);
				/* On Incr�mente le nombre de t�l�chargement*/
				sysFichiers.nbDowloadInc();
			/* Si la variable reponse a un type de PDU correspondant � ERR */
			} else if (reponse.getType().compareTo("ERR") == 0) {
				/* On r�cup�re les donn�es qu'elle a pu recevoir */
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
