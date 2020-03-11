package serveur;

import java.util.HashMap;

import envoie.reception.PDUAnnuaire;
import envoie.reception.PDUControle;
import systeme.fichiers.Fichier;
import systeme.fichiers.GestionFichier;

public class ServeurAnnuaire {
	HashMap<String, GestionFichier> listServeurs;
	PDUAnnuaire reponse;

	public PDUAnnuaire Registration(PDUAnnuaire pdu,String adresse) {
		GestionFichier t;
		t = listServeurs.put(adresse, pdu.getListFichiers());
		if(t == null) {
			reponse = new PDUAnnuaire("ANN","REGISTRATION",null,"NOK");
		}else {
			reponse = new PDUAnnuaire("ANN","REGISTRATION",null,"OK");
		}
		return reponse;
	}

	public PDUAnnuaire Search(PDUAnnuaire pdu) {

		return null;
	}

	public PDUAnnuaire Dowload(PDUAnnuaire pdu) {

		return null;
	}
}
