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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import java.util.Scanner;

import uml.UmlRechercheAuteur;
import uml.UmlRechercheDate;
import uml.UmlRechercheNom;

/*
 * Classe Client --> Classe permettant de cr�er un ClientControle
 */
public class GestionFichier implements Serializable {

	/* D�claration de variables */
	HashMap<String, Fichier> listFichier;
	String chemin;
	int nbrLireEnCours= 0, nbrRechercheEnCours= 0, nbrAjoutEnCours= 0, nbrLectureDispoEnCours = 0;
	int nbLectureDowload= 0,nbLectureUpload = 0;
	int nbDowload;
	int nbUpload;
	/*
	 * Constructeur ClientDonnees --> Ce constructeur prend en param�tre un
	 * SocketClient et un gestion de fichier Ce constructeur permet de cr�er un
	 * nouveau ClientDonnees.
	 */
	public GestionFichier(String c) {
		this.chemin = c;
		listFichier = new HashMap<String, Fichier>();
		this.nbDowload = 1;
		this.nbUpload = 1;
	}

	/*
	 * M�thode RechercheFichier : M�thode permettant avec un nom de fichier de
	 * retouner l'objet fichier correspondant
	 * 
	 * @param : le type de PDU en String
	 * 
	 * @return : l'objet Fichier du nom de fichier
	 */


	public Fichier RechercheFichier(String nomFichier) {
		this.DebutRecherche();
		/* D�claration de fichiers */
		Fichier recherche = null;
		/* On parcourt une hash map de fichiers */
		for (Entry<String, Fichier> listFichier : this.getListFichier().entrySet()) {
			/* On compare la cl� de la hash map avec le nom fichier */
			if (listFichier.getKey().compareTo(nomFichier) == 0) {
				/* On r�cup�re le fichier */
				recherche = this.listFichier.get(nomFichier);
				break;
			}
		}
		this.FinRecherche();
		/* On retourne le fichier */
		return recherche;
	}

	/*
	 * M�thode EtatFichier : M�thode permettant de retourner l'�tat du fichier
	 * actuel
	 * 
	 * @param : le nom du fichier
	 * 
	 * @return : -1 si non disponible, 0 si le fichier est en cours de
	 * t�l�chargement, 1 si le fichier est disponible
	 */

