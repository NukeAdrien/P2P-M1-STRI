package ihm;
import javax.swing.*;
import javax.swing.border.Border;

import client.ClientControle;
import systeme.fichiers.GestionFichier;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;    

/*
 * Classe IHM : G�n�re la fen�tre JFrame destin�e aux fonctionnalit�s du projet.
 * 
 */
public class IHM extends JFrame implements ActionListener {
	
	/*
	 * D�claration des variables
	 */
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * D�claration des JButton repr�sentant les diff�rentes fonctionnalit�s
	 */
	JButton bt1 = new JButton("T�l�chargement Simple");
	JButton bt2 = new JButton("T�l�chargement Parall�le");
	JButton bt3 = new JButton("T�l�chargement P2P");
	JButton bt4 = new JButton("Gestion de Fichiers");

	ClientControle controle;
	
	/* Cr�ation d'une bordure */
	Border border = BorderFactory.createLineBorder(Color.BLACK);

	/* Cr�ation des JMenu et JMenuItem */
	JMenu item1,item4;
	JMenuItem item2,item3,item5,item6,item7;

	/* Declaration et initialisation d'une JTextArea */
	JTextArea jta = new JTextArea();

	/* Declaration et initialisation de la JFrame */
	JFrame go = new JFrame("P2P-M1-STRI");
	/* D�claration et initialisation d'un panel. C'est un composant de type conteneur dont
	 *  la vocation est d'accueillir d'autres objets de m�me type ou des objets de type composant (boutons, cases � cocher�). 
	 */
	JPanel panel = new JPanel();
	/* D�claration et initialisation d'un Layout */
	GridBagLayout Grid = new GridBagLayout();
	JLabel Title = new JLabel("P2P-M1-STRI", SwingConstants.CENTER);


	/*
	 * Constructeur Uml : G�n�re la fen�tre et les actions (ActionListener) associ�es
	 */
	public IHM(GestionFichier f) {
		/*On ajoute au panel un nouveau Layout*/
		panel.setLayout(Grid);

		Title.setFont(new Font("Serif", Font.BOLD, 60));

		@SuppressWarnings("unused")
		JButton button;
		
		/*Pour chaque �l�ment � ajouter on le place gr�ce au param�tre "de positionnement" du GridBagLayout en fonction de l'abscisse x et y l'ordonn�e*/
		panel.setLayout(new GridBagLayout());
		/* D�claration et initialisation d'un Layout */
		GridBagConstraints c = new GridBagConstraints();
		/* Initialisation des param�tres de ce Layout. Le but est d'ajouter au panel, chaque composant (JTexteArea ou JLabel) ainsi que
		 * chaque param�tre "de positionnement" c d�crit ci-dessous:
	     */
		GridBagConstraints constraints1=new GridBagConstraints();
		constraints1.insets.top=7;
		constraints1.insets.bottom=7;
		/* Initialisation des param�tres de ce Layout. Le but est d'ajouter au panel, chaque composant (JTexteArea ou JLabel) ainsi que
		 * chaque param�tre "de positionnement" constraints1 d�crit ci-dessous:
	     */
		
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
		controle = new ClientControle("TCP",f);
		bt1.addActionListener(this);

		c.ipady = 0;
		c.weighty = 1.0;
		c.insets = new Insets(2, 0, 0, 0); 
		c.gridx = 1; 
		c.gridwidth = 1; 
		c.gridy = 2;
		panel.add(bt2, c);

		/* On modifie la bordure des JButton*/
		bt2.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		/* Bouton permettant d'ex�cuter une action gr�ce � l'int�raction avec l'utilisateur*/
		bt2.addActionListener(this);

		constraints1.anchor=GridBagConstraints.SOUTH;
		constraints1.insets = new Insets(0, 10, 6, 0);
		constraints1.gridx=2;
		constraints1.gridy=2;
		panel.add(jta,constraints1);

		/* On modifie la bordure des JTextArea*/
		jta.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		/* On modifie la taille des JTextArea*/
		jta.setPreferredSize(new Dimension(20,22));

		c.ipady = 0;
		c.weighty = 1.0;
		c.insets = new Insets(2, 0, 0, 0); 
		c.gridx = 1; 
		c.gridwidth = 1; 
		c.gridy = 3;
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
		c.gridy = 4;
		panel.add(bt4, c);

		/* On modifie la bordure des JButton*/
		bt4.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		/* Boutton permettant d'ex�cuter une action contenue dans les JMenu's gr�ce � l'int�raction avec l'utilisateur*/
		bt4.addActionListener(this);
		/* On ajoute le panel ainsi cr��e */
		go.add(panel);
		setLocationRelativeTo(null);
		/* D�s que le quitte le programme (croix rouge) le programme s'arr�te */
		go.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			@SuppressWarnings("unused")
			/* On cr�e l'objet TelechargementSimpleUml */

			TelechargementSimpleIHM tsu = new TelechargementSimpleIHM(this.controle);
		}
		/* Si l'utilisateur a cliqu� sur le bouton bt2 */
		if(arg0.getSource() == bt2) {
			/* On r�cup�re le num�ro de champ que l'utilisateur a choisi */
			String gjt = jta.getText();
			/* Si le champ n'est pas vide */
			if(gjt.length()!=0) {
				/* On parse le String en entier */
				Integer nb = Integer.parseInt(gjt);
				/* On cr�e un objet TelechargementParalleleUml */
				TelechargementParalleleIHM tpu = new TelechargementParalleleIHM(this.controle);
				/* On lance la m�thode GenererTrame avec comme param�tre le num�ro de champ
				 * (qui correspond � une paire de JTextArea (IP) et JTextArea (Port)) que l'utilisateur a saisi
				 */
				tpu.GenererTrame(nb);
			}else {
				/* Sinon on affiche un message d'erreur */
				javax.swing.JOptionPane.showMessageDialog(panel, "Le champ est vide", "Error", JOptionPane.ERROR_MESSAGE); 

			}		
		}
		
		/* Si l'utilisateur a cliqu� sur le bouton bt3 */
		if(arg0.getSource() == bt3) {
			/* Lancement de la fen�tre UmlP2P */
			@SuppressWarnings("unused")
			IHMP2P umlp2p = new IHMP2P();
		}
		/* Si l'utilisateur a cliqu� sur le bouton bt4 */
		if(arg0.getSource() == bt4) {
			/* Lancement de la fen�tre UmlGF */
			@SuppressWarnings("unused")
			IHMGF umgf = new IHMGF(this.controle);
			
		}
	}
} 