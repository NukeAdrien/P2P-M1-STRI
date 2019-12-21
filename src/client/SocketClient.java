package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import envoie.reception.*;


public class SocketClient {
	DatagramSocket sockClientUDP = null;
	Socket sockClientTCP = null;
	String typeTransport;
	Envoie envoyer;
	Recevoir recevoir;

	public SocketClient(String type) {
		typeTransport = type;
	}

	public Integer InitialisationSocket(String ip, Integer port) {
		if (typeTransport == "UDP") {
			try {
				sockClientUDP = new DatagramSocket(4444);
			} catch (SocketException e) {
				e.printStackTrace();
				return 1;
			}
			envoyer = new Envoie(sockClientUDP);
		} else {
			try {
				sockClientTCP = new Socket(ip, port);
				Envoie envoyer = new Envoie(sockClientTCP);
			} catch (IOException ioe) {
				System.out.println("Erreur de création ou de connexion : " + ioe.getMessage());
				return 1;
			}
		}
		return 0;
	}

	public Integer EnvoiePDU(PDU pdu) {
		if (sockClientTCP != null) {
			if(envoyer.EnvoiePDU(pdu)!= 0) {
				System.out.println("Erreur lors de l'envoi de la PDU");
				return 0;
			}
		} else if (sockClientUDP != null) {
			// TODO Auto-generated method stu
		} else {
			System.out.println("Le sockect client n'a pas été initialisé");
			return 1;
		}
		return null;
	}
	
	public PDU RecevoirPDU() {
		if (sockClientTCP != null) {
			if(recevoir.RecevoirPDU() != null) {
				System.out.println("Erreur lors de la reception de la PDU");
				return null;
			}
		} else if (sockClientUDP != null) {
			// TODO Auto-generated method stu
		} else {
			System.out.println("Le sockect client n'a pas été initialisé");
			return null;
		}
		return null;
	}

}
