package envoie.reception;

import java.io.Serializable;

/*
 * Classe PDU --> Classe permettant d'envoyer de créer une PDU
 */

public class PDU implements Serializable {
	
	/*Déclaration de variables*/
	String type,donnees;
	
	/*
	 * Constructeur PDU --> Ce constructeur prend en paramètre le type de PDU ainsi que les données qu'il contienne
	 */

	public PDU(String type, String donnees) {
		super();
		this.type = type;
		this.donnees = donnees;
		}

	/*
	 * Méthode getType : Méthode permettant de récupérer le type de la PDU
	 * @return : le type de PDU en String
	 */
	public String getType() {
		return type;
	}

	/*
	 * Méthode setType : Méthode permettant de changer le type de la PDU
	 * @param : le nouveau type de la PDU
	 */
	public void setType(String type) {
		this.type = type;
	}

	/*
	 * Méthode getType : Méthode permettant d'obtenir le type de la PDU
	 * @return : les données de la PDU en String
	 */
	public String getDonnees() {
		return donnees;
	}

	/*
	 * Méthode getType : Méthode permettant de changer les données de la PDU
	 * @param : les nouvelles données de la PDU en String
	 */
	public void setDonnees(String donnees) {
		this.donnees = donnees;
	}
	
}
