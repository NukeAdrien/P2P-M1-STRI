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

import client.ClientControle;
import client.ClientControleThread;
import client.ClientDonnees;
import client.ClientP2P;
import serveur.ServeurControle;
import serveur.ServeurDonnees;
import socket.SocketClient;

/*
 * Classe GestionFichier --> Classe permettant de gérer un fichier (Lecture, Ecriture, etc ....)
 */

public class GestionFichier {
	
	/* Déclaration de variables */
	HashMap<String, Fichier> listFichier;
	String chemin;

	/*
	 * Constructeur ClientDonnees --> Ce constructeur prend en paramètre un SocketClient et un gestion de fichier
	 * Ce constructeur permet de créer un nouveau ClientDonnees.
	 */
	public GestionFichier(String c) {
		this.chemin = c;
		listFichier= new HashMap<String, Fichier>();
	}

	 
	/*
	 * Méthode RechercheFichier : Méthode permettant avec un nom de fichier de retouner l'objet fichier correspondant
	 * @param : le type de PDU en String
	 * @return : l'objet Fichier du nom de fichier
	 */

	public Fichier RechercheFichier(String nomFichier) {
		/* Déclaration de fichiers */
		Fichier recherche = null;
		/* On parcourt une hash map de fichiers */
		for (Entry<String, Fichier> listFichier : this.getListFichier().entrySet()) {
			/* On compare la clé de la hash map avec le nom fichier */
			if(listFichier.getKey().contentEquals(nomFichier)) {
				/* On récupère le fichier */
				recherche = this.listFichier.get(nomFichier);
				break;
			} else {
				/* Affichage d'un message d'erreur */
				System.out.println("Le fichier est inexistant dans la HashMap");
			}
	
		}
		/* On retourne le fichier */
		return recherche;
	}

	/*
	 * Méthode EtatFichier : Méthode permettant de retourner l'état du fichier actuel
	 * @param : le nom du fichier
	 * @return : -1 si non disponible, 0 si le fichier est en cours de téléchargement, 1 si le fichier est disponible
	 */

