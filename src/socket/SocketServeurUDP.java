package socket;

import java.net.DatagramSocket;
import envoie.reception.Envoie;
import envoie.reception.PDU;
import envoie.reception.Recevoir;
import serveur.GestionProtocole;

import java.io.IOException;

/*
 * Classe SocketUDP --> Cette classe permet de cr�er, d'instancier et de lancer un socket
 */

public class SocketServeurUDP extends Thread {
	/* D�claration de vaiables */
	GestionProtocole gestion;
	int port;
	PDU requete, reponse;
	String adresse;

	/*
	 * Constructeur SocketServeurUDP --> Ce constructeur prend en param�tre un
	 * gestion de protocole Ce constructeur permet de cr�er un nouveau
	 * SocketServeurUDP.
	 */
	public SocketServeurUDP(GestionProtocole g, int p) {
		gestion = g;
		this.port = p;
	}

	@Override
	public void run() {
		/* D�claration de variables locales */
		DatagramSocket sockServeur;
		try {
			/* On initialise un serveur Socket */
			sockServeur = new DatagramSocket(this.port);
			/* On cree la PDU au client */
			Envoie envoieClient = new Envoie(sockServeur);
			/* On cree la PDU du client */
			Recevoir receptionClient = new Recevoir(sockServeur);
			/* On r�alise une boucle finie */
			while (true) {
				/* On initialise la variable requete */
				requete = null;
				/* On re�oit la PDU du client */
				requete = receptionClient.RecevoirPDUUDP();
				/* Si il y a un probl�me lors de la r�ception */
				if (requete == null) {
					/* Affichage d'un message d'erreur */
					System.out.println("Erreur de r�ception de la PDU");
					/* On ferme le socket */
					sockServeur.close();
					return;
				} else {
					adresse = receptionClient.getIpRequete();
					adresse = adresse.substring(1);
					receptionClient.setIpRequete(adresse);
					/* Si il n'y a pas de probl�mes alors on va pouvoir g�rer la requ�te */
					reponse = gestion.gestionRequete(requete,receptionClient.getIpRequete());
					/*
					 * Apr�s la gestion de la requete, on envoie la PDU au client pour une r�ception
					 */
					envoieClient.EnvoiePDUUDP(reponse,receptionClient.getIpRequete(),receptionClient.getPortRequete());

				}
			}
		} catch (IOException ioe) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de cr�ation du Server Socket: " + ioe.getMessage());
		}
		return;
	}

}
