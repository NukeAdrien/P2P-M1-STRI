package socket;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import envoie.reception.*;
import serveur.GestionProtocole;

/*
 * Classe GererClient --> Classe permettant de gérer un client
 */

public class GererClient implements Runnable {

	/* Déclaration de variables */
	Socket sockClient = null;
	Envoie envoieClient = null;
	Recevoir receptionClient = null;
	GestionProtocole gestion;
	PDU requete, reponse;
	Boolean quitter = false;
	String adresse;

	/*
	 * Constructeur GererClient --> Ce constructeur prend en paramètres un socket
	 * (UDP ou TCP) * crée ainsi qu'un gestion de Protocole
	 */
	public GererClient(Socket socket, GestionProtocole g) {
		sockClient = socket;
		gestion = g;
	}

	/* Méthode run : méthode d'exécution du thread */
	@Override
	public void run() {
		/* On envoie la PDU au client */
		Envoie envoieClient = new Envoie(sockClient);
		/* On reçoit la PDU du client */
		Recevoir receptionClient = new Recevoir(sockClient);
		/* Affichage d'un message de succès */
		System.out.println("Reception du client");
		requete = null;
		requete = receptionClient.RecevoirPDU();
		/* Tant que la connexion est toujours active */
		while (quitter == false) {
			/* Si il y a un problème lors de la réception */
			if (requete == null) {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur de réception de la PDU");
				try {
					/* On ferme le socket */
					sockClient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			} else {
				/* Si il n'y a pas de problèmes alors on va pouvoir gérer la requête */
				adresse = sockClient.getInetAddress().toString();
				adresse = adresse + ":";
				adresse = adresse + sockClient.getPort();
				reponse = gestion.gestionRequete(requete, adresse);
				/*
				 * Après la gestion de la requete, on envoie la PDU au client pour une
				 * éventuelle réception
				 */
				envoieClient.EnvoiePDU(reponse);
			}
			/* Initialisation d'une requête */
			requete = null;
			requete = receptionClient.RecevoirPDU();
			if (requete.getDonnees().compareTo("FIN") == 0) {
				quitter = true;
			}
		}
		try {
			sockClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return;

	}

	public Boolean FinSocket(PDU reponse) {
		return false;
	}
}
