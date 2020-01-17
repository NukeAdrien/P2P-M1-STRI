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
	HashMap<Integer, HeaderBloc> listHeaderBlocs = new HashMap<Integer, HeaderBloc>(); 

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

	@SuppressWarnings("null")
	public Fichier RechercheFichier(String nomFichier) {
		Fichier recherche = null;
		recherche = new Fichier(recherche.getNomFichier(), recherche.getAuteur(), recherche.getDate(),
				recherche.getEmplacement(), recherche.getTailleOctets());
		ArrayList<HeaderBloc> hb = new ArrayList<HeaderBloc>();
		
		for (int i=0; i<listFichier.size();i++) {
			if (listFichier.get(i).getNomFichier().equals(recherche.getNomFichier())) {
				System.out.println("Le fichier existe déjà");
				return listFichier.get(i);
			} else {
				System.out.println("Le fichier n'existe pas");
			}

		}
		if (recherche.getTailleOctets()==0) {
			for (int i=0; i<(this.getTailleFichier(recherche.getNomFichier())/4);i++) {
				hb.add(new HeaderBloc(1));
				recherche.AjouterHeaderBloc(i, hb.get(i));

			}
			this.AjouterFichier(recherche);
			return recherche;
		}
		return recherche;
	}

	/*
	 * *Parcours la liste des blocs
	 * Si il y en a un qui 0 ou -1 sortie
	 */

	public Integer EtatFichier(String nomFichier) {
		Integer etat = 0;
		for (int i=0; i<listFichier.size();i++) {
			for(int j=0; j<listHeaderBlocs.size();i++) {
				if(listHeaderBlocs.get(j).getDisponible()==0 || listHeaderBlocs.get(j).getDisponible()==1) {
					System.out.println("Erreur fichier non existant ou en cours de téléchargement");
					return -1;
				} else {
					etat =1;
					return etat;
				}
				
			}
		}
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
