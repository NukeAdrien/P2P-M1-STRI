package client;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import systeme.fichiers.Fichier;
import systeme.fichiers.GestionFichier;

/*
 * Classe ClientP2P --> Cette classe permet de créer un client P2P
 */

public class ClientP2P implements Runnable {
	/* Déclaration de variables */
	GestionFichier sysFichiers;
	ClientAnnuaire annuaire = null;
	int portServeur;
	/*
	 * Constructeur ClientP2P --> Ce constructeur prend en paramètre un gestion de
	 * fichier Ce constructeur permet de créer un nouveau ClientP2P.
	 */
	public ClientP2P(GestionFichier g, int p) {
		this.sysFichiers = g;
		this.portServeur = p;
	}

	/* Méthode run : méthode d'exécution du thread */
	@SuppressWarnings("resource")
	public void run() {
		/* Déclaration de variables */
		int choix = -1;
		int port, nbServeur, i;
		boolean fin = false;
		String nomFichier, ip;
		String N4;

		/* Déclaration d'une varibale de type Scanner */
		Scanner sc = new Scanner(System.in);
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		/* Choix du protocole */
		System.out.println("Entrez TCP ou UDP: ");
		N4 = sc.nextLine();
		if (N4.compareTo("TCP") == 0 && N4.compareTo("UDP") == 0) {
			return;
		}
		ClientControle controle = new ClientControle(N4, sysFichiers);

		/* On laisse le choix à l'utilisateur */
		while (fin == false) {
			choix = -1;
			/* On crée une liste d'adresses IP */
			List<String> listIP = new ArrayList<String>();
			/* On crée une liste de ports */
			List<Integer> listPort = new ArrayList<Integer>();
			System.out.println("Menu : ");
			/* Présentation du menu */
			while (choix < 0 || choix > 5) {
				System.out.println("1 - Télécharger un simple fichier");
				System.out.println("2 - Télécharger un fichier depuis plusieurs serveurs");
				System.out.println("3 - Télécharger en P2P");
				System.out.println("4 - Voir la liste des fichiers disponibles");
				System.out.println("5 - Vos fichiers");
				System.out.println("0 - Quitter");
				/* L'utilisateur entre son choix */
				System.out.println("Entrez votre choix :");
				if (sc.hasNextInt()) {
					choix = sc.nextInt();
					/* Si il y a un problème au niveau de la saisie */
				} else {
					sc.nextLine();
					System.out.println("Entrée invalide");
				}
				if (choix < 0 && choix > 5) {
					/* Affichage d'un message d'erreur */
					System.out.println("Erreur de saisie");
				}
			}
			nomFichier = sc.nextLine();
			switch (choix) {
			/* Si l'utilisateur a choisi l'option 1 */
			case 0:
				fin = true;
				break;
			case 1:
				/*
				 * On le laisse entrer le nom de fichier, l'adresse IP, et le niméro de port à
				 * lier au socket
				 */
				System.out.println("Entrez le nom du fichier a télécharger : ");
				nomFichier = sc.nextLine();
				System.out.println("Entrez l'IP du serveur : ");
				ip = sc.nextLine();
				System.out.println("Entrez le port du serveur : ");
				port = sc.nextInt();
				sc.nextLine();
				/*
				 * On peut donc télécharger le fichier avec comme paramètre le nom de fichier,
				 * l'adresse IP et le port
				 */
				controle.TelechargementSimple(nomFichier, ip, port);
				break;
			case 2:
				/* On le laisse entrer le nom de fichier, le nombre de serveurs à contacter */

				System.out.println("Entrez le nom du fichier à télécharger : ");
				nomFichier = sc.nextLine();
				System.out.println("Entrez le nombre de serveurs à contacter : ");
				nbServeur = sc.nextInt();
				sc.nextLine();
				/*
				 * Pour chaque serveur, on demande à l'utilisateur son adresse IP et son numéro
				 * de ports
				 */
				if (nbServeur > 0) {
					for (i = 0; i < nbServeur; i++) {
						System.out.println("Entrez l'IP du serveur : ");
						listIP.add(sc.nextLine());
						System.out.println("Entrez le port du serveur : ");
						listPort.add(sc.nextInt());
						sc.nextLine();
					}
					/*
					 * On appelle la méthode telechargementParallele permettant de télécharger des
					 * blocs sur plusieurs serveurs Elle prend en paramètre le nom de fichier mais
					 * aussi la liste d'adresses IP et la liste des ports
					 */
					controle.TelechargementParallele(nomFichier, listIP, listPort);
				} else {
					/* Affichage d'un message d'erreur */
					System.out.println("Erreur de saisie");
				}
				break;
			case 3:
				boolean menuAnnuaire = false;
				String adresseIP;
				int numeroPort;
				/* Tant que l'utilisateur ne s'est pas encore inscrit */
				if (this.annuaire == null) {
					System.out.println("Entrez l'IP du serveur : ");
					adresseIP = sc.nextLine();
					System.out.println("Entrez le port du serveur : ");
					numeroPort = sc.nextInt();
					sc.nextLine();
					/* L'annuaire "ajoute" ce nouveau client */
					annuaire = new ClientAnnuaire(N4, sysFichiers);
					/* L'annuaire enregistre l'adresse IP du serveur, le port du serveur, et le port du serveur annuaire */
					annuaire.Inscription(adresseIP, numeroPort,this.portServeur);
				}
				while (menuAnnuaire == false) {
					choix = -1;
					/* On laisse le choix à l'utilisateur */
					System.out.println("Menu-Annuaire : ");
					while (choix < 1 || choix > 4) {
						System.out.println("1 - Inscription/Changer de serveur d'annauire");
						System.out.println("2 - Recherche un fichier dans l'annuaire");
						System.out.println("3 - Télécharger en P2P");
						System.out.println("4 - Retour");

						System.out.println("Entrez votre choix :");
						if (sc.hasNextInt()) {
							choix = sc.nextInt();
						} else {
							sc.nextLine();
							System.out.println("Entrée invalide");
						}
						if (choix < 0 && choix > 4) {
							System.out.println("Erreur de saisie");
						}
					}
					nomFichier = sc.nextLine();
					switch (choix) {
					/* Si l'utilisateur a choisi l'option 1 */
					case 1:
						/* On laisse l'utilisateur saisir adresse Ip/port*/
						System.out.println("Entrez l'IP du serveur : ");
						adresseIP = sc.nextLine();
						System.out.println("Entrez le port du serveur : ");
						numeroPort = sc.nextInt();
						sc.nextLine();
						/*L'annuaire inscrit ce nouveau serveur */
						annuaire.Inscription(adresseIP, numeroPort,this.portServeur);
						break;
					case 2:

						break;
					case 3:
						/*
						 * On le laisse entrer le nom de fichier
						 */
						System.out.println("Entrez le nom du fichier à télécharger : ");
						nomFichier = sc.nextLine();
						/*
						 * Si le téléchargement est possible
						 * On peut donc télécharger le fichier avec comme paramètre le nom de fichier, la liste IP des serveurs et la liste des ports des serveurs
						 */
						if(annuaire.Telechargement(nomFichier, listIP, listPort, this.portServeur)==0) {
							controle.TelechargementParallele(nomFichier, listIP, listPort);
						}
						else {
							/* Affichage d'un message d'erreur */
							System.out.println("Le fichier n'a pas pu être téléchargé");
						}
						/* On réinitialise la liste d'adresses IP */
						listIP = new ArrayList<String>();
						/* On réinitialise la liste de ports */
						listPort = new ArrayList<Integer>();
						break;
					case 4:
						menuAnnuaire = true;
						break;
					default:
						/* Sinon il y a une erreur de saisie */
						System.out.println(choix);
						System.out.println("Erreur choix");
					}
				}
				break;
			case 4:
				/* Si l'utilisateur souhaite voir ses fichiers disponibles */
				System.out.println("Entrez le chemin du dossier :");
				String chemin = sc.nextLine();
				sysFichiers = new GestionFichier(chemin);
				this.sysFichiers.initGestionFichier();
				this.sysFichiers.afficherFichierDisponible();
				break;
			case 5:
				/* Déclaration d'une varibale de type Scanner */
				sc = new Scanner(System.in);
				/* On laisse le choix à l'utilisateur */
				while (fin == false) {
					choix = -1;
					System.out.println("Menu : ");
					while (choix < 0 || choix > 3) {
						System.out.println("1 - Afficher les details du fichier");
						System.out.println("2 - Supprimer un fichier");
						System.out.println("3 - Renommer un fichier");
						System.out.println("Entrez votre choix :");
						if (sc.hasNextInt()) {
							choix = sc.nextInt();
						} else {
							sc.nextLine();
							System.out.println("Entrée invalide");
						}
						if (choix < 0 && choix > 3) {
							System.out.println("Erreur de saisie");
						}
					}
					switch (choix) {
					case 1:
						/* Si l'utilisateur souhaite voir le détail de ses fichiers */
						String res = null;
						try {
							/*On laisse l'utilisateur entrer son nom de fichier */
							System.out.println("Veuillez entrer le nom du fichier : ");
							/* On initialise un gestionnaire de fichier */
							sysFichiers = new GestionFichier("./Telechargement/");
							res = scan.next();
							this.sysFichiers.initGestionFichier();
							/* Puis on affiche les détails */
							this.sysFichiers.afficherDetailFichier(res);
						} catch (NoSuchElementException e) {
							/* Affichage d'un message d'erreur si le fichier est introuvable */
							System.out.println("Aucune entrée trouvée");
							e.printStackTrace();
						}
						break;
					case 2:
						/* Si l'utilisateur souhaite supprimer l'un de ses fichiers */
						res = null;
						try {
							/*On laisse l'utilisateur entrer son nom de fichier */
							System.out.println("Veuillez entrer le nom du fichier : ");
							/* On initialise un gestionnaire de fichier */
							sysFichiers = new GestionFichier("./Telechargment/");
							res = scan.next();
							this.sysFichiers.initGestionFichier();
							/* On recherche le fichier afin de savoir si il existe dans la hashMap */
							Fichier red = this.sysFichiers.RechercheFichier(res);
							/* Puis on supprime le fichier en question */
							this.sysFichiers.supprimerFichier(red);
							System.out.println(this.sysFichiers.getListFichier());
						} catch (NoSuchElementException e) {
							/* Affichage d'un message d'erreur si le fichier est introuvable */
							System.out.println("Aucune entrée trouvée");
							e.printStackTrace();
						}
						break;
					case 3:
						/* Si l'utilisateur souhaite renommer l'un de ses fichiers */
						res = null;
						try {
							/*On laisse l'utilisateur entrer son nom de fichier */
							System.out.println("Veuillez entrer le nom du fichier à renommer : ");
							/* On initialise un gestionnaire de fichier */
							sysFichiers = new GestionFichier("./Telechargment/");
							res = scan.next();
							this.sysFichiers.initGestionFichier();
							/* On recherche le fichier afin de savoir si il existe dans la hashMap */
							Fichier red = this.sysFichiers.RechercheFichier(res);
							res = null;
							/* On laisse l'utilisateur entrer le nouveau de fichier */
							System.out.println("Veuillez entrer le nouveau nom du fichier : ");
							res = scan.next();
							/* Puis on renomme le fichier en question */
							this.sysFichiers.renommerFichier(red, res);
							System.out.println(this.sysFichiers.getListFichier());
							this.sysFichiers.afficherDetailFichier(res);
						} catch (NoSuchElementException e) {
							/* Affichage d'un message d'erreur si le fichier est introuvable */
							System.out.println("Aucune entrée trouvée");
							e.printStackTrace();
						}
						break;
					default:
						System.out.println("Erreur de saisie");
						break;
					}
				}

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
