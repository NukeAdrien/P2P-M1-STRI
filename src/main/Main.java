package main;

import client.ClientP2P;
import serveur.*;
import socket.SocketServeurTCP;
import systeme.fichiers.GestionFichier;

/*
 * Classe Main --> C'est � partir de cette classe que le programme sera ex�cut�e
 */

public class Main {

	public static void main(String[] args) {
		/* Cr�e et instancie un GestionFichier client et un GestionFichier serveur */
		GestionFichier sysFichiersClient = new GestionFichier("./Upload/");
		GestionFichier sysFichiersServeur = new GestionFichier("./Telechargment/");
		/* Cr�e et instancie un client P2P */
		ClientP2P client = new ClientP2P(sysFichiersClient);
		/* Cr�e et instancie un ServeurControle et un ServeurDonnees */
		ServeurControle sc = new ServeurControle(sysFichiersServeur);
		ServeurDonnees sd = new ServeurDonnees(sysFichiersServeur);
		/* Cr�e et instancie un GestionProtocole */
		GestionProtocole protocole = new GestionProtocole(sc,sd);
		/* Cr�e et instancie un SocketServeurTCP */
		SocketServeurTCP serveurTCP = new SocketServeurTCP(protocole);
		/*Lancement du socket serveurTCP*/
		serveurTCP.start();
		/*Lancement du run se trouvant dans le ClientP2P*/
		client.run();
	}

}
