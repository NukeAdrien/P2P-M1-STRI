package envoie.reception;

import systeme.fichiers.GestionFichier;

public class PDUAnnuaire extends PDU {
	GestionFichier listFichiers;
	int nbDowload,nbUpload;
	String donnees;
	public PDUAnnuaire(String type, String donnees, GestionFichier lF, String d) {
		super(type, donnees);
		listFichiers = lF;
		donnees = d;
	}
	public GestionFichier getListFichiers() {
		return listFichiers;
	}
	public void setListFichiers(GestionFichier listFichiers) {
		this.listFichiers = listFichiers;
	}
	
	

}
