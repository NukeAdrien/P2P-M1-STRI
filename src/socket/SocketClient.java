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
	/* Déclaration de variables */
	DatagramSocket sockClientUDP = null;
	Socket sockClientTCP = null;
	String typeTransport;
	Envoie envoyer;
	Recevoir recevoir;
	String ip;
	int port;


	/*
	 * Constructeur SocketClient --> Ce constructeur prend en paramètre le type de connexion utilisé
	 * Ce constructeur permet de créer un nouveau socketClient.
	 */
	
	public SocketClient(String type) {
		typeTransport = type;
	}
	
	/*
	 * Méthode InitialisationSocket : Permet d'initialiser un socket UDP ou TCP
	 * @param : L'adresse ip de destination et le port de destination
	 * @return : 0 si ça d'est bien passée, 1 sinon 
	 */

	public Integer InitialisationSocket(String i, Integer p) {
		this.ip=i;
		this.port=p;
		/* Si c'est UDP */
		if (typeTransport.compareTo("UDP")==0) {
			try {
				/* On crée un datagramme UDP */
				sockClientUDP = new DatagramSocket();
				/* On en crée unobjet Envoie */
				envoyer = new Envoie(sockClientUDP);
				/* On en crée un objet Recevoir */
				recevoir = new Recevoir(sockClientUDP);
			} catch (SocketException e) {
				e.printStackTrace();
				return 1;
			}
			/* On envoie ce datagramme pour qu'il soit traité */
			envoyer = new Envoie(sockClientUDP);
		} else {
			try {
				/* On crée un socket TCP */
				sockClientTCP = new Socket(ip, port);
				/* On en crée un objet Envoie */
				envoyer = new Envoie(sockClientTCP);
				/* On en crée un objet Recevoir */
				recevoir = new Recevoir(sockClientTCP);
			} catch (IOException ioe) {
				/* Affichage d'un message d'erreur si'il y a un problème */
				System.out.println("Erreur de création ou de connexion : " + ioe.getMessage());
				return 1;
			}
		}
		return 0;
	}

	/*
	 * Méthode EnvoiePDU : Permet d'envoyer la PDU UDP ou TCP
	 * @param : La PDU à envoyer
	 * @return : 0 si ça d'est bien passée, 1 sinon 
	 */
	
	public Integer EnvoiePDU(PDU pdu) {
		/* Si le socket TCP existe */
		if (sockClientTCP != null) {
			/* Appel la méthode envoie TCP*/
			if (envoyer.EnvoiePDUTCP(pdu) != 0) {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur lors de l'envoi de la PDU");
				return 1;
			}
			/* Si le datagramme existe UDP */
		} else if (sockClientUDP != null) {
			/* Appel la méthode envoie UDP*/
			if (envoyer.EnvoiePDUUDP(pdu,this.ip,this.port) != 0) {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur lors de l'envoi de la PDU");
				return 1;
			}
		} else {
			/* Affichage d'un message d'erreur sinon */
			System.out.println("Le socket client n'a pas été initialisé");
			return 1;
		}
		return 0;
	}

	/*
	 * Méthode RecevoirPDU : Permet d'envoyer la PDU UDP ou TCP
	 * @return : 0 si ça d'est bien passée, 1 sinon 
	 */
	public PDU RecevoirPDU() {
		/* Déclaration de variables */
		PDU reponse = null;
		/* Si le socket TCP existe  */
		if (sockClientTCP != null) {
			/*On reçoit la PDU TCP */
			reponse = recevoir.RecevoirPDUTCP();
			if (reponse == null) {
				/* Affichage d'un message d'erreur s'il n'y a pas de réponses */
				System.out.println("Erreur lors de la réception de la PDU");
				return null;
			}
			/* Si le datagramme UDP existe */
		} else if (sockClientUDP != null) {
				/*On reçoit la PDU UDP */
				reponse = recevoir.RecevoirPDUUDP();
				if (reponse == null) {
					/* Affichage d'un message d'erreur s'il n'y a pas de réponses */
					System.out.println("Erreur lors de la réception de la PDU");
					return null;
				}
		} else {
			/* Affichage d'un message d'erreur sinon */
			System.out.println("Le socket client n'a pas été initialisé");
			return null;
		}
		return reponse;
	}
	
	/*
	 * Méthode FermerSocket : Permet de fermer la connexion d'un socket 
	 */
	public void FermerSocket() {
		/* Si c'est de l'UDP */
		if (typeTransport.compareTo("UDP")==0) {
			/* On ferme la connexion */
			sockClientUDP.close();
		} else {
			/* Si c'est du TCP */
			/* On crée la PDU de fin de téléchargement */
			PDUControle fin = new PDUControle(null, null, "FIN", null);
			/* On envoie la PDU */
			this.EnvoiePDU(fin);
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
