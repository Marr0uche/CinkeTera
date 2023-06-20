package ihm;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import controleur.*;
import metier.*;

public class PanelCarte extends JPanel implements MouseListener, ActionListener, MouseMotionListener
{
	private Controleur   ctrl;

	private FrameCarte frame;

	private JButton     btnPasserTour;
	private JButton     btnQuitter;

	private Image        imgDosDeCarte = this.getToolkit ( ).getImage ( "donnees/imagesCartes/dos_carte.png" );
	private int          inutile;

	private int      bHover;
	private int      scoreJ1;
	private int      scoreJ2;

	/** Constructeur de PanelCarte
	 * @param ctrl de type Controleur
	 * 
	 */
	public PanelCarte ( Controleur ctrl, FrameCarte frame )
	{
		this.ctrl   = ctrl;
		this.frame  = frame;

		this.setBackground ( new Color ( 172, 209, 232 ) ) ;
		this.setLayout ( new BorderLayout ( ) );

		this.inutile = 0;
		this.bHover = -1;

		//Bouton passer tour
		JPanel panelDroit = new JPanel ( new GridLayout ( 5, 1 ) );
		
		this.btnPasserTour = new JButton ( "Passer Tour" );
		this.btnQuitter    = new JButton ( "Quitter"     );

		panelDroit.setOpaque ( false );

		panelDroit.add ( new JLabel ( )     );
		panelDroit.add ( this.btnPasserTour );
		panelDroit.add ( this.btnQuitter    );
		panelDroit.add ( new JLabel ( )     );

		this.add ( panelDroit, BorderLayout.EAST );

		this.btnPasserTour.addActionListener      ( this );
		this.btnQuitter   .addActionListener      ( this );
		this              .addMouseMotionListener ( this );
		this              .addMouseListener       ( this );
	}

	@Override
	public void paintComponent ( Graphics g )
	{
		super.paintComponent ( g );
		Graphics2D g2 = ( Graphics2D ) g;

		//Présenter le score
		g2.setStroke ( new BasicStroke ( 4 ) );
		this.scoreJ1 = this.ctrl.getJoueur1 ( ).getPartie ( ).calculerScore ( );
		this.scoreJ2 = this.ctrl.getJoueur1 ( ).getPartie ( ).calculerScore ( );

		g2.drawRect   ( 720, 42, 120, 60                    );
		g2.setColor   ( new Color ( 246, 247, 64 )          );
		g2.fillRect   ( 720, 42, 120, 60                    );
		g2.setColor   ( Color.BLACK                         );
		g2.drawString ( "Score J1 : " + this.scoreJ1, 740,75 );

		if (this.ctrl.getJoueur2 ( ) != null) 
		{
			g2.drawRect   ( 920, 42, 120, 60                     );
			g2.setColor   ( new Color ( 246, 247, 64 )           );
			g2.fillRect   ( 920, 42, 120, 60                     );
			g2.setColor   ( Color.BLACK                          );
			g2.drawString ( "Score J2 : " + this.scoreJ2, 940, 75 );
		}
		
		//Desinner les cartes
		this.dessinerCartes ( g2 );

		Carte cJ1 = this.ctrl.getJoueur1 ( ).getPartie ( ).getCarteEnCours ( );
		Carte cJ2 = null;
		if ( this.ctrl.getJoueur2 ( ) != null ) { cJ2 = this.ctrl.getJoueur2 ( ).getPartie ( ).getCarteEnCours ( ); }

		Image image = this.imgDosDeCarte;

		if (cJ1 == null && cJ2 == null) 
			image = this.imgDosDeCarte;
		else if (cJ1 == null)
			image = this.getToolkit ( ).getImage ( "donnees/imagesCartes/" + cJ2.getTypeCarte ( ) + cJ2.getCouleurCarte ( ) + ".png" );
		else
			image = this.getToolkit ( ).getImage ( "donnees/imagesCartes/" + cJ1.getTypeCarte ( ) + cJ1.getCouleurCarte ( ) + ".png" );


		g2.drawImage ( image, 500, 20, this.imgDosDeCarte.getWidth ( null ) / 3, this.imgDosDeCarte.getHeight ( null ) / 3, null );

		if ( this.inutile < 5 )
		{
			this.repaint ( );
			this.inutile++;
		}
	}

	public void dessinerCartes ( Graphics2D g2 )
	{
		Paquet paquetDeCartes = this.ctrl.getJoueur1 ( ).getPartie ( ).getPaquet ( );
		
		for ( int cpt = 0; cpt < paquetDeCartes.taillePaquet ( ); cpt++ )
		{
			int y = 20;
			if ( cpt == this.bHover ) y = 30;

			Image image = new ImageIcon ("donnees/imagesCartes/" + paquetDeCartes.getCarte( cpt ).getTypeCarte ( ) + paquetDeCartes.getCarte( cpt ).getCouleurCarte ( ) + ".png" ).getImage ( );
			g2.drawImage ( image, 50 + 20 * cpt, y, this.imgDosDeCarte.getWidth ( null ) / 3, this.imgDosDeCarte.getHeight ( null ) / 3, null );
		}
	}

