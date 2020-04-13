package envoie.reception;

import systeme.fichiers.Fichier;


/*
 * Classe PDUControle --> Classe permettant de contrôler une PDU ... Hérite de la classe PDU
 */


@SuppressWarnings("serial")
public class PDUControle extends PDU {
	/* Déclaration de variables */
	String commande;
	Fichier fichier;
	
	/*
	 * Constructeur PDUControle --> Ce constructeur prend en paramètres celles de sa classe héritée mais aussi la commande qui va permettre
	 * d'interagir avec la PDU et le Fichier où sera stockée la PDU.
	 */
	public PDUControle(String type, String commande, String donnees, Fichier fichier) {
		super(type, donnees);
		this.commande = commande;
		this.fichier = fichier;
	}
	
	/*
	 * Méthode getCommande : Méthode permettant d'obtenir la commande permettant d'intéragir avec la PDU
	 * @return : la commande en String
	 */
	public String getCommande() {
		return commande;
	}
	
	/*
	 * Méthode setCommande : Méthode permettant de changer la commande permettant d'intéragir avec la PDU
	 * @param : la nouvelle commande en String
	 */
	public void setCommande(String commande) {
		this.commande = commande;
	}
	
	/*
	 * Méthode getFichier : Méthode permettant d'obtenir le Fichier dans lequel sera stockée la PDU
	 * @return : le fichier de type Fichier
	 */
	public Fichier getFichier() {
		return fichier;
	}
	
	/*
	 * Méthode setFichier : Méthode permettant de changer le Fichier dans lequel sera stockée la PDU
	 * @param : le nouveau fichier de type Fichier
	 */
	public void setFichier(Fichier fichier) {
		this.fichier = fichier;
	}
	
}
