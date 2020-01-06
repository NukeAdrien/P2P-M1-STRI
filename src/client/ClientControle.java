package client;

import envoie.reception.PDU;

public class ClientControle {
	String transport;

	public ClientControle(String t) {
		transport = t;
	}

	// Permet de réaliser un simple téléchargement
	public void SimpleTelechargement(String nomFichier, String ip) {
		// Cree un objet PDU pour l'envoyer au serveur
		PDU simpleTel = new PDU("CTRL", "TSF", nomFichier, null);
		// Cree le socket en indiquant le mode de transport (TCP ou UDP)
		SocketClient serveur = new SocketClient(transport);
		// Initialise le socket avec l'adresse IP et le port du destinataire
		if (serveur.InitialisationSocket(ip, 4444) != 0) {
			System.out.println("Impossible de joindre le serveur");
			return;
		}
		// Envoie de la PDU au serveur
		serveur.EnvoiePDU(simpleTel);
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
		if (simpleTel.getCommande() == "TSF") {
			if (simpleTel.getFichier() != null) {
				ClientDonnees transfert = new ClientDonnees(serveur);
			} else if (simpleTel.getFichier() == null) {
				System.out.println(simpleTel.getDonnees());
			} else {
				System.out.println("Erreur de type de la PDU");
				
			}
		}
		return;
	}

}
