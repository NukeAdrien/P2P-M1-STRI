package main;

import java.util.Scanner;

import client.ClientP2P;
import envoie.reception.PDUAnnuaire;
import serveur.GestionProtocole;
import serveur.ServeurAnnuaire;
import serveur.ServeurControle;
import serveur.ServeurDonnees;
import socket.SocketServeurTCP;
import socket.SocketServeurUDP;
import systeme.fichiers.GestionFichier;

public class P2P {

	public static void main(String[] args) {
		/* Déclaration des variables */
		String chemin, annuaire = "", transport = "",ip="";
		int port = -1;
		SocketServeurTCP serveurTCP;
		SocketServeurUDP serveurUDP;
		GestionProtocole protocole;
		/* Déclaration d'une varibale de type Scanner */
		@SuppressWarnings("resource")
		Scanner c = new Scanner(System.in);

		/*
		 * On laisse l'utilisateur entrer le port du serveur, le chemin du dossier ainsi
		 * qu'un potentiel ajout du module annuaire
		 * DE plus on sécurise la saisie
		 */
		System.out.println("Application P2P");
		while (port == -1) {
			System.out.println("Entrez le port du serveur :");
			if (c.hasNextInt()) {
				port = c.nextInt();
				if (port < 1024 || port > 49151) {
					port = -1;
					System.out.println("Nombre non compris entre 1024 et 49151");
				}
			} else {
				c.nextLine();
				System.out.println("Entrer invalide");
			}
		}
		c.nextLine();
		while(transport.compareTo("TCP") != 0 && transport.compareTo("UDP") != 0 ) {
			System.out.println("Entrez le protocole transport : (TCP) ou (UDP)");
			transport = c.nextLine();
			if(transport.compareTo("TCP") != 0 && transport.compareTo("UDP") != 0 ) {
				System.out.println("Erreur de saisie");
			}
		}
		System.out.println("Entrez le chemin du dossier :");
		chemin = c.nextLine();
		while(annuaire.compareTo("OUI") != 0 && annuaire.compareTo("NON") != 0 ) {
			System.out.println("Voulez-vous ajouter le module annuaire : (OUI) ou (NON) :");
			annuaire = c.nextLine();
			if(annuaire.compareTo("OUI") != 0 && annuaire.compareTo("NON") != 0 ) {
				System.out.println("Erreur de saisie");
			}
		}

		/* Crée et initialise un GestionFichier client et un GestionFichier serveur */
		GestionFichier sysFichiers = new GestionFichier(chemin);
		sysFichiers.initGestionFichier();

		/* Crée et initialise un client P2P */
		ClientP2P client = new ClientP2P(sysFichiers, port, transport);

		/* Crée et initialise un ServeurControle et un ServeurDonnees */
		ServeurControle sc = new ServeurControle(sysFichiers);
		ServeurDonnees sd = new ServeurDonnees(sysFichiers);

		/* Si l'utilisateur a choisi d'un module annuaire */
		if (annuaire.compareTo("OUI") == 0) {
			/* On laisse l'utilisateur entrer l'adresse IP pour le serveur d'annuaire */
			while(! ip.matches("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
				System.out.println("Entrez une adresse IP pour le serveur d'annuaire");
				ip = c.nextLine();
				if(! ip.matches("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
					System.out.println("Erreur de saisie");
				}
			}
			
			
			/*
			 * Crée et instancie un ServeurAnnuaire pour ajouter l'adresse IP précedemment
			 * entré
			 */
			ServeurAnnuaire sa = new ServeurAnnuaire(sysFichiers);
			/* Crée et instancie un GestionProtocole */
			protocole = new GestionProtocole(sc, sd, sa);
			/*
			 * On crée une PDU indiquant la volonté d'ajouter l'adresse IP précédemment
			 * ajouté
			 */
			PDUAnnuaire init = new PDUAnnuaire("ANN", "REGISTRATION", sysFichiers, Integer.toString(port), null);
			/* On procède à l'inscription */
			sa.Inscription(init, ip);
		} else {
			/* Crée et instancie un GestionProtocole */
			protocole = new GestionProtocole(sc, sd);
		}
		/*Test pour savoir quel type serveur va etre instancier*/
		if(transport.compareTo("UDP")== 0) {
			/* Crée et instancie un SocketServeurUDP */
			serveurUDP = new SocketServeurUDP(protocole, port);
			/* Lancement du socket serveurUDP */
			serveurUDP.start();
		}else {
			/* Crée et instancie un SocketServeurTCP */
			serveurTCP = new SocketServeurTCP(protocole, port);
			/* Lancement du socket serveurTCP */
			serveurTCP.start();
		}
		/* Lancement du run se trouvant dans le ClientP2P */
		client.run();

	}

}
