package ihm;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import client.ClientControle;

/*
 * Classe TelechargementSimpleIHM : G�n�re la fen�tre JFrame destin�e au T�l�chargementSimple.
 * 
 */

public class TelechargementSimpleIHM extends JFrame implements ActionListener {
	/*
	 * D�claration des variables
	 */
	private static final long serialVersionUID = 1L;



	
	/*
	 * D�claration du JLabel et du JTextArea associ�
	 */
	JLabel j = new JLabel("Entrez le nom du fichier � t�l�charger : ",SwingConstants.CENTER);
	static JTextArea jta = new JTextArea();
	JLabel j2 = new JLabel("Entrez l'adresse IP : ", SwingConstants.CENTER);
	JTextArea jta1 = new JTextArea();
	JLabel j3 = new JLabel("Entrez le num�ro de port : ", SwingConstants.CENTER);
	JTextArea jta2 = new JTextArea();


	/*
	 * D�claration du JButton n�cessaire pour le t�l�chargement
	 */
	JButton j4 = new JButton("T�l�charger ");

	/*
	 * Constructeur TelechargementSimpleIhm : G�n�re la fen�tre et les actions (ActionListener) associ�es
	 */
	ClientControle cc = null;
	
	@SuppressWarnings("static-access")
	public TelechargementSimpleIHM(ClientControle cc) {
		
		this.cc=cc;
		/* Declaration et initialisation de la JFrame */
		JFrame f=new JFrame();
		/*D�claration d'une bordure pour les contours des champs modifiables*/
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		/* Cr�ation d'un container*/
		Container cont=f.getContentPane();
		/*On ajoute au container un nouveau Layout*/
		cont.setLayout(new GridBagLayout());

		/* D�claration et initialisation d'un Layout */
		GridBagConstraints constraints=new GridBagConstraints();
		/* Initialisation des param�tres de ce Layout. Le but est d'ajouter au panel, chaque composant (JTexteArea ou JLabel) ainsi que
		 * chaque param�tre "de positionnement" constraints d�crit ci-dessous:
	     */
		constraints.insets.top=7;
		constraints.insets.bottom=7;
		constraints.fill = GridBagConstraints.HORIZONTAL;


		GridBagConstraints constraints1=new GridBagConstraints();
		constraints1.insets.top=7;
		constraints1.insets.bottom=7;



		constraints.anchor=GridBagConstraints.SOUTH;
		constraints.gridx=0;
		constraints.gridy=0;
		cont.add(j,constraints);


		constraints1.anchor=GridBagConstraints.SOUTH;
		constraints1.weightx = 0.2;
		constraints1.gridx=1;
		constraints1.gridy=0;
		cont.add(jta,constraints1);
	
		/* On modifie la bordure des JTextArea*/
		jta.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		/* On modifie la taille des JTextArea*/
		jta.setPreferredSize(new Dimension(200,20));

		constraints.anchor=GridBagConstraints.SOUTH;
		constraints.gridx=0;
		constraints.gridy=-2;
		cont.add(j2,constraints);


		constraints1.anchor=GridBagConstraints.SOUTH;
		constraints1.weightx = 0.2;
		constraints1.gridx=1;
		constraints1.gridy=-2;
		cont.add(jta1,constraints1);

		/* On modifie la bordure des JTextArea*/
		jta1.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		/* On modifie la taille des JTextArea*/
		jta1.setPreferredSize(new Dimension(200,20));

		constraints.anchor=GridBagConstraints.SOUTH;
		constraints.gridx=0;
		constraints.gridy=-3;
		cont.add(j3,constraints);


		constraints1.anchor=GridBagConstraints.SOUTH;
		constraints1.weightx = 0.2;
		constraints1.gridx=1;
		constraints1.gridy=-3;
		cont.add(jta2,constraints1);


		/* On modifie la bordure des JTextArea*/
		jta2.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		/* On modifie la taille des JTextArea*/
		jta2.setPreferredSize(new Dimension(200,20));

		constraints.anchor=GridBagConstraints.SOUTH;
		constraints.gridx=0;
		constraints.gridy=-4;
		cont.add(j4,constraints);

		/* On modifie la bordure du JButton*/
		j4.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		/* Bouton permettant d'ex�cuter une action gr�ce � l'int�raction avec l'utilisateur*/
		j4.addActionListener(this);

		/* On fixe une taille � la JFrame*/
		f.setSize(600,600);
		/* On rend impossible la modification de la taille de la JFrame*/
		f.setResizable(false);
		/* Dimensionne le cadre de sorte que tout son contenu soit au niveau ou au-dessus de ses preferredSize */
		this.pack();
		/* On rend visible la fen�tre */
		f.setVisible(true);    
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/* On r�cup�re le champ contenant le port */
		Integer nb = Integer.parseInt(jta2.getText()); 
		/*On v�rifie que l'adresse IP est bien format�*/
		if (! jta1.getText().matches("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
			/* Affichage d'un message d'erreur si le champ n'est pas bien format� */
			javax.swing.JOptionPane.showMessageDialog(null, "L'adresse IP n'est pas valide", "Error", JOptionPane.ERROR_MESSAGE); 
		}else {
			/*On v�rifie que les champs ne sont pas vides*/
			if (jta.getText().length()!=0 && jta1.getText().length()!=0 && nb!=0) {
				/* Si les champs ne sont pas vides, on lance le t�l�chargement */
				/*
				 * D�claration et initialisation d'un syst�me de fichiers
				 */
				
				
				cc.TelechargementSimple(jta.getText(),jta1.getText(), nb );

				
			}else {
				/* Sinon on affiche un message d'erreur */
				javax.swing.JOptionPane.showMessageDialog(null, "Un ou plusieurs champs sont vides", "Error", JOptionPane.ERROR_MESSAGE); 

			}
		}

	}     
	/*
	 * Methode getJTextField2() : Permet de r�cuperer le champ du JTextArea
	 * @return : Le texte contenu dans le JTextArea
	 */
	public static String getJTextField2() {
		return  jta.getText();
	}


}