	public Integer EtatFichier(String nomFichier) {
		/* Déclaration de variables */
		Fichier fichier = null;
		/* On récupère le nom de fichier (si il existe) */
		fichier = this.listFichier.get(nomFichier);
		/* Si le fichier n'existe pas*/
		if (fichier == null) {
			/* Affichage d'un message d'erreur */
			System.out.println("Le fichier est inexistant ! ");
			return -1;
		}
		// On viens de parcourir la liste de Header Bloc contenue dans une HashMap
		for (Map.Entry<Integer, HeaderBloc> headerbloc : fichier.listHeaderBlocs.entrySet()) {
			/* Si la valeur du header bloc est a -1 */
			if(headerbloc.getValue().getDisponible() == -1) {
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
		return 1;
	}

	/*
	 * Méthode Lire : Méthode permettant de lire un fichier
	 * @param : le nom du fichier, le numéro de bloc à lire
	 * @return : le tableau d'octets correspondant aux données
	 */

	public byte[] Lire(Fichier fichier, Integer numBloc) {
		/* Déclaration de variables */
		byte[] bloc = new byte[4000];
		FileInputStream fileis = null;
		int taille = bloc.length;
		long offset = taille * numBloc;
		
		/* On récupère l'emplacement du fichier */
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
		/* On retourne le bloc lu */
		return bloc;

	}

	/*
	 * Méthode Ecrire : Méthode permettant d'écrire dans un fichier
	 * @param : le nom du fichier, le numéro de bloc dans lequel on écrira dans les données, et les données qui sont écrires
	 * @return : 0 si ça s'est bien passée, 1 sinon
	 */
	public Integer Ecrire(Fichier fichier, Integer numbloc, byte[] donnees) {
		/* On récupère l'emplacement du fichier */
		String file = fichier.getEmplacement();
		/* Déclaration d'un nouveau fichier */
		File fle = new File(file);
		/* Si le fichier n'existe pas */
		if (!fle.exists()) {
			try {
				/* On crée un nouveau fichier */
				fle.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/* Déclaration de la taille */
		int taille = 4000;
		/* Positionnement de l'offset en fonction des numéros de blocs */
		long offset = taille * numbloc;
		try {
			/* Positionnement des flux pour l'écriture */
			FileOutputStream o = new FileOutputStream(fle, true);
			/* Positionnement du curseur */
			o.getChannel().position(offset);
			/* Ecriture des données en fonction de la position du curseur */
			o.write(donnees, 0, taille);
			/* Fermeture des flux */
			o.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 0;
	}

	/*
	 * Méthode getListFichier : Méthode permettant de récupérer la liste des fichiers dans la HashMap d'un bloc
	 * @return : la liste des fichiers
	 */
	public HashMap<String, Fichier> getListFichier() {
		return listFichier;
	}

	/*
	 * Méthode setListFichier : Méthode permettant de changer la liste des fichiers dans la HashMap d'un bloc
	 * @return : la nouvelle liste des fichiers
	 */
	public void setListFichier(HashMap<String, Fichier> listFichier) {
		this.listFichier = listFichier;
	
	}
	/*
	 * Méthode getDisponible : Méthode permettant de récupérer la disponibilité d'un bloc
	 * @param : le nom du fichier, l'index du header bloc
	 * @return : la disponibilité du fichier : -1 si le fichier n'est dispo, 0 si le fichier est en cours de téléchargement et 1 si le fichier est disponible
	 */
	public int getDisponible(String nom, Integer index) {
		return this.listFichier.get(nom).getDisponible(index);
	}

	/*
	 * Méthode setDisponible : Méthode permettant de changer la disponibilité d'un bloc
	 * @param : le nom du fichier, l'index du header bloc, la nouvelle disponibilité
	 * @return : la nouvelle disponibilité du fichier : -1 si le fichier n'est dispo, 0 si le fichier est en cours de téléchargement et 1 si le fichier est disponible
	 */
	public void setDisponible(String nom, Integer index, int disponible) {
		this.listFichier.get(nom).setDisponible(index, disponible);
	}

	/*
	 * Méthode AjouterFichier : Méthode permettant d'ajouter un fichier à la HashMap
	 * @param : le fichier à ajouter
	 */
	public void AjouterFichier(Fichier f) {
		this.listFichier.put(f.getNomFichier(), f);
	}

	/*
	 * Méthode getChemin : Méthode permettant de retourner le chemin pour un fichier donné
	 * @return : le chemin du fichier
	 */
	
	public String getChemin() {
		return chemin;
	}

	/*
	 * Méthode getChemin : Méthode permettant de changer le chemin pour un fichier donné
	 * @return : le nouveau chemin du fichier
	 */
	public void setChemin(String chemin) {
		this.chemin = chemin;
	}

/*	private HashMap<String, Fichier> getFilesRec() {
		File f = new File(nomFichier);
		File[] listFiles = f.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			if (listFiles[i].isDirectory()) {
				//getFilesRec(allFiles, listFiles[i].toString());
			} else
				allFiles.add(listFiles[i].toString());
		}
		for (int i=0; i<allFiles.size(); i++) {
			this.getTailleFichier(nomFichier);
			Fichier fichier = new Fichier(allFiles.get(i), this.dateModifFichier(nomFichier), this.chemin+allFiles.get(i), 
					this.getTailleFichier(nomFichier));
			this.listFichier.add(nomFichier, fichier);
		}
	}*/

	/*
	 * Méthode getTailleFichier : Méthode permettant de récpérer la taille du fichier.
	 * @param : Le nom du fichier
	 * @return : la taille du fichier associée (en octets)
	 */
	
	private Long getTailleFichier(String nomFichier) {
		File f = new File(nomFichier);
		return f.length();
	}

	/*
	 * Méthode dateModifFichier : Méthode permettant de récupérer la date de la dernière modification du fichier.
	 * @param : Le nom du fichier
	 * @return : la date du fichier associée (en octets)
	 */
	
	private String dateModifFichier(String nomFichier) {
		/* On parse dans le format d'une date */
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
		/* Création nouveau fichier */
		File f = new File(nomFichier);
		/* Fonction permettant de récupérer la dernière date de modification */
		Date d = new Date(f.lastModified());
		/* Affichage message de succès */
		System.out.println("Dernière modification le : " + sdf.format(d));
		/* Retour dans le format d'une date */
		return sdf.format(d);

	}
	
	/*
	 * Méthode initGestionFichier : Méthode permettant d'initialiser une gestion de fichier.
	 * @return : 0 si ça s'est bien passée, 1 sinon*/
	 
	@SuppressWarnings("unused")
	public Integer initGestionFichier() {
		 /* Déclaration de variables */
		ArrayList<String> allFiles = new ArrayList<String>();
		File f = new File(this.chemin);
		HeaderBloc blc =null;
		
		/* On liste les fichiers dans un tableau de fichier */
		File[] listFiles = f.listFiles();
		/* Si il n'y a pas de fichiers */
		if (!(listFiles != null)) {
			return -1;
		}
		/* On parcourt la liste de fichiers*/
		for (int i = 0; i < listFiles.length; i++) {
			/* Si le fichier est un répertoire */
			if (listFiles[i].isDirectory()) {
				//getFilesRec(allFiles, listFiles[i].toString());
			} else
				/* Ajout du fichier dans l'Array List*/
				allFiles.add(listFiles[i].getName());
		}
		
		/* On parcourt la liste de fichiers contenues dans l'ArrayList*/

		for (int i=0; i<allFiles.size(); i++) {
			/* On récupère la taille du fichier */
			long taillFich = this.getTailleFichier(this.chemin+allFiles.get(i));
			/* On récupère la dernière date de modification du fichier */
			String date = this.dateModifFichier(allFiles.get(i));
			/* On déclaration un nouveau fichier */
			Fichier fichier = new Fichier(allFiles.get(i), date, this.chemin+allFiles.get(i), 
					taillFich);
			/* On récupère le nombre de blocs en fonction de la taille du fichier */
			int nbBloc = Math.round((int) taillFich/4000);
			/* On parcourt les numéros du bloc */
			for (int j=0; j<nbBloc; j++) {
				/* On initialise un tableau de bytes de données */
				byte[] donnees = (byte[]) null;
				/*On lit les données contenues dans le fichier */
				donnees=this.Lire(fichier, j);
				/* Si il n'y a pas de données */
				if (!(donnees != null)) {
					/* Le bloc n'est pas disponible */
					blc = new HeaderBloc(-1);
				}else {
					/* Le bloc est disponible */
					blc = new HeaderBloc(1);
				}
				/* On ajoute l'header bloc au fichier */
				fichier.AjouterHeaderBloc(j, blc);
				
			}
			/* On ajoute le fichier à la Hash Map */
			this.listFichier.put(allFiles.get(i), fichier);
		}
		
		return 0;
	}
}
