package envoie.reception;

import java.io.Serializable;

import systeme.fichiers.Fichier;

public class PDU implements Serializable {
	String type,commande,donnees;
	Fichier fichier;
		
	public PDU(String type, String commande, String donnees, Fichier fichier) {
		super();
		this.type = type;
		this.commande = commande;
		this.donnees = donnees;
		this.fichier = fichier;
	}


	public Fichier getFichier() {
		return fichier;
	}
	public void setFichier(Fichier fichier) {
		this.fichier = fichier;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCommande() {
		return commande;
	}

	public void setCommande(String commande) {
		this.commande = commande;
	}

	public String getDonnees() {
		return donnees;
	}

	public void setDonnees(String donnees) {
		this.donnees = donnees;
	}
	
}
