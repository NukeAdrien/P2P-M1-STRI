package envoie.reception;

import java.io.Serializable;

public class PDU implements Serializable {
	String type,donnees;
	
		
	public PDU(String type, String donnees) {
		super();
		this.type = type;
		this.donnees = donnees;
		}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDonnees() {
		return donnees;
	}

	public void setDonnees(String donnees) {
		this.donnees = donnees;
	}
	
}
