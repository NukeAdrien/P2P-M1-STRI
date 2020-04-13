package socket;
import java.net.ServerSocket;
import java.net.Socket;

import serveur.GestionProtocole;

import java.io.IOException;

/*
 * Classe SocketTCP --> Cette classe permet de créer, d'instancier et de lancer un socket
 */

public class SocketServeurTCP extends Thread {

	/* Déclaration de vaiables */
	GestionProtocole gestion;
	int port;

	/*
	 * Constructeur SocketServeurTCP --> Ce constructeur prend en paramètre un gestion de protocole
	 * Ce constructeur permet de créer un nouveau SocketServeurTCP.
	 */
	public SocketServeurTCP (GestionProtocole g,int p) {
		gestion = g;
		this.port = p;
	}

	/* Méthode run : méthode d'exécution du thread */
	public void run() {
		/* Déclaration de variables locales */
		ServerSocket sockServeur = null;
		Socket sockClient = null;
		try {
			/*On initialise un serveur Socket */
			sockServeur = new ServerSocket(port);
			try {
				/* On réalise une boucle finie */
				while (true) {
					/* On accepte les sockets venant du client */
					sockClient = sockServeur.accept();
					/* On gère les requêtes du client */
					GererClient client = new GererClient(sockClient, gestion);
					/* On crée un thread pour lancer plusieurs clients */
					Thread pC = new Thread(client);
					/* On lance un thread */
					pC.start();
				}
			} catch (IOException ioe) {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur d'accept : " + ioe.getMessage());
			}

		} catch (IOException ioe) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de création du Server Socket: " + ioe.getMessage());
		}
		return;
	}

}
