package socket;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import envoie.reception.*;

/*
 * Classe SocketClient --> Cette classe permet de créer, d'instancier et de lancer un socket
 */

public class SocketClient {
	
	/* Déclaration de vaiables */
	DatagramSocket sockClientUDP = null;
	Socket sockClientTCP = null;
	String typeTransport;
	Envoie envoyer;
	Recevoir recevoir;

	/*
	 * Constructeur SocketClient --> Ce constructeur prend en paramètre le type de socket,
	 * si c'est TCP ou encore si c'est UDP
	 * Ce constructeur permet de créer un nouveau socketClient.
	 */
	public SocketClient(String type) {
		typeTransport = type;
	}

	/*
	 * Méthode InitialisationSocket : Méthode permettant d'initialiser un socket
	 * @param : l'adresse IP et le numéro de port à lier au scoket
	 * @return : Un entier 0 si ça s'est bien passée, 1 sinon.
	 */
	public Integer InitialisationSocket(String ip, Integer port) {
		/* Si le type de transport correspond à de l'UDP*/
		if (typeTransport == "UDP") {
			try {
				/* On initialise un datagrameSocket*/
				sockClientUDP = new DatagramSocket(4444);
			} catch (SocketException e) {
				e.printStackTrace();
				return 1;
			}
			/* On envoie le socket UDP pour positionnement des flux*/
			envoyer = new Envoie(sockClientUDP);
		} else {
			/* Sinon le type de transport correspond à du TCP*/
			try {
				sockClientTCP = new Socket(ip, port);
				/* On envoie le socket TCP pour positionnement des flux*/
				envoyer = new Envoie(sockClientTCP);
				/* On reçoit le socket TCP après positionnement des flux*/
				recevoir = new Recevoir(sockClientTCP);
			} catch (IOException ioe) {
				/* Affichage d'un message d'erreur*/
				System.out.println("Erreur de création ou de connexion : " + ioe.getMessage());
				return 1;
			}
		}
		return 0;
	}

	/*
	 * Méthode EnvoiePDU : Méthode permettant d'envoyer une PDU
	 * @param : La PDU à envoyer
	 * @return : 0 si ça s'est bien passée, 1 sinon.
	 */
	
	public Integer EnvoiePDU(PDU pdu) {
		/* Si le socket TCP existe */
		if (sockClientTCP != null) {
			/* Si l'envoie de la PDU est un échec */
			if (envoyer.EnvoiePDU(pdu) != 0) {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur lors de l'envoi de la PDU");
				return 1;
			}
			/* Si le socket UDP existe */
		} else if (sockClientUDP != null) {
			// TODO Auto-generated method stu
		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Le sockect client n'a pas été initialisé");
			return 1;
		}
		return 0;
	}

	/*
	 * Méthode EnvoiePDU : Méthode permettant de recevoir une PDU
	 * @return : La PDU à recevoir
	 */
	public PDU RecevoirPDU() {
		/* Déclaration d'une variable locale de type PDU*/
		PDU reponse = null;
		/* Si le socket TCP existe */
		if (sockClientTCP != null) {
			/* On reçoit la PDU*/
			reponse = recevoir.RecevoirPDU();
			/* Si la PDU n'est pas reçue */
			if (reponse == null) {
				/* Affichage d'un message d'erreur*/
				System.out.println("Erreur lors de la reception de la PDU");
				return null;
			}
			/* Si le socket UDP existe */
		} else if (sockClientUDP != null) {
			// TODO Auto-generated method stu
		} else {
			/* Affichage d'un message d'erreur*/
			System.out.println("Le sockect client n'a pas été initialisé");
			return null;
		}
		return reponse;
	}
	
	/*
	 * Méthode FermerSocket : Méthode permettant de fermer un socket
	 */
	
	public void FermerSocket() {
		/* Si le type de transport est de l'UDP*/
		if (typeTransport == "UDP") {
			/* On ferme le socket UDP */
			sockClientUDP.close();
		} else {
			/* On ferme le socket TCP */
			try {
				sockClientTCP.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
