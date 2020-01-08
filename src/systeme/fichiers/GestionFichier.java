package systeme.fichiers;

import java.util.HashMap;
import java.util.List;

public class GestionFichier {
	HashMap<String, Fichier> capitalCities = new HashMap<String, Fichier>();
	public Fichier RechercheFichier(String nomFichier) {
		Fichier recherche = null;
		recherche = new Fichier("TOTO.txt","TOTO","10/10/2020","/Telechargement/TOTO.txt",2657);
		return recherche;
	}
	
	public Integer EtatFichier(String nomFichier) {
		Integer etat = 0;
		etat = 1;//Provisoire
		return etat;
	}
	
	public Byte Lire(Fichier fichier,Integer numBloc) {
		Byte lecture = null;
		return lecture;
	}
	
	public Integer Ecrire(Fichier fichier,Integer numbloc,Byte donness ) {
		Integer etat = 0;
		etat = 1;//Provisoire
		return etat;
	}

	public HashMap<String, Fichier> getCapitalCities() {
		return capitalCities;
	}

	public void setCapitalCities(HashMap<String, Fichier> capitalCities) {
		this.capitalCities = capitalCities;
	}

	
	
	
	
}
