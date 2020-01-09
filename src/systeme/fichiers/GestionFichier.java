package systeme.fichiers;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class GestionFichier {
	HashMap<String, Fichier> listFichier = new HashMap<String, Fichier>();

	public Fichier RechercheFichier(String nomFichier) {
		Fichier recherche = null;
		recherche = new Fichier("TOTO.txt", "TOTO", "10/10/2020",
				"./Telechargment/TOTO.txt", 9100);
		HeaderBloc b1 = new HeaderBloc(true);
		HeaderBloc b2 = new HeaderBloc(true);
		HeaderBloc b3 = new HeaderBloc(true);
		recherche.AjouterHeaderBloc(0, b1);
		recherche.AjouterHeaderBloc(1, b2);
		recherche.AjouterHeaderBloc(2, b3);
		this.AjouterFichier(recherche);
		return recherche;
	}

	public Integer EtatFichier(String nomFichier) {
		Integer etat = 0;
		etat = 1;// Provisoire
		return etat;
	}

	public byte[] Lire(Fichier fichier, Integer numBloc) {
		byte[] bloc = new byte[4000];
		FileInputStream fileis = null;
		int taille = bloc.length;
		long offset = taille * numBloc;
		File fle = new File(fichier.getEmplacement());
		try {
			fileis = new FileInputStream(fle);
			fileis.getChannel().position(offset);
			fileis.read(bloc, 0, taille);
			fileis.close();
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
		return bloc;

	}

	public Integer Ecrire(Fichier fichier, Integer numbloc, byte[] donnees) {
		String file = fichier.getEmplacement();
		File fle = new File(file);
		if (!fle.exists()) {
			try {
				fle.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int taille = 4000;
		long offset = taille * numbloc;
		try {
			FileOutputStream o = new FileOutputStream(fle, true);
			o.getChannel().position(offset);
			o.write(donnees, 0, taille);
			o.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 0;
	}

	public HashMap<String, Fichier> getListFichier() {
		return listFichier;
	}

	public void setListFichier(HashMap<String, Fichier> listFichier) {
		this.listFichier = listFichier;
	}

	public Boolean getDisponible(String nom, Integer index) {
		return this.listFichier.get(nom).getDisponible(index);
	}

	public void setDisponible(String nom, Integer index, Boolean disponible) {
		this.listFichier.get(nom).setDisponible(index, disponible);
	}

	public void AjouterFichier(Fichier f) {
		this.listFichier.put(f.getNomFichier(), f);
	}
}
