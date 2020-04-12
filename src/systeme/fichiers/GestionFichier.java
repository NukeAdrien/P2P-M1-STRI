package systeme.fichiers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/*
 * Classe GestionFichier --> Classe permettant de gérer un fichier (Lecture, Ecriture, etc ....)
 */
public class GestionFichier implements Serializable {

	/* Déclaration de variables */
	HashMap<String, Fichier> listFichier;
	String chemin;
	int nbrLireEnCours= 0, nbrRechercheEnCours= 0, nbrAjoutEnCours= 0, nbrLectureDispoEnCours = 0;
	int nbLectureDowload= 0,nbLectureUpload = 0;
	int nbDowload;
	int nbUpload;
	/*
	 * Constructeur ClientDonnees --> Ce constructeur prend en paramétre un
	 * SocketClient et un gestion de fichier Ce constructeur permet de créer un
	 * nouveau ClientDonnees.
	 */
	public GestionFichier(String c) {
		this.chemin = c;
		listFichier = new HashMap<String, Fichier>();
		this.nbDowload = 1;
		this.nbUpload = 1;
	}

	/*
	 * Méthode RechercheFichier : Méthode permettant avec un nom de fichier de
	 * retouner l'objet fichier correspondant
	 * 
	 * @param : le type de PDU en String
	 * 
	 * @return : l'objet Fichier du nom de fichier
	 */

	public Fichier RechercheFichier(String nomFichier) {
		this.DebutRecherche();
		/* Déclaration de fichiers */
		Fichier recherche = null;
		/* On parcourt une hash map de fichiers */
		for (Entry<String, Fichier> listFichier : this.getListFichier().entrySet()) {
			/* On compare la clé de la hash map avec le nom fichier */
			if (listFichier.getKey().compareTo(nomFichier) == 0) {
				/* On récupére le fichier */
				recherche = this.listFichier.get(nomFichier);
				break;
			}
		}
		this.FinRecherche();
		/* On retourne le fichier */
		return recherche;
	}

	/*
	 * Méthode EtatFichier : Méthode permettant de retourner l'état du fichier
	 * actuel
	 * 
	 * @param : le nom du fichier
	 * 
	 * @return : -1 si non disponible, 0 si le fichier est en cours de
	 * téléchargement, 1 si le fichier est disponible
	 */

	public Integer EtatFichier(String nomFichier) {
		this.DebutRecherche();
		/* Déclaration de variables */
		Fichier fichier = null;
		/* On récupére le nom de fichier (si il existe) */
		fichier = this.listFichier.get(nomFichier);
		/* Si le fichier n'existe pas */
		if (fichier == null) {
			/* Affichage d'un message d'erreur */
			System.out.println("Le fichier est inexistant ! ");
			return -1;
		}
		// On viens de parcourir la liste de Header Bloc contenue dans une HashMap
		for (Map.Entry<Integer, HeaderBloc> headerbloc : fichier.listHeaderBlocs.entrySet()) {
			/* Si la valeur du header bloc est a -1 */
			if (headerbloc.getValue().getDisponible() == -1) {
				/* Affichage d'un message d'erreur */
				System.out.println("Fichier inexistant");
				return -1;
			} else {
				/* Si la valeur du header bloc est a 0 */
				if (headerbloc.getValue().getDisponible() == 0) {
					/* Affichage d'un message */
					System.out.println("Fichier en cours de téléchargement");
					return 0;
				}
			}
			/* Si on quitte la boucle for alors on retourne 1; */
		}
		this.FinRecherche();
		return 1;
	}
	
