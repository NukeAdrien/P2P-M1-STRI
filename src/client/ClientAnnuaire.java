package client;

import java.util.ArrayList;
import java.util.List;

import envoie.reception.PDU;
import envoie.reception.PDUAnnuaire;
import envoie.reception.PDUControle;
import socket.SocketClient;
import systeme.fichiers.GestionFichier;

public class ClientAnnuaire {
	/* Déclaration de variables */
	String transport;
	GestionFichier sysFichiers;
	String ip;
	int port;

	/*
	 * Constructeur ClientAnnuaire --> Ce constructeur prend en paramétre le
	 * protocole de transport choisi et un GestionFichier Ce constructeur permet de
	 * créer un nouveau ClientAnnuaire.
	 */

	public ClientAnnuaire(String t, GestionFichier g) {
		transport = t;
		sysFichiers = g;
	}

	/*
	 * Methode Inscription : Permet de s'inscrire auprés d'un serveur annuaire
	 * @param : L'adresse ip du serveur, le port source et le port de destination
	 */
	public void Inscription(String ip, int port, int portServeur) {
		/* Cree le socket en indiquant le mode de transport (TCP ou UDP) */
		SocketClient serveur = new SocketClient(transport);
		/*
		 * Si il y a un probléme avec l'initialisation avec le socket, l'adresse IP et
		 * le port du destinataire
		 */
		if (serveur.InitialisationSocket(ip, port) != 0) {
			/* Affichage d'un message d'erreur */
			System.out.println("Impossible de joindre le serveur");
			return;
		}
		/* Création d'une PDU Annuaire */
		PDUAnnuaire register = new PDUAnnuaire("ANN", "REGISTRATION", this.sysFichiers, Integer.toString(portServeur),
				null);

		/* Si il y un probléme avec l'envoi de la PDU au serveur */
		if (serveur.EnvoiePDU(register) != 0) {
			/* On ferme le socket */
			serveur.FermerSocket();
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur lors de l'envoi de la requéte");
			return;
		}
		/* On initialise la variable */
		PDU reponse = null;
		/* On récupére la PDU reéu */
		reponse = serveur.RecevoirPDU();
		/* Si la PDU n'est pas nulle */
		if (reponse == null) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de connexion avec le serveur");
			return;
			/* Si la réponse est une instance de PDU contréle */
		} else if (reponse instanceof PDUAnnuaire) {
			/* On récupére la PDUControle */
			register = (PDUAnnuaire) reponse;
		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de PDU");
			/* On ferme la socket */
			serveur.FermerSocket();
			return;
		}
		/* On vérifie la reponse */
		/* Si la commande correspond à celle de l'inscription auprés d'un Serveur Annuaire */
		if (register.getMethode().compareTo("REGISTRATION") == 0) {
			/* Si la commande est un enregistrement */
			if (register.getDonnees().compareTo("OK") == 0) {
				System.out.println("Vous pouvez rechercher des fichier depuis l'annuaire !");
				this.ip = ip;
				this.port = port;
			} 
			/* Si la commande correspond à une MAJ des données */
			else if(register.getDonnees().compareTo("MAJ") == 0) {
				System.out.println("Vous pouvez rechercher des fichier depuis l'annuaire !");
				this.ip = ip;
				this.port = port;
				this.sysFichiers.setNbDowload(register.getSysFichiers().getNbDowload());
				this.sysFichiers.setNbUpload(register.getSysFichiers().getNbUpload());
			} else {
				System.out.println("Une erreur est survenue lors de l'inscription");
			}

		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de commande");
		}
		/* Création d'une PDU de fin de recherhe */
		PDUControle fin = new PDUControle(null, null, "FIN", null);
		/* On l'envoie */
		serveur.EnvoiePDU(fin);
		/* On ferme le socket */
		serveur.FermerSocket();
		return;
	}

	/*
	 * Méthode Telechargement : Permet le téléchargement d'un fichier sur plusieurs serveurs 
	 * @param : Le nom du fichier, la liste des adresses ip, la liste des ports et le port du serveur
	 */

	public int Telechargement(String nomFichier, List<String> listIP, List<Integer> listPort,int pS) {
		/* Déclaration de variables */
		List<String> portServeur = new ArrayList<String>(); 
		String tmp =Integer.toString(pS);
		portServeur.add(0, tmp);
		
		/* On réinitialise les compteurs */
		sysFichiers.reinitialisation();

		/* Crée le socket en indiquant le mode de transport (TCP ou UDP) */
		SocketClient serveur = new SocketClient(transport);
		/*
		 * Si il y a un probléme avec l'initialisation avec le socket, l'adresse IP et
		 * le port du destinataire
		 */
		if (serveur.InitialisationSocket(ip, port) != 0) {
			/* Affichage d'un message d'erreur */
			System.out.println("Impossible de joindre le serveur");
			return 1;
		}
		/* Création d'une PDU Annuaire */
		PDUAnnuaire dowload = new PDUAnnuaire("ANN", "DOWLOAD", this.sysFichiers, nomFichier, portServeur);

		/* Si il y un probléme avec l'envoi de la PDU au serveur */
		if (serveur.EnvoiePDU(dowload) != 0) {
			/* On ferme le socket */
			serveur.FermerSocket();
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur lors de l'envoie de la requete");
			return 1;
		}
		/* On initialise la variable */
		PDU reponse = null;
		/* On récupére la PDU recu */
		reponse = serveur.RecevoirPDU();
		/* Si la PDU n'est pas nulle */
		if (reponse == null) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de connexion avec le serveur");
			return 1;
			/* Si la réponse est une instance de PDU contréle */
		} else if (reponse instanceof PDUAnnuaire) {
			/* On récupére la PDUControle */
			dowload = (PDUAnnuaire) reponse;
		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de PDU");
			/* On ferme la socket */
			serveur.FermerSocket();
			return 1;
		}
		/* On vérifie la réponse */
		/* Si la commande correspond à celle de DOWLOAD */
		if (dowload.getMethode().compareTo("DOWLOAD") == 0) {
			/* Si le fichier existe */
			if (dowload.getListServeurs() != null) {
				/* On récupére les données */
				System.out.println(dowload.getDonnees());

				/* Si la donnée contenuer dans la PDU est équivalent à "Le fichier va etre telecharge dans 30 secondes" */
				/* Alors on crée un timer */
				if (dowload.getDonnees().compareTo("Le fichier va etre telecharge dans 30 secondes") == 0) {
					int t = 31;
					while (t > 0) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						t--;
						System.out.println(t);
					}
				}

				/* On parcours la liste des serveurs */
				for(int i =0; i < dowload.getListServeurs().size();i++) {
					String[] temp = dowload.getListServeurs().get(i).split(":");
					/* On ajoute dans les arrayList les éléments respectifs au modéle suivant : <IP>:<Port>*/
					listIP.add(i,temp[0]);
					listPort.add(i,Integer.parseInt(temp[1]));
				}
			} else {
				System.out.println(dowload.getDonnees());
				/* On crée la PDU de fin de téléchargement */
				PDUControle fin = new PDUControle(null, null, "FIN", null);
				/* On envoie la PDU */
				serveur.EnvoiePDU(fin);
				/* On ferme le socket */
				serveur.FermerSocket();
				return 1;
			}

		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de commande");
			/* On crée la PDU de fin de téléchargement */
			PDUControle fin = new PDUControle(null, null, "FIN", null);
			/* On envoie la PDU */
			serveur.EnvoiePDU(fin);
			/* On ferme le socket */
			serveur.FermerSocket();
			return 1;
		}
		/* On crée la PDU de fin de téléchargement */
		PDUControle fin = new PDUControle(null, null, "FIN", null);
		/* On envoie la PDU */
		serveur.EnvoiePDU(fin);
		/* On ferme le socket */
		serveur.FermerSocket();
		return 0;
	}

}
