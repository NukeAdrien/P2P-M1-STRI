package client;

import envoie.reception.PDU;
import socket.SocketClient;
import systeme.fichiers.GestionFichier;

public class ClientControle {
	String transport;
	GestionFichier sysFichiers;
	public ClientControle(String t, GestionFichier g) {
		transport = t;
		sysFichiers = g;
	}

	// Permet de réaliser un simple téléchargement
	public void SimpleTelechargement(String nomFichier, String ip) {
		// Cree un objet PDU pour l'envoyer au serveur
		PDU simpleTel = new PDU("CTRL","TSF", nomFichier, null);
		// Cree le socket en indiquant le mode de transport (TCP ou UDP)
		SocketClient serveur = new SocketClient(transport);
		// Initialise le socket avec l'adresse IP et le port du destinataire
		if (serveur.InitialisationSocket(ip, 4444) != 0) {
			System.out.println("Impossible de joindre le serveur");
			return;
		}
		// Envoie de la PDU au serveur
		if(serveur.EnvoiePDU(simpleTel) != 0) {
			serveur.FermerSocket();
			System.out.println("Erreur lors de l'envoie de la requete");
			return;
		}
		// Rénitialise la variable
		simpleTel = null;
		// Recupere la PDU recu
		simpleTel = serveur.RecevoirPDU();
		// Test si la PDU n'est pas null
		if (simpleTel == null) {
			System.out.println("Erreur de connexion avec le serveur");
			return;
		}
		// Vérification de la reponse
		if (simpleTel.getCommande().compareTo("TSF") == 0) {
			if (simpleTel.getFichier() != null) { 
				ClientDonnees transfert = new ClientDonnees(sysFichiers,serveur);
				transfert.Dowload(simpleTel.getFichier(), null);
			} else if (simpleTel.getFichier() == null) {
				System.out.println(simpleTel.getDonnees());
			} else {
				System.out.println("Erreur de type de la PDU");
			}
		}
		return;
	}

}
