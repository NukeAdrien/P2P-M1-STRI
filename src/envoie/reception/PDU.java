package envoie.reception;

import java.io.Serializable;

/*
 * Classe PDU --> Classe permettant d'envoyer de cr�er une PDU
 */

public class PDU implements Serializable {
	
	/*D�claration de variables*/
	String type,donnees;
	
	/*
	 * Constructeur PDU --> Ce constructeur prend en param�tre le type de PDU ainsi que les donn�es qu'il contienne
	 */

	public PDU(String type, String donnees) {
		super();
		this.type = type;
		this.donnees = donnees;
		}

	/*
	 * M�thode getType : M�thode permettant de r�cup�rer le type de la PDU
	 * @return : le type de PDU en String
	 */
	public String getType() {
		return type;
	}

	/*
	 * M�thode setType : M�thode permettant de changer le type de la PDU
	 * @param : le nouveau type de la PDU
	 */
	public void setType(String type) {
		this.type = type;
	}

	/*
	 * M�thode getType : M�thode permettant d'obtenir le type de la PDU
	 * @return : les donn�es de la PDU en String
	 */
	public String getDonnees() {
		return donnees;
	}

	/*
	 * M�thode getType : M�thode permettant de changer les donn�es de la PDU
	 * @param : les nouvelles donn�es de la PDU en String
	 */
	public void setDonnees(String donnees) {
		this.donnees = donnees;
	}
	
}
