package client;

import java.util.HashMap;
import java.util.Map;

import envoie.reception.PDU;
import envoie.reception.PDUDonnees;
import socket.SocketClient;
import systeme.fichiers.*;

public class ClientDonnees {
	SocketClient serveur;
	GestionFichier sysFichiers;

	public ClientDonnees(GestionFichier g, SocketClient s) {
		sysFichiers = g;
		serveur = s;
	}

	public ClientDonnees(GestionFichier g) {
		sysFichiers = g;
	}

	public Integer InitialiserConnexion(String ip, Integer port) {
		if (serveur.InitialisationSocket(ip, 4444) != 0) {
			System.out.println("Impossible de joindre le serveur");
			return 1;
		} else {
			return 0;
		}
	}

	public Integer Dowload(Fichier fichier, HashMap<Integer, HeaderBloc> listHeaderBlocs) {
		if (listHeaderBlocs == null) {
			listHeaderBlocs = fichier.getListHeaderBlocs();
		}
		for (Map.Entry<Integer,HeaderBloc> headerbloc : listHeaderBlocs.entrySet()) {
			PDUDonnees requete = new PDUDonnees("DATA",fichier.getNomFichier() ,(int)headerbloc.getKey(), null);
			// Envoie de la PDU au serveur
			if (serveur.EnvoiePDU(requete) != 0) {
				serveur.FermerSocket();
				System.out.println("Erreur lors de l'envoie de la requete");
				return 1;
			}
			PDU reponsePDU = null;
			PDUDonnees reponse = null;
			// Recupere la PDU recu
			reponsePDU = serveur.RecevoirPDU();
			// Test si la PDU n'est pas null
			if (reponsePDU == null) {
				System.out.println("Erreur de connexion avec le serveur");
				return 1;
			}else if(reponsePDU instanceof PDUDonnees) {
				reponse = (PDUDonnees) reponsePDU;
			}else {
				System.out.println("Erreur de PDU");
				serveur.FermerSocket();
				return 1;
			}
			// Vérification de la reponse
			if (reponse.getType().compareTo("DATA") == 0) {
				if(sysFichiers.Ecrire(fichier,(int) headerbloc.getKey(),reponse.getBloc()) != 0) {
					sysFichiers.setDisponible(fichier.getNomFichier(), (int) headerbloc.getKey(), -1);
				}
				sysFichiers.setDisponible(fichier.getNomFichier(), (int) headerbloc.getKey(), 1);
			} else if (reponse.getType().compareTo("ERR") == 0) {
				System.out.println(reponse.getDonnees());
			} else {
				System.out.println("Erreur de type de la PDU");
				return 1;
			}

		}
		return 0;
	}

}
