package systeme.fichiers;

public class GestionFichier {
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
	
}
