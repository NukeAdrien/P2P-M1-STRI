package envoie.reception;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

/*
 * Classe Recevoir --> Classe permettant de recevoir une PDU et le contenu de la PDU associ�
 */

public class Recevoir {
	/* D�claration de variables */
	DatagramSocket sockClientUDP = null;
	Socket sockClientTCP = null;
	ObjectInputStream receptionPDU;
	 DatagramPacket dgram;
	 String ipRequete;
	 int portRequete;

	/*
	 * Constructeur Recevoir --> Ce constructeur prend en param�tre un socket TCP instanci�
	 * Ce constructeur instancie aussi un flux OutputStream travaillant avec le socket TCP instanci�
	 */
	public Recevoir(Socket s) {
		this.sockClientTCP = s;
		try {
			receptionPDU = new ObjectInputStream(sockClientTCP.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Constructeur Recevoir --> Ce constructeur prend en param�tre un socket UDP instanci�
	 */
	public Recevoir(DatagramSocket s) {
		this.sockClientUDP = s;
	}

	/*
	 * M�thode EnvoiePDU : Cette m�thode permet d'envoyer une PDU.
	 * @return : Retourne la PDU re�u  
	 */
	public PDU RecevoirPDUTCP() {
		/* Si le socket TCP cr�e n'est pas nulle... Autrement si le socket TCP a bien �t� cr��e*/
		if (sockClientTCP != null) {
			/* On instancie un objet pour pouvoir lire la PDU */
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
			/* Si l'objet est du type de la classe PDU */
			if (o instanceof PDU) {
				/* On retourne la PDU de l'objet o */
				return (PDU) o;
			} else {
				/* Sinon on retourne null */
				return null;
			}
			
		}else{
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur d'initialisation du socket");
			return null;
		}
	}
		public PDU RecevoirPDUUDP() {
			/* Si le socket TCP cr�e n'est pas nulle... Autrement si le socket TCP a bien �t� cr��e*/
			if (sockClientUDP != null) {
				try{
					// Construction du tampon et de l�objet qui vont servir � recevoir
					 byte[] buffer = new byte[100000];
					 this.dgram = new DatagramPacket(buffer, buffer.length);
					 // Attends puis re�oit un datagramme
					 sockClientUDP.receive(dgram); 
					 this.ipRequete = dgram.getAddress().toString();
					 this.portRequete = dgram.getPort();
					 // Extrait les donn�es
					 ByteArrayInputStream stream = new ByteArrayInputStream(dgram.getData());
					 ObjectInputStream o = new ObjectInputStream(stream);
					 try {
						PDU pdu = (PDU) o.readObject();
						if(pdu == null) {
							System.out.println("Erreur readObject");
						}
						return pdu;
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						System.out.println("Erreur UDP");
						return null;
					}
					}
					catch(IOException ioe) {
					 System.out.println("Erreur socket : " + ioe.getMessage());
					 return null;
					}

			} else{
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur d'initialisation du socket");
				return null;
			}
	}

		public String getIpRequete() {
			return ipRequete;
		}

		public void setIpRequete(String ipRequete) {
			this.ipRequete = ipRequete;
		}

		public int getPortRequete() {
			return portRequete;
		}

		public void setPortRequete(int portRequete) {
			this.portRequete = portRequete;
		}

}
