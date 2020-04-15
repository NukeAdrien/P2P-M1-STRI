package envoie.reception;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

/*
 * Classe Recevoir --> Classe permettant de recevoir une PDU et le contenu de la PDU associé
 */

public class Recevoir {
	/* Déclaration de variables */
	DatagramSocket sockClientUDP = null;
	Socket sockClientTCP = null;
	ObjectInputStream receptionPDU;
	 DatagramPacket dgram;
	 String ipRequete;
	 int portRequete;

	/*
	 * Constructeur Recevoir --> Ce constructeur prend en paramètre un socket TCP instancié
	 * Ce constructeur instancie aussi un flux OutputStream travaillant avec le socket TCP instancié
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
	 * Constructeur Recevoir --> Ce constructeur prend en paramètre un socket UDP instancié
	 */
	public Recevoir(DatagramSocket s) {
		this.sockClientUDP = s;
	}

	/*
	 * Méthode EnvoiePDU : Cette méthode permet d'envoyer une PDU.
	 * @return : Retourne la PDU reçu  
	 */
	public PDU RecevoirPDUTCP() {
		/* Si le socket TCP crée n'est pas nulle... Autrement si le socket TCP a bien été créée*/
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
			/* Verifie que le socket UDP a été créé*/
			if (sockClientUDP != null) {
				try{
					// Construction du tampon et de l’objet qui vont servir à recevoir
					 byte[] buffer = new byte[100000];
					 this.dgram = new DatagramPacket(buffer, buffer.length);
					 // Attends puis reçoit un datagramme
					 sockClientUDP.receive(dgram); 
					 this.ipRequete = dgram.getAddress().toString();
					 this.portRequete = dgram.getPort();
					 // Extraction des données
					 ByteArrayInputStream stream = new ByteArrayInputStream(dgram.getData());
					 ObjectInputStream o = new ObjectInputStream(stream);
					 try {
						 //Cast l'objet inputstream en PDU
						PDU pdu = (PDU) o.readObject();
						if(pdu == null) {
							System.out.println("Erreur readObject");
						}
						//Retourne la PDU
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
		
		/*
		 * Methode getIpRequete : Permet d'obtenir ip de l'emetteur du datagramme
		 * @return : IP
		 */
		public String getIpRequete() {
			return ipRequete;
		}
		
		/*
		 * Methode setIpRequete : Permet de changer l'IP de l'emetteur du datagramme
		 * @param : la nouvelle IP
		 */
		public void setIpRequete(String ipRequete) {
			this.ipRequete = ipRequete;
		}
		
		/*
		 * Methode getPortRequete: Permet d'obtenir le port de l'emetteur
		 * @return : le numero du port
		 */
		public int getPortRequete() {
			return portRequete;
		}
		
		/*
		 * Methode setPortRequete: Permet de changer le numero de port de l'emetteur
		 * @param : le nouveau numero du port
		 */
		public void setPortRequete(int portRequete) {
			this.portRequete = portRequete;
		}

}
