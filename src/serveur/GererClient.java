package serveur;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import envoie.reception.*;

public class GererClient implements Runnable {
	Socket sockClient = null;
	Envoie envoieClient = null;
	Recevoir receptionClient = null;
	GestionProtocole gestion;
	PDU requete, reponse;
	Boolean quitter = false;

	public GererClient(Socket socket, GestionProtocole g) {
		sockClient = socket;
		gestion = g;
		
	}

	@Override
	public void run() {
		Envoie envoieClient = new Envoie(sockClient);
		Recevoir receptionClient = new Recevoir(sockClient);
		System.out.println("Reception du client");
		while (quitter == false) {
			requete = null;
			requete = receptionClient.RecevoirPDU();
			if (requete == null) {
				System.out.println("Erreur de réception de la PDU");
				try {
					sockClient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			} else {
				reponse = gestion.gestionRequete(requete);
				envoieClient.EnvoiePDU(reponse);
			}
		}

	}

	public Boolean FinSocket(PDU reponse) {

		return false;
	}
}
