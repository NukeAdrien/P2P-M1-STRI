package socket;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import envoie.reception.*;

/*
 * Classe SocketClient --> Cette classe permet de cr�er, d'instancier et de lancer un socket
 */

public class SocketClient {
	
	/* D�claration de vaiables */
	DatagramSocket sockClientUDP = null;
	Socket sockClientTCP = null;
	String typeTransport;
	Envoie envoyer;
	Recevoir recevoir;

	/*
	 * Constructeur SocketClient --> Ce constructeur prend en param�tre le type de socket,
	 * si c'est TCP ou encore si c'est UDP
	 * Ce constructeur permet de cr�er un nouveau socketClient.
	 */
	public SocketClient(String type) {
		typeTransport = type;
	}

	/*
	 * M�thode InitialisationSocket : M�thode permettant d'initialiser un socket
	 * @param : l'adresse IP et le num�ro de port � lier au scoket
	 * @return : Un entier 0 si �a s'est bien pass�e, 1 sinon.
	 */
	public Integer InitialisationSocket(String ip, Integer port) {
		/* Si le type de transport correspond � de l'UDP*/
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
			/* Sinon le type de transport correspond � du TCP*/
			try {
				sockClientTCP = new Socket(ip, port);
				/* On envoie le socket TCP pour positionnement des flux*/
				envoyer = new Envoie(sockClientTCP);
				/* On re�oit le socket TCP apr�s positionnement des flux*/
				recevoir = new Recevoir(sockClientTCP);
			} catch (IOException ioe) {
				/* Affichage d'un message d'erreur*/
				System.out.println("Erreur de cr�ation ou de connexion : " + ioe.getMessage());
				return 1;
			}
		}
		return 0;
	}

	/*
	 * M�thode EnvoiePDU : M�thode permettant d'envoyer une PDU
	 * @param : La PDU � envoyer
	 * @return : 0 si �a s'est bien pass�e, 1 sinon.
	 */
	
	public Integer EnvoiePDU(PDU pdu) {
		/* Si le socket TCP existe */
		if (sockClientTCP != null) {
			/* Si l'envoie de la PDU est un �chec */
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
			System.out.println("Le sockect client n'a pas �t� initialis�");
			return 1;
		}
		return 0;
	}

	/*
	 * M�thode EnvoiePDU : M�thode permettant de recevoir une PDU
	 * @return : La PDU � recevoir
	 */
	public PDU RecevoirPDU() {
		/* D�claration d'une variable locale de type PDU*/
		PDU reponse = null;
		/* Si le socket TCP existe */
		if (sockClientTCP != null) {
			/* On re�oit la PDU*/
			reponse = recevoir.RecevoirPDU();
			/* Si la PDU n'est pas re�ue */
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
			System.out.println("Le sockect client n'a pas �t� initialis�");
			return null;
		}
		return reponse;
	}
	
	/*
	 * M�thode FermerSocket : M�thode permettant de fermer un socket
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
