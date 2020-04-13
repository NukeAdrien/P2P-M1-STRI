package client;

import java.util.HashMap;
import java.util.Map;

import envoie.reception.PDU;
import envoie.reception.PDUControle;
import socket.SocketClient;
import systeme.fichiers.Fichier;
import systeme.fichiers.GestionFichier;
import systeme.fichiers.HeaderBloc;

/*
 * Classe ClientControleThread --> Permet de créer un ClientControleThread
 */

public class ClientControleThread implements Runnable {
	/* Déclaration de variables */
	String transport;
	GestionFichier sysFichiers;
	String nomFichier;
	String ip;
	int port;
	int main;

	/*
	 * Constructeur ClientControleThread --> Ce constructeur prend en paramètre le
	 * protocole de transport choisie un GestionFichier, le nom du fichier,
	 * l'adresse ip et le port Ce constructeur permet de créer un nouveau
	 * ClientControleThread.
	 */

	public ClientControleThread(String t, GestionFichier g, String i, int p, String nf) {
		this.transport = t;
		this.sysFichiers = g;
		this.nomFichier = nf;
		this.ip = i;
		this.port = p;
		main = 0;
	}
	
	public ClientControleThread(String t, GestionFichier g, String i, int p, String nf, int m) {
		this.transport = t;
		this.sysFichiers = g;
		this.nomFichier = nf;
		this.ip = i;
		this.port = p;
		main = m;
	}

	/* Méthode run : méthode d'exécution du thread */

	public void run() {
		/* Déclaration de variables */
		Fichier fichierDl;
		int nbBlocIndisp = 0;
		// Crée un objet PDU pour l'envoyer au serveur
		PDUControle requete = new PDUControle("CTRL", "TPF", nomFichier, null);
		// Crée le socket en indiquant le mode de transport (TCP ou UDP)
		SocketClient serveur = new SocketClient(transport);
		/*
		 * Si il y a un problème avec l'initialisation du socket, l'adresse IP et le
		 * port du destinataire
		 */
		if (serveur.InitialisationSocket(ip, port) != 0) {
			/* Affichage d'un message d'erreur */
			System.out.println("Impossible de joindre le serveur");
			return;
		}
		/* Si il y un problème avec l'envoie de la PDU au serveur */
		if (serveur.EnvoiePDU(requete) != 0) {
			/* On ferme le socket */
			serveur.FermerSocket();
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur lors de l'envoi de la requête");
			return;
		}
		/* On initialise la variable */
		PDU reponse = null;
		/* On récupère la PDU recu */
		reponse = serveur.RecevoirPDU();
		/* Si la PDU n'est pas nulle */
		if (reponse == null) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de connexion avec le serveur " + this.ip + ":" + this.port);
			return;
			/* Si la réponse est une instance de PDU contrôle */
		} else if (reponse instanceof PDUControle) {
			/* On récupère la PDUContrôle */
			requete = (PDUControle) reponse;
		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de PDU");
			/* On ferme la socket */
			serveur.FermerSocket();
			return;
		}
		/* Vérification de la réponse */
		/* Si la commande correspond à celle de TPF */

