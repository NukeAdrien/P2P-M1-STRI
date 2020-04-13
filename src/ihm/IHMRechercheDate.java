package ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import systeme.fichiers.GestionFichier;

/*
 * Classe IHMRechercheDate : G�n�re la fen�tre JFrame permettant la recherche par date
 * 
 */

public class IHMRechercheDate implements ActionListener {
	/*
	 * D�claration des variables
	 */
	/* Declaration et initialisation de la JFrame */
	
	JFrame go1 = new JFrame("P2P-M1-STRI");
	/* D�claration et initialisation d'un panel. C'est un composant de type conteneur dont
	 *  la vocation est d'accueillir d'autres objets de m�me type ou des objets de type composant (boutons, cases � cocher�). 
	 */
	JPanel panel = new JPanel();
	/* D�claration et initialisation d'un Layout */
	GridBagLayout Grid = new GridBagLayout();
	JLabel Title = new JLabel("P2P-M1-STRI", SwingConstants.CENTER);

	/* D�claration et initialisation des JLabel et JTextArea */
	JLabel anotherLabel1 = new JLabel("Date :", SwingConstants.CENTER);
	static JTextArea textArea1 = new JTextArea();
	JButton button;

	
	/*
	 * Constructeur IhmRechercheDate : G�n�re la fen�tre et les actions (ActionListener) associ�es
	 */
	
	public IHMRechercheDate() {
		/* Cr�ation d'une bordure */
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		/*On ajoute au panel un nouveau Layout*/
		panel.setLayout(Grid);
		Title.setFont(new Font("Serif", Font.BOLD, 60));
		
		/*Pour chaque �l�ment � ajouter on le place gr�ce au param�tre "de positionnement" du GridBagLayout en fonction de l'abscisse x et y l'ordonn�e*/
		panel.setLayout(new GridBagLayout());
		/* Initialisation des param�tres de ce Layout. Le but est d'ajouter au panel, chaque composant (JTexteArea ou JLabel) ainsi que
		 * chaque param�tre "de positionnement" c d�crit ci-dessous:
	     */
		GridBagConstraints c = new GridBagConstraints();
		c.insets.top=7;
		c.insets.bottom=7;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.ipady = 40; //Augmente la hauteur du titre
		c.weightx = 0.5;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(Title, c);

		c.ipady = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		panel.add(anotherLabel1, c);

		c.ipady = 0;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(textArea1, c);

		/* On modifie la bordure du JTextArea*/
		textArea1.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		/* Cr�ation du JButton permettant la recherche en fonction de ce que l'utilisateur a saisi */
		button = new JButton("Rechercher");
		c.ipady = 0;
		c.weighty = 1.0;
		c.insets = new Insets(10, 0, 0, 0); 
		c.gridx = 1; 
		c.gridwidth = 2; 
		c.gridy = 4;
		panel.add(button, c);

		/* On modifie la bordure du JButton*/
		button.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		/* Bouton permettant d'ex�cuter une action gr�ce � l'int�raction avec l'utilisateur*/
		button.addActionListener(this);

		/* On ajoute le panel ainsi cr��e */
		go1.add(panel);
		/* On rend impossible la modification de la taille de la JFrame*/
		go1.setResizable(false);
		/* Dimensionne le cadre de sorte que tout son contenu soit au niveau ou au-dessus de ses preferredSize */
		go1.pack();
		/* On fixe une taille � la JFrame*/
		go1.setSize(750, 300);
		/* On rend visible la fen�tre */
		go1.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/* Si l'utilisateur clique sur le bouton */
		if (e.getSource() == button) {
			/* On v�rifie que le champ ne soit pas vide */
			if(textArea1.getText().length()!=0) {
				/*On cr�e un objet de type GestionFichier*/
				GestionFichier gf = new GestionFichier(PreIHM.getJTextField1()+PreIHM.getJTextField2());
				/*On initialise le Gestion de Fichier */
				gf.initGestionFichier();
				/* On d�clare et initialise une ArrayList qui permettre d'accueillir les noms des fichiers */
				ArrayList<String> al = new ArrayList<String>();
				/* On effectuer la recherche */
				al = gf.rechercheDateFichierIHM();
				/* On affiche le r�sultat */
				javax.swing.JOptionPane.showMessageDialog(null, al); 
			}else {
				/* Si le champ est vide on affiche un message d'erreur */
				javax.swing.JOptionPane.showMessageDialog(panel, "Le champ est vide", "Error", JOptionPane.ERROR_MESSAGE); 

			}

		}
	}
	
	/*
	 * Methode getJTextField1() : Permet de r�cuperer le champ du JTextArea
	 * @return : Le texte contenu dans le JTextArea
	 */
	public static String getJTextField2() {
		return  textArea1.getText();
	}


}
