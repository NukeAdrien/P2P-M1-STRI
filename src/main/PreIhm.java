package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import ihm.Ihm;
import serveur.GestionProtocole;
import serveur.ServeurControle;
import serveur.ServeurDonnees;
import socket.SocketServeurTCP;
import systeme.fichiers.GestionFichier;

/*
 * Classe PreUML : Génère la première fenêtre JFrame.
 * 
 */
public class PreIhm extends JFrame implements ActionListener {

	/*
	 * Déclaration des variables
	 */

	private static final long serialVersionUID = 1L;

	/* Declaration et initialisation de la JFrame */
	JFrame go1 = new JFrame("P2P-M1-STRI");
	/* Déclaration et initialisation d'un panel. C'est un composant de type conteneur dont
	 *  la vocation est d'accueillir d'autres objets de même type ou des objets de type composant (boutons, cases à cocher…). 
	 */
	JPanel panel = new JPanel();
	/* Déclaration et initialisation d'un Layout */
	GridBagLayout Grid = new GridBagLayout();
	JLabel Title = new JLabel("P2P-M1-STRI", SwingConstants.CENTER);

	/* Declaration et initialisation des JLabel */
	JLabel anotherLabel1 = new JLabel("Chemin du fichier :", SwingConstants.CENTER);
	JLabel anotherLabel2 = new JLabel("Port :", SwingConstants.CENTER);

	/* Declaration et initialisation des JTextArea */
	static JTextArea textArea1 = new JTextArea();
	static JTextArea textArea2 = new JTextArea();

	/*Déclaration des JButton*/
	JButton button;

	/* Génération de la fenêtre */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new PreIhm();
			}
		});
	}

	/*
	 * Constructeur PreUml : Génère la fenêtre et les actions (ActionListener) associées
	 */
	public PreIhm() {

		/*Déclaration d'une bordure pour les contours des champs modifiables*/
		Border border = BorderFactory.createLineBorder(Color.BLACK);

		/*On ajoute au panel un nouveau Layout*/
		panel.setLayout(Grid);

		Title.setFont(new Font("Serif", Font.BOLD, 60));

		/*Pour chaque élément à ajouter on le place grâce au paramètre "de positionnement" du GridBagLayout en fonction de l'abscisse x et y l'ordonnée*/
		panel.setLayout(new GridBagLayout());
		/* Déclaration et initialisation d'un Layout */
		GridBagConstraints c = new GridBagConstraints();
		/* Initialisation des paramètres de ce Layout. Le but est d'ajouter au panel, chaque composant (JTexteArea ou JLabel) ainsi que
		 * chaque paramètre "de positionnement" c décrit ci-dessous:
		 */
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

		/* On modifie la bordure des JTextArea*/
		textArea1.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));

		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 3;
		panel.add(anotherLabel2, c);

		c.ipady = 0;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1; 
		c.weightx=1;

		panel.add(textArea2, c);

		/* On modifie la bordure des JTextArea*/
		textArea2.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));

		button = new JButton("Valider");
		c.ipady = 0;
		c.weighty = 1.0;
		c.insets = new Insets(10, 0, 0, 0); 
		c.gridx = 1; 
		c.gridwidth = 2; 
		c.gridy = 4;
		panel.add(button, c);

		/* On modifie la bordure des JButton*/
		button.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));

		/* Boutton permettant d'exécuter une action grâce à l'intéraction avec l'utilisateur*/
		button.addActionListener(this);


		/* Ajout du panel à la JFrame */
		go1.add(panel);
		/* On rend impossible la modification de la taille de la JFrame*/
		go1.setResizable(false);
		/* Dès que le quitte le programme (croix rouge) le programme s'arrête */
		go1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/* Dimensionne le cadre de sorte que tout son contenu soit au niveau ou au-dessus de ses preferredSize */
		go1.pack();
		/* On fixe une taille à la JFrame*/
		go1.setSize(750, 300);
		/* On rend visible la fenêtre */
		go1.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/* Si l'utilisateur clique sur le boutton */
		if (e.getSource() == button) {
			/* On recupère le texte des champs */
			String st = textArea1.getText();
			String sta = textArea2.getText();

			/* Si le port n'est pas constitué de chiffres */
			if (! sta.matches("[0-9]*")) {
				/* Affichage d'un message d'erreur*/
				javax.swing.JOptionPane.showMessageDialog(panel, "Le port doit être constitué de chiffres", "Error", JOptionPane.ERROR_MESSAGE); 
			}else {	
				/* Si les champs ne sont pas vides */
				if(st.length()!=0 && sta.length()!=0) {
					/* On lance la prochaine fenêtre */
					GestionFichier sysFichiers = new GestionFichier(PreIhm.getJTextField1());
					sysFichiers.initGestionFichier();
					ServeurControle sc = new ServeurControle(sysFichiers);
					ServeurDonnees sd = new ServeurDonnees(sysFichiers);
					GestionProtocole protocole = new GestionProtocole(sc,sd);
					SocketServeurTCP serveurTCP = new SocketServeurTCP(protocole,Integer.parseInt(PreIhm.getJTextField2()));
					serveurTCP.start();

					@SuppressWarnings("unused")
					Ihm ihm = new Ihm(sysFichiers);
				}else {
					javax.swing.JOptionPane.showMessageDialog(panel, "Un ou plusieurs champs sont vides", "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
		}
	}

	/*
	 * Methode getJTextField1() : Permet de récuperer le champ du premier JTextArea
	 * @return : Le texte contenu dans le premierJTextArea
	 */
	public static String getJTextField1() {
		return  textArea1.getText();
	}


	/*
	 * Methode getJTextField2() : Permet de récuperer le champ du second JTextArea
	 * @return : Le texte contenu dans le second JTextArea
	 */

	public static String getJTextField2() {
		return  textArea2.getText();
	}
}