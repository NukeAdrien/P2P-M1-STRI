package socket;
import java.net.ServerSocket;
import java.net.Socket;

import serveur.GestionProtocole;

import java.io.IOException;

/*
 * Classe SocketTCP --> Cette classe permet de cr�er, d'instancier et de lancer un socket
 */

public class SocketServeurTCP extends Thread {

	/* D�claration de vaiables */
	GestionProtocole gestion;
	int port;

	/*
	 * Constructeur SocketServeurTCP --> Ce constructeur prend en param�tre un gestion de protocole
	 * Ce constructeur permet de cr�er un nouveau SocketServeurTCP.
	 */
	public SocketServeurTCP (GestionProtocole g,int p) {
		gestion = g;
		this.port = p;
	}

	/* M�thode run : m�thode d'ex�cution du thread */
	public void run() {
		/* D�claration de variables locales */
		ServerSocket sockServeur = null;
		Socket sockClient = null;
		try {
			/*On initialise un serveur Socket */
			sockServeur = new ServerSocket(port);
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
				System.out.println("Erreur d'accept : " + ioe.getMessage());
			}

		} catch (IOException ioe) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de cr�ation du Server Socket: " + ioe.getMessage());
		}
		return;
	}

}
