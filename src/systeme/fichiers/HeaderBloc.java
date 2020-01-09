package systeme.fichiers;

import java.io.Serializable;

public class HeaderBloc implements Serializable {
	Boolean disponible;
	
	
	public HeaderBloc(Boolean disponible) {
		this.disponible = disponible;
	}
	
	public Boolean getDisponible() {
		return disponible;
	}
	public void setDisponible(Boolean disponible) {
		this.disponible = disponible;
	}

	
}
