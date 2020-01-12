package client;

import java.util.List;

import envoie.reception.*;
import socket.SocketClient;
import systeme.fichiers.Fichier;
import systeme.fichiers.GestionFichier;

public class ClientControle {
	String transport;
	GestionFichier sysFichiers;

	public ClientControle(String t, GestionFichier g) {
		transport = t;
		sysFichiers = g;
	}

	// Permet de réaliser un simple téléchargement
	public void TelechargementSimple(String nomFichier, String ip, int port) {
		Fichier fichierDl;
		// Cree un objet PDU pour l'envoyer au serveur
		PDUControle simpleTel = new PDUControle("CTRL", "TSF", nomFichier, null);
		// Cree le socket en indiquant le mode de transport (TCP ou UDP)
		SocketClient serveur = new SocketClient(transport);
		// Initialise le socket avec l'adresse IP et le port du destinataire
		if (serveur.InitialisationSocket(ip, port) != 0) {
			System.out.println("Impossible de joindre le serveur");
			return;
		}
		// Envoie de la PDU au serveur
		if (serveur.EnvoiePDU(simpleTel) != 0) {
			serveur.FermerSocket();
			System.out.println("Erreur lors de l'envoie de la requete");
			return;
		}
		// initialise la variable
		PDU reponse = null;
		// Recupere la PDU recu
		reponse = serveur.RecevoirPDU();
		// Test si la PDU n'est pas null ou une autre PDU
		if (reponse == null) {
			System.out.println("Erreur de connexion avec le serveur");
			return;
		} else if (reponse instanceof PDUControle) {
			simpleTel = (PDUControle) reponse;
		} else {
			System.out.println("Erreur de PDU");
			serveur.FermerSocket();
			return;
		}
		// Vérification de la reponse
		if (simpleTel.getCommande().compareTo("TSF") == 0) {
			if (simpleTel.getFichier() != null) {
				fichierDl = simpleTel.getFichier();
				fichierDl.setEmplacement(sysFichiers.getChemin() + fichierDl.getNomFichier());
				sysFichiers.AjouterFichier(simpleTel.getFichier());
				ClientDonnees transfert = new ClientDonnees(sysFichiers, serveur);
				transfert.Dowload(fichierDl, null);
				serveur.FermerSocket();
			} else if (simpleTel.getFichier() == null) {
				System.out.println(simpleTel.getDonnees());
			} else {
				System.out.println("Erreur de commande");
			}
		}
		return;
	}

	public void TelechargementParallele(String nomFichier, List<String> ip, List<Integer> port) {
		int i;
		for (i = 0; i > ip.size(); i++) {
			ClientControleThread cct = new ClientControleThread(this.transport, this.sysFichiers, ip.get(i),
					port.get(i), nomFichier);
			Thread thread = new Thread(cct);
			thread.start();
		}
		i=0;
		while(sysFichiers.EtatFichier(nomFichier) != 1 && i != ip.size()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		if(sysFichiers.EtatFichier(nomFichier) != 1) {
			System.out.println("Erreur lors du téléchargement du fichier");
		}
		return;
	}

}