	public Integer EtatFichier(String nomFichier) {
		this.DebutRecherche();
		/* D�claration de variables */
		Fichier fichier = null;
		/* On r�cup�re le nom de fichier (si il existe) */
		fichier = this.listFichier.get(nomFichier);
		/* Si le fichier n'existe pas */
		if (fichier == null) {
			/* Affichage d'un message d'erreur */
			System.out.println("Le fichier est inexistant ! ");
			return -1;
		}
		// On vient de parcourir la liste de Header Bloc contenue dans une HashMap
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
					System.out.println("Fichier en cours de t�l�chargement");
					return 0;
				}
			}
			/* Si on quitte la boucle for alors on retourne 1; */
		}
		this.FinRecherche();
		return 1;
	}

	/*
	 * M�thode EtatFichier : M�thode permettant de retourner l'�tat du fichier
	 * actuel
	 * 
	 * @param : le fichier
	 * 
	 * @return : -1 si non disponible, 0 si le fichier est en cours de
	 * t�l�chargement, 1 si le fichier est disponible
	 */


	public Integer EtatFichier(Fichier fichier) {
		this.DebutRecherche();
		// On vient de parcourir la liste de Header Bloc contenue dans une HashMap
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
					System.out.println("Fichier en cours de t�l�chargement");
					return 0;
				}
			}
			/* Si on quitte la boucle for alors on retourne 1; */
		}
		this.FinRecherche();
		return 1;
	}

	/*
	 * M�thode Lire : M�thode permettant de lire un fichier
	 * 
	 * @param : le nom du fichier, le num�ro de bloc � lire
	 * 
	 * @return : le tableau d'octets correspondant aux donn�es
	 */

	public byte[] Lire(Fichier fichier, Integer numBloc) {
		this.DebutLire();
		/* D�claration de variables */
		byte[] bloc = new byte[4000];
		FileInputStream fileis = null;
		int taille = bloc.length;
		long offset = taille * numBloc;

		/* On r�cup�re l'emplacement du fichier */		
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
	 * M�thode Ecrire : M�thode permettant d'�crire dans un fichier
	 * 
	 * @param : le nom du fichier, le num�ro de bloc dans lequel on �crira dans les
	 * donn�es, et les donn�es qui sont �crites
	 * 
	 * @return : 0 si �a s'est bien pass�e, 1 sinon
	 */
	public synchronized Integer Ecrire(Fichier fichier, Integer numbloc, byte[] donnees) {
		while (nbrLireEnCours != 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		/* On r�cup�re l'emplacement du fichier */
		String file = fichier.getEmplacement();
		/* D�claration d'un nouveau fichier */
		RandomAccessFile ecriture = null;
		File fle = new File(file);
		/* Si le fichier n'existe pas */
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
		/* D�claration de la taille */
		int taille = 4000;
		/* Positionnement de l'offset en fonction des num�ros de blocs */
		long offset = taille * numbloc;
		try {
			/* Positionnement du curseur */
			ecriture.seek(offset);
			/* Ecriture des donn�es en fonction de la position du curseur */
			ecriture.write(donnees, 0, taille);
			/* Fermeture des flux */
			ecriture.close();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		/* On notifie les modifications */
		notifyAll();
		return 0;
	}

	/*
	 * M�thode getListFichier : M�thode permettant de r�cup�rer la liste des
	 * fichiers dans la HashMap d'un bloc
	 * 
	 * @return : la liste des fichiers
	 */
	public HashMap<String, Fichier> getListFichier() {
		return listFichier;
	}

	/*
	 * M�thode setListFichier : M�thode permettant de changer la liste des fichiers
	 * dans la HashMap d'un bloc
	 * 
	 * @return : la nouvelle liste des fichiers
	 */
	public void setListFichier(HashMap<String, Fichier> listFichier) {
		this.listFichier = listFichier;

	}

	/*
	 * M�thode getDisponible : M�thode permettant de r�cup�rer la disponibilit� d'un
	 * bloc
	 * 
	 * @param : le nom du fichier, l'index du header bloc
	 * 
	 * @return : la disponibilit� du fichier : -1 si le fichier n'est dispo, 0 si le
	 * fichier est en cours de t�l�chargement et 1 si le fichier est disponible
	 */
	public int getDisponible(String nom, Integer index) {
		int disp;
		this.DebutLectureDispo();
		disp = this.listFichier.get(nom).getDisponible(index);
		this.FinLectureDispo();
		return disp;

	}

	/*
	 * M�thode setDisponible : M�thode permettant de changer la disponibilit� d'un
	 * bloc
	 * 
	 * @param : le nom du fichier, l'index du header bloc, la nouvelle disponibilit�
	 * 
	 * @return : la nouvelle disponibilit� du fichier : -1 si le fichier n'est
	 * dispo, 0 si le fichier est en cours de t�l�chargement et 1 si le fichier est
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
		/* On modifie la disponibilit� du fichier */
		this.listFichier.get(nom).setDisponible(index, disponible);
		notifyAll();
	}
	/*
	 * M�thode setReserver : Permet de modifier la r�servation du bloc de fichier.
	 * @param : Le nom de fichier ainsi que son num�ro de bloc
	 * @return : 0 si �a s'est bien pass�e, 1 sinon  
	 */
	public synchronized int setReserver(String nom, Integer index) {
		/* Si le bloc n'est pas disponible */
		while (nbrLectureDispoEnCours > 0 && this.getDisponible(nom, index) == -1) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}

		/*Si le bloc est indisponible ou en cours de t�l�chargement */
		if (this.getDisponible(nom, index) == 0 || this.getDisponible(nom, index) == 1) {
			notifyAll();
			return 1;
		}	
		/* On modifie la disponibilit� du bloc */
		this.listFichier.get(nom).setDisponible(index, 0);
		/* On notifie la modification */
		notifyAll();
		return 0;
	}

	/*
	 * M�thode AjouterFichier : M�thode permettant d'ajouter un fichier et la HashMap
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
				System.out.println("J'attends");
				this.wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			System.out.println("Je suis coinc�");
			if (this.RechercheFichier(f.nomFichier) != null) {
				notifyAll();
				System.out.println("Quitte sans cree le fichier");
				return;
			}
		}
		this.listFichier.put(f2.getNomFichier(), f2);
		/*
		 * On change l'emplacement du fichier pour le mettre avec les autres fichiers
		 * t�l�charg�s
		 */
		this.listFichier.get(f2.getNomFichier()).setEmplacement(this.chemin + f2.getNomFichier());
		/* On parcourt les headers blocs contenues dans le fichier */
		for (Map.Entry<Integer, HeaderBloc> headerbloc : f2.getListHeaderBlocs().entrySet()) {
			this.listFichier.get(f2.getNomFichier()).setDisponible(headerbloc.getKey(), -1);
		}
		System.out.println("Quitte en cr�ant le fichier");
		notifyAll();
		return;
	}

	/*
	 * M�thode getChemin : M�thode permettant de retourner le chemin pour un fichier
	 * donn�
	 * 
	 * @return : le chemin du fichier
	 */

	public String getChemin() {
		return chemin;
	}

	/*
	 * M�thode setChemin : M�thode permettant de changer le chemin pour un fichier
	 * donn�
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
	 * M�thode getTailleFichier : M�thode permettant de r�cp�rer la taille du
	 * fichier.
	 * 
	 * @param : Le nom du fichier
	 * 
	 * @return : la taille du fichier associ�e (en octets)
	 */

	private Long getTailleFichier(String nomFichier) {
		File f = new File(nomFichier);
		return f.length();
	}

	/*
	 * M�thode dateModifFichier : M�thode permettant de r�cup�rer la date de la
	 * derni�re modification du fichier.
	 * 
	 * @param : Le nom du fichier
	 * 
	 * @return : la date du fichier associ�e (en octets)
	 */

	private String dateModifFichier(String nomFichier) {
		/* On parse dans le format d'une date */
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
		/* Cr�ation nouveau fichier */
		File f = new File(nomFichier);
		/* Fonction permettant de r�cup�rer la derni�re date de modification */
		Date d = new Date(f.lastModified());
		/* Retour dans le format d'une date */
		return sdf.format(d);

	}
	/*
	 * M�thode initGestionFichier : M�thode permettant d'initialiser une gestion de
	 * fichier.
	 * 
	 * @return : 0 si �a s'est bien pass�e, 1 sinon
	 */

	public Integer initGestionFichier() {
		/* D�claration de variables */
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
			/* Si le fichier est un r�pertoire */
			if (listFiles[i].isDirectory()) {
				// getFilesRec(allFiles, listFiles[i].toString());
			} else
				/* Ajout du fichier dans l'Array List */
				allFiles.add(listFiles[i].getName());
		}

		/* On parcourt la liste de fichiers contenues dans l'ArrayList */

		for (int i = 0; i < allFiles.size(); i++) {
			/* On r�cup�re la taille du fichier */
			double taillFich = this.getTailleFichier(this.chemin + allFiles.get(i));
			/* On r�cup�re la derni�re date de modification du fichier */
			String date = this.dateModifFichier(allFiles.get(i));
			/* On d�clare un nouveau fichier */
			Fichier fichier = new Fichier(allFiles.get(i), date, this.chemin + allFiles.get(i), (long) taillFich);
			/* On d�coupe en nombre de blocs en fonction de la taille du fichier */
			int nbBloc = (int) (taillFich / 4000);
			double tmp = taillFich / 4000;
			if (tmp > nbBloc) {
				nbBloc++;
			}
			/* On parcourt les blocs */
			for (int j = 0; j < nbBloc; j++) {
				/* On initialise un tableau de bytes de donn�es */
				byte[] donnees = (byte[]) null;
				/* On lit les donn�es contenues dans le fichier */
				donnees = this.Lire(fichier, j);
				/* Si il n'y a pas de donn�es */
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
			/* On ajoute le fichier dans la Hash Map */
			this.listFichier.put(allFiles.get(i), fichier);
		}

		return 0;
	}

	/*
	 * M�thode DebutLire() : Permet d'indiquer le d�but de lecture
	 */
	public synchronized void DebutLire() {
		nbrLireEnCours++;
	}

	/*
	 * M�thode FinLire() : Permet d'indiquer la fin de lecture
	 */
	public synchronized void FinLire() {
		nbrLireEnCours--;
		notifyAll();

	}

	/*
	 * M�thode DebutRecherche() : Permet d'indiquer le d�but de la recherche
	 */
	public synchronized void DebutRecherche() {
		nbrRechercheEnCours++;
	}

	/*
	 * M�thode FinRecherche() : Permet d'indiquer la fin de la recherche
	 */
	public synchronized void FinRecherche() {
		nbrRechercheEnCours--;
		notifyAll();

	}

	/*
	 * M�thode supprimerFichier() : Permet de supprimer un fichier
	 * @param: Le fichier � supprimer
	 * 
	 */
	public synchronized void supprimerFichier(Fichier f2) {
		try {
			/* D�claration des variables */
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
			/* On supprime le fichier au niveau de la HashMap */
			this.listFichier.remove(f2.getNomFichier());
			/* On supprime le fichier au niveau de l'arborescence */
			f.delete();
			/* On notifie la modification */
			notifyAll();
		} catch (Exception e) {
			System.out.println("Fichier introuvable");
		}
	}

	/*
	 * Methode afficherDetailFichier() : Permet d'afficher les d�tails d'un fichier
	 * @param : Le nom du fichier 
	 */
	public void afficherDetailFichier(String c) {
		/* On recherche la pr�sence du fichier */
		Fichier f = this.RechercheFichier(c);
		try {
			/* On affiche les d�tails du fichier */
			System.out.println("Nom du fichier : " + f.getNomFichier());
			System.out.println("Date du fichier : " + f.getDate());
			System.out.println("Emplacement du fichier : " + f.getEmplacement());
			System.out.println("Taille du fichier : " + f.getTailleOctets() + " octets");
			System.out.println("Disponibilit� du fichier : " + this.EtatFichier(f.getNomFichier()));
			System.out.println("List Header Blocs : " + f.getListHeaderBlocs().toString());
		}	catch (Exception e) {
			System.out.println("Fichier introuvable");
		}

	}

	/*
	 * M�thode renommerFichier() : Permet de renommer un fichier
	 * @param: Le fichier � renommer, le nouveau nom de fichier
	 * 
	 */
	public synchronized void renommerFichier(Fichier f2, String nom) {
		try {
			/* D�claration de variables */
			File f = new File(this.chemin+f2.getNomFichier());
			File f1 = new File(this.chemin+nom);

			/* On attend que le fichier soit disponible */
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
			/* On renomme le fichier dans la HashMap */
			this.listFichier.replace(nom,f2);
			/* On renomme le fichier dans l'arborescence */
			f2.setNomFichier(f1.getName());
			/* On renomme le fichier */
			f.renameTo(f1);
			/* On notifie la modification */
			notifyAll();
		} catch (Exception e) {
			System.out.println("Fichier introuvable");
		}
	}

	/*
	 * M�thode afficherFichierDisponible() : Afficher les fichiers disponibles
	 * 
	 */
	public void afficherFichierDisponible() {
		/* On parcourt chaque entr�e de la HashMap */
		for (Entry<String, Fichier> listFichier : this.getListFichier().entrySet()) {
			/* Si l'�tat du fichier indique que le fichier est disponible */
			if (this.EtatFichier(listFichier.getKey()) == 0) {
				/* On affiche le nom du fichier */
				System.out.println("Ce fichier est disponible : " + listFichier.getKey());
			}
		}
	}

	/*
	 * M�thode DebutLectureDispo(): Permettre d'incr�menter le nombre de lecture des fichiers disponibles
	 * 
	 */

	public synchronized void DebutLectureDispo() {
		nbrLectureDispoEnCours++;
	}

	
	/*
	 * M�thode FinLectureDispo(): Permettre de d�cr�menter le nombre de lecture des fichiers disponibles
	 * 
	 */
	public synchronized void FinLectureDispo() {
		nbrLectureDispoEnCours--;
		notifyAll();

	}

	/*
	 * Methode nbDowloadInc : Permet d'incr�menter le nombre de t�l�chargements
	 * @return : Le nombre de t�l�chargements
	 */

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
	
	/*
	 * M�thode DebutLectureDispo(): Permettre d'incr�menter le nombre de lecture pour le d�but de l'upload
	 * 
	 */
	
	public synchronized void DebutLectureUpload() {
		nbLectureUpload++;
	}

	/*
	 * M�thode FinLectureUpload(): Permettre de d�cr�menter le nombre de lecture pour la fin de l'upload
	 * 
	 */
	public synchronized void FinLectureUpload() {
		nbLectureUpload--;
		notifyAll();

	}
	
	/*
	 * M�thode DebutLectureDowload(): Permettre d'incr�menter le nombre de lecture pour le d�but du t�l�chargement
	 * 
	 */
	
	public synchronized void DebutLectureDowload() {
		nbLectureDowload++;
	}

	/*
	 * M�thode FinLectureDowload(): Permettre de d�cr�menter le nombre de lecture pour la fin du t�l�chargement
	 * 
	 */
	public synchronized void FinLectureDowload() {
		nbLectureDowload--;
		notifyAll();
	}
	

	/*
	 * Methode nbUploadInc : Permet d'incr�menter le nombre d'uploads
	 * @return : Le nombre d'uploads
	 */
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

	/*
	 * Methode getNbDowload : Permet d'obtenir le nombre de t�l�chargements
	 * @return : Le nombre de t�l�chargements
	 */
	public synchronized int getNbDowload() {
		int nb;
		DebutLectureDowload();
		nb = this.nbDowload;
		FinLectureDowload();
		return nb; 
	}

	/*
	 * Methode getNbUpload : Permet d'obtenir le nombre d'uploads
	 * @return : Le nombre d'uploads
	 */
	public synchronized int getNbUpload() {
		int nb;
		DebutLectureUpload();
		nb = this.nbUpload;
		FinLectureUpload();
		return nb;
	}

	/*
	 * Methode setNbDowload : Permet de changer le nombre de t�l�chargements
	 * @param : Le nouveau nombre de t�l�chargements
	 */
	public void setNbDowload(int nbDowload) {
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

	/*
	 * Methode setNbUpload : Permet de changer le nombre d'uploads
	 * @param : Le nouveau nombre d'uploads
	 */
	public void setNbUpload(int nbUpload) {
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
	
	/*
	 * M�thode reinitialisation : Permet de remettre les compteurs � 0 
	 */
	
	public void reinitialisation () {
		this.nbrAjoutEnCours=0;
		this.nbrLireEnCours=0;
		this.nbrRechercheEnCours=0;
		this.nbLectureDowload=0;
		this.nbLectureUpload=0;
	}



	/*
	 * Methode rechercheNomFichierUml() : Permet de rechercher un nom de fichier depuis la JFrame
	 * @return : La liste des fichiers qui correspond � la recherche
	 */
	public ArrayList<String> rechercheNomFichierUml() {
		/* D�claration des variables */
		ArrayList<String> al = new ArrayList<String>();		
		/* On parcourt chaque entr�e de la hashMap */
		for(Entry<String, Fichier> listFichier : this.getListFichier().entrySet()) {
			try {
				/* Si la cl� du fichier (Nom du fichier) correspond � la recherche (ou au moins les premi�res lettres) */
				if(listFichier.getKey().startsWith(UmlRechercheNom.getJTextField2().substring(0,3))) {
					al.add(listFichier.getKey());
				}
			} catch (StringIndexOutOfBoundsException id) {
				javax.swing.JOptionPane.showMessageDialog(null, "Veuillez saisir les trois premi�res lettres", "Error", JOptionPane.ERROR_MESSAGE); 
				break;
			}
		}
		/* On retourne l'arrayList */
		return al;
	}

	/*
	 * Methode rechercheDateFichierUml() : Permet de rechercher une date depuis la JFrame
	 * @return : La liste des fichiers qui correspond � la recherche
	 */

	public ArrayList<String> rechercheDateFichierUml() {
		/* D�claration de variables*/
		ArrayList<String> al = new ArrayList<String>();
		File rep = new File(this.chemin);
		/* On r�cup�re les fichiers du r�pertoire */
		File[] files = rep.listFiles();
		/* On parse la date */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		/* On parcourt chaque fichier du r�pertoire */
		for(int i=0; i<files.length; i++) {
			/* On r�cup�re la date du fichier */
			Date d = new Date(files[i].lastModified());
			String da = dateFormat.format(d);
			/* Si la date correspond � la recherche */
			if(da.equals(UmlRechercheDate.getJTextField2())) {
				/* On l'ajoute � l'arrayList */
				al.add(files[i].getName());
			}
		}

		/* On retourne l'arrayList */
		return al;

	}

	/*
	 * Methode rechercheAuteurFichierUml() : Permet de rechercher un auteur depuis la JFrame
	 * @return : La liste des fichiers qui correspond � la recherche
	 */
	public ArrayList<String> rechercheAuteurFichierUml() {
		/* D�claration de variables*/
		ArrayList<String> al = new ArrayList<String>();
		Path path = Paths.get(this.chemin);

		/* On recup�re les propri�t�s du fichier et plus principalement l'auteur */
		FileOwnerAttributeView ownerAttributeView = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
		UserPrincipal owner = null;
		try {
			owner = ownerAttributeView.getOwner();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String [] str =  owner.getName().split("\\\\");
		/* On parcourt la liste des fichiers du r�pertoire */
		for(Entry<String, Fichier> listFichier : this.getListFichier().entrySet()) {
			/* Si l'auteur recherch� correspond � celui du fichier */
			if(str[1].equals(UmlRechercheAuteur.getJTextField2())) {
				/* On l'ajoute dans l'arrayList */
				al.add(listFichier.getKey());
			}
		}

		/* On retourne l'arrayList */
		return al;

	}

	/*
	 * Methode rechercheNomFichier() : Permet de rechercher un nom de fichier
	 * @return : La liste des fichiers qui correspond � la recherche
	 * @param : le crit�re de recherche
	 */
	public ArrayList<String> rechercheNomFichier(String carac) {

		/* D�claration des variables */
		ArrayList<String> al = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		carac=sc.next();

		/* Pour chaque entr�e de la HashMap*/
		for(Entry<String, Fichier> listFichier : this.getListFichier().entrySet()) {
			/* Si le nom du fichier correspond au crit�re de recherche (ou au moins ses premi�res lettres)*/
			if(listFichier.getKey().startsWith(carac.substring(0,1))) {
				/* On ajoute � l'ArrayList le nom du fichier */
				al.add(listFichier.getKey());
			}
		}
		/* On affiche les fichiers correspondant au crit�re de recherche */
		for (int i=0; i<listFichier.size(); i++) {
			System.out.println("Voici les fichiers disponibles :" + al.get(i));

		}
		/* On retourne l'ArrayList */
		return al;

	}

	/*
	 * Methode rechercheDateFichier() : Permet de rechercher une date
	 * @return : La liste des fichiers qui correspond � la recherche
	 * @param : le crit�re de recherche
	 */
	public ArrayList<String> rechercheDateFichier(long carac) {
		/* D�claration de variables*/
		ArrayList<String> al = new ArrayList<String>();
		File rep = new File(this.chemin);
		/* On r�cup�re les fichiers du r�pertoire */
		File[] files = rep.listFiles();
		/* On parse la date */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Scanner sc = new Scanner(System.in);
		carac=sc.nextInt();

		/* On parcourt chaque fichier du r�pertoire */
		for(int i=0; i<files.length; i++) {
			/* On r�cup�re la date du fichier */
			Date d = new Date(files[i].lastModified());
			String da = dateFormat.format(d);
			/* Si la date correspond � la recherche */
			if(da.equals(carac)) {
				/* On l'ajoute � l'arrayList */
				al.add(files[i].getName());
			}
		}

		return al;

	}


	/*
	 * Methode rechercheAuteurFichier() : Permet de rechercher un auteur
	 * @return : La liste des fichiers qui correspond � la recherche
	 * @param : le crit�re de recherche
	 */
	public ArrayList<String> rechercheAuteurFichier(String carac) {
		/* D�claration de variables*/
		ArrayList<String> al = new ArrayList<String>();
		Path path = Paths.get(this.chemin);
		Scanner sc = new Scanner(System.in);
		carac=sc.next();

		/* On recup�re les propri�t�s du fichier et plus principalement l'auteur */
		FileOwnerAttributeView ownerAttributeView = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
		UserPrincipal owner = null;
		try {
			owner = ownerAttributeView.getOwner();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String [] str =  owner.getName().split("\\\\");
		/* On parcourt la liste des fichiers du r�pertoire */
		for(Entry<String, Fichier> listFichier : this.getListFichier().entrySet()) {
			/* Si l'auteur recherch� correspond � celui du fichier */
			if(str[1].equals(carac)) {
				/* On l'ajoute dans l'arrayList */
				al.add(listFichier.getKey());
			}
		}

		/* On retourne l'arrayList */
		return al;

	}

}
