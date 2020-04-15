package client;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import systeme.fichiers.Fichier;
import systeme.fichiers.GestionFichier;

/*
 * Classe ClientP2P --> Cette classe permet de cr�er un client P2P
 */

public class ClientP2P implements Runnable {
	/* D�claration de variables */
	GestionFichier sysFichiers;
	ClientAnnuaire annuaire = null;
	int portServeur;
	String transport;

	/*
	 * Constructeur ClientP2P --> Ce constructeur prend en param�tre un gestion de
	 * fichier Ce constructeur permet de cr�er un nouveau ClientP2P.
	 */
	public ClientP2P(GestionFichier g, int p, String t) {
		this.sysFichiers = g;
		this.portServeur = p;
		this.transport = t;
	}

	/* M�thode run : m�thode d'ex�cution du thread */
	public void run() {
		/* D�claration de variables */
		int choix = -1;
		int port = -1, nbServeur = -1, i;
		boolean fin = false;
		String nomFichier, ip = "";

		/* D�claration d'une varibale de type Scanner */
		Scanner sc = new Scanner(System.in);

		ClientControle controle = new ClientControle(this.transport, sysFichiers);

		/* On laisse le choix � l'utilisateur */
		while (fin == false) {
			choix = -1;
			/* On cr�e une liste d'adresses IP */
			List<String> listIP = new ArrayList<String>();
			/* On cr�e une liste de ports */
			List<Integer> listPort = new ArrayList<Integer>();
			System.out.println("Menu : ");
			while (choix < 0 || choix > 5) {
				System.out.println("1 - T�l�charger un simple fichier");
				System.out.println("2 - T�l�charger un fichier depuis plusieur serveur");
				System.out.println("3 - T�l�charger en P2P");
				System.out.println("4 - Voir la liste des fichiers disponible");
				System.out.println("5 - Vos fichiers");
				System.out.println("0 - Quitter");

				System.out.println("Entrez votre choix :");
				if (sc.hasNextInt()) {
					choix = sc.nextInt();
				} else {
					sc.nextLine();
					System.out.println("Entr�e invalide");
				}
				if (choix < 0 && choix > 5) {
					System.out.println("Erreur saisie");
				}
			}
			nomFichier = sc.nextLine();
			switch (choix) {
			/* Si l'utilisateur a choisi l'option 1 */
			case 0:
				fin = true;
				break;
			case 1:
				/* Initialisation */
				ip = "";
				port = -1;
				/*
				 * On le laisse entrer le nom de fichier, l'adresse IP, et le nim�ro de port �
				 * lier au socket
				 */
				System.out.println("Entrez le nom du fichier a t�l�charger : ");
				nomFichier = sc.nextLine();
				while (!ip.matches(
						"^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
					System.out.println("Entrez l'IP du serveur : ");
					ip = sc.nextLine();
					if (!ip.matches(
							"^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
						System.out.println("Erreur de saisie");
					}
				}
				while (port == -1) {
					System.out.println("Entrez le port du serveur :");
					if (sc.hasNextInt()) {
						port = sc.nextInt();
						if (port < 1) {
							port = -1;
						}
					} else {
						sc.nextLine();
						System.out.println("Entr�e invalide");
					}
				}
				sc.nextLine();
				/*
				 * On peut donc t�l�charger le fichier avec comme param�tre le nom de fichier,
				 * l'adresse IP et le port
				 */
				controle.TelechargementSimple(nomFichier, ip, port);
				break;
			case 2:
				/* Initialisation */
				ip = "";
				port = -1;
				nbServeur = -1;
				/* On le laisse entrer le nom de fichier et le nombre de serveurs � contacter */
				System.out.println("Entrez le nom du fichier a t�l�charger : ");
				nomFichier = sc.nextLine();
				while (nbServeur == -1) {
					System.out.println("Entrez le nombre de serveur a contacter : ");
					if (sc.hasNextInt()) {
						nbServeur = sc.nextInt();
						if (nbServeur < 1) {
							nbServeur = -1;
						}
					} else {
						sc.nextLine();
						System.out.println("Entr�e invalide");
					}
				}
				sc.nextLine();
				/*
				 * Pour chaque serveur, on demande � l'utilisateur son adresse IP et son num�ro
				 * de port
				 */
				if (nbServeur > 0) {
					for (i = 0; i < nbServeur; i++) {
						/* On v�rifie que l'adresse IP est bien format�*/
						while (!ip.matches(
								"^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
							System.out.println("Entrez l'IP du serveur : ");
							ip = sc.nextLine();
							/* On v�rifie que l'adresse IP est bien format�*/
							if (!ip.matches(
									"^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
								/* Sinon affichage d'un message d'erreur */
								System.out.println("Erreur de saisie");
							} else {
								listIP.add(ip);
							}
						}
						while (port == -1) {
							System.out.println("Entrez le port du serveur :");
							if (sc.hasNextInt()) {
								port = sc.nextInt();
								if (port < 1) {
									port = -1;
								} else {
									listPort.add(port);
								}
							} else {
								sc.nextLine();
								System.out.println("Entr�e invalide");
							}
						}
						sc.nextLine();
						port = -1;
						ip = "";
					}
					/*
					 * On appelle la m�thode telechargementParallele permettant de t�l�charger des
					 * blocs sur plusieurs serveurs Elle prend en param�tre le nom de fichier mais
					 * aussi la liste d'adresses IP et la liste des ports
					 */
					controle.TelechargementParallele(nomFichier, listIP, listPort);
				} else {
					System.out.println("Erreur de saisie");
				}
				break;
			case 3:
				boolean menuAnnuaire = false;
				String adresseIP = "";
				int numeroPort=-1;
				if (this.annuaire == null) {
					/* On v�rifie que l'adresse IP est bien format�*/
					while (!adresseIP.matches(
							"^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
						System.out.println("Entrez l'IP du serveur : ");
						adresseIP = sc.nextLine();
						/* On v�rifie que l'adresse IP est bien format�*/
						if (!adresseIP.matches(
								/* Sinon affichage d'un message d'erreur */
								"^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
							System.out.println("Erreur de saisie");
						}
					}
					while (numeroPort == -1) {
						System.out.println("Entrez le port du serveur :");
						if (sc.hasNextInt()) {
							numeroPort = sc.nextInt();
							if (numeroPort < 1) {
								numeroPort = -1;
							}
						} else {
							sc.nextLine();
							System.out.println("Entr�e invalide");
						}
					}
					sc.nextLine();
					annuaire = new ClientAnnuaire(this.transport, sysFichiers);
					annuaire.Inscription(adresseIP, numeroPort, this.portServeur);
				}
				while (menuAnnuaire == false) {
					choix = -1;
					/* On laisse le choix � l'utilisateur */
					System.out.println("Menu-Annuaire : ");
					while (choix < 1 || choix > 4) {
						System.out.println("1 - Inscription/Changer de serveur d'annauire");
						System.out.println("2 - Recherche un fichier dans l'annuaire");
						System.out.println("3 - T�l�charger en P2P");
						System.out.println("4 - Retour");

						System.out.println("Entrez votre choix :");
						if (sc.hasNextInt()) {
							choix = sc.nextInt();
						} else {
							sc.nextLine();
							System.out.println("Entrer invalide");
						}
						if (choix < 0 && choix > 4) {
							System.out.println("Erreur saisie");
						}
					}
					nomFichier = sc.nextLine();
					switch (choix) {
					/* Si l'utilisateur a choisi l'option 1 */
					case 1:
						/* On v�rifie que l'adresse IP est bien format�*/
						while (!adresseIP.matches(
								"^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
							System.out.println("Entrez l'IP du serveur : ");
							adresseIP = sc.nextLine();
							/* On v�rifie que l'adresse IP est bien format�*/
							if (!adresseIP.matches(
									"^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
								/* Sinon affichage d'un message d'erreur */
								System.out.println("Erreur de saisie");
							}
						}
						while (numeroPort == -1) {
							System.out.println("Entrez le port du serveur :");
							if (sc.hasNextInt()) {
								numeroPort = sc.nextInt();
								if (numeroPort < 1) {
									numeroPort = -1;
								}
							} else {
								sc.nextLine();
								System.out.println("Entr�e invalide");
							}
						}
						sc.nextLine();
						/* On proc�de � l'inscription */
						annuaire.Inscription(adresseIP, numeroPort, this.portServeur);
						break;
					case 2:
						System.out.println("La fonction n'a pas �t� encore d�velop�e");
						break;
					case 3:
						/*
						 * On le laisse entrer le nom de fichier
						 */
						System.out.println("Entrez le nom du fichier a t�l�charger : ");
						nomFichier = sc.nextLine();
						/*
						 * On peut donc t�l�charger le fichier avec comme param�tre le nom de fichier
						 */
						if (annuaire.Telechargement(nomFichier, listIP, listPort, this.portServeur) == 0) {
							controle.TelechargementParallele(nomFichier, listIP, listPort);
						} else {
							System.out.println("Le fichier n'a pas pu etre t�l�charg�");
						}
						/* On r�initialise une liste d'adresses IP */
						listIP = new ArrayList<String>();
						/* On r�initialise une liste de ports */
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
				this.sysFichiers.afficherFichierDisponible();
				break;
			case 5:
				/* D�claration d'une varibale de type Scanner */
				sc = new Scanner(System.in);
				/* On laisse le choix � l'utilisateur */
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
							System.out.println("Entrer invalide");
						}
						if (choix < 0 && choix > 3) {
							System.out.println("Erreur saisie");
						}
					}
					switch (choix) {
					/* Si l'utilisateur choisi l'option 1 */
					case 1:
						String res = null;
						try {
							System.out.println("Veuillez entrer le nom du fichier : ");
							res = sc.next();
							/* On affiche les details du fichier en fonction du nom de fichier entr� */
							this.sysFichiers.afficherDetailFichier(res);
						} catch (NoSuchElementException e) {
							System.out.println("Aucune entr�e trouv�e");
							e.printStackTrace();
						}
						break;
					case 2:
						/* Si l'utilisateur choisi l'option 2 */
						res = null;
						try {
							/* On supprime le fichier en fonction du nom de fichier entr� */
							System.out.println("Veuillez entrer le nom du fichier : ");
							res = sc.next();
							Fichier red = this.sysFichiers.RechercheFichier(res);
							this.sysFichiers.supprimerFichier(red);
							System.out.println(this.sysFichiers.getListFichier());
						} catch (NoSuchElementException e) {
							System.out.println("Aucune entr�e trouv�e");
							e.printStackTrace();
						}
						break;
					case 3:
						/* Si l'utilisateur choisi l'option 3 */
						res = null;
						try {
							/* On renomme le fichier en fonction du nom de fichier entr� */
							System.out.println("Veuillez entrer le nom du fichier � renommer : ");
							res = sc.next();
							Fichier red = this.sysFichiers.RechercheFichier(res);
							res = null;
							System.out.println("Veuillez entrer le nouveau nom du fichier : ");
							res = sc.next();
							this.sysFichiers.renommerFichier(red, res);
							System.out.println(this.sysFichiers.getListFichier());
							this.sysFichiers.afficherDetailFichier(res);
						} catch (NoSuchElementException e) {
							System.out.println("Aucune entr�e trouv�e");
							e.printStackTrace();
						}
						break;
					default:
						/* Sinon il y a une erreur de saisie */
						System.out.println("Erreur saisie");
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
