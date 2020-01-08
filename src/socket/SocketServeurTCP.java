package socket;
import java.net.ServerSocket;
import java.net.Socket;

import serveur.GestionProtocole;

import java.io.IOException;

public class SocketServeurTCP extends Thread {

	GestionProtocole gestion;

	public SocketServeurTCP (GestionProtocole g) {
		gestion = g;
	}

	public void run() {
		ServerSocket sockServeur = null;
		Socket sockClient = null;
		try {
			sockServeur = new ServerSocket(4444);
			try {
				while (true) {
					sockClient = sockServeur.accept();
					GererClient client = new GererClient(sockClient, gestion);
					Thread pC = new Thread(client);
					pC.start();
				}
			} catch (IOException ioe) {
				System.out.println("Erreur de accept : " + ioe.getMessage());
			}

		} catch (IOException ioe) {
			System.out.println("Erreur de création du server socket: " + ioe.getMessage());
		}
		return;
	}

}
