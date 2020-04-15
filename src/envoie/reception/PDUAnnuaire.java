package envoie.reception;

import java.util.List;

import systeme.fichiers.GestionFichier;

/*
 * Classe PDUAnnuaire : Permet de g�rer les PDU provenant de l'annuaire, h�rite de la classe PDU
 */
@SuppressWarnings("serial")
public class PDUAnnuaire extends PDU {
	/* D�claration de variables */

	GestionFichier  sysFichiers;
	String methode;
	List<String> listServeurs;
	
	/* 
	 * M�thode getListServeurs : Permet de r�cup�rer la liste des serveurs enregistr�s
	 * @return: La liste des serveurs enregistr�s
	 */
	public List<String> getListServeurs() {
		return listServeurs;
	}

	/* 
	 * M�thode setListServeurs : Permet de changer la liste des serveurs enregistr�s
	 * @param: La nouvelle liste des serveurs enregistr�s
	 */
	public void setListServeurs(List<String> listServeurs) {
		this.listServeurs = listServeurs;
	}

	/*
	 * Constructeur PDUAnnaire : Prend en param�tre le type de connexion utilis� (UDP ou TCP), le gestionnaire de fichiers associ�, les donn�es � envoyer ainsi que la liste des serveurs.
	 * Instancie un nouveau PDUAnnuaire
	 */
	public PDUAnnuaire(String type, String methode, GestionFichier lF,String donnees,List<String> lS) {
		super(type, donnees);
		this. sysFichiers = lF;
		this.methode = methode;
		this. listServeurs = lS;
	}

	/*
	 * M�thode getSysFichiers : Permet de r�cup�rer le syst�me de fichiers de l'annuaire
	 * @return : Le syst�me de fichiers de l'annuaire
	 */
	public GestionFichier getSysFichiers() {
		return  sysFichiers;
	}
	/*
	 * M�thode setSysFichiers : Permet de changer le syst�me de fichiers de l'annuaire
	 * @param : Le nouveau syst�me de fichiers de l'annuaire
	 */
	public void setSysFichiers(GestionFichier sysFichiers) {
		this. sysFichiers =  sysFichiers;
	}
	
	/*
	 * Methode getMethode : Permet de r�cup�rer la m�thode
	 * @return : La m�thode utilis�e
	 */
	public String getMethode() {
		return methode;
	}
	
	/*
	 * Methode setMethode : Permet de changer de m�thode
	 * @return : La nouvelle m�thode utilis�e
	 */
	public void setMethode(String methode) {
		this.methode = methode;
	}
	
	

}
