package systeme.fichiers;

import java.io.Serializable;
import java.util.HashMap;

/*
 * Classe Fichier --> Classe permettant de créer et d'instancier un fichier
 */

public class Fichier implements Serializable {
	
	/* Déclaration de variables */
	String nomFichier, auteur, date, emplacement;
	Integer tailleOctets;
	HashMap<Integer, HeaderBloc> listHeaderBlocs = new HashMap<Integer, HeaderBloc>(); 

	/*
	 * Constructeur Fichier --> Ce constructeur prend en paramètre le nom du fichier, l'auteur, la date, l'emplacement,
	 * et la taille en octets. Ce constructeur permet de créer un nouveau fichier.
	 */
	
	public Fichier(String n,String a,String d, String e, Integer t) {
		nomFichier = n;
		auteur = a;
		date = d;
		emplacement =e ;
		tailleOctets = t;
	}

	/*
	 * Méthode getAuteur : Méthode permettant de récupérer l'auteur du fichier
	 * @return : l'auteur du fichier en String
	 */
	
	public String getAuteur() {
		return auteur;
	}

	/*
	 * Méthode setAuteur : Méthode permettant de changer l'auteur du fichier
	 * @param : le nouvel auteur du fichier en String
	 */
	
	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	/*
	 * Méthode getDate : Méthode permettant de récupérer la dernière date de modification du fichier
	 * @return : la dernière date de modification du fichier en String
	 */
	
	public String getDate() {
		return date;
	}

	/*
	 * Méthode setDate : Méthode permettant de changer la dernière date de modification du fichier
	 * @param : la nouvelle dernière date de modification du fichier en String
	 */
	
	public void setDate(String date) {
		this.date = date;
	}

	/*
	 * Méthode getTailleOctets : Méthode permettant de récupérer la taille en octets du fichier
	 * @return : la taille du fichier en octets en Integer
	 */
	
	public Integer getTailleOctets() {
		return tailleOctets;
	}

	/*
	 * Méthode setTailleOctets : Méthode permettant de changer la taille en octets du fichier
	 * @param : la nouvelle taille du fichier en octets en Integer
	 */
	public void setTailleOctets(Integer tailleOctets) {
		this.tailleOctets = tailleOctets;
	}

	/*
	 * Méthode setlistHeaderBlocs : Méthode permettant de changer les HeaderBlocs d'un fichier
	 * @param : la nouvelle liste des HeaderBlocs du fichier stockées dans une HashMap (avec comme clé l'index, et en valeur
	 * l'HeaderBloc). Pour plus d'informations se référer à la classe HeaderBloc.java
	 */
	/*public void setlistHeaderBlocs(HashMap<Integer, HeaderBloc> listHeaderBlocs) {
		this.listHeaderBlocs = listHeaderBlocs;
	}*/
	

	/*
	 * Méthode getListHeaderBlocs : Méthode permettant de récuperer tous les HeaderBlocs d'un fichier
	 * @return : la liste de tous les HeaderBlocs du fichier stockées dans une HashMap (avec comme clé l'index, et en valeur
	 * l'HeaderBloc). Pour plus d'informations se référer à la classe HeaderBloc.java
	 */
	
	public HashMap<Integer, HeaderBloc> getListHeaderBlocs() {
		return listHeaderBlocs;
	}

	/*
	 * Méthode setListHeaderBlocs : Méthode permettant de changer les HeaderBlocs d'un fichier
	 * @param : la nouvelle liste des HeaderBlocs du fichier stockées dans une HashMap (avec comme clé l'index, et en valeur
	 * l'HeaderBloc). Pour plus d'informations se référer à la classe HeaderBloc.java
	 */
	public void setListHeaderBlocs(HashMap<Integer, HeaderBloc> listHeaderBlocs) {
		this.listHeaderBlocs = listHeaderBlocs;
	}

	/*
	 * Méthode setNomFichier : Méthode permettant de changer le nom du fichier
	 * @param : le nouveau nom du fichier en String
	 */
	
	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}

	/*
	 * Méthode getNomFichier : Méthode permettant de récupérer le nom du fichier
	 * @return : le nom du fichier en String
	 */
	public String getNomFichier() {
		return nomFichier;
	}

	/*
	 * Méthode getEmplacement : Méthode permettant de récupérer l'emplacement du fichier
	 * @return : l'emplacement du fichier en String
	 */
	
	public String getEmplacement() {
		return emplacement;
	}
	
	/*
	 * Méthode getDisponible : Méthode permettant de récupérer la disponibilité d'un bloc de fichier (si il est inexitant 
	 * ou encore si il est en cours de téléchargement ou encore si il est disponible).
	 * @param : Un index d'un des blocs constituant la PDU
	 * @return : Sa disponibilité en Integer.
	 */
	
	public int getDisponible(Integer index) {
		return this.listHeaderBlocs.get(index).getDisponible();
	}
	
	/*
	 * Méthode setDisponible : Méthode permettant de changer la disponibilité d'un bloc de fichier (si il est inexitant 
	 * ou encore si il est en cours de téléchargement ou encore si il est disponible).
	 * @param : Le nouvel index d'un des blocs constituant la PDU et sa nouvelle disponibilité
	 */
	
	public void setDisponible(Integer index, int disponible) {
		this.listHeaderBlocs.get(index).setDisponible(disponible);
	}
	
	/*
	 * Méthode setEmplacement : Méthode permettant de changer l'emplacement du fichier
	 * @param : le nouvel emplacement du fichier en String
	 */
	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}
	
	/*
	 * Méthode AjouterHeaderBloc : Méthode permettant d'ajouter un nouveau bloc de fichier
	 * @param : Son index et son HeaderBloc (Voir la classe HeaderBloc.java)
	 */
	public void AjouterHeaderBloc(Integer index, HeaderBloc hd) {
		this.listHeaderBlocs.put(index, hd);
	}
	
}
