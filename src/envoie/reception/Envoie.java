package envoie.reception;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.Socket;

public class Envoie {
	DatagramSocket sockClientUDP = null;
	Socket sockClientTCP = null;
	ObjectOutputStream envoiPDU;

	public Envoie(Socket s) {
		this.sockClientTCP = s;
		// Instancie un object output stream travaillant sur l’output stream de la
		// socket
		try {
			envoiPDU = new ObjectOutputStream(sockClientTCP.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur");
		}
	}

	public Envoie(DatagramSocket s) {
		this.sockClientUDP = s;
		// TODO Auto-generated method stu
	}

	public Integer EnvoiePDU(PDU requete) {
		if (sockClientTCP != null) {
			try {
				envoiPDU.writeObject(requete);
			} catch (IOException e) {
				e.printStackTrace();
				return 1;
			}
		} else if (sockClientUDP != null) {
			// TODO Auto-generated method stu
		} else {
			System.out.println("Erreur d'initialisation du socket");
			return 1;
		}
		return 0;
	}

	public Integer EnvoieByte(PDU requete) {
		if (sockClientTCP != null) {

		} else if (sockClientUDP != null) {

		} else {
			System.out.println("Erreur d'initialisation du socket");
			return 1;
		}
		return 0;
	}

}
