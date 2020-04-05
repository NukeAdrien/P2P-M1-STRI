package main;

import java.util.Scanner;

import client.ClientP2P;
import envoie.reception.PDUAnnuaire;
import serveur.GestionProtocole;
import serveur.ServeurAnnuaire;
import serveur.ServeurControle;
import serveur.ServeurDonnees;
import socket.SocketServeurTCP;
import systeme.fichiers.GestionFichier;

public class P2P {

	public static void main(String[] args) {
		/* D�claration d'une varibale de type Scanner*/
		String chemin,annuaire;
		int port;
		SocketServeurTCP serveurTCP;
		GestionProtocole protocole;
		/* D�claration d'une varibale de type Scanner*/
		@SuppressWarnings("resource")
		Scanner c = new Scanner(System.in);
		System.out.println("Application P2P");
		System.out.println("Entrer le port du serveur :");
		port= c.nextInt();
		c.nextLine();
		System.out.println("Entrer le chemin du dossier :");
		chemin = c.nextLine();
		System.out.println("Voulez ajouter le module annuaire : (OUI)");
		annuaire = c.nextLine();
		/* Cr�e et instancie un GestionFichier client et un GestionFichier serveur */
		GestionFichier sysFichiers = new GestionFichier(chemin);
		sysFichiers.initGestionFichier();
		/* Cr�e et instancie un client P2P */
		ClientP2P client = new ClientP2P(sysFichiers,port);
		/* Cr�e et instancie un ServeurControle et un ServeurDonnees */
		ServeurControle sc = new ServeurControle(sysFichiers);
		ServeurDonnees sd = new ServeurDonnees(sysFichiers);
		/*Cr�e et instancie un serveurAnnuaire*/
		if(annuaire.compareTo("OUI")==0) {
			System.out.println("Entrer une adresse IP pour le serveur d'annuaire");
			String ip = c.nextLine();
			ServeurAnnuaire sa = new ServeurAnnuaire(sysFichiers);
			/* Cr�e et instancie un GestionProtocole */
			protocole = new GestionProtocole(sc,sd,sa);
			/* Cr�e et instancie un SocketServeurTCP */
			serveurTCP = new SocketServeurTCP(protocole,port);
			PDUAnnuaire init = new PDUAnnuaire("ANN", "REGISTRATION", sysFichiers,Integer.toString(port),null);
			sa.Inscription(init, ip);
		}
		else {
			/* Cr�e et instancie un GestionProtocole */
			protocole = new GestionProtocole(sc,sd);
			/* Cr�e et instancie un SocketServeurTCP */
			serveurTCP = new SocketServeurTCP(protocole,port);
		}
		
		/*Lancement du socket serveurTCP*/
		serveurTCP.start();
		/*Lancement du run se trouvant dans le ClientP2P*/
		client.run();
		
	}

}
