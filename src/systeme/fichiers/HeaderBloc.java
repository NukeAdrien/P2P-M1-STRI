package systeme.fichiers;

import java.io.Serializable;


/*
 * Classe HeaderBloc --> Classe permettant de cr�er et d'instancier un HeaderBloc
 */

public class HeaderBloc implements Serializable, Cloneable {
	
	/* D�claration de variables */
	int disponible;
	
	/*
	 * Constructeur HeaderBloc --> Ce constructeur prend en param�tre sa disponibilit�. 
	 * Ce constructeur permet de cr�er un nouvel HeaderBloc.
	 */
	public HeaderBloc(int disponible) {
		this.disponible = disponible;
	}
	
	/*
	 * M�thode getDisponible : M�thode permettant de r�cup�rer la disponibilit� du bloc de fichier donn�
	 * @return : la disponibilit� du bloc de Fichier en Integer :  0 si le bloc est en cours de t�l�chargement,
	 * -1 si le bloc n'existe pas et 1 si le bloc est disponible
	 */
	public int getDisponible() {
		return disponible;
	}
	
	/*
	 * M�thode setDisponible : M�thode permettant de changer la disponibilit� du bloc de fichier donn�
	 * @param : la disponibilit� du bloc de Fichier en Integer :  0 si le bloc est en cours de t�l�chargement,
	 * -1 si le bloc n'existe pas et 1 si le bloc est disponible
	 */
	public void setDisponible(int disponible) {

		this.disponible = disponible;

	}
	
	/*
	 * M�thode clone() : Permet de cloner un �l�ment
	 * @return : L'objet clon�
	 */
	
	public Object clone() {
		/* D�claration de variables*/ 
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
