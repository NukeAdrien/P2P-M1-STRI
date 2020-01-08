package main;

import client.ClientP2P;
import serveur.*;
import socket.SocketServeurTCP;
import systeme.fichiers.GestionFichier;

public class Main {

	public static void main(String[] args) {
		GestionFichier sysFichiers = new GestionFichier();
		ClientP2P client = new ClientP2P(sysFichiers);
		ServeurControle sc = new ServeurControle(sysFichiers);
		ServeurDonnees sd = new ServeurDonnees(sysFichiers);
		GestionProtocole protocole = new GestionProtocole(sc,sd);
		SocketServeurTCP serveurTCP = new SocketServeurTCP(protocole);
		serveurTCP.start();
		client.run();
	}

}
