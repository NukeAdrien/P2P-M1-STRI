package client;

import java.util.Scanner;

public class ClientP2P extends Thread {

	public void run() {
		int choix = -1;
		boolean fin = false;
		String nomFichier,ip;
		ClientControle controle = new ClientControle("TCP");
		
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

			switch (choix) {
			case 1:
				System.out.println("Entrez le nom du fichier a télécharger : ");
				nomFichier = sc.nextLine();
				System.out.println("Entrez l'IP du serveur : ");
				ip = sc.nextLine();
				controle.SimpleTelechargement(nomFichier,ip);
				break;
			case 2:
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
