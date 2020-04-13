package systeme.fichiers;

import java.io.Serializable;


/*
 * Classe HeaderBloc --> Classe permettant de créer et d'instancier un HeaderBloc
 */

public class HeaderBloc implements Serializable, Cloneable {
	
	/* Déclaration de variables */
	int disponible;
	
	/*
	 * Constructeur HeaderBloc --> Ce constructeur prend en paramètre sa disponibilité. 
	 * Ce constructeur permet de créer un nouvel HeaderBloc.
	 */
	public HeaderBloc(int disponible) {
		this.disponible = disponible;
	}
	
	/*
	 * Méthode getDisponible : Méthode permettant de récupérer la disponibilité du bloc de fichier donné
	 * @return : la disponibilité du bloc de Fichier en Integer :  0 si le bloc est en cours de téléchargement,
	 * -1 si le bloc n'existe pas et 1 si le bloc est disponible
	 */
	public int getDisponible() {
		return disponible;
	}
	
	/*
	 * Méthode setDisponible : Méthode permettant de changer la disponibilité du bloc de fichier donné
	 * @param : la disponibilité du bloc de Fichier en Integer :  0 si le bloc est en cours de téléchargement,
	 * -1 si le bloc n'existe pas et 1 si le bloc est disponible
	 */
	public void setDisponible(int disponible) {

		this.disponible = disponible;

	}
	
	/*
	 * Méthode clone() : Permet de cloner un élément
	 * @return : L'objet cloné
	 */
	
	public Object clone() {
		/* Déclaration de variables*/ 
		HeaderBloc o = null;
		try {
			o = (HeaderBloc) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	
}
