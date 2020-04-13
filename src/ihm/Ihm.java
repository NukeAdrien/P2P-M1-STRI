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
 * Classe IHM : Génère la fenêtre JFrame destinée aux fonctionnalités du projet.
 * 
 */
public class IHM extends JFrame implements ActionListener {
	
	/*
	 * Déclaration des variables
	 */
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * Déclaration des JButton représentant les différentes fonctionnalités
	 */
	JButton bt1 = new JButton("Téléchargement Simple");
	JButton bt2 = new JButton("Téléchargement Parallèle");
	JButton bt3 = new JButton("Téléchargement P2P");
	JButton bt4 = new JButton("Gestion de Fichiers");

	ClientControle controle;
	
	/* Création d'une bordure */
	Border border = BorderFactory.createLineBorder(Color.BLACK);

	/* Création des JMenu et JMenuItem */
	JMenu item1,item4;
	JMenuItem item2,item3,item5,item6,item7;

	/* Declaration et initialisation d'une JTextArea */
	JTextArea jta = new JTextArea();

	/* Declaration et initialisation de la JFrame */
	JFrame go = new JFrame("P2P-M1-STRI");
	/* Déclaration et initialisation d'un panel. C'est un composant de type conteneur dont
	 *  la vocation est d'accueillir d'autres objets de même type ou des objets de type composant (boutons, cases à cocher…). 
	 */
	JPanel panel = new JPanel();
	/* Déclaration et initialisation d'un Layout */
	GridBagLayout Grid = new GridBagLayout();
	JLabel Title = new JLabel("P2P-M1-STRI", SwingConstants.CENTER);


	/*
	 * Constructeur Uml : Génère la fenêtre et les actions (ActionListener) associées
	 */
	public IHM(GestionFichier f) {
		/*On ajoute au panel un nouveau Layout*/
		panel.setLayout(Grid);

		Title.setFont(new Font("Serif", Font.BOLD, 60));

		@SuppressWarnings("unused")
		JButton button;
		
		/*Pour chaque élément à ajouter on le place grâce au paramètre "de positionnement" du GridBagLayout en fonction de l'abscisse x et y l'ordonnée*/
		panel.setLayout(new GridBagLayout());
		/* Déclaration et initialisation d'un Layout */
		GridBagConstraints c = new GridBagConstraints();
		/* Initialisation des paramètres de ce Layout. Le but est d'ajouter au panel, chaque composant (JTexteArea ou JLabel) ainsi que
		 * chaque paramètre "de positionnement" c décrit ci-dessous:
	     */
		GridBagConstraints constraints1=new GridBagConstraints();
		constraints1.insets.top=7;
		constraints1.insets.bottom=7;
		/* Initialisation des paramètres de ce Layout. Le but est d'ajouter au panel, chaque composant (JTexteArea ou JLabel) ainsi que
		 * chaque paramètre "de positionnement" constraints1 décrit ci-dessous:
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
		/* Bouton permettant d'exécuter une action grâce à l'intéraction avec l'utilisateur*/
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
		/* Bouton permettant d'exécuter une action grâce à l'intéraction avec l'utilisateur*/
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
		/* Bouton permettant d'exécuter une action grâce à l'intéraction avec l'utilisateur*/
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
		/* Boutton permettant d'exécuter une action contenue dans les JMenu's grâce à l'intéraction avec l'utilisateur*/
		bt4.addActionListener(this);
		/* On ajoute le panel ainsi créée */
		go.add(panel);
		setLocationRelativeTo(null);
		/* Dès que le quitte le programme (croix rouge) le programme s'arrête */
		go.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/* On fixe une taille à la JFrame*/
		go.setSize(750, 300);
		/* On rend impossible la modification de la taille de la JFrame*/
		go.setResizable(false);
		/* On rend visible la fenêtre */
		go.setVisible(true);
	}


	public void actionPerformed(ActionEvent arg0) {
		/* Si l'utilisateur a cliqué sur le bouton bt1 */
		if(arg0.getSource() == bt1) {
			@SuppressWarnings("unused")
			/* On crée l'objet TelechargementSimpleUml */

			TelechargementSimpleIHM tsu = new TelechargementSimpleIHM(this.controle);
		}
		/* Si l'utilisateur a cliqué sur le bouton bt2 */
		if(arg0.getSource() == bt2) {
			/* On récupère le numéro de champ que l'utilisateur a choisi */
			String gjt = jta.getText();
			/* Si le champ n'est pas vide */
			if(gjt.length()!=0) {
				/* On parse le String en entier */
				Integer nb = Integer.parseInt(gjt);
				/* On crée un objet TelechargementParalleleUml */
				TelechargementParalleleIHM tpu = new TelechargementParalleleIHM(this.controle);
				/* On lance la méthode GenererTrame avec comme paramètre le numéro de champ
				 * (qui correspond à une paire de JTextArea (IP) et JTextArea (Port)) que l'utilisateur a saisi
				 */
				tpu.GenererTrame(nb);
			}else {
				/* Sinon on affiche un message d'erreur */
				javax.swing.JOptionPane.showMessageDialog(panel, "Le champ est vide", "Error", JOptionPane.ERROR_MESSAGE); 

			}		
		}
		
		/* Si l'utilisateur a cliqué sur le bouton bt3 */
		if(arg0.getSource() == bt3) {
			/* Lancement de la fenêtre UmlP2P */
			@SuppressWarnings("unused")
			IHMP2P umlp2p = new IHMP2P();
		}
		/* Si l'utilisateur a cliqué sur le bouton bt4 */
		if(arg0.getSource() == bt4) {
			/* Lancement de la fenêtre UmlGF */
			@SuppressWarnings("unused")
			IHMGF umgf = new IHMGF(this.controle);
			
		}
	}
} 