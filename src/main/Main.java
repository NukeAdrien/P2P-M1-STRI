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
		GestionFichier sysFichiersClient = new GestionFichier("./Upload/");
		GestionFichier sysFichiersServeur = new GestionFichier("./Telechargment/");
		/* Crée et instancie un client P2P */
		ClientP2P client = new ClientP2P(sysFichiersClient);
		/* Crée et instancie un ServeurControle et un ServeurDonnees */
		ServeurControle sc = new ServeurControle(sysFichiersServeur);
		ServeurDonnees sd = new ServeurDonnees(sysFichiersServeur);
		/* Crée et instancie un GestionProtocole */
		GestionProtocole protocole = new GestionProtocole(sc,sd);
		/* Crée et instancie un SocketServeurTCP */
		SocketServeurTCP serveurTCP = new SocketServeurTCP(protocole);
		/*Lancement du socket serveurTCP*/
		serveurTCP.start();
		/*Lancement du run se trouvant dans le ClientP2P*/
		client.run();
	}

}
