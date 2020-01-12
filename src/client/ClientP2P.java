package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import systeme.fichiers.GestionFichier;

public class ClientP2P extends Thread {
	GestionFichier sysFichiers;
	public ClientP2P(GestionFichier g ) {
		sysFichiers = g;
	}
	public void run() {
		int choix = -1;
		int port,nbServeur,i;
		boolean fin= false;
		boolean continuer = true; 
		String nomFichier,ip;
		ClientControle controle = new ClientControle("TCP",sysFichiers);
		
		Scanner sc = new Scanner(System.in);

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
			case 1:
				System.out.println("Entrez le nom du fichier a télécharger : ");
				nomFichier = sc.nextLine();
				System.out.println("Entrez l'IP du serveur : ");
				ip = sc.nextLine();
				System.out.println("Entrez le port du serveur : ");
				port = sc.nextInt();
				sc.nextLine();
				controle.TelechargementSimple(nomFichier,ip,port);
				break;
			case 2:
				System.out.println("Entrez le nom du fichier a télécharger : ");
				nomFichier = sc.nextLine();
				List<String> listIP = new ArrayList();
				List<Integer> listPort = new ArrayList();
				System.out.println("Entrez le nombre de serveur a contacter : ");
				nbServeur = sc.nextInt();
				sc.nextLine();
				if(nbServeur > 0) {
					for(i=0;i<nbServeur;i++) {
						System.out.println("Entrez l'IP du serveur : ");
						listIP.add(sc.nextLine());
						System.out.println("Entrez le port du serveur : ");
						listPort.add(sc.nextInt());
						sc.nextLine();
					}
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
				System.out.println(choix);
				System.out.println("Erreur choix");
				return;
			}

			

		}
		sc.close();
	}

}
