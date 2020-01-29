package systeme.fichiers;

import java.io.Serializable;
import java.util.HashMap;

/*
 * Classe Fichier --> Classe permettant de cr�er et d'instancier un fichier
 */

public class Fichier implements Serializable {
	
	/* D�claration de variables */
	String nomFichier, auteur, date, emplacement;
	Long tailleOctets;
	HashMap<Integer, HeaderBloc> listHeaderBlocs = new HashMap<Integer, HeaderBloc>(); 

	/*
	 * Constructeur Fichier --> Ce constructeur prend en param�tre le nom du fichier, l'auteur, la date, l'emplacement,
	 * et la taille en octets. Ce constructeur permet de cr�er un nouveau fichier.
	 */
	
	public Fichier(String n,String d, String e, Long t) {
		nomFichier = n;
		date = d;
		emplacement =e ;
		tailleOctets = t;
	}


	/*
	 * M�thode setAuteur : M�thode permettant de changer l'auteur du fichier
	 * @param : le nouvel auteur du fichier en String
	 */
	
	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	/*
	 * M�thode getDate : M�thode permettant de r�cup�rer la derni�re date de modification du fichier
	 * @return : la derni�re date de modification du fichier en String
	 */
	
	public String getDate() {
		return date;
	}

	/*
	 * M�thode setDate : M�thode permettant de changer la derni�re date de modification du fichier
	 * @param : la nouvelle derni�re date de modification du fichier en String
	 */
	
	public void setDate(String date) {
		this.date = date;
	}

	/*
	 * M�thode getTailleOctets : M�thode permettant de r�cup�rer la taille en octets du fichier
	 * @return : la taille du fichier en octets en Integer
	 */
	
	public Long getTailleOctets() {
		return tailleOctets;
	}

	/*
	 * M�thode setTailleOctets : M�thode permettant de changer la taille en octets du fichier
	 * @param : la nouvelle taille du fichier en octets en Integer
	 */
	public void setTailleOctets(Long tailleOctets) {
		this.tailleOctets = tailleOctets;
	}

	/*
	 * M�thode setlistHeaderBlocs : M�thode permettant de changer les HeaderBlocs d'un fichier
	 * @param : la nouvelle liste des HeaderBlocs du fichier stock�es dans une HashMap (avec comme cl� l'index, et en valeur
	 * l'HeaderBloc). Pour plus d'informations se r�f�rer � la classe HeaderBloc.java
	 */
	/*public void setlistHeaderBlocs(HashMap<Integer, HeaderBloc> listHeaderBlocs) {
		this.listHeaderBlocs = listHeaderBlocs;
	}*/
	

	/*
	 * M�thode getListHeaderBlocs : M�thode permettant de r�cuperer tous les HeaderBlocs d'un fichier
	 * @return : la liste de tous les HeaderBlocs du fichier stock�es dans une HashMap (avec comme cl� l'index, et en valeur
	 * l'HeaderBloc). Pour plus d'informations se r�f�rer � la classe HeaderBloc.java
	 */
	
	public HashMap<Integer, HeaderBloc> getListHeaderBlocs() {
		return listHeaderBlocs;
	}

	/*
	 * M�thode setListHeaderBlocs : M�thode permettant de changer les HeaderBlocs d'un fichier
	 * @param : la nouvelle liste des HeaderBlocs du fichier stock�es dans une HashMap (avec comme cl� l'index, et en valeur
	 * l'HeaderBloc). Pour plus d'informations se r�f�rer � la classe HeaderBloc.java
	 */
	public void setListHeaderBlocs(HashMap<Integer, HeaderBloc> listHeaderBlocs) {
		this.listHeaderBlocs = listHeaderBlocs;
	}

	/*
	 * M�thode setNomFichier : M�thode permettant de changer le nom du fichier
	 * @param : le nouveau nom du fichier en String
	 */
	
	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}

	/*
	 * M�thode getNomFichier : M�thode permettant de r�cup�rer le nom du fichier
	 * @return : le nom du fichier en String
	 */
	public String getNomFichier() {
		return nomFichier;
	}

	/*
	 * M�thode getEmplacement : M�thode permettant de r�cup�rer l'emplacement du fichier
	 * @return : l'emplacement du fichier en String
	 */
	
	public String getEmplacement() {
		return emplacement;
	}
	
	/*
	 * M�thode getDisponible : M�thode permettant de r�cup�rer la disponibilit� d'un bloc de fichier (si il est inexitant 
	 * ou encore si il est en cours de t�l�chargement ou encore si il est disponible).
	 * @param : Un index d'un des blocs constituant la PDU
	 * @return : Sa disponibilit� en Integer.
	 */
	
	public int getDisponible(Integer index) {
		int disp;
		this.listHeaderBlocs.get(index).DebutLecture();
		disp =this.listHeaderBlocs.get(index).getDisponible();
		this.listHeaderBlocs.get(index).FinLecture();
		return disp;
	}
	
	/*
	 * M�thode setDisponible : M�thode permettant de changer la disponibilit� d'un bloc de fichier (si il est inexitant 
	 * ou encore si il est en cours de t�l�chargement ou encore si il est disponible).
	 * @param : Le nouvel index d'un des blocs constituant la PDU et sa nouvelle disponibilit�
	 */
	
	public synchronized void setDisponible(Integer index, int disponible) {
		this.listHeaderBlocs.get(index).setDisponible(disponible);
	}
	
	/*
	 * M�thode setEmplacement : M�thode permettant de changer l'emplacement du fichier
	 * @param : le nouvel emplacement du fichier en String
	 */
	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}
	
	/*
	 * M�thode AjouterHeaderBloc : M�thode permettant d'ajouter un nouveau bloc de fichier
	 * @param : Son index et son HeaderBloc (Voir la classe HeaderBloc.java)
	 */
	public void AjouterHeaderBloc(Integer index, HeaderBloc hd) {
		this.listHeaderBlocs.put(index, hd);
	}
	
}
