package main;

import client.ClientP2P;
import serveur.*;
import socket.SocketServeurTCP;
import systeme.fichiers.GestionFichier;

/*
 * Classe Main --> C'est à partir de cette classe que le programme sera exécutée
 */

public class Main {

	public static void main(String[] args) {
		/* Crée et instancie un GestionFichier client et un GestionFichier serveur */
		GestionFichier sysFichiersServeur1 = new GestionFichier("./Upload/4444/");
		GestionFichier sysFichiersServeur2 = new GestionFichier("./Upload/5555/");
		sysFichiersServeur1.initGestionFichier();
		sysFichiersServeur2.initGestionFichier();
		GestionFichier sysFichiersClient = new GestionFichier("./Telechargment/");
		sysFichiersClient.initGestionFichier();
		/* Crée et instancie un client P2P */
		ClientP2P client = new ClientP2P(sysFichiersClient,4444);
		/* Crée et instancie un ServeurControle et un ServeurDonnees */
		ServeurControle sc1 = new ServeurControle(sysFichiersServeur1);
		ServeurDonnees sd1 = new ServeurDonnees(sysFichiersServeur1);
		ServeurControle sc2 = new ServeurControle(sysFichiersServeur2);
		ServeurDonnees sd2 = new ServeurDonnees(sysFichiersServeur2);
		/* Crée et instancie un GestionProtocole */
		GestionProtocole protocole1 = new GestionProtocole(sc1,sd1);
		GestionProtocole protocole2 = new GestionProtocole(sc2,sd2);
		/* Crée et instancie un SocketServeurTCP */
		SocketServeurTCP serveurTCP1 = new SocketServeurTCP(protocole1,4444);
		SocketServeurTCP serveurTCP2 = new SocketServeurTCP(protocole2,5555);
		/*Lancement du socket serveurTCP*/
		serveurTCP1.start();
		serveurTCP2.start();
		/*Lancement du run se trouvant dans le ClientP2P*/
		client.run();
	}

}
