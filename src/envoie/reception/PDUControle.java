package envoie.reception;

import systeme.fichiers.Fichier;


/*
 * Classe PDUControle --> Classe permettant de contr�ler une PDU ... H�rite de la classe PDU
 */


@SuppressWarnings("serial")
public class PDUControle extends PDU {
	/* D�claration de variables */
	String commande;
	Fichier fichier;
	
	/*
	 * Constructeur PDUControle --> Ce constructeur prend en param�tres celles de sa classe h�rit�e mais aussi la commande qui va permettre
	 * d'interagir avec la PDU et le Fichier o� sera stock�e la PDU.
	 */
	public PDUControle(String type, String commande, String donnees, Fichier fichier) {
		super(type, donnees);
		this.commande = commande;
		this.fichier = fichier;
	}
	
	/*
	 * M�thode getCommande : M�thode permettant d'obtenir la commande permettant d'int�ragir avec la PDU
	 * @return : la commande en String
	 */
	public String getCommande() {
		return commande;
	}
	
	/*
	 * M�thode setCommande : M�thode permettant de changer la commande permettant d'int�ragir avec la PDU
	 * @param : la nouvelle commande en String
	 */
	public void setCommande(String commande) {
		this.commande = commande;
	}
	
	/*
	 * M�thode getFichier : M�thode permettant d'obtenir le Fichier dans lequel sera stock�e la PDU
	 * @return : le fichier de type Fichier
	 */
	public Fichier getFichier() {
		return fichier;
	}
	
	/*
	 * M�thode setFichier : M�thode permettant de changer le Fichier dans lequel sera stock�e la PDU
	 * @param : le nouveau fichier de type Fichier
	 */
	public void setFichier(Fichier fichier) {
		this.fichier = fichier;
	}
	
}
