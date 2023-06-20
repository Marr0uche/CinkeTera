package ihm;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

import javax.swing.*;
import java.awt.*;

import controleur.*;
import metier.*;

public class FramePlateau extends JFrame 
{
	/*-------------*/
	/*--Attributs--*/
	/*-------------*/

	/**
	 * Un controleur pour la frame
	 */
	protected Controleur    ctrl;

	/**
	 * PanelPlateau pour la frame
	 */
	protected PanelPlateau  panelPlateau;

	/**
	 * FrameCarte pour la frame
	 */
	protected FrameCarte   frameCarte;

	/**
	 * la debut du jeu;
	 */
	private Long debut;

	/**
	 * la fin du jeu
	 */
	protected Long fin;


	/*----------------*/
	/*--Constructeur--*/
	/*----------------*/

	/** Constructuer de FramePlateau qui crée un panelPlateau et frameCarte
	 * @param ctrl le controleur
	 * 
	 */
	public FramePlateau ( Controleur ctrl )
	{
		this.ctrl = ctrl;

		this.setLayout ( new GridLayout ( 1, 2 ) );

		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit ( ).getScreenSize ( );
		Image icon = Toolkit.getDefaultToolkit ( ).getImage ( "donnees/images/boat.png" );

		this.setTitle    ( "CinkeTera"                                                                    );
		this.setSize     ( ( int ) tailleEcran.getWidth ( ), ( int ) ( tailleEcran.getHeight ( ) * 0.84 ) );
		this.setLocation ( 0                               , 0                                            );

		/*Création des composants*/
		this.frameCarte    = new FrameCarte   ( this.ctrl, this );
		this.panelPlateau  = new PanelPlateau ( this.ctrl, this, this.ctrl.getJoueur1 ( ) );

		/*Placement des composants*/
		this.add ( this.panelPlateau );

		/* Activation des composants */
		this.setDefaultCloseOperation ( EXIT_ON_CLOSE );
		
		this.setUndecorated           ( true          );
		this.setVisible               ( true          );
		this.setIconImage             ( icon          ); 

		
		this.debut = System.currentTimeMillis ( );
	}

	/**
	 * Mise a jour de la frame de cartes 
	 */
	public void majFrameCarte ( ) { this.frameCarte.majCartes ( ); }
	
	/** Getteur qui retourne l'VoieMaritime à colorier
	 * @return l'VoieMaritime à colorier
	 */
	public VoieMaritime getVoieMaritimeAColorier ( ) { return this.panelPlateau.getVoieMaritimeAColorier ( ); }

	public void majIHM ( ) { this.panelPlateau.repaint ( ); }

	public void init ( )
	{
		this.frameCarte.dispose ( );
		this           .dispose ( );
	}

	public void finPartieInit ( )
	{
		String formatString = "%-30s";
		String sRet         = "";

		sRet += String.format ( formatString, "Nb regions visitées : " ) + this.ctrl.getJoueur1 ( ).getPartie  ( ).getNbRegionsVisite          ( ) + "\n";
		sRet += String.format ( formatString, "Nb arcs colorées    : " ) + this.ctrl.getJoueur1 ( ).getPlateau ( ).getNbVoiesMaritimesColorie  ( ) + "\n";
		sRet += String.format ( formatString, "Nb Points Total     : " ) + this.ctrl.getJoueur1 ( ).getPartie  ( ).calculerScore               ( )       ;

		//écriture du journal 
		try
		{
			PrintWriter pw = new PrintWriter ( "journal.txt" );

			pw.write ( "Journal\n\n" );

			List<String> journal  = this.ctrl.getJoueur1 ( ).getPlateau ( ).getJournal ( );

			for ( String s : journal ) 
				pw.println ( s );

			this.fin        = System.currentTimeMillis ( );
			int dureeTotal  = ( int ) ( ( this.fin - this.fin ) / 1000 );

			int mins = ( int ) ( dureeTotal  / 60 );
			int secs = ( int ) ( dureeTotal  % 60 );

			pw.write ( "Durée totale du jeu : " + mins + " minutes et " + secs + " secondes \n" );

			pw.write ( "Points total : " + this.ctrl.getJoueur1 ( ).getPartie ( ).calculerScore ( ) );

			Calendar aujourdhui = Calendar.getInstance ( );
			aujourdhui.set ( Calendar.HOUR_OF_DAY, 0 ); // same for minutes and seconds
			
			pw.write ( "\n\nDate : " + aujourdhui );

			pw.close ( );
			
		}
		catch ( Exception e ) { }

		//Création d'une "Pop-up" pour demander si le joueur veux rejouer ou quitter
		Object[] choix = { "Rejouer","Quitter" };
		int      rep   = JOptionPane.showOptionDialog ( this,sRet, "Game End", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choix, choix[0] );

		if ( rep == 0 ) //Si Rejouer est sélectionné
		{
			this.init ( );      //On ferme le fenêtre
			new Controleur ( ); //On relance une partie
		}
		else
			System.exit ( 1 );  //On ferme le scripte
	}
}