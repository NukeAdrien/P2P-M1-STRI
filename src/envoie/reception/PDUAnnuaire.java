package envoie.reception;

import java.util.List;

import systeme.fichiers.GestionFichier;

public class PDUAnnuaire extends PDU {
	GestionFichier  sysFichiers;
	String methode;
	List<String> listServeurs;
	
	public List<String> getListServeurs() {
		return listServeurs;
	}

	public void setListServeurs(List<String> listServeurs) {
		this.listServeurs = listServeurs;
	}

	public PDUAnnuaire(String type, String methode, GestionFichier lF,String donnees,List<String> lS) {
		super(type, donnees);
		this. sysFichiers = lF;
		this.methode = methode;
		this. listServeurs = lS;
	}

	public GestionFichier getSysFichiers() {
		return  sysFichiers;
	}
	public void setSysFichiers(GestionFichier sysFichiers) {
		this. sysFichiers =  sysFichiers;
	}
	public String getMethode() {
		return methode;
	}
	public void setMethode(String methode) {
		this.methode = methode;
	}
	
	

}