		if (requete.getCommande().compareTo("TPF") == 0) {
			/* Si le fichier existe */
			if (requete.getFichier() != null) {
				System.out.println(ip+":"+port+" a le fichier");
				/* On récupère le fichier */
				fichierDl = requete.getFichier();
				/* Si le fichier n'existe pas dans GestionFichier */
				if (sysFichiers.RechercheFichier(nomFichier) == null) {
					/* On ajoute le fichier dans GestionFichier */
					System.out.println(ip+":"+port+" crée le fichier");
					sysFichiers.AjouterFichier(fichierDl);
					System.out.println(ip+":"+port+" a crée le fichier");
				} 
				System.out.println(ip+":"+port+" je ne suis pas coincé");
				/* On crée une liste d'headers blocs */
				HashMap<Integer, HeaderBloc> listHeaderBlocs = new HashMap<Integer, HeaderBloc>();
				/* On parcourt les headers blocs */
				for (Map.Entry<Integer, HeaderBloc> headerbloc : sysFichiers.getListFichier().get(nomFichier)
						.getListHeaderBlocs().entrySet()) {
					/*Si le fichier est disponible */
					if (sysFichiers.getDisponible(fichierDl.getNomFichier(), headerbloc.getKey()) == -1
							&& fichierDl.getListHeaderBlocs().get(headerbloc.getKey()).getDisponible() == 1) {
						/* Si le bloc n'est pas réservé */
						if (sysFichiers.setReserver(fichierDl.getNomFichier(), headerbloc.getKey()) == 0) {
							/*On ajoute le bloc dans la HashMap*/
							listHeaderBlocs.put(headerbloc.getKey(), headerbloc.getValue());
							System.out.println(ip+":"+port+" reserve  :" +headerbloc.getKey());
							try {
								Thread.sleep(20);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else {
							System.out.println(ip+":"+port+"je n'ai pas reussi a réserver "+headerbloc.getKey());
						}
					} else {
						/* Si le bloc n'est pas disponible, on incrémente le nombre de blocs indisponible*/
						if (fichierDl.getDisponible(headerbloc.getKey()) == -1) {
							System.out.println(ip+":"+port+" reserve  :" +headerbloc.getKey()+"ne possède pas le bloc");
							nbBlocIndisp++;
						}else {
							System.out.println(ip+":"+port+" reserve  :" +headerbloc.getKey()+"bloc déjà réservé ou dl");
						}
					}
				}
				/* Si il n'y a pas de blocs */
				if (listHeaderBlocs.size() == 0) {
					if (nbBlocIndisp == sysFichiers.getListFichier().get(fichierDl.getNomFichier()).getListHeaderBlocs()
							.size()) {
						/* Affichage d'un message d'erreur */
						System.out.println(ip + ":" + port + " Ne possède aucun bloc du fichier");
					}
					/* Fermeture du socket */
					requete = new PDUControle(null, null, "FIN", null);
					serveur.EnvoiePDU(requete);
					serveur.FermerSocket();
					return;
				}
				System.out.println("" + ip + ":" + port+" dispose de "+ listHeaderBlocs.size() + "/"
						+ sysFichiers.getListFichier().get(fichierDl.getNomFichier()).getListHeaderBlocs().size());
				/* Création d'un objet ClientDonnees */
				ClientDonnees transfert = new ClientDonnees(sysFichiers, serveur);
				/* Téléchargement du fichier */
				System.out.println("Début du téléchargement des blocs pour" + ip + ":" + port);
				transfert.Dowload(sysFichiers.getListFichier().get(fichierDl.getNomFichier()), listHeaderBlocs);
				System.out.println(ip + ":" + port + " | J'ai téléchargé : " + listHeaderBlocs.size() + "/"
						+ sysFichiers.getListFichier().get(fichierDl.getNomFichier()).getListHeaderBlocs().size());
				/* On crée la PDU de fin de téléchargement */
				requete = new PDUControle(null, null, "FIN", null);
				/* On envoie la PDU */
				serveur.EnvoiePDU(requete);
				/* On ferme le socket */
				serveur.FermerSocket();
				if(this.main == 1) {
					while(sysFichiers.EtatFichier(fichierDl.getNomFichier())!= 1){
						
					}
				}
			/* Si le fichier n'existe pas */
			} else if (requete.getFichier() == null) {
				/* On affiche les données du fichier */
				System.out.println(requete.getDonnees());
				/* On crée la PDU de fin de téléchargement */
				requete = new PDUControle(null, null, "FIN", null);
				/* On envoie la PDU */
				serveur.EnvoiePDU(requete);
				/* On ferme le socket */
				serveur.FermerSocket();
			} else {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur de commande");
				/* On crée la PDU de fin de téléchargement */
				requete = new PDUControle(null, null, "FIN", null);
				/* On envoie la PDU */
				serveur.EnvoiePDU(requete);
				/* On ferme le socket */
				serveur.FermerSocket();
			}

			return;
		}
		
	}
}
