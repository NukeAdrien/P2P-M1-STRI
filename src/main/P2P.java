package main;

import java.util.Scanner;

import client.ClientP2P;
import serveur.GestionProtocole;
import serveur.ServeurControle;
import serveur.ServeurDonnees;
import socket.SocketServeurTCP;
import systeme.fichiers.GestionFichier;

public class P2P {

	public static void main(String[] args) {
		/* Déclaration d'une varibale de type Scanner*/
		String chemin;
		int port;
		/* Déclaration d'une varibale de type Scanner*/
		Scanner c = new Scanner(System.in);
		System.out.println("Application P2P");
		System.out.println("Entrer le port du serveur :");
		port= c.nextInt();
		c.nextLine();
		System.out.println("Entrer le chemin du dossier :");
		chemin = c.nextLine();
		/* Crée et instancie un GestionFichier client et un GestionFichier serveur */
		GestionFichier sysFichiers = new GestionFichier(chemin);
		sysFichiers.initGestionFichier();
		/* Crée et instancie un client P2P */
		ClientP2P client = new ClientP2P(sysFichiers);
		/* Crée et instancie un ServeurControle et un ServeurDonnees */
		ServeurControle sc = new ServeurControle(sysFichiers);
		ServeurDonnees sd = new ServeurDonnees(sysFichiers);
		/* Crée et instancie un GestionProtocole */
		GestionProtocole protocole = new GestionProtocole(sc,sd);
		/* Crée et instancie un SocketServeurTCP */
		SocketServeurTCP serveurTCP = new SocketServeurTCP(protocole,port);
		/*Lancement du socket serveurTCP*/
		serveurTCP.start();
		/*Lancement du run se trouvant dans le ClientP2P*/
		client.run();
		
	}

}
