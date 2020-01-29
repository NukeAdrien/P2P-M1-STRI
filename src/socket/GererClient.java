package socket;

import java.io.IOException;
import java.net.Socket;

import envoie.reception.*;
import serveur.GestionProtocole;

/*
 * Classe GererClient --> Classe permettant de g�rer un client
 */

public class GererClient implements Runnable {
	
	/* D�claration de variables */
	Socket sockClient = null;
	Envoie envoieClient = null;
	Recevoir receptionClient = null;
	GestionProtocole gestion;
	PDU requete, reponse;
	Boolean quitter = false;
	
	/*
	 * Constructeur GererClient --> Ce constructeur prend en param�tres un socket (UDP ou TCP) *
	 * cr�e ainsi qu'un gestion de Protocole
	 */
	public GererClient(Socket socket, GestionProtocole g) {
		sockClient = socket;
		gestion = g;
	}

	/* M�thode run : m�thode d'ex�cution du thread */
	@Override
	public void run() {
		/* On envoie la PDU au client */
		Envoie envoieClient = new Envoie(sockClient);
		/* On re�oit la PDU du client */
		Recevoir receptionClient = new Recevoir(sockClient);
		/* Affichage d'un message de succ�s */
		System.out.println("Reception du client");
		/* Tant que la connexion est toujours active */
		while (quitter == false) {
			/* Initialisation d'une requ�te */
			requete = null;
			/* On re�oit la PDU � partir de la variable pr�c�demment cr�e*/
			requete = receptionClient.RecevoirPDU();
			/* Si il y a un probl�me lors de la r�ception */
			if (requete == null) {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur de r�ception de la PDU");
				try {
					/* On ferme le socket */
					sockClient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			} else {
				/* Si il n'y a pas de probl�mes alors on va pouvoir g�rer la requ�te*/
				reponse = gestion.gestionRequete(requete);
				/* Apr�s la gestion de la requete, on envoie la PDU au client pour une �ventuelle r�ception */
				envoieClient.EnvoiePDU(reponse);
			}
		}

	}

	public Boolean FinSocket(PDU reponse) {
		return false;
	}
}
