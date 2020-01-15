package systeme.fichiers;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFileChooser;

public class GestionFichier {
	HashMap<String, Fichier> listFichier = new HashMap<String, Fichier>();
	String chemin;
	public GestionFichier (String c) {
		this.chemin = c;
	}
	/*
	 * new fichier avec methode 
	 * Parcours fichier for(...taille/4)
	 * bi
	 * Ouverture fichier à sa création 
	 * Si le fichier le bloc lu à 0 bytes --> Indisponible --> False
	 * 
	 */
	
	public Fichier RechercheFichier(String nomFichier) {
		Fichier recherche = null;
		recherche = new Fichier("TOTO.txt", "TOTO", "10/10/2020",
				"./Telechargment/TOTO.txt", 9100);
		HeaderBloc b1 = new HeaderBloc(1);
		HeaderBloc b2 = new HeaderBloc(1);
		HeaderBloc b3 = new HeaderBloc(1);
		recherche.AjouterHeaderBloc(0, b1);
		recherche.AjouterHeaderBloc(1, b2);
		recherche.AjouterHeaderBloc(2, b3);
		this.AjouterFichier(recherche);
		return recherche;
	}

	/*
	 * *Parcours la liste des blocs
	 * Si il y en a un qui 0 ou -1 sortie
	 */
	
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

	public int getDisponible(String nom, Integer index) {
		return this.listFichier.get(nom).getDisponible(index);
	}

	public void setDisponible(String nom, Integer index, int disponible) {
		this.listFichier.get(nom).setDisponible(index, disponible);
	}

	public void AjouterFichier(Fichier f) {
		this.listFichier.put(f.getNomFichier(), f);
	}

	public String getChemin() {
		return chemin;
	}

	public void setChemin(String chemin) {
		this.chemin = chemin;
	}
	
	private void getFilesRec(ArrayList<String> allFiles, String nomFichier) {
		File f = new File(nomFichier);
		File[] listFiles = f.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			if (listFiles[i].isDirectory()) {
				getFilesRec(allFiles, listFiles[i].toString());
			}
			else allFiles.add(listFiles[i].toString());
		}
	}  

	private Integer getTailleFichier(String nomFichier) {

		double bytes;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
		JFileChooser chooser = new JFileChooser();

		File f = new File (nomFichier);
		f= new File (f.getAbsolutePath());
		bytes = f.length();
		System.out.println("Taille : " + bytes + " octets");
		Date d = new Date(f.lastModified());
		System.out.println("Dernière modification le : " + sdf.format(d));
		System.out.println("Type : " + chooser.getTypeDescription(f));

		return 1;
	}
	
	public Integer initGestionFichier(String nomFichier) {
		ArrayList<String> allFiles = new ArrayList<String>();
		getFilesRec(allFiles, nomFichier);
		for (int i = 0; i < allFiles.size(); i++) {
			System.out.println(allFiles.get(i));
			this.getTailleFichier(allFiles.get(i));
			System.out.println("");

		}
		return null;
	}
	
}
