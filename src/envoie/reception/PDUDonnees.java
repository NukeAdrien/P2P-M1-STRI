package envoie.reception;

/*
 * Classe PDUDonnees --> Classe permettant d'int�ragir avec les blocs de donn�es d'une PDU ... H�rite de la classe PDU
 */

public class PDUDonnees extends PDU {
	
	/* D�claration de variables */
	byte[] bloc;
	Integer index;
	
	/*
	 * Constructeur PDUDonnees --> Ce constructeur prend en param�tres celles de sa classe h�rit�e qu'est le type de la PDU 
	 * et les donn�es qu'elle contienne... Il prend aussi en param�tre l'index d'un bloc contenue dans la PDU
	 * mais aussi les donn�es d'un bloc contenue dans la PDU �galement.
	 */
	public PDUDonnees(String type, String donnees,Integer index,byte[] bloc) {
		super(type, donnees);
		this.bloc = bloc;
		this.index = index;
	}
	
	/*
	 * M�thode getBloc : M�thode permettant d'obtenir les donn�es d'un des blocs constituant la PDU
	 * @return : le contenu d'un des blocs constituant la PDU
	 */
	public byte[] getBloc() {
		return bloc;
	}
	
	/*
	 * M�thode setBloc : M�thode permettant de changer les donn�es d'un des blocs constituant la PDU
	 * @param : le nouveau contenu d'un des blocs constituant la PDU
	 */
	public void setBloc(byte[] bloc) {
		this.bloc = bloc;
	}
	
	/*
	 * M�thode getIndex : M�thode permettant d'obtenir l'index d'un des blocs constituant la PDU
	 * @return : l'index d'un des blocs constituant la PDU
	 */
	public Integer getIndex() {
		return index;
	}
	
	/*
	 * M�thode setIndex : M�thode permettant de changer l'index d'un des blocs constituant la PDU
	 * @param : le nouvel index d'un des blocs constituant la PDU
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

}
