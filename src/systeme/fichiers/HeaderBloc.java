package systeme.fichiers;

import java.io.Serializable;

public class HeaderBloc implements Serializable {
	int disponible;
	
	
	public HeaderBloc(int disponible) {
		this.disponible = disponible;
	}
	
	public int getDisponible() {
		return disponible;
	}
	public void setDisponible(int disponible) {
		this.disponible = disponible;
	}

	
}
