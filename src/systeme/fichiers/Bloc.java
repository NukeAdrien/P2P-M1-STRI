package systeme.fichiers;

public class Bloc {
	Integer index;
	Byte donnees;
	Boolean disponible;
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public Byte getDonnees() {
		return donnees;
	}
	public void setDonnees(Byte donnees) {
		this.donnees = donnees;
	}
	public Boolean getDisponible() {
		return disponible;
	}
	public void setDisponible(Boolean disponible) {
		this.disponible = disponible;
	}
}
