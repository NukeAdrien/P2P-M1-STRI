package envoie.reception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * Classe Envoie --> Classe permettant d'envoyer une PDU et le contenu de la PDU
 */

public class Envoie {

	/* Déclaration de variables */
	DatagramSocket sockClientUDP = null;
	Socket sockClientTCP = null;
	ObjectOutputStream envoiPDU;

	/*
	 * Constructeur Envoie --> Ce constructeur prend en paramètre un socket TCP crée
	 * et déjà instancié Ce constructeur instancie aussi un flux OutputStream
	 * travaillant avec le socket TCP instancié
	 */

	public Envoie(Socket s) {
		this.sockClientTCP = s;
		try {
			envoiPDU = new ObjectOutputStream(sockClientTCP.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur");
		}
	}

	/*
	 * Constructeur Envoie --> Ce constructeur prend en paramètre un socket UDP crée
	 * et déjà instancié
	 */

	public Envoie(DatagramSocket s) {
		this.sockClientUDP = s;
	}

	/*
	 * Méthode EnvoiePDUTCP : Cette méthode permet d'envoyer une PDU.
	 * 
	 * @param : Prend en paramètre une PDU à envoyer
	 * 
	 * @return : Retourne 0 si ça s'est bien passée, sinon 1
	 */
	public Integer EnvoiePDUTCP(PDU requete) {
		/*Si le socket n'est pas null*/
		if (sockClientTCP != null) {
			try {
				/* On écrit dans la PDU et qui sera envoyé a travers le flux OutputStream */
				envoiPDU.writeObject(requete);
			} catch (IOException e) {
				e.printStackTrace();
				return 1;
			}
			/* Si le socket n'est pas initialiser alors on envoie un message d'erreur*/
		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur d'initialisation du socket");
			return 1;
		}
		return 0;
	}

	/*
	 * Méthode EnvoiePDUUDP : Cette méthode permet d'envoyer une PDU.
	 * 
	 * @param : Prend en paramètre une PDU à envoyer
	 * 
	 * @return : Retourne 0 si ça s'est bien passée, sinon 1
	 */
	public Integer EnvoiePDUUDP(PDU requete,String ip,int port) {
		
		 if (sockClientUDP != null) {
			// Transformation en tableau d'octets
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			try {
				ObjectOutputStream o = new ObjectOutputStream(stream);
				o.writeObject(requete);
			} catch (IOException e) {
				System.err.println("Erreur lors de la sérialisation : " + e);
				return 1;
			}
			// Création et envoi du segment UDP
			try {
				byte[] donnees = stream.toByteArray();
				InetAddress adresse = InetAddress.getByName(ip);
				DatagramPacket pdu = new DatagramPacket(donnees, donnees.length, adresse, port);
				this.sockClientUDP.send(pdu);
			} catch (UnknownHostException e) {
				System.err.println("Erreur lors de la création de l'adresse : " + e);
				return 1;
			} catch (IOException e) {
				System.err.println("Erreur lors de l'envoi du message : " + e);
				return 1;
			}
			/*Si le socket n'est pas initialiser alors on envoie un message d'erreur*/
		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur d'initialisation du socket");
			return 1;
		}
		return 0;
	}

}
