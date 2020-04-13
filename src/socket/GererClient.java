package socket;

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
		/* On cree la PDU au client */
		Envoie envoieClient = new Envoie(sockClient);
		/* On cree la PDU du client */
		Recevoir receptionClient = new Recevoir(sockClient);
		/* On initialise la variable requete */
		requete = null;
		/* On reçoit la PDU du client */
		requete = receptionClient.RecevoirPDUTCP();
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
					e.printStackTrace();
				}
				return;
			} else {
				/* Si il n'y a pas de problèmes alors on va pouvoir gérer la requête */
				adresse = sockClient.getInetAddress().toString();
				adresse = adresse.substring(1);
				reponse = gestion.gestionRequete(requete, adresse);
				/*
				 * Après la gestion de la requete, on envoie la PDU au client pour une
				 * réception
				 */
				envoieClient.EnvoiePDUTCP(reponse);
			}
			/* Initialisation d'une requête */
			requete = null;
			/* On reçoit la PDU du client */
			requete = receptionClient.RecevoirPDUTCP();
			/* Si tous les données ont été transmises */
			if (requete.getDonnees().compareTo("FIN") == 0) {
				/* On peut fermer la connexion */
				quitter = true;
			}
		}
		try {
			/* On ferme le socket */
			sockClient.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
		return;

	}
}
