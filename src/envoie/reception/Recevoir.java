package envoie.reception;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.net.Socket;

public class Recevoir {
	DatagramSocket sockClientUDP = null;
	Socket sockClientTCP = null;
	ObjectInputStream receptionPDU;

	public Recevoir(Socket s) {
		this.sockClientTCP = s;
		try {
			receptionPDU = new ObjectInputStream(sockClientTCP.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Recevoir(DatagramSocket s) {
		this.sockClientUDP = s;
	}

	public PDU RecevoirPDU() {
		if (sockClientTCP != null) {
			Object o = null;
			try {
				o = receptionPDU.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (o instanceof PDU) {
				return (PDU) o;
			} else {
				return null;
			}
		} else if (sockClientUDP != null) {
			// TODO Auto-generated method stu

		} else

		{
			System.out.println("Erreur d'initialisation du socket");
			return null;
		}
		return null;
	}

	public Integer RecevoirByte(PDU requete) {
		if (sockClientTCP != null) {

		} else if (sockClientUDP != null) {

		} else {
			System.out.println("Erreur d'initialisation du socket");
			return 1;
		}
		return 0;
	}
}
