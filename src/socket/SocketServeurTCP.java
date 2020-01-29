package socket;
import java.net.ServerSocket;
import java.net.Socket;

import serveur.GestionProtocole;

import java.io.IOException;

/*
 * Classe SocketClientTCP --> Cette classe permet de cr�er, d'instancier et de lancer un socket
 */

public class SocketServeurTCP extends Thread {

	/* D�claration de vaiables */
	GestionProtocole gestion;

	/*
	 * Constructeur SocketServeurTCP --> Ce constructeur prend en param�tre un gestion de protocole
	 * Ce constructeur permet de cr�er un nouveau socketClient.
	 */
	public SocketServeurTCP (GestionProtocole g) {
		gestion = g;
	}

	/* M�thode run : m�thode d'ex�cution du thread */
	public void run() {
		/* D�claration de variables locales */
		ServerSocket sockServeur = null;
		Socket sockClient = null;
		try {
			/*On initialise un serveur Socket */
			sockServeur = new ServerSocket(4444);
			try {
				/* On r�alise une boucle finie */
				while (true) {
					/* On accepte les sockets venant du client */
					sockClient = sockServeur.accept();
					/* On g�re les requ�tes du client */
					GererClient client = new GererClient(sockClient, gestion);
					/* On cr�e un thread pour lancer plusieurs clients */
					Thread pC = new Thread(client);
					/* On lance un thread */
					pC.start();
				}
			} catch (IOException ioe) {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur de accept : " + ioe.getMessage());
			}

		} catch (IOException ioe) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de cr�ation du server socket: " + ioe.getMessage());
		}
		return;
	}

}
