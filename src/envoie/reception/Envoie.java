package envoie.reception;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.Socket;

/*
 * Classe Envoie --> Classe permettant d'envoyer une PDU et le contenu de la PDU
 */

public class Envoie {

	/* Déclaration de variables */
	DatagramSocket sockClientUDP = null;
	Socket sockClientTCP = null;
	ObjectOutputStream envoiPDU;

	/*
	 * Constructeur Envoie --> Ce constructeur prend en paramètre un socket TCP crée et déjà instancié
	 * Ce constructeur instancie aussi un flux OutputStream travaillant avec le socket TCP instancié
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
	 * Constructeur Envoie --> Ce constructeur prend en paramètre un socket UDP crée et déjà instancié
	 */


	public Envoie(DatagramSocket s) {
		this.sockClientUDP = s;
	}

	/*
	 * Méthode EnvoiePDU : Cette méthode permet d'envoyer une PDU.
	 * @param : Prend en paramètre une PDU à envoyer
	 * @return : Retourne 0 si ça s'est bien passée,  sinon 1  
	 */
	public Integer EnvoiePDU(PDU requete) {
		/* Si le socket TCP crée n'est pas nulle... Autrement si le socket TCP a bien été créée*/
		if (sockClientTCP != null) {
			try {
				/*On écrit dans la PDU et qui sera envoyé a travers le flux OutputStream*/
				envoiPDU.writeObject(requete);
			} catch (IOException e) {
				e.printStackTrace();
				return 1;
			}
			/* Si le socket UDP crée n'est pas nulle... Autrement dit si le socket UDP a bien été créée*/
		} else if (sockClientUDP != null) {
			// TODO Auto-generated method stub
		} else {
			/* Affichage d'un message d'erreur*/
			System.out.println("Erreur d'initialisation du socket");
			return 1;
		}
		return 0;
	}

	/*
	 * Méthode EnvoiePDUByte.. Cette méthode permet d'envoyer une PDU avec les données.
	 * @param : Prend en paramètre une PDU à envoyer
	 * @return : Retourne 0 si ça s'est bien passée,  sinon 1  
	 */
	//public Integer EnvoieByte(PDU requete) {
	//	/* Si le socket TCP crée n'est pas nulle... Autrement si le socket TCP a bien été créée*/
		//if (sockClientTCP != null) {

			/* Si le socket UDP crée n'est pas nulle... Autrement si le socket UDP a bien été créée*/
		//} else if (sockClientUDP != null) {

		//} else {
			/* Affichage d'un message d'erreur*/
			//System.out.println("Erreur d'initialisation du socket");
			//return 1;
		//}
		//return 0;
	//}

}
