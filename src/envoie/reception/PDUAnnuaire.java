package envoie.reception;

import java.util.List;

import systeme.fichiers.GestionFichier;

/*
 * Classe PDUAnnuaire : Permet de gérer les PDU provenant de l'annuaire, hérite de la classe PDU
 */
@SuppressWarnings("serial")
public class PDUAnnuaire extends PDU {
	/* Déclaration de variables */

	GestionFichier  sysFichiers;
	String methode;
	List<String> listServeurs;
	
	/* 
	 * Méthode getListServeurs : Permet de récupérer la liste des serveurs enregistrés
	 * @return: La liste des serveurs enregistrés
	 */
	public List<String> getListServeurs() {
		return listServeurs;
	}

	/* 
	 * Méthode setListServeurs : Permet de changer la liste des serveurs enregistrés
	 * @param: La nouvelle liste des serveurs enregistrés
	 */
	public void setListServeurs(List<String> listServeurs) {
		this.listServeurs = listServeurs;
	}

	/*
	 * Constructeur PDUAnnaire : Prend en paramètre le type de connexion utilisé (UDP ou TCP), le gestionnaire de fichiers associé, les données à envoyer ainsi que la liste des serveurs.
	 * Instancie un nouveau PDUAnnuaire
	 */
	public PDUAnnuaire(String type, String methode, GestionFichier lF,String donnees,List<String> lS) {
		super(type, donnees);
		this. sysFichiers = lF;
		this.methode = methode;
		this. listServeurs = lS;
	}

	/*
	 * Méthode getSysFichiers : Permet de récupérer le système de fichiers de l'annuaire
	 * @return : Le système de fichiers de l'annuaire
	 */
	public GestionFichier getSysFichiers() {
		return  sysFichiers;
	}
	/*
	 * Méthode setSysFichiers : Permet de changer le système de fichiers de l'annuaire
	 * @param : Le nouveau système de fichiers de l'annuaire
	 */
	public void setSysFichiers(GestionFichier sysFichiers) {
		this. sysFichiers =  sysFichiers;
	}
	
	/*
	 * Methode getMethode : Permet de récupérer la méthode
	 * @return : La méthode utilisée
	 */
	public String getMethode() {
		return methode;
	}
	
	/*
	 * Methode setMethode : Permet de changer de méthode
	 * @return : La nouvelle méthode utilisée
	 */
	public void setMethode(String methode) {
		this.methode = methode;
	}
	
	

}
