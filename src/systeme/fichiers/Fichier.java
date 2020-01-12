package systeme.fichiers;

import java.io.Serializable;
import java.util.HashMap;

public class Fichier implements Serializable {
	String nomFichier, auteur, date, emplacement;
	Integer tailleOctets;
	HashMap<Integer, HeaderBloc> listHeaderBlocs = new HashMap<Integer, HeaderBloc>(); 

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

	public void setlistHeaderBlocs(HashMap<Integer, HeaderBloc> listHeaderBlocs) {
		this.listHeaderBlocs = listHeaderBlocs;
	}
	

	public HashMap<Integer, HeaderBloc> getListHeaderBlocs() {
		return listHeaderBlocs;
	}

	public void setListHeaderBlocs(HashMap<Integer, HeaderBloc> listHeaderBlocs) {
		this.listHeaderBlocs = listHeaderBlocs;
	}

	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}

	public String getNomFichier() {
		return nomFichier;
	}

	public String getEmplacement() {
		return emplacement;
	}
	
	
	public int getDisponible(Integer index) {
		return this.listHeaderBlocs.get(index).getDisponible();
	}
	public void setDisponible(Integer index, int disponible) {
		this.listHeaderBlocs.get(index).setDisponible(disponible);
	}

	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}
	
	public void AjouterHeaderBloc(Integer index, HeaderBloc hd) {
		this.listHeaderBlocs.put(index, hd);
	}
	
}