	public void mouseClicked ( MouseEvent e )
	{
		//Pour toutes les cartes
		for ( int cpt = 0; cpt < this.ctrl.getJoueur1 ( ).getPartie ( ).getPaquet ( ).taillePaquet ( ); cpt++ )
		{
			Rectangle2D zoneCarte = new Rectangle2D.Double ( );

			//Si c'est la fin de la partie
			if ( this.ctrl.getJoueur1 ( ).getPartie ( ).getFinPartie ( ) )
				this.frame.getFramePlateau ( ).finPartieInit ( );

			if ( cpt < this.ctrl.getJoueur1 ( ).getPartie ( ).getPaquet ( ).taillePaquet ( ) - 1 )
				zoneCarte.setRect ( 50 + 20 * cpt, 20, 20, this.imgDosDeCarte.getHeight ( null ) / 3 );
			else
				zoneCarte.setRect ( 50 + 20 * cpt, 20, this.imgDosDeCarte.getWidth ( null ) / 3, this.imgDosDeCarte.getHeight ( null ) / 3 );

			if ( zoneCarte.contains ( e.getPoint ( ) ) )
			{
				this.ctrl.getJoueur1 ( ).getPartie ( ).tourSuivant ( );
				if ( this.ctrl.getJoueur2 ( ) != null ) this.ctrl.getJoueur2 ( ).getPartie ( ).tourSuivant ( );

				// System.out.println(this.ctrl.getJoueur2().getPartie().getCarteEnCours());
				// System.out.println(this.ctrl.getJoueur1().getPartie().getCarteEnCours());


				if ( this.ctrl.getJoueur1 ( ).getPartie ( ).estBiffurcation ( ) || this.ctrl.getJoueur1 ( ).getPartie ( ).estBiffurcation ( ) )
				{
					String precision="";
					if ( this.ctrl.getJoueur1 ( ).getPartie ( ).estBiffurcation ( ) )  precision  = "pour le joueur 1 ";	
					else                                							 precision  = "pour le joueur 2 ";


					JOptionPane.showMessageDialog ( this.frame, "La biffurcation a été mise en place " + precision , "Biffurcation", JOptionPane.INFORMATION_MESSAGE ); //Affiche que la sélection est mauvaise
					this.ctrl.getJoueur1 ( ).getPlateau ( ).ajouterAuJournal ( "La biffurcation est arrivée " + precision );
				}
			}
		}
		this.repaint ( );
		this.ctrl.majIHM ( );
	}

	public void mouseMoved( MouseEvent e )
	{
		//Hover cartes
		for ( int cpt = 0; cpt < this.ctrl.getJoueur1 ( ).getPartie ( ).getPaquet ( ).taillePaquet ( ); cpt++ )
		{
			Rectangle2D zoneCarte = new Rectangle2D.Double ( );
			if ( cpt < this.ctrl.getJoueur1 ( ).getPartie ( ).getPaquet ( ).taillePaquet ( ) - 1 )
				zoneCarte.setRect ( 50 + 20 * cpt, 20,                                      20 , this.imgDosDeCarte.getHeight ( null ) / 3 );
			else
				zoneCarte.setRect ( 50 + 20 * cpt, 20, this.imgDosDeCarte.getWidth ( null ) / 3, this.imgDosDeCarte.getHeight ( null ) / 3);

			if ( zoneCarte.contains ( e.getPoint ( ) ) )
			{
				this.bHover = cpt;
				break;
			}
			else
				this.bHover = -1;
		}
		this.repaint ( );
	}

	public void actionPerformed ( ActionEvent e )
	{
		if ( e.getSource ( ) == this.btnPasserTour )
		{
			this.ctrl.getJoueur1 ( ).getPartie ( ).tourSuivant ( );

			if ( this.ctrl.getJoueur2 ( ) != null) this.ctrl.getJoueur2 ( ).getPartie ( ).tourSuivant ( );
			
			this.ctrl.majIHM ( );
			this.ctrl.getJoueur1 ( ).getPlateau ( ).ajouterAuJournal ( " -> Vous avez passé votre tour" );

			//Si la partie est fini
			if ( this.ctrl.getJoueur1 ( ).getPartie ( ).getFinPartie ( ) )
					this.frame.getFramePlateau ( ).finPartieInit ( );
		}

		
		if ( e.getSource ( ) == this.btnQuitter )
		{
			this.frame.getFramePlateau( ).init( );
			new Controleur( );
		}
			

		this.repaint ( );
	}

	public void mousePressed  ( MouseEvent e ) { }
	public void mouseReleased ( MouseEvent e ) { }
	public void mouseEntered  ( MouseEvent e ) { }
	public void mouseExited   ( MouseEvent e ) { }
	public void mouseDragged  ( MouseEvent e ) { }

}