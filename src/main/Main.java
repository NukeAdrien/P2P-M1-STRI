package main;

import client.ClientP2P;
import serveur.*;
import socket.SocketServeurTCP;
import systeme.fichiers.GestionFichier;

public class Main {

	public static void main(String[] args) {
		GestionFichier sysFichiersClient = new GestionFichier("./Upload/");
		GestionFichier sysFichiersServeur = new GestionFichier("./Telechargment/");
		ClientP2P client = new ClientP2P(sysFichiersClient);
		ServeurControle sc = new ServeurControle(sysFichiersServeur);
		ServeurDonnees sd = new ServeurDonnees(sysFichiersServeur);
		GestionProtocole protocole = new GestionProtocole(sc,sd);
		SocketServeurTCP serveurTCP = new SocketServeurTCP(protocole);
		serveurTCP.start();
		client.run();
	}

}
