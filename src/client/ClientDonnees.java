package client;

import java.util.List;

import envoie.reception.PDU;
import socket.SocketClient;
import systeme.fichiers.Bloc;
import systeme.fichiers.Fichier;
import systeme.fichiers.GestionFichier;

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
	
	public Integer Dowload(String commande,Fichier fichier, List<Bloc> listBlocs) {
		Integer i;
		if(listBlocs == null) {
			listBlocs = fichier.getListBlocs();
		}
		for(i =0; i < listBlocs.size(); i++) {
			PDU requete = new PDU ("DATA",commande,listBlocs.get(i).getIndex().toString(),fichier);
			// Envoie de la PDU au serveur
			if(serveur.EnvoiePDU(requete) != 0) {
				serveur.FermerSocket();
				System.out.println("Erreur lors de l'envoie de la requete");
				return 1;
			}
			PDU reponse = null;
			// Recupere la PDU recu
			reponse = serveur.RecevoirPDU();
			// Test si la PDU n'est pas null
			if (reponse == null) {
				System.out.println("Erreur de connexion avec le serveur");
				return 1;
			}
			// Vérification de la reponse
			if (reponse.getCommande().compareTo("TSF") == 0) {
				if (reponse.getFichier() != null) {
					sysFichiers.Ecrire(fichier,listBlocs.get(i).getIndex(),reponse.getFichier().getListBlocs().get(listBlocs.get(i).getIndex()).getDonnees());
					
				} else if (reponse.getFichier() == null) {
					System.out.println(reponse.getDonnees());
				} else {
					System.out.println("Erreur de type de la PDU");
					return 1;
				}
			}
		}
		return 0;
	}
	
}
