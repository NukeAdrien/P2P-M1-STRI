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
	/* D�claration de variables */
	DatagramSocket sockClientUDP = null;
	Socket sockClientTCP = null;
	String typeTransport;
	Envoie envoyer;
	Recevoir recevoir;

	/*
	 * Constructeur SocketClient --> Ce constructeur prend en param�tre le type de connexion utilis�
	 * Ce constructeur permet de cr�er un nouveau socketClient.
	 */
	
	public SocketClient(String type) {
		typeTransport = type;
	}
	
	/*
	 * M�thode InitialisationSocket : Permet d'initialiser un socket
	 * @param : L'adresse ip de destination et le port de destination
	 * @return : 0 si �a d'est bien pass�e, 1 sinon 
	 */

	public Integer InitialisationSocket(String ip, Integer port) {
		/* Si c'est UDP */
		if (typeTransport == "UDP") {
			try {
				/* On cr�e un datagramme UDP */
				sockClientUDP = new DatagramSocket(4444);
			} catch (SocketException e) {
				e.printStackTrace();
				return 1;
			}
			/* On envoie ce datagramme pour qu'il soit trait� */
			envoyer = new Envoie(sockClientUDP);
		} else {
			try {
				/* On cr�e un socket TCP */
				sockClientTCP = new Socket(ip, port);
				/* On envoie ce socket pour qu'il soit trait� */
				envoyer = new Envoie(sockClientTCP);
				/* On re�oit la socket trait� */
				recevoir = new Recevoir(sockClientTCP);
			} catch (IOException ioe) {
				/* Affichage d'un message d'erreur si'il y a un probl�me */
				System.out.println("Erreur de cr�ation ou de connexion : " + ioe.getMessage());
				return 1;
			}
		}
		return 0;
	}

	/*
	 * M�thode EnvoiePDU : Permet d'envoyer la PDU
	 * @param : La PDU � envoyer
	 * @return : 0 si �a d'est bien pass�e, 1 sinon 
	 */
	
	public Integer EnvoiePDU(PDU pdu) {
		/* Si le socket existe */
		if (sockClientTCP != null) {
			/* Si il y a un probl�me lors de l'envoi */
			if (envoyer.EnvoiePDU(pdu) != 0) {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur lors de l'envoi de la PDU");
				return 1;
			}
			/* Si le datagramme existe */
		} else if (sockClientUDP != null) {
			// TODO Auto-generated method stu
		} else {
			/* Affichage d'un message d'erreur sinon */
			System.out.println("Le socket client n'a pas �t� initialis�");
			return 1;
		}
		return 0;
	}

	/*
	 * M�thode RecevoirPDU : Permet d'envoyer la PDU
	 * @return : 0 si �a d'est bien pass�e, 1 sinon 
	 */
	public PDU RecevoirPDU() {
		/* D�claration de variables */
		PDU reponse = null;
		/* Si le socket existe */
		if (sockClientTCP != null) {
			/*On re�oit la PDU */
			reponse = recevoir.RecevoirPDU();
			if (reponse == null) {
				/* Affichage d'un message d'erreur s'il n'y a pas de r�ponses */
				System.out.println("Erreur lors de la r�ception de la PDU");
				return null;
			}
			/* Si le datagramme existe */
		} else if (sockClientUDP != null) {
			// TODO Auto-generated method stu
		} else {
			/* Affichage d'un message d'erreur sinon */
			System.out.println("Le socket client n'a pas �t� initialis�");
			return null;
		}
		return reponse;
	}
	
	/*
	 * M�thode FermerSocket : Permet de fermer la connexion d'un socket 
	 */
	public void FermerSocket() {
		/* Si c'est de l'UDP */
		if (typeTransport == "UDP") {
			/* On ferme la connexion */
			sockClientUDP.close();
		} else {
			/* Si c'est du TCP */
			try {
				/* On ferme la connexion */
				sockClientTCP.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
