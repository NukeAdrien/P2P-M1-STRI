package systeme.fichiers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;

public class GestionFichier {
	HashMap<String, Fichier> listFichier = new HashMap<String, Fichier>();
	String chemin;

	public GestionFichier(String c) {
		this.chemin = c;
	}

	//Alexandre 
	//Permet avec un nom de fichier de retouner l'objet fichier correspondant
	//Le fichier est dans la hash map
	public Fichier RechercheFichier(String nomFichier) {
		Fichier recherche = null;
		// Permet de parcourir une hash map de fichier
		for (Entry<String, Fichier> listFichier : this.getListFichier().entrySet()) {
			// Il faut comparer la cle de la has map avec le nom fichier si c'est bon alors
			if(listFichier.getKey().contentEquals(nomFichier)) {
				recherche = this.listFichier.get(nomFichier);
				break;
			} else {
				System.out.println("Le fichier est inexistant dans la HashMap");
			}
			// on affecte a recherche le fichier correspondant
			/*listFichier.getKey();// Renvoie le nom du fichier (index)
			listFichier.getValue();// renvoie l'objet fichier rechercher*/
		}
		return recherche;
	}


	//Alexandre Fonction permetant de retourner l'etat d'un fichier
	//Sachant que si on a 0 ou -1  sur l'etat d'un hearder bloc on retourn directement l'info
	public Integer EtatFichier(String nomFichier) {
		Fichier fichier = null;
		// On recupere d'abord le fichier demande avec ca
		fichier = this.listFichier.get(nomFichier);
		if (fichier == null) {
			System.out.println("Le fichier est inexistant ! ");
			return -1;
		}
		// On test si le fichier n'est pas egal a null
		// on renvoie -1 si c'est le cas
		// puis on viens parcourrir la list de hash map de header bloc
		for (Map.Entry<Integer, HeaderBloc> headerbloc : fichier.listHeaderBlocs.entrySet()) {
			/*headerbloc.getKey();// renvoie le numero d'index du bloc
			headerbloc.getValue();// renvoie le headerbloc
			headerbloc.getValue().getDisponible();// renvoie l'etat d'un header bloc en cour
			// test si la valeur de header bloc est a -1 on retourne le -1*/
			if(headerbloc.getValue().getDisponible() == -1) {
				System.out.println("Fichier inexistant");
				return -1;
			} else {
				if (headerbloc.getValue().getDisponible() == 0) {
					System.out.println("Fichier en cours de téléchargement");
					return 0;
				} 
			}
			// test si la valeur est a 0 on retourne 0;
			// si on quitte la boucle for alors on retourne 1;
		}
		return 1;
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
			} else
				allFiles.add(listFiles[i].toString());
		}
	}

	private Integer getTailleFichier(String nomFichier) {

		double bytes;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
		JFileChooser chooser = new JFileChooser();

		File f = new File(nomFichier);
		f = new File(f.getAbsolutePath());
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
