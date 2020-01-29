package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import systeme.fichiers.GestionFichier;

/*
 * Classe ClientP2P --> Cette classe permet de cr�er un client P2P
 */

public class ClientP2P extends Thread {
	/* D�claration de variables */
	GestionFichier sysFichiers;
	
	/*
	 * Constructeur ClientP2P --> Ce constructeur prend en param�tre un gestion de fichier
	 * Ce constructeur permet de cr�er un nouveau ClientP2P.
	 */
	public ClientP2P(GestionFichier g ) {
		sysFichiers = g;
	}
	
	/* M�thode run : m�thode d'ex�cution du thread */
	public void run() {
		/* D�claration de variables */
		int choix = -1;
		int port,nbServeur,i;
		boolean fin= false;
		String nomFichier,ip;
		ClientControle controle = new ClientControle("TCP",sysFichiers);
		
		/* D�claration d'une varibale de type Scanner*/
		Scanner sc = new Scanner(System.in);
		
		/* On laisse le choix � l'utilisateur*/
		while (fin == false) {
			choix = -1;
			System.out.println("Menu : ");
			while (choix < 0 || choix > 4) {
				System.out.println("1 - T�l�charger un simple fichier");
				System.out.println("2 - T�l�charger un fichier depuis plusieur serveur");
				System.out.println("3 - T�l�charger en P2P depuis plusieur serveur");
				System.out.println("4 - T�l�charger en P2P");
				System.out.println("5 - Voir la liste des fichiers disponible");
				System.out.println("6 - Vos fichiers");
				System.out.println("Entrez votre choix :");
				choix = sc.nextInt();
				if (choix < 0 && choix > 6) {
					System.out.println("Erreur saisie");
				}
			}
			nomFichier = sc.nextLine();
			switch (choix) {
			/* Si l'utilisateur a choisi l'option 1 */
			case 1:
				/*On le laisse entrer le nom de fichier, l'adresse IP, et le nim�ro de port � lier au socket*/
				System.out.println("Entrez le nom du fichier a t�l�charger : ");
				nomFichier = sc.nextLine();
				System.out.println("Entrez l'IP du serveur : ");
				ip = sc.nextLine();
				System.out.println("Entrez le port du serveur : ");
				port = sc.nextInt();
				sc.nextLine();
				/*On peut donc t�l�charger le fichier avec comme param�tre le nom de fichier, l'adresse IP et le port*/
				controle.TelechargementSimple(nomFichier,ip,port);
				break;
			case 2:
				/*On le laisse entrer le nom de fichier, le nombre de serveurs � contacter*/

				System.out.println("Entrez le nom du fichier a t�l�charger : ");
				nomFichier = sc.nextLine();
				/* On cr�e une liste d'adresses IP*/
				List<String> listIP = new ArrayList<String>();
				/* On cr�e une liste de ports*/
				List<Integer> listPort = new ArrayList<Integer>();
				System.out.println("Entrez le nombre de serveur a contacter : ");
				nbServeur = sc.nextInt();
				sc.nextLine();
				/* Pour chaque serveur, on demande � l'utilisateur son adresse IP et son num�ro de ports*/
				if(nbServeur > 0) {
					for(i=0;i<nbServeur;i++) {
						System.out.println("Entrez l'IP du serveur : ");
						listIP.add(sc.nextLine());
						System.out.println("Entrez le port du serveur : ");
						listPort.add(sc.nextInt());
						sc.nextLine();
					}
					/*
					 *  On appelle la m�thode telechargementParallele permettant de t�l�charger des blocs sur plusieurs serveurs
					 * Elle prend en param�tre le nom de fichier mais aussi la liste d'adresses IP et la liste des ports
					 */
					controle.TelechargementParallele(nomFichier, listIP, listPort);
				}else {
					System.out.println("Erreur de saisie");
				}
				break;
			case 3:
				break;
			case 4:
				break;
			default:
				/* Sinon il y a une erreur de saisie */
				System.out.println(choix);
				System.out.println("Erreur choix");
				return;
			}

			

		}
		/* On ferme le scanner */
		sc.close();
	}

}
