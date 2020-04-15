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
 * Classe ClientControleThread --> Permet de cr�er un ClientControleThread
 */

public class ClientControleThread implements Runnable {
	/* D�claration de variables */
	String transport;
	GestionFichier sysFichiers;
	String nomFichier;
	String ip;
	int port;
	int main;

	/*
	 * Constructeur ClientControleThread --> Ce constructeur prend en param�tre le
	 * protocole de transport choisi un GestionFichier, le nom du fichier,
	 * l'adresse ip et le port Ce constructeur permet de cr�er un nouveau
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
	
	/*
	 * Constructeur ClientControleThread --> Ce constructeur prend en param�tre le
	 * protocole de transport choisi un GestionFichier, le nom du fichier,
	 * l'adresse, le port, ainsi que le main. Ce constructeur permet de cr�er un nouveau
	 * ClientControleThread.
	 */
	
	public ClientControleThread(String t, GestionFichier g, String i, int p, String nf, int m) {
		this.transport = t;
		this.sysFichiers = g;
		this.nomFichier = nf;
		this.ip = i;
		this.port = p;
		main = m;
	}

	/* M�thode run : m�thode d'ex�cution du thread */

	public void run() {
		/* D�claration de variables */
		Fichier fichierDl;
		int nbBlocIndisp = 0;
		// Cr�e un objet PDU pour l'envoyer au serveur
		PDUControle requete = new PDUControle("CTRL", "TPF", nomFichier, null);
		// Cr�e le socket en indiquant le mode de transport (TCP ou UDP)
		SocketClient serveur = new SocketClient(transport);
		/*
		 * Si il y a un probl�me avec l'initialisation du socket, l'adresse IP et le
		 * port du destinataire
		 */
		if (serveur.InitialisationSocket(ip, port) != 0) {
			/* Affichage d'un message d'erreur */
			System.out.println("Impossible de joindre le serveur");
			return;
		}
		/* Si il y un probl�me avec l'envoi de la PDU au serveur */
		if (serveur.EnvoiePDU(requete) != 0) {
			/* On ferme le socket */
			serveur.FermerSocket();
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur lors de l'envoi de la requ�te");
			return;
		}
		/* On initialise la variable */
		PDU reponse = null;
		/* On r�cup�re la PDU recu */
		reponse = serveur.RecevoirPDU();
		/* Si la PDU n'est pas nulle */
		if (reponse == null) {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de connexion avec le serveur " + this.ip + ":" + this.port);
			return;
			/* Si la r�ponse est une instance de PDU contr�le */
		} else if (reponse instanceof PDUControle) {
			/* On r�cup�re la PDUControle */
			requete = (PDUControle) reponse;
		} else {
			/* Affichage d'un message d'erreur */
			System.out.println("Erreur de PDU");
			/* On ferme la socket */
			serveur.FermerSocket();
			return;
		}
		/* V�rification de la r�ponse */
		/* Si la commande correspond � celle de TPF */

		if (requete.getCommande().compareTo("TPF") == 0) {
			/* Si le fichier existe */
			if (requete.getFichier() != null) {
				/* On r�cup�re le fichier */
				fichierDl = requete.getFichier();
				/* Si le fichier n'existe pas dans GestionFichier */
				if (sysFichiers.RechercheFichier(nomFichier) == null) {
					/* On ajoute le fichier dans GestionFichier */
					sysFichiers.AjouterFichier(fichierDl);
				} 
				/* On cr�e une liste d'headers blocs */
				HashMap<Integer, HeaderBloc> listHeaderBlocs = new HashMap<Integer, HeaderBloc>();
				/* On parcourt les headers blocs */
				for (Map.Entry<Integer, HeaderBloc> headerbloc : sysFichiers.getListFichier().get(nomFichier)
						.getListHeaderBlocs().entrySet()) {
					/*Si le fichier est disponible */
					if (sysFichiers.getDisponible(fichierDl.getNomFichier(), headerbloc.getKey()) == -1
							&& fichierDl.getListHeaderBlocs().get(headerbloc.getKey()).getDisponible() == 1) {
						/* Si le bloc n'est pas r�serv� */
						if (sysFichiers.setReserver(fichierDl.getNomFichier(), headerbloc.getKey()) == 0) {
							/*On ajoute le bloc dans la HashMap*/
							listHeaderBlocs.put(headerbloc.getKey(), headerbloc.getValue());
							try {
								Thread.sleep(20);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else {
							System.out.println(ip+":"+port+"je n'ai pas reussi a r�server "+headerbloc.getKey());
						}
					} else {
						/* Si le bloc n'est pas disponible, on incr�mente le nombre de blocs indisponible*/
						if (fichierDl.getDisponible(headerbloc.getKey()) == -1) {
							nbBlocIndisp++;
						}
					}
				}
				/* Si il n'y a pas de blocs */
				if (listHeaderBlocs.size() == 0) {
					if (nbBlocIndisp == sysFichiers.getListFichier().get(fichierDl.getNomFichier()).getListHeaderBlocs()
							.size()) {
						/* Affichage d'un message d'erreur */
						System.out.println(ip + ":" + port + " Ne poss�de aucun bloc du fichier");
					}
					/* Fermeture du socket */
					serveur.FermerSocket();
					return;
				}
				/* Cr�ation d'un objet ClientDonnees */
				ClientDonnees transfert = new ClientDonnees(sysFichiers, serveur);
				/* T�l�chargement du fichier */
				System.out.println("D�but du t�l�chargement des blocs pour" + ip + ":" + port);
				transfert.Dowload(sysFichiers.getListFichier().get(fichierDl.getNomFichier()), listHeaderBlocs);
				System.out.println(ip + ":" + port + " | J'ai t�l�charg� : " + listHeaderBlocs.size() + "/"
						+ sysFichiers.getListFichier().get(fichierDl.getNomFichier()).getListHeaderBlocs().size());
				/* On ferme le socket */
				serveur.FermerSocket();
				if(this.main == 1) {
					while(sysFichiers.EtatFichier(fichierDl.getNomFichier())!= 1){
						
					}
				}
			/* Si le fichier n'existe pas */
			} else if (requete.getFichier() == null) {
				/* On affiche les donn�es du fichier */
				System.out.println(requete.getDonnees());
				/* On ferme le socket */
				serveur.FermerSocket();
			} else {
				/* Affichage d'un message d'erreur */
				System.out.println("Erreur de commande");
				/* On ferme le socket */
				serveur.FermerSocket();
			}

			return;
		}
		
	}
}
