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

/*
 * Classe GestionFichier --> Classe permettant de g�rer un fichier (Lecture, Ecriture, etc ....)
 */
public class GestionFichier {
	
	/* D�claration de variables */
	HashMap<String, Fichier> listFichier;
	String chemin;
	int nbrLireEnCours,nbrRechercheEnCours,nbrAjoutEnCours;

	/*
	 * Constructeur ClientDonnees --> Ce constructeur prend en param�tre un SocketClient et un gestion de fichier
	 * Ce constructeur permet de cr�er un nouveau ClientDonnees.
	 */
	public GestionFichier(String c) {
		this.chemin = c;
		listFichier= new HashMap<String, Fichier>();
	}

	 
	/*
	 * M�thode RechercheFichier : M�thode permettant avec un nom de fichier de retouner l'objet fichier correspondant
	 * @param : le type de PDU en String
	 * @return : l'objet Fichier du nom de fichier
	 */

	public Fichier RechercheFichier(String nomFichier) {
		while(nbrAjoutEnCours != 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.DebutRecherche();
		/* D�claration de fichiers */
		Fichier recherche = null;
		/* On parcourt une hash map de fichiers */
		for (Entry<String, Fichier> listFichier : this.getListFichier().entrySet()) {
			/* On compare la cl� de la hash map avec le nom fichier */
			if(listFichier.getKey().compareTo(nomFichier) == 0) {
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
	 * M�thode EtatFichier : M�thode permettant de retourner l'�tat du fichier actuel
	 * @param : le nom du fichier
	 * @return : -1 si non disponible, 0 si le fichier est en cours de t�l�chargement, 1 si le fichier est disponible
	 */

	public Integer EtatFichier(String nomFichier) {
		/* D�claration de variables */
		Fichier fichier = null;
		/* On r�cup�re le nom de fichier (si il existe) */
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
					System.out.println("Fichier en cours de t�l�chargement");
					return 0;
				} 
			}
			/* Si on quitte la boucle for alors on retourne 1; */
		}
		return 1;
	}

	/*
	 * M�thode Lire : M�thode permettant de lire un fichier
	 * @param : le nom du fichier, le num�ro de bloc � lire
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
	 * @param : le nom du fichier, le num�ro de bloc dans lequel on �crira dans les donn�es, et les donn�es qui sont �crires
	 * @return : 0 si �a s'est bien pass�e, 1 sinon
	 */
	public synchronized Integer Ecrire(Fichier fichier, Integer numbloc, byte[] donnees) {
		while(nbrLireEnCours != 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/* On r�cup�re l'emplacement du fichier */
		String file = fichier.getEmplacement();
		/* D�claration d'un nouveau fichier */
		File fle = new File(file);
		/* Si le fichier n'existe pas */
		if (!fle.exists()) {
			try {
				/* On cr�e un nouveau fichier */
				fle.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/* D�claration de la taille */
		int taille = 4000;
		/* Positionnement de l'offset en fonction des num�ros de blocs */
		long offset = taille * numbloc;
		try {
			/* Positionnement des flux pour l'�criture */
			FileOutputStream o = new FileOutputStream(fle, true);
			/* Positionnement du curseur */
			o.getChannel().position(offset);
			/* Ecriture des donn�es en fonction de la position du curseur */
			o.write(donnees, 0, taille);
			/* Fermeture des flux */
			o.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		notifyAll();
		return 0;
	}

	/*
	 * M�thode getListFichier : M�thode permettant de r�cup�rer la liste des fichiers dans la HashMap d'un bloc
	 * @return : la liste des fichiers
	 */
	public HashMap<String, Fichier> getListFichier() {
		return listFichier;
	}

	/*
	 * M�thode setListFichier : M�thode permettant de changer la liste des fichiers dans la HashMap d'un bloc
	 * @return : la nouvelle liste des fichiers
	 */
	public void setListFichier(HashMap<String, Fichier> listFichier) {
		this.listFichier = listFichier;
	
	}
	/*
	 * M�thode getDisponible : M�thode permettant de r�cup�rer la disponibilit� d'un bloc
	 * @param : le nom du fichier, l'index du header bloc
	 * @return : la disponibilit� du fichier : -1 si le fichier n'est dispo, 0 si le fichier est en cours de t�l�chargement et 1 si le fichier est disponible
	 */
	public int getDisponible(String nom, Integer index) {
		return this.listFichier.get(nom).getDisponible(index);
	}

	/*
	 * M�thode setDisponible : M�thode permettant de changer la disponibilit� d'un bloc
	 * @param : le nom du fichier, l'index du header bloc, la nouvelle disponibilit�
	 * @return : la nouvelle disponibilit� du fichier : -1 si le fichier n'est dispo, 0 si le fichier est en cours de t�l�chargement et 1 si le fichier est disponible
	 */
	public void setDisponible(String nom, Integer index, int disponible) {
		this.listFichier.get(nom).setDisponible(index, disponible);
	}

	/*
	 * M�thode AjouterFichier : M�thode permettant d'ajouter un fichier � la HashMap
	 * @param : le fichier � ajouter
	 */
	public void AjouterFichier(Fichier f) {
		while(nbrRechercheEnCours != 0 && nbrAjoutEnCours != 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(this.RechercheFichier(f.nomFichier) != null) {
				return;
			}
		}
		this.DebutAjout();
		this.listFichier.put(f.getNomFichier(), f);
		this.FinAjout();
	}

	/*
	 * M�thode getChemin : M�thode permettant de retourner le chemin pour un fichier donn�
	 * @return : le chemin du fichier
	 */
	
	public String getChemin() {
		return chemin;
	}

	/*
	 * M�thode getChemin : M�thode permettant de changer le chemin pour un fichier donn�
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
	 * M�thode getTailleFichier : M�thode permettant de r�cp�rer la taille du fichier.
	 * @param : Le nom du fichier
	 * @return : la taille du fichier associ�e (en octets)
	 */
	
	private Long getTailleFichier(String nomFichier) {
		File f = new File(nomFichier);
		return f.length();
	}

	/*
	 * M�thode dateModifFichier : M�thode permettant de r�cup�rer la date de la derni�re modification du fichier.
	 * @param : Le nom du fichier
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
	 * M�thode initGestionFichier : M�thode permettant d'initialiser une gestion de fichier.
	 * @return : 0 si �a s'est bien pass�e, 1 sinon*/
	 
	public Integer initGestionFichier() {
		 /* D�claration de variables */
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
			/* Si le fichier est un r�pertoire */
			if (listFiles[i].isDirectory()) {
				//getFilesRec(allFiles, listFiles[i].toString());
			} else
				/* Ajout du fichier dans l'Array List*/
				allFiles.add(listFiles[i].getName());
		}
		
		/* On parcourt la liste de fichiers contenues dans l'ArrayList*/

		for (int i=0; i<allFiles.size(); i++) {
			/* On r�cup�re la taille du fichier */
			double taillFich = this.getTailleFichier(this.chemin+allFiles.get(i));
			/* On r�cup�re la derni�re date de modification du fichier */
			String date = this.dateModifFichier(allFiles.get(i));
			/* On d�claration un nouveau fichier */
			Fichier fichier = new Fichier(allFiles.get(i), date, this.chemin+allFiles.get(i), 
					(long) taillFich);
			/* On r�cup�re le nombre de blocs en fonction de la taille du fichier */
			int nbBloc = (int) (taillFich/4000);
			double tmp = taillFich/4000;
			if(tmp > nbBloc) {
				nbBloc++;
			}
			/* On parcourt les num�ros du bloc */
			for (int j=0; j<nbBloc; j++) {
				/* On initialise un tableau de bytes de donn�es */
				byte[] donnees = (byte[]) null;
				/*On lit les donn�es contenues dans le fichier */
				donnees=this.Lire(fichier, j);
				/* Si il n'y a pas de donn�es */
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
			/* On ajoute le fichier � la Hash Map */
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
	
	public synchronized void DebutAjout() {
		nbrAjoutEnCours++;
	}

	public synchronized void FinAjout() {
		nbrAjoutEnCours--;	
		notifyAll();
			
		}
	
	public synchronized void DebutRecherche() {
		nbrRechercheEnCours++;
	}

	public synchronized void FinRecherche() {
		nbrRechercheEnCours--;	
		notifyAll();
			
		}
	

}
