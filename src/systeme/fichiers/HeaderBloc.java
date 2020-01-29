package systeme.fichiers;

import java.io.Serializable;

public class HeaderBloc implements Serializable {
	int disponible;
	int nbrLecteurEnCours;
	
	
	public HeaderBloc(int disponible) {
		this.disponible = disponible;
	}
	
	public int getDisponible() {
		return disponible;
	}
	public void setDisponible(int disponible) {
		while(nbrLecteurEnCours != 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.disponible = disponible;
		notifyAll();
	}
	
	public synchronized void DebutLecture() {
		nbrLecteurEnCours++;
	}

	public synchronized void FinLecture() {
		nbrLecteurEnCours--;	
		notifyAll();
			
		}

	
}