	public Integer EtatFichier(Fichier fichier) {
		this.DebutRecherche();
		// On viens de parcourir la liste de Header Bloc contenue dans une HashMap
		for (Map.Entry<Integer, HeaderBloc> headerbloc : fichier.listHeaderBlocs.entrySet()) {
			/* Si la valeur du header bloc est a -1 */
			if (headerbloc.getValue().getDisponible() == -1) {
				/* Affichage d'un message d'erreur */
				System.out.println("Fichier inexistant");
				return -1;
			} else {
				/* Si la valeur du header bloc est a 0 */
				if (headerbloc.getValue().getDisponible() == 0) {
					/* Affichage d'un message */
					System.out.println("Fichier en cours de téléchargement");
					return 0;
				}
			}
			/* Si on quitte la boucle for alors on retourne 1; */
		}
		this.FinRecherche();
		return 1;
	}

	/*
	 * Méthode Lire : Méthode permettant de lire un fichier
	 * 
	 * @param : le nom du fichier, le numéro de bloc é lire
	 * 
	 * @return : le tableau d'octets correspondant aux données
	 */

	public byte[] Lire(Fichier fichier, Integer numBloc) {
		this.DebutLire();
		/* Déclaration de variables */
		byte[] bloc = new byte[4000];
		FileInputStream fileis = null;
		int taille = bloc.length;
		long offset = taille * numBloc;

		/* On récupére l'emplacement du fichier */
		File fle = new File(fichier.getEmplacement());
		try {
			/* On ouvre les flux */
			fileis = new FileInputStream(fle);
			/* On positionne le curseur */
			fileis.getChannel().position(offset);
			/* Lecture des octets */
			fileis.read(bloc, 0, taille);
			/* Fermeture des flux */
			fileis.close();
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
		this.FinLire();
		/* On retourne le bloc lu */
		return bloc;

	}

	/*
	 * Méthode Ecrire : Méthode permettant d'écrire dans un fichier
	 * 
	 * @param : le nom du fichier, le numéro de bloc dans lequel on écrira dans les
	 * données, et les données qui sont écrires
	 * 
	 * @return : 0 si éa s'est bien passée, 1 sinon
	 */
	public synchronized Integer Ecrire(Fichier fichier, Integer numbloc, byte[] donnees) {
		while (nbrLireEnCours != 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		/* On récupére l'emplacement du fichier */
		String file = fichier.getEmplacement();
		/* Déclaration d'un nouveau fichier */
		RandomAccessFile ecriture = null;
		File fle = new File(file);
		try {
			ecriture = new RandomAccessFile(file, "rw");
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		/* Si le fichier n'existe pas */
		if (!fle.exists()) {
			try {
				 ecriture.setLength(fichier.getTailleOctets());
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		/* Déclaration de la taille */
		int taille = 4000;
		/* Positionnement de l'offset en fonction des numéros de blocs */
		long offset = taille * numbloc;
		try {
			/* Positionnement du curseur */
			ecriture.seek(offset);
			/* Ecriture des données en fonction de la position du curseur */
			ecriture.write(donnees, 0, taille);
			/* Fermeture des flux */
			ecriture.close();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		notifyAll();
		return 0;
	}

	/*
	 * Méthode getListFichier : Méthode permettant de récupérer la liste des
	 * fichiers dans la HashMap d'un bloc
	 * 
	 * @return : la liste des fichiers
	 */
	public HashMap<String, Fichier> getListFichier() {
		return listFichier;
	}

	/*
	 * Méthode setListFichier : Méthode permettant de changer la liste des fichiers
	 * dans la HashMap d'un bloc
	 * 
	 * @return : la nouvelle liste des fichiers
	 */
	public void setListFichier(HashMap<String, Fichier> listFichier) {
		this.listFichier = listFichier;

	}

	/*
	 * Méthode getDisponible : Méthode permettant de récupérer la disponibilité d'un
	 * bloc
	 * 
	 * @param : le nom du fichier, l'index du header bloc
	 * 
	 * @return : la disponibilité du fichier : -1 si le fichier n'est dispo, 0 si le
	 * fichier est en cours de téléchargement et 1 si le fichier est disponible
	 */
	public int getDisponible(String nom, Integer index) {
		int disp;
		this.DebutLectureDispo();
		disp = this.listFichier.get(nom).getDisponible(index);
		this.FinLectureDispo();
		return disp;

	}

	/*
	 * Méthode setDisponible : Méthode permettant de changer la disponibilité d'un
	 * bloc
	 * 
	 * @param : le nom du fichier, l'index du header bloc, la nouvelle disponibilité
	 * 
	 * @return : la nouvelle disponibilité du fichier : -1 si le fichier n'est
	 * dispo, 0 si le fichier est en cours de téléchargement et 1 si le fichier est
	 * disponible
	 */
	public synchronized void setDisponible(String nom, Integer index, int disponible) {
		while (nbrLectureDispoEnCours != 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		this.listFichier.get(nom).setDisponible(index, disponible);
		notifyAll();
	}

	public synchronized int setReserver(String nom, Integer index) {
		while (nbrLectureDispoEnCours > 0 && this.getDisponible(nom, index) == -1) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		if (this.getDisponible(nom, index) == 0 || this.getDisponible(nom, index) == 1) {
			notifyAll();
			return 1;
		}
		this.listFichier.get(nom).setDisponible(index, 0);
		notifyAll();
		return 0;
	}

	/*
	 * Méthode AjouterFichier : Méthode permettant d'ajouter un fichier et la HashMap
	 * 
	 * @param : le fichier et ajouter
	 */
	public synchronized void AjouterFichier(Fichier f) {
		Fichier f2 = (Fichier) f.clone();
		System.out.println(nbrRechercheEnCours);
		System.out.println("Entrer dans la creation du fichier");
		while (nbrRechercheEnCours != 0) {
			System.out.println(nbrRechercheEnCours);
			try {
				System.out.println("J'attend");
				this.wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			System.out.println("Je suis coince");
			if (this.RechercheFichier(f.nomFichier) != null) {
				notifyAll();
				System.out.println("Quitte sans cree le fichier");
				return;
			}
		}
		this.listFichier.put(f2.getNomFichier(), f2);
		/*
		 * On change l'emplacement du fichier pour le mettre avec les autres fichiers
		 * téléchargés
		 */
		this.listFichier.get(f2.getNomFichier()).setEmplacement(this.chemin + f2.getNomFichier());
		/* On parcourt les headers blocs contenues dans le fichier */
		for (Map.Entry<Integer, HeaderBloc> headerbloc : f2.getListHeaderBlocs().entrySet()) {
			this.listFichier.get(f2.getNomFichier()).setDisponible(headerbloc.getKey(), -1);
		}
		System.out.println("Quitte en creeant le fichier");
		notifyAll();
		return;
	}

	/*
	 * Méthode getChemin : Méthode permettant de retourner le chemin pour un fichier
	 * donné
	 * 
	 * @return : le chemin du fichier
	 */

	public String getChemin() {
		return chemin;
	}

	/*
	 * Méthode getChemin : Méthode permettant de changer le chemin pour un fichier
	 * donné
	 * 
	 * @return : le nouveau chemin du fichier
	 */
	public void setChemin(String chemin) {
		this.chemin = chemin;
	}

	/*
	 * private HashMap<String, Fichier> getFilesRec() { File f = new
	 * File(nomFichier); File[] listFiles = f.listFiles(); for (int i = 0; i <
	 * listFiles.length; i++) { if (listFiles[i].isDirectory()) {
	 * //getFilesRec(allFiles, listFiles[i].toString()); } else
	 * allFiles.add(listFiles[i].toString()); } for (int i=0; i<allFiles.size();
	 * i++) { this.getTailleFichier(nomFichier); Fichier fichier = new
	 * Fichier(allFiles.get(i), this.dateModifFichier(nomFichier),
	 * this.chemin+allFiles.get(i), this.getTailleFichier(nomFichier));
	 * this.listFichier.add(nomFichier, fichier); } }
	 */

	/*
	 * Méthode getTailleFichier : Méthode permettant de récpérer la taille du
	 * fichier.
	 * 
	 * @param : Le nom du fichier
	 * 
	 * @return : la taille du fichier associée (en octets)
	 */

	private Long getTailleFichier(String nomFichier) {
		File f = new File(nomFichier);
		return f.length();
	}

	/*
	 * Méthode dateModifFichier : Méthode permettant de récupérer la date de la
	 * derniére modification du fichier.
	 * 
	 * @param : Le nom du fichier
	 * 
	 * @return : la date du fichier associée (en octets)
	 */

	private String dateModifFichier(String nomFichier) {
		/* On parse dans le format d'une date */
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
		/* Création nouveau fichier */
		File f = new File(nomFichier);
		/* Fonction permettant de récupérer la derniére date de modification */
		Date d = new Date(f.lastModified());
		/* Retour dans le format d'une date */
		return sdf.format(d);

	}
	/*
	 * Méthode initGestionFichier : Méthode permettant d'initialiser une gestion de
	 * fichier.
	 * 
	 * @return : 0 si éa s'est bien passée, 1 sinon
	 */

	public Integer initGestionFichier() {
		/* Déclaration de variables */
		ArrayList<String> allFiles = new ArrayList<String>();
		File f = new File(this.chemin);
		HeaderBloc blc = null;

		/* On liste les fichiers dans un tableau de fichier */
		File[] listFiles = f.listFiles();
		/* Si il n'y a pas de fichiers */
		if (!(listFiles != null)) {
			return -1;
		}
		/* On parcourt la liste de fichiers */
		for (int i = 0; i < listFiles.length; i++) {
			/* Si le fichier est un répertoire */
			if (listFiles[i].isDirectory()) {
				// getFilesRec(allFiles, listFiles[i].toString());
			} else
				/* Ajout du fichier dans l'Array List */
				allFiles.add(listFiles[i].getName());
		}

		/* On parcourt la liste de fichiers contenues dans l'ArrayList */

		for (int i = 0; i < allFiles.size(); i++) {
			/* On récupére la taille du fichier */
			double taillFich = this.getTailleFichier(this.chemin + allFiles.get(i));
			/* On récupére la derniére date de modification du fichier */
			String date = this.dateModifFichier(allFiles.get(i));
			/* On déclaration un nouveau fichier */
			Fichier fichier = new Fichier(allFiles.get(i), date, this.chemin + allFiles.get(i), (long) taillFich);
			/* On récupére le nombre de blocs en fonction de la taille du fichier */
			int nbBloc = (int) (taillFich / 4000);
			double tmp = taillFich / 4000;
			if (tmp > nbBloc) {
				nbBloc++;
			}
			/* On parcourt les numéros du bloc */
			for (int j = 0; j < nbBloc; j++) {
				/* On initialise un tableau de bytes de données */
				byte[] donnees = (byte[]) null;
				/* On lit les données contenues dans le fichier */
				donnees = this.Lire(fichier, j);
				/* Si il n'y a pas de données */
				if (!(donnees != null)) {
					/* Le bloc n'est pas disponible */
					blc = new HeaderBloc(-1);
				} else {
					/* Le bloc est disponible */
					blc = new HeaderBloc(1);
				}
				/* On ajoute l'header bloc au fichier */
				fichier.AjouterHeaderBloc(j, blc);

			}
			/* On ajoute le fichier é la Hash Map */
			this.listFichier.put(allFiles.get(i), fichier);
		}

		return 0;
	}

	public synchronized void DebutLire() {
		nbrLireEnCours++;
	}

	public synchronized void FinLire() {
		nbrLireEnCours--;
		notifyAll();

	}

	public synchronized void DebutRecherche() {
		nbrRechercheEnCours++;
	}

	public synchronized void FinRecherche() {
		nbrRechercheEnCours--;
		notifyAll();

	}

	public synchronized void supprimerFichier(Fichier f2) {
		try {
			File f = new File(this.chemin+f2.getNomFichier());
			while (nbrRechercheEnCours != 0) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				if (this.RechercheFichier(f2.nomFichier) != null) {
					notifyAll();
					return;
				}
			}
			this.listFichier.remove(f2.getNomFichier());
			f.delete();
			notifyAll();
		} catch (Exception e) {
			System.out.println("Fichier introuvable");
		}
	}

	public void afficherDetailFichier(String c) {
		Fichier f = this.RechercheFichier(c);
		try {
			System.out.println("Nom du fichier : " + f.getNomFichier());
			System.out.println("Date du fichier : " + f.getDate());
			System.out.println("Emplacement du fichier : " + f.getEmplacement());
			System.out.println("Taille du fichier : " + f.getTailleOctets() + " octets");
			System.out.println("Disponibilité du fichier : " + this.EtatFichier(f.getNomFichier()));
			System.out.println("List Header Blocs : " + f.getListHeaderBlocs().toString());
		}	catch (Exception e) {
			System.out.println("Fichier introuvable");
		}

	}
	public synchronized void renommerFichier(Fichier f2, String nom) {
		try {
			File f = new File(this.chemin+f2.getNomFichier());
			File f1 = new File(this.chemin+nom);

			while (nbrRechercheEnCours != 0) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				if (this.RechercheFichier(f2.nomFichier) != null) {
					notifyAll();
					return;
				}
			}
			this.listFichier.replace(nom,f2);
			f2.setNomFichier(f1.getName());
			f.renameTo(f1);
			notifyAll();
		} catch (Exception e) {
			System.out.println("Fichier introuvable");
		}
	}

	public void afficherFichierDisponible() {
		for (Entry<String, Fichier> listFichier : this.getListFichier().entrySet()) {
			if (this.EtatFichier(listFichier.getKey()) == 0) {
				System.out.println("Ce fichier est disponible : " + listFichier.getKey());
			}
		}
	}

	public synchronized void DebutLectureDispo() {
		nbrLectureDispoEnCours++;
	}

	public synchronized void FinLectureDispo() {
		nbrLectureDispoEnCours--;
		notifyAll();

	}
	
	public synchronized void DebutLectureUpload() {
		nbLectureUpload++;
	}

	public synchronized void FinLectureUpload() {
		nbLectureUpload--;
		notifyAll();

	}
	
	public synchronized void DebutLectureDowload() {
		nbLectureDowload++;
	}

	public synchronized void FinLectureDowload() {
		nbLectureDowload--;
		notifyAll();
	}

	/* Etape 5*/
	public int getNbDowload() {
		int nb;
		DebutLectureDowload();
		nb = this.nbDowload;
		FinLectureDowload();
		return nb; 
	}

	public int getNbUpload() {
		int nb;
		DebutLectureUpload();
		nb = this.nbUpload;
		FinLectureUpload();
		return nb; 
	}
	
	public synchronized void nbDowloadInc() {
		while (nbLectureDowload > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		this.nbDowload++;
		notifyAll();
	}
	
	public synchronized void nbUploadInc() {
		while (nbLectureUpload > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		this.nbUpload++;
		notifyAll();
	}

	public synchronized void setNbDowload(int nbDowload) {
		while (nbLectureDowload > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		this.nbDowload = nbDowload;
		notifyAll();
	}

	public synchronized void setNbUpload(int nbUpload) {
		while (nbLectureUpload > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		this.nbUpload = nbUpload;
		notifyAll();
		
	}
	
	public void renitialisation () {
		this.nbrAjoutEnCours=0;
		this.nbrLireEnCours=0;
		this.nbrRechercheEnCours=0;
		this.nbLectureDowload=0;
		this.nbLectureUpload=0;
	}
	

}
