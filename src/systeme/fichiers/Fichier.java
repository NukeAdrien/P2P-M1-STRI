package systeme.fichiers;

import java.util.List;

public class Fichier {
	String nomFichier, auteur, date, emplacement;
	Integer tailleOctets;
	List<Bloc> listBlocs;
	
	public Fichier(String n,String a,String d, String e, Integer t) {
		nomFichier = n;
		auteur = a;
		date = d;
		emplacement =e ;
		tailleOctets = t;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getTailleOctets() {
		return tailleOctets;
	}

	public void setTailleOctets(Integer tailleOctets) {
		this.tailleOctets = tailleOctets;
	}

	public List<Bloc> getListBlocs() {
		return listBlocs;
	}

	public void setListBlocs(List<Bloc> listBlocs) {
		this.listBlocs = listBlocs;
	}

	public String getNomFichier() {
		return nomFichier;
	}

	public String getEmplacement() {
		return emplacement;
	}
	
}
