package envoie.reception;

import systeme.fichiers.Fichier;

public class PDUControle extends PDU {
	String commande;
	Fichier fichier;
	public PDUControle(String type, String commande, String donnees, Fichier fichier) {
		super(type, donnees);
		this.commande = commande;
		this.fichier = fichier;
	}
	public String getCommande() {
		return commande;
	}
	public void setCommande(String commande) {
		this.commande = commande;
	}
	public Fichier getFichier() {
		return fichier;
	}
	public void setFichier(Fichier fichier) {
		this.fichier = fichier;
	}
	
}
