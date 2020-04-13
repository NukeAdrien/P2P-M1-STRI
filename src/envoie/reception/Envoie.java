package envoie.reception;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.Socket;

/*
 * Classe Envoie --> Classe permettant d'envoyer une PDU et le contenu de la PDU
 */

public class Envoie {

	/* D�claration de variables */
	DatagramSocket sockClientUDP = null;
	Socket sockClientTCP = null;
	ObjectOutputStream envoiPDU;

	/*
	 * Constructeur Envoie --> Ce constructeur prend en param�tre un socket TCP cr�e et d�j� instanci�
	 * Ce constructeur instancie aussi un flux OutputStream travaillant avec le socket TCP instanci�
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
	 * Constructeur Envoie --> Ce constructeur prend en param�tre un socket UDP cr�e et d�j� instanci�
	 */


	public Envoie(DatagramSocket s) {
		this.sockClientUDP = s;
	}

	/*
	 * M�thode EnvoiePDU : Cette m�thode permet d'envoyer une PDU.
	 * @param : Prend en param�tre une PDU � envoyer
	 * @return : Retourne 0 si �a s'est bien pass�e,  sinon 1  
	 */
	public Integer EnvoiePDU(PDU requete) {
		/* Si le socket TCP cr�e n'est pas nulle... Autrement si le socket TCP a bien �t� cr��e*/
		if (sockClientTCP != null) {
			try {
				/*On �crit dans la PDU et qui sera envoy� a travers le flux OutputStream*/
				envoiPDU.writeObject(requete);
			} catch (IOException e) {
				e.printStackTrace();
				return 1;
			}
			/* Si le socket UDP cr�e n'est pas nulle... Autrement dit si le socket UDP a bien �t� cr��e*/
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
	 * M�thode EnvoiePDUByte.. Cette m�thode permet d'envoyer une PDU avec les donn�es.
	 * @param : Prend en param�tre une PDU � envoyer
	 * @return : Retourne 0 si �a s'est bien pass�e,  sinon 1  
	 */
	//public Integer EnvoieByte(PDU requete) {
	//	/* Si le socket TCP cr�e n'est pas nulle... Autrement si le socket TCP a bien �t� cr��e*/
		//if (sockClientTCP != null) {

			/* Si le socket UDP cr�e n'est pas nulle... Autrement si le socket UDP a bien �t� cr��e*/
		//} else if (sockClientUDP != null) {

		//} else {
			/* Affichage d'un message d'erreur*/
			//System.out.println("Erreur d'initialisation du socket");
			//return 1;
		//}
		//return 0;
	//}

}
