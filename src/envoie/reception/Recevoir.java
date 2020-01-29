package envoie.reception;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.net.Socket;

/*
 * Classe Envoie --> Classe permettant de recevoir une PDU et le contenu de la PDU
 */

public class Recevoir {
	/* Déclaration de variables */
	DatagramSocket sockClientUDP = null;
	Socket sockClientTCP = null;
	ObjectInputStream receptionPDU;

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
	 * Ce constructeur instancie aussi un flux OutputStream travaillant avec le socket UDP instancié
	 */
	public Recevoir(DatagramSocket s) {
		this.sockClientUDP = s;
	}

	/*
	 * Méthode EnvoiePDU.. Cette méthode permet d'envoyer une PDU.
	 * @return : Retourne la PDU reçu  
	 */
	public PDU RecevoirPDU() {
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
			/* Si le socket UDP crée n'est pas nulle... Autrement si le socket UDP a bien été créée*/
		} else if (sockClientUDP != null) {
			// TODO Auto-generated method stu

		} else

		{
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur d'initialisation du socket");
			return null;
		}
		return null;
	}

	/*
	 * Méthode RecevoirByte.. Cette méthode permet de recevoir une PDU avec les données.
	 * @param : Prend en paramètre une PDU à recevoir
	 * @return : Retourne 0 si ça s'est bien passée,  sinon 1  
	 */
	public Integer RecevoirByte(PDU requete) {
		/* Si le socket TCP crée n'est pas nulle... Autrement si le socket TCP a bien été créée*/
		if (sockClientTCP != null) {

			/* Si le socket UDP crée n'est pas nulle... Autrement si le socket UDP a bien été créée*/
		} else if (sockClientUDP != null) {

			/* Affichage d'un message d'erreur*/
		} else {
			System.out.println("Erreur d'initialisation du socket");
			return 1;
		}
		return 0;
	}
}
