package ihm;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import controleur.*;

public class FrameAccueil extends JFrame implements ActionListener
{
	/*-------------*/
	/*--Attributs--*/
	/*-------------*/

	/** Un Controleur pour pouvoir accéder au controleur
	 * 
	 */
	private Controleur  ctrl;

	private JButton  btnScenario;
	private JButton  btnQuitter;
	private JButton  btn2J;
	private JButton  btn1J;

	/*----------------*/
	/*--Constructeur--*/
	/*----------------*/

	/** Constructuer de FrameAcceuil qui crée un panelGraphe et panelAction
	 * @param ctrl le controleur
	 * 
	 */
	public FrameAccueil ( Controleur ctrl )
	{
		this.ctrl = ctrl;

		Image img       = new ImageIcon ( this.getToolkit ( ).getImage ( "donnees/images/image.png" ) ).getImage ( );
		Image scaledImg = img.getScaledInstance ( 1000, 700, Image.SCALE_SMOOTH );
		
		Image     icon        = Toolkit.getDefaultToolkit ( ).getImage ( "donnees/images/boat.png" );
		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit ( ).getScreenSize ( );

		int l = ( tailleEcran.width  - 1000 ) / 2;
		int h = ( tailleEcran.height -  700 ) / 2;

		this.setSize     ( 1000, 700   );
		this.setLocation (    l,   h   );
		this.setTitle    ( "CinkeTera" );

		/* ------------------------- */
		/* Création des composants   */
		/* ------------------------- */

		JPanel panel         = new JPanel ( new BorderLayout (      )         );
		JPanel panelTest     = new JPanel ( new GridLayout   ( 1, 3 )         );
		JPanel panelButton   = new JPanel ( new GridLayout   ( 6, 1, 15, 15 ) );
		JPanel panelInutile  = new JPanel (                                   );
		JPanel panelInutile2 = new JPanel (                                   );
		
		JLabel j = new JLabel ( new ImageIcon ( scaledImg ) );
		j.setLayout ( new BorderLayout ( ) );

		panel        .setOpaque ( false );
		panelTest    .setOpaque ( false );
		panelButton  .setOpaque ( false );
		panelInutile .setOpaque ( false );
		panelInutile2.setOpaque ( false );

		this.btn1J       = new JButton ( "1 Joueur" );
		this.btn2J       = new JButton ( "2 Joueur" );
		this.btnScenario = new JButton ( "Scénario" );
		this.btnQuitter  = new JButton ( "Quitter"  );

		panelButton.add ( this.btn1J       );
		panelButton.add ( this.btn2J       );
		panelButton.add ( this.btnScenario );
		panelButton.add ( this.btnQuitter  );

		panelTest.add ( panelInutile  );
		panelTest.add ( panelButton   );
		panelTest.add ( panelInutile2 );

		panel.add ( panelTest, BorderLayout.SOUTH );
		
		j.add ( panel, BorderLayout.CENTER );

		this.getContentPane ( ).add ( j );

		/* ------------------------- */
		/* Activation des composants */
		/* ------------------------- */

		this.btn1J      .addActionListener ( this );
		this.btn2J      .addActionListener ( this );
		this.btnScenario.addActionListener ( this );
		this.btnQuitter .addActionListener ( this );

		this.setIconImage             ( icon          );
		this.setDefaultCloseOperation ( EXIT_ON_CLOSE );
		this.setVisible               ( true          );
	}

	/* ActionListener */
	public void actionPerformed ( ActionEvent e )
	{
		if ( e.getSource ( ) == this.btn1J )
			this.ctrl.lancerFrame ( );
		if ( e.getSource ( ) == this.btn2J )
		{
			this.ctrl.lancerDeuxJoueurs ( );
			//JOptionPane.showMessageDialog ( this, "En cours de développement, reviens plus tard", "Erreur", JOptionPane.INFORMATION_MESSAGE ); //Affiche qu'il y a une biffurcation
			//new FrameAccueil ( ctrl );
		}
			
		if ( e.getSource ( ) == this.btnScenario )
			new FrameChoixScenario(this.ctrl);

		this.cacher ( );
	}

	public void cacher ( ) { this.dispose ( ); }

}
