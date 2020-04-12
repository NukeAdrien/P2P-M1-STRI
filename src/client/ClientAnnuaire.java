package client;

import java.util.ArrayList;
import java.util.List;

import envoie.reception.PDU;
import envoie.reception.PDUAnnuaire;
import envoie.reception.PDUControle;
import socket.SocketClient;
import systeme.fichiers.GestionFichier;

public class ClientAnnuaire {
	/* D�claration de variables */
	String transport;
	GestionFichier sysFichiers;
	String ip;
	int port;

	/*
	 * Constructeur ClientControle --> Ce constructeur prend en param�tre le
	 * protocole de transport choisie et un GestionFichier Ce constructeur permet de
	 * cr�er un nouveau ClientControle.
	 */

	public ClientAnnuaire(String t, GestionFichier g) {
		transport = t;
		sysFichiers = g;
	}

	public void Inscription(String ip, int port, int portServeur) {
		/* Cree le socket en indiquant le mode de transport (TCP ou UDP) */
		SocketClient serveur = new SocketClient(transport);
		/*
		 * Si il y a un probl�me avec l'initialisation avec le socket, l'adresse IP et
		 * le port du destinataire
		 */
		if (serveur.InitialisationSocket(ip, port) != 0) {
			/* Affichage d'un message d'erreur */
			System.out.println("Impossible de joindre le serveur");
			return;
		}
		PDUAnnuaire register = new PDUAnnuaire("ANN", "REGISTRATION", this.sysFichiers, Integer.toString(portServeur),
				null);

		/* Si il y un probl�me avec l'envoie de la PDU au serveur */
		if (serveur.EnvoiePDU(register) != 0) {
			/* On ferme le socket */
			serveur.FermerSocket();
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur lors de l'envoie de la requete");
			return;
		}
		/* On initialise la variable */
		PDU reponse = null;
		/* On r�cup�re la PDU recu */
		reponse = serveur.RecevoirPDU();
		/* Si la PDU est pas nulle */
		if (reponse == null) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de connexion avec le serveur");
			return;
			/* Si la reponse est une instance de PDU controle */
		} else if (reponse instanceof PDUAnnuaire) {
			/* On r�cup�re la PDUControle */
			register = (PDUAnnuaire) reponse;
		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de PDU");
			/* On ferme la socket */
			serveur.FermerSocket();
			return;
		}
		/* On v�rifie la reponse */
		/* Si la commande correspond � celle de TSF */
		if (register.getMethode().compareTo("REGISTRATION") == 0) {
			/* Si le fichier existe */
			if (register.getDonnees().compareTo("OK") == 0) {
				System.out.println("Vous pouvez rechercher des fichier depuis l'annuaire !");
				this.ip = ip;
				this.port = port;
			} else if (register.getDonnees().compareTo("MAJ") == 0) {
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
		PDUControle fin = new PDUControle(null, null, "FIN", null);
		serveur.EnvoiePDU(fin);
		serveur.FermerSocket();
		return;
	}

	public int Telechargement(String nomFichier, List<String> listIP, List<Integer> listPort,int pS) {
		/* D�claration de variables */
		List<String> portServeur = new ArrayList<String>(); 
		String tmp =Integer.toString(pS);
		portServeur.add(0, tmp);
		sysFichiers.renitialisation();
		/* Cree le socket en indiquant le mode de transport (TCP ou UDP) */
		SocketClient serveur = new SocketClient(transport);
		/*
		 * Si il y a un probl�me avec l'initialisation avec le socket, l'adresse IP et
		 * le port du destinataire
		 */
		if (serveur.InitialisationSocket(ip, port) != 0) {
			/* Affichage d'un message d'erreur */
			System.out.println("Impossible de joindre le serveur");
			return 1;
		}
		PDUAnnuaire dowload = new PDUAnnuaire("ANN", "DOWLOAD", this.sysFichiers, nomFichier, portServeur);

		/* Si il y un probl�me avec l'envoie de la PDU au serveur */
		if (serveur.EnvoiePDU(dowload) != 0) {
			/* On ferme le socket */
			serveur.FermerSocket();
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur lors de l'envoie de la requete");
			return 1;
		}
		/* On initialise la variable */
		PDU reponse = null;
		/* On r�cup�re la PDU recu */
		reponse = serveur.RecevoirPDU();
		/* Si la PDU est pas nulle */
		if (reponse == null) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de connexion avec le serveur");
			return 1;
			/* Si la reponse est une instance de PDU controle */
		} else if (reponse instanceof PDUAnnuaire) {
			/* On r�cup�re la PDUControle */
			dowload = (PDUAnnuaire) reponse;
		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de PDU");
			/* On ferme la socket */
			serveur.FermerSocket();
			return 1;
		}
		/* On v�rifie la reponse */
		/* Si la commande correspond � celle de TSF */
		if (dowload.getMethode().compareTo("DOWLOAD") == 0) {
			/* Si le fichier existe */
			if (dowload.getListServeurs() != null) {
				System.out.println(dowload.getDonnees());
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
				for (int i = 0; i < dowload.getListServeurs().size(); i++) {
					String[] temp = dowload.getListServeurs().get(i).split(":");
					listIP.add(i, temp[0]);
					listPort.add(i, Integer.parseInt(temp[1]));
				}
			} else {
				System.out.println(dowload.getDonnees());
				PDUControle fin = new PDUControle(null, null, "FIN", null);
				serveur.EnvoiePDU(fin);
				serveur.FermerSocket();
				return 1;
			}

		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de commande");
			PDUControle fin = new PDUControle(null, null, "FIN", null);
			serveur.EnvoiePDU(fin);
			serveur.FermerSocket();
			return 1;
		}
		PDUControle fin = new PDUControle(null, null, "FIN", null);
		serveur.EnvoiePDU(fin);
		serveur.FermerSocket();
		return 0;
	}

}
