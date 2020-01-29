package envoie.reception;

/*
 * Classe PDUDonnees --> Classe permettant d'intéragir avec les blocs de données d'une PDU ... Hérite de la classe PDU
 */

public class PDUDonnees extends PDU {
	
	/* Déclaration de variables */
	byte[] bloc;
	Integer index;
	
	/*
	 * Constructeur PDUDonnees --> Ce constructeur prend en paramètres celles de sa classe héritée qu'est le type de la PDU 
	 * et les données qu'elle contienne... Il prend aussi en paramètre l'index d'un bloc contenue dans la PDU
	 * mais aussi les données d'un bloc contenue dans la PDU également.
	 */
	public PDUDonnees(String type, String donnees,Integer index,byte[] bloc) {
		super(type, donnees);
		this.bloc = bloc;
		this.index = index;
	}
	
	/*
	 * Méthode getBloc : Méthode permettant d'obtenir les données d'un des blocs constituant la PDU
	 * @return : le contenu d'un des blocs constituant la PDU
	 */
	public byte[] getBloc() {
		return bloc;
	}
	
	/*
	 * Méthode setBloc : Méthode permettant de changer les données d'un des blocs constituant la PDU
	 * @param : le nouveau contenu d'un des blocs constituant la PDU
	 */
	public void setBloc(byte[] bloc) {
		this.bloc = bloc;
	}
	
	/*
	 * Méthode getIndex : Méthode permettant d'obtenir l'index d'un des blocs constituant la PDU
	 * @return : l'index d'un des blocs constituant la PDU
	 */
	public Integer getIndex() {
		return index;
	}
	
	/*
	 * Méthode setIndex : Méthode permettant de changer l'index d'un des blocs constituant la PDU
	 * @param : le nouvel index d'un des blocs constituant la PDU
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

}
