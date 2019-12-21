package serveur;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class GererClient implements Runnable {
	Socket sockClient = null;
	GestionProtocole gestion;

	public GererClient(Socket socket, GestionProtocole g) {
		sockClient = socket;
		gestion = g;
	}

	@Override
	public void run() {
		String reponse;
		System.out.println("Reception du client");
		String requete = null;
		try{
			 // Instancie un BufferedReader travaillant sur un InputStreamReader li� �
			 // l�input stream de la socket
			 BufferedReader reader = new BufferedReader (
			 new InputStreamReader(sockClient.getInputStream()));

			 // Lit une ligne de caract�res depuix le flux, et donc la re�oit du client
			 requete = reader.readLine();
			}
			catch(IOException ioe) {
			 System.out.println("Erreur de lecture : " + ioe.getMessage());
			}
		System.out.println(requete);
		reponse = gestion.gestion(requete);

		try {
			// Instancie un PrintStream travaillant sur l�output stream de la socket
			PrintStream pStream = new PrintStream(sockClient.getOutputStream());

			// �crit une ligne de caract�res sur le flux, et donc l�envoie au client
			pStream.println(reponse);
		} catch (IOException ioe) {
			System.out.println("Erreur d��criture : " + ioe.getMessage());
		}

	}
}
