package client;

import java.util.HashMap;
import java.util.Map;

import envoie.reception.PDU;
import envoie.reception.PDUControle;
import socket.SocketClient;
import systeme.fichiers.Fichier;
import systeme.fichiers.GestionFichier;
import systeme.fichiers.HeaderBloc;

public class ClientControleThread implements Runnable {
	String transport;
	GestionFichier sysFichiers;
	String nomFichier;
	String ip;
	int port;

	public ClientControleThread(String t, GestionFichier g, String i, int p, String nf) {
		this.transport = t;
		this.sysFichiers = g;
		this.nomFichier = nf;
		this.ip = i;
		this.port = p;
	}

	public void run() {
		Fichier fichierDl;
		int nbBlocIndisp = 0;
		// Cree un objet PDU pour l'envoyer au serveur
		PDUControle requete = new PDUControle("CTRL", "TPF", nomFichier, null);
		// Cree le socket en indiquant le mode de transport (TCP ou UDP)
		SocketClient serveur = new SocketClient(transport);
		// Initialise le socket avec l'adresse IP et le port du destinataire
		if (serveur.InitialisationSocket(ip, port) != 0) {
			System.out.println("Impossible de joindre le serveur");
			return;
		}
		// Envoie de la PDU au serveur
		if (serveur.EnvoiePDU(requete) != 0) {
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
			System.out.println("Erreur de connexion avec le serveur " + this.ip + ":" + this.port);
			return;
		} else if (reponse instanceof PDUControle) {
			requete = (PDUControle) reponse;
		} else {
			System.out.println("Erreur de PDU");
			serveur.FermerSocket();
			return;
		}
		// Vérification de la reponse
		if (requete.getCommande().compareTo("TPF") == 0) {
			if (requete.getFichier() != null) {
				fichierDl = requete.getFichier();
				if (sysFichiers.RechercheFichier(nomFichier) == null) {
					sysFichiers.AjouterFichier(fichierDl);
					sysFichiers.getListFichier().get(fichierDl.getNomFichier())
							.setEmplacement(sysFichiers.getChemin() + fichierDl.getNomFichier());
					for (Map.Entry<Integer, HeaderBloc> headerbloc : fichierDl.getListHeaderBlocs().entrySet()) {
						sysFichiers.getListFichier().get(fichierDl.getNomFichier()).setDisponible(headerbloc.getKey(),
								-1);
					}
				}
				HashMap<Integer, HeaderBloc> listHeaderBlocs = new HashMap<Integer, HeaderBloc>();
				for (Map.Entry<Integer, HeaderBloc> headerbloc : sysFichiers.getListFichier().get(nomFichier)
						.getListHeaderBlocs().entrySet()) {
					if (sysFichiers.getListFichier().get(fichierDl.getNomFichier())
							.getDisponible(headerbloc.getKey()) == -1
							&& requete.getFichier().getListHeaderBlocs().get(headerbloc.getKey())
									.getDisponible() == 1) {
						sysFichiers.getListFichier().get(nomFichier).setDisponible(headerbloc.getKey(), 0);
						listHeaderBlocs.put(headerbloc.getKey(), headerbloc.getValue());
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						if (requete.getFichier().getDisponible(headerbloc.getKey()) == -1) {
							nbBlocIndisp++;
						}
					}
				}
				if (listHeaderBlocs.size() == 0) {
					if (nbBlocIndisp == sysFichiers.getListFichier().get(fichierDl.getNomFichier()).getListHeaderBlocs()
							.size()) {
						System.out.println(ip + ":" + port + " Ne possede aucun bloc du fichier");
					}
					return;
				}
				ClientDonnees transfert = new ClientDonnees(sysFichiers, serveur);
				transfert.Dowload(fichierDl, listHeaderBlocs);
				System.out.println(ip + ":" + port+" | J'ai télécharger : "+listHeaderBlocs.size()+"/"+sysFichiers.getListFichier().get(fichierDl.getNomFichier()).getListHeaderBlocs().size());
				requete = new PDUControle("CTRL", "TSF", "FIN", null);
				serveur.EnvoiePDU(requete);
				serveur.FermerSocket();
			} else if (requete.getFichier() == null) {
				System.out.println(requete.getDonnees());
			} else {
				System.out.println("Erreur de commande");
			}

		}

	}

}
