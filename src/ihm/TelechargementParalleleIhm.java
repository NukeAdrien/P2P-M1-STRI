package ihm;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import client.ClientControle;
import systeme.fichiers.GestionFichier;

/*
 * Classe TelechargementParalleleIHM : Génère la fenêtre JFrame destinée au TéléchargementParallèle.
 * 
 */

public class TelechargementParalleleIHM extends JFrame implements ActionListener {
	
	/*
	 * Déclaration des variables
	 */
	
	private static final long serialVersionUID = 1L;
	
	/* Declaration et initialisation de la JFrame */
	JFrame f=new JFrame();   
	
	/*
	 * Déclaration du JButton nécessaire pour le téléchargement
	 */
	JButton j4 = new JButton("Télécharger ");
	JTextArea jta = new JTextArea();
	int nbre;

	/* Declaration et initialisation des JLabel et JTextArea*/
	List<JLabel> listLabel = new ArrayList<JLabel>();
	List<JTextArea> listTextArea = new ArrayList<JTextArea>();
	List<String> listString = new ArrayList<String>();
	List<Integer> li = new ArrayList<Integer>();
	ClientControle cc = null;


	/*
	 * Constructeur TelechargementParalleleUml : Génère la fenêtre et les actions (ActionListener) associées
	 */
	
	public TelechargementParalleleIHM(ClientControle cc) {
		this.cc=cc;
		@SuppressWarnings("unused")
		/*Déclaration d'une bordure pour les contours des champs modifiables*/
		Border border = BorderFactory.createLineBorder(Color.BLACK);

		/* On fixe une taille à la JFrame*/
		f.setSize(600,600);
		/* On rend impossible la modification de la taille de la JFrame*/
		f.setResizable(false);
		/* Dimensionne le cadre de sorte que tout son contenu soit au niveau ou au-dessus de ses preferredSize */
		this.pack();
		/* On rend visible la fenêtre */
		f.setVisible(true);  
	}


	/*
	 * Methode GenererTrame(): Permet de generer le nombre de JTextArea et de JTextLabel nécessaire pour le téléchargement sur plusieurs serveurs
	 * @param : le nombre de JTextArea/JLabel qu'il souhaite entrer
	 */
	public void GenererTrame (int nb) {

		/* Declaration et initialisation d'un JLabel pour le nom  du fichier */
		JLabel j = new JLabel("Entrez le nom du fichier à télécharger : ",SwingConstants.CENTER);
		/* Declaration et initialisation d'un JTextArea pour le nom  du fichier */
		jta = new JTextArea();
		
		/*Déclaration d'une bordure pour les contours des champs modifiables*/
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		
		/* Création d'un container*/
		Container cont=f.getContentPane();
		/*On ajoute au container un Layout*/
		cont.setLayout(new GridBagLayout());

		/* Déclaration et initialisation d'un Layout */
		GridBagConstraints constraints=new GridBagConstraints();
		/* Initialisation des paramètres de ce Layout. Le but est d'ajouter au panel, chaque composant (JTexteArea ou JLabel) ainsi que
		 * chaque paramètre "de positionnement" constraints décrit ci-dessous:
	     */
		constraints.insets.top=7;
		constraints.insets.bottom=7;
		constraints.fill = GridBagConstraints.HORIZONTAL;


		GridBagConstraints constraints1=new GridBagConstraints();
		constraints1.insets.top=7;
		constraints1.insets.bottom=7;
		/*
		 * Pour chaque JLabel/JTextArea entré on met en place les composants
		 */
		for (int i=0; i<nb*2; i++) {
			listLabel.add(new JLabel("label"+i));
			constraints.anchor=GridBagConstraints.SOUTH;
			constraints.gridx=0;
			constraints.gridy=i;
			cont.add(listLabel.get(i),constraints);

			listTextArea.add(new JTextArea());
			constraints1.anchor=GridBagConstraints.SOUTH;
			constraints1.weightx = 0.2;
			constraints1.gridx=1;
			constraints1.gridy=i;
			cont.add(listTextArea.get(i),constraints1);

			/* On modifie la bordure des JTextArea*/
			listTextArea.get(i).setBorder(BorderFactory.createCompoundBorder(border,
					BorderFactory.createEmptyBorder(2, 2, 2, 2)));

			/* On modifie la taille des JTextArea*/
			listTextArea.get(i).setPreferredSize(new Dimension(200,20));

			/* 
			 * Une fois les JLabel/JTextArea placés on met en place le premier
			 * JTextArea/JLabel (nécessaire pour le nom du fichier) ainsi que je JButton pour télécharger
			 * 
			 */

			if (i==nb*2-1) {
				constraints.anchor=GridBagConstraints.SOUTH;
				constraints.gridx=0;
				constraints.gridy=-nb*2+2;
				cont.add(j,constraints);


				constraints1.anchor=GridBagConstraints.SOUTH;
				constraints1.weightx = 0.2;
				constraints1.gridx=1;
				constraints1.gridy=-nb*2+2;
				cont.add(jta,constraints1);

				/* On modifie la bordure des JTextArea*/
				jta.setBorder(BorderFactory.createCompoundBorder(border,
						BorderFactory.createEmptyBorder(2, 2, 2, 2)));

				/* On modifie la taille des JTextArea*/
				jta.setPreferredSize(new Dimension(200,20));

				constraints.anchor=GridBagConstraints.SOUTH;
				constraints.gridx=0;
				constraints.gridy=nb*2+20;
				cont.add(j4,constraints);

				j4.setBorder(BorderFactory.createCompoundBorder(border,
						BorderFactory.createEmptyBorder(2, 2, 2, 2)));
			}

		}
		/* 
		 * Pour chaque JLabel, on modifie le texte associé
		 */
		for(int i=0; i<nb*2; i=i+2) {
			listLabel.get(i).setText("Entrez l'adresse IP : ");
			nbre++;

		}

		/* 
		 * Pour chaque JTextArea, on modifie le texte associé
		 */
		for(int i=1; i<nb*2; i=i+2 ) {
			listLabel.get(i).setText("Entrez le port : ");
			nbre++;
		}
		/* Boutton permettant d'exécuter une action grâce à l'intéraction avec l'utilisateur*/
		j4.addActionListener(this);
		nbre++;
	}	


