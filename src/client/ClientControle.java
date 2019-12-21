package client;

import envoie.reception.PDU;

public class ClientControle {
	String transport;
	
	public ClientControle (String t) {
		transport = t;
	}
	
	public void SimpleTelechargement(String nomFichier, String ip){
		PDU simpleTel = new PDU("REQ","TSF",nomFichier);
		SocketClient serveur = new SocketClient(transport);
		if(serveur.InitialisationSocket("172.0.0.1", 4444) != 0) {
			System.out.println("Impossible de joindre le serveur");
			return;
		}
		serveur.EnvoiePDU(simpleTel);
		simpleTel = null;
		simpleTel = serveur.RecevoirPDU();
		if(simpleTel == null) {
			System.out.println("Erreur de connexion avec le serveur");
			return;
		}
		if(simpleTel.getType() == "RES") {
			ClientDonnees transfert = new ClientDonnees(serveur);
		}else if(simpleTel.getType() == "ERR") {
			System.out.println(simpleTel.getDonnees());
		}else {
			System.out.println("Erreur de type de la PDU");
			return;
		}
		
	}

}
