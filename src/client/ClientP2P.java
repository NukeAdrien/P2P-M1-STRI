package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import systeme.fichiers.GestionFichier;

/*
 * Classe ClientP2P --> Cette classe permet de créer un client P2P
 */

public class ClientP2P extends Thread {
	/* Déclaration de variables */
	GestionFichier sysFichiers;
	
	/*
	 * Constructeur ClientP2P --> Ce constructeur prend en paramètre un gestion de fichier
	 * Ce constructeur permet de créer un nouveau ClientP2P.
	 */
	public ClientP2P(GestionFichier g ) {
		sysFichiers = g;
	}
	
	/* Méthode run : méthode d'exécution du thread */
	public void run() {
		/* Déclaration de variables */
		int choix = -1;
		int port,nbServeur,i;
		boolean fin= false;
		String nomFichier,ip;
		ClientControle controle = new ClientControle("TCP",sysFichiers);
		
		/* Déclaration d'une varibale de type Scanner*/
		Scanner sc = new Scanner(System.in);
		
		/* On laisse le choix à l'utilisateur*/
		while (fin == false) {
			choix = -1;
			System.out.println("Menu : ");
			while (choix < 0 || choix > 4) {
				System.out.println("1 - Télécharger un simple fichier");
				System.out.println("2 - Télécharger un fichier depuis plusieur serveur");
				System.out.println("3 - Télécharger en P2P depuis plusieur serveur");
				System.out.println("4 - Télécharger en P2P");
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
				/*On le laisse entrer le nom de fichier, l'adresse IP, et le niméro de port à lier au socket*/
				System.out.println("Entrez le nom du fichier a télécharger : ");
				nomFichier = sc.nextLine();
				System.out.println("Entrez l'IP du serveur : ");
				ip = sc.nextLine();
				System.out.println("Entrez le port du serveur : ");
				port = sc.nextInt();
				sc.nextLine();
				/*On peut donc télécharger le fichier avec comme paramètre le nom de fichier, l'adresse IP et le port*/
				controle.TelechargementSimple(nomFichier,ip,port);
				break;
			case 2:
				/*On le laisse entrer le nom de fichier, le nombre de serveurs à contacter*/

				System.out.println("Entrez le nom du fichier a télécharger : ");
				nomFichier = sc.nextLine();
				/* On crée une liste d'adresses IP*/
				List<String> listIP = new ArrayList<String>();
				/* On crée une liste de ports*/
				List<Integer> listPort = new ArrayList<Integer>();
				System.out.println("Entrez le nombre de serveur a contacter : ");
				nbServeur = sc.nextInt();
				sc.nextLine();
				/* Pour chaque serveur, on demande à l'utilisateur son adresse IP et son numéro de ports*/
				if(nbServeur > 0) {
					for(i=0;i<nbServeur;i++) {
						System.out.println("Entrez l'IP du serveur : ");
						listIP.add(sc.nextLine());
						System.out.println("Entrez le port du serveur : ");
						listPort.add(sc.nextInt());
						sc.nextLine();
					}
					/*
					 *  On appelle la méthode telechargementParallele permettant de télécharger des blocs sur plusieurs serveurs
					 * Elle prend en paramètre le nom de fichier mais aussi la liste d'adresses IP et la liste des ports
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