	public void actionPerformed(ActionEvent e) {
		boolean ok = true;
		/* On verifie que tous les champs sont bien formatés */
		for(int i=0; i<listTextArea.size(); i=i+2) {
			/*On parcourt tous les JTextArea et on vérifie que l'adresse IP est bien formaté*/
			if(! listTextArea.get(i).getText().matches("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
				/* Affichage d'un message d'erreur si le champ n'est pas bien formaté */
				javax.swing.JOptionPane.showMessageDialog(null, "L'(Les) adresse(s) IP n'est (ne sont) pas valide(s)", "Error", JOptionPane.ERROR_MESSAGE); 
				ok = false;
				break;
			}
		}
		
		for(int i=0; i<listTextArea.size(); i=i+1) {
			/*On parcourt tous les JTextArea (pour les adresses IP) et on vérifie que les champs ne sont pas vides*/
			if(listTextArea.get(i).getText().length()==0) {
				/* Affichage d'un message d'erreur si le champ est vide */
				javax.swing.JOptionPane.showMessageDialog(null, "Un ou plusieurs champs relatifs à l'adresse IP sont vides", "Error", JOptionPane.ERROR_MESSAGE); 
				ok = false;
				break;
			}
		}

		for(int i=1; i<listTextArea.size(); i=i+2) {
			/*On parcourt tous les JTextArea et on vérifie que le port est bien formaté*/
			if(! listTextArea.get(i).getText().matches("[0-9]*")) {
				/* Affichage d'un message d'erreur si le champ n'est pas bien formaté */
				javax.swing.JOptionPane.showMessageDialog(null, "Le (Les) port(s) n'est (ne sont) pas valide(s)", "Error", JOptionPane.ERROR_MESSAGE); 
				ok = false;
				break;
			}
		}
		
		for(int i=1; i<listTextArea.size(); i=i+2) {
			/*On parcourt tous les JTextArea (pour les ports) et on vérifie que les champs ne sont pas vides*/
			if(listTextArea.get(i).getText().length()==0) {
				/* Affichage d'un message d'erreur si le champ est vide */
				javax.swing.JOptionPane.showMessageDialog(null, "Un ou plusieurs champs relatifs au port sont vides", "Error", JOptionPane.ERROR_MESSAGE); 
				ok = false;
				break;
			}
		}

		/* On récupère le texte du nom de fichier */
		String str=jta.getText();
		/* Si le champ est vide */
		if (str.length()==0) {
			/* Affichage d'un message d'erreur */
			javax.swing.JOptionPane.showMessageDialog(null, "Le champ relatif au nom du fichier est vide", "Error", JOptionPane.ERROR_MESSAGE); 
			ok = false;
		}

		/* Si tout est ok */
		if (ok == true) {
			/* On ajoute dans les différents ArrayList les champs associés */
			for(int i=0; i<listTextArea.size(); i=i+2 ) {
				listString.add(listTextArea.get(i).getText());
			}
			for(int i=1; i<listTextArea.size(); i=i+2) {
				li.add(Integer.parseInt(listTextArea.get(i).getText()));
			}

			/* On lance le Téléchargement Parallèle*/
			cc.TelechargementParallele(str, listString, li);
		}
	}
}








