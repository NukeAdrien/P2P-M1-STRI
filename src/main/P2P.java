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
		/* D�claration des variables*/
		String chemin,annuaire;
		int port;
		SocketServeurTCP serveurTCP;
		GestionProtocole protocole;
		/* D�claration d'une varibale de type Scanner*/
		@SuppressWarnings("resource")
		Scanner c = new Scanner(System.in);
		
		/* On laisse l'utilisateur entrer le port du serveur, le chemin du dossier ainsi qu'un potentiel ajout du module annuaire */
		System.out.println("Application P2P");
		System.out.println("Entrez le port du serveur :");
		port= c.nextInt();
		c.nextLine();
		System.out.println("Entrez le chemin du dossier :");
		chemin = c.nextLine();
		System.out.println("Voulez-vous ajouter le module annuaire : (OUI)");
		annuaire = c.nextLine();
		
		/* Cr�e et initialise un GestionFichier client et un GestionFichier serveur */
		GestionFichier sysFichiers = new GestionFichier(chemin);
		sysFichiers.initGestionFichier();
	
		/* Cr�e et initialise un client P2P */
		ClientP2P client = new ClientP2P(sysFichiers,port);
		
		/* Cr�e et initialise un ServeurControle et un ServeurDonnees */
		ServeurControle sc = new ServeurControle(sysFichiers);
		ServeurDonnees sd = new ServeurDonnees(sysFichiers);
		
		/*Si l'utilisateur a choisi d'un module annuaire*/
		if(annuaire.compareTo("OUI")==0) {
			/* On laisse l'utilisateur entrer l'adresse IP pour le serveur d'annuaire*/
			System.out.println("Entrez une adresse IP pour le serveur d'annuaire");
			String ip = c.nextLine();
			/* Cr�e et instancie un ServeurAnnuaire pour ajouter l'adresse IP pr�cedemment entr� */
			ServeurAnnuaire sa = new ServeurAnnuaire(sysFichiers);
			/* Cr�e et instancie un GestionProtocole */
			protocole = new GestionProtocole(sc,sd,sa);
			/* Cr�e et instancie un SocketServeurTCP */
			serveurTCP = new SocketServeurTCP(protocole,port);
			/*On cr�e une PDU indiquant la volont� d'ajouter l'adresse IP pr�c�demment ajout� */
			PDUAnnuaire init = new PDUAnnuaire("ANN", "REGISTRATION", sysFichiers,Integer.toString(port),null);
			/*On proc�de � l'inscription */
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
