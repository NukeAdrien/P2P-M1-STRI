package ihm;
import javax.swing.*;
import javax.swing.border.Border;

import client.ClientControle;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;    

/*
 * Classe IHMGF : G�n�re la fen�tre JFrame destin�e � la fonctionnalit� gestion de fichiers 
 * 
 */
public class IhmGF extends JFrame implements ActionListener {

	/*
	 * D�claration des variables
	 */
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * D�claration des JButton repr�sentant les diff�rentes fonctionnalit�s
	 */
	JButton bt1 = new JButton("Recherche par Nom");
	JButton bt3 = new JButton("Recherche par Date");
	JButton bt4 = new JButton("Recherche par Auteur");

	/* Cr�ation d'une bordure */
	Border border = BorderFactory.createLineBorder(Color.BLACK);


	/* Declaration et initialisation de la JFrame */
	JFrame go = new JFrame("P2P-M1-STRI");
	/* D�claration et initialisation d'un panel. C'est un composant de type conteneur dont
	 *  la vocation est d'accueillir d'autres objets de m�me type ou des objets de type composant (boutons, cases � cocher�). 
	 */
	JPanel panel = new JPanel();
	/* D�claration et initialisation d'un Layout */
	GridBagLayout Grid = new GridBagLayout();
	JLabel Title = new JLabel("P2P-M1-STRI", SwingConstants.CENTER);
	ClientControle cc;

	/*
	 * Constructeur UmlGF : G�n�re la fen�tre et les actions (ActionListener) associ�es
	 */

	public IhmGF(ClientControle cc) {
		this.cc=cc;
		/*On ajoute au panel un nouveau Layout*/
		panel.setLayout(Grid);

		Title.setFont(new Font("Serif", Font.BOLD, 60));

		@SuppressWarnings("unused")
		JButton button;
		/*Pour chaque �l�ment � ajouter on le place gr�ce au param�tre "de positionnement" du GridBagLayout en fonction de l'abscisse x et y l'ordonn�e*/
		panel.setLayout(new GridBagLayout());
		/* Initialisation des param�tres de ce Layout. Le but est d'ajouter au panel, chaque composant (JTexteArea ou JLabel) ainsi que
		 * chaque param�tre "de positionnement" c d�crit ci-dessous:
	     */
		GridBagConstraints c = new GridBagConstraints();
		/* Initialisation des param�tres de ce Layout. Le but est d'ajouter au panel, chaque composant (JTexteArea ou JLabel) ainsi que
		 * chaque param�tre "de positionnement" constraints1 d�crit ci-dessous:
	     */
		GridBagConstraints constraints1=new GridBagConstraints();
		constraints1.insets.top=7;
		constraints1.insets.bottom=7;

		c.ipady = 40; //increase height of the title
		c.weightx = 0.5;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(Title, c);
		c.fill = GridBagConstraints.HORIZONTAL;

		c.ipady = 0;
		c.weighty = 1.0;
		c.insets = new Insets(2, 0, 0, 0); 
		c.gridx = 1; 
		c.gridwidth = 1; 
		c.gridy = 1;
		panel.add(bt1, c);

		/* On modifie la bordure des JButton*/
		bt1.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		/* Bouton permettant d'ex�cuter une action gr�ce � l'int�raction avec l'utilisateur*/
		bt1.addActionListener(this);


		c.ipady = 0;
		c.weighty = 1.0;
		c.insets = new Insets(2, 0, 0, 0); 
		c.gridx = 1; 
		c.gridwidth = 1; 
		c.gridy = 2;
		panel.add(bt3, c);

		/* On modifie la bordure des JButton*/
		bt3.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		/* Bouton permettant d'ex�cuter une action gr�ce � l'int�raction avec l'utilisateur*/
		bt3.addActionListener(this);


		c.ipady = 0;
		c.weighty = 1.0;
		c.insets = new Insets(2, 0, 0, 0); 
		c.gridx = 1; 
		c.gridwidth = 1; 
		c.gridy = 3;
		panel.add(bt4, c);

		/* On modifie la bordure des JButton*/
		bt4.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		/* Bouton permettant d'ex�cuter une action gr�ce � l'int�raction avec l'utilisateur*/
		bt4.addActionListener(this);
		
		/* On ajoute le panel ainsi cr��e */
		go.add(panel);
		setLocationRelativeTo(null);
		/* On fixe une taille � la JFrame*/
		go.setSize(750, 300);
		/* On rend impossible la modification de la taille de la JFrame*/
		go.setResizable(false);
		/* On rend visible la fen�tre */
		go.setVisible(true);
	}



	public void actionPerformed(ActionEvent arg0) {
		/* Si l'utilisateur a cliqu� sur le bouton bt1 */
		if(arg0.getSource() == bt1) {
			/* Cr�ation de l'objet qui permet la recherche par nom */
			@SuppressWarnings("unused")
			IhmRechercheNom urn = new IhmRechercheNom(cc);
		}
		/* Si l'utilisateur a cliqu� sur le bouton bt3 */
		if(arg0.getSource() == bt3) {
			/* Cr�ation de l'objet qui permet la recherche par date */
			@SuppressWarnings("unused")
			IhmRechercheDate urd = new IhmRechercheDate();
		}
		/* Si l'utilisateur a cliqu� sur le bouton bt4 */
		if(arg0.getSource() == bt4) {
			/* Cr�ation de l'objet qui permet la recherche par auteur */
			@SuppressWarnings("unused")
			IhmRechercheAuteur ura = new IhmRechercheAuteur();
		}
	}
} 