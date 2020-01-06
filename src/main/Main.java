package main;

import client.ClientP2P;
import serveur.*;
import socket.SocketServeurTCP;
import systeme.fichiers.GestionFichier;

public class Main {

	public static void main(String[] args) {
		ClientP2P client = new ClientP2P();
		GestionFichier fichier = new GestionFichier();
		ServeurControle sc = new ServeurControle(fichier);
		ServeurDonnees sd = new ServeurDonnees();
		GestionProtocole protocole = new GestionProtocole(sc,sd);
		SocketServeurTCP serveurTCP = new SocketServeurTCP(protocole);
		serveurTCP.start();
		client.run();
	}

}
