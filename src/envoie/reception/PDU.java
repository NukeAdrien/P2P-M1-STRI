package envoie.reception;

import java.io.Serializable;

public class PDU implements Serializable {
	String type,commande,donnees;
		
	public PDU(String type, String commande, String donnees) {
		super();
		this.type = type;
		this.commande = commande;
		this.donnees = donnees;
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
