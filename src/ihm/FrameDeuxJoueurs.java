package ihm;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import controleur.*;
import metier.*;

public class FrameDeuxJoueurs extends FramePlateau
{
	private Joueur j2;

	private PanelPlateau panelPlateauJoueurDeux;

	public FrameDeuxJoueurs ( Controleur ctrl, Joueur j1, Joueur j2 ) 
	{
		super ( ctrl );

		this.j2 = j2;

		this.panelPlateauJoueurDeux = new PanelPlateau ( ctrl, this, this.j2 );

		super.add ( this.panelPlateauJoueurDeux );
		
	}

	public void finPartieInit ( )
	{
		String formatString = "%-30s";
		String sRet         = "";

		sRet += String.format ( formatString ,"Nb regions visitées (j1) : " ) + this.ctrl.getJoueur1 ( ).getPartie  ( ).getNbRegionsVisite           ( ) + "\n" ;
		sRet += String.format ( formatString ,"Nb arcs colorées    (j1): " )  + this.ctrl.getJoueur1 ( ).getPlateau ( ).getNbVoiesMaritimesColorie   ( ) + "\n" ;
		sRet += String.format ( formatString ,"Nb Points Total     (j1): " )  + this.ctrl.getJoueur1 ( ).getPartie  ( ).calculerScore ( )                       ;
		
		sRet += "\n\n";
		
		sRet += String.format ( formatString ,"Nb regions visitées (j2): " ) + this.ctrl.getJoueur2 ( ).getPartie  ( ).getNbRegionsVisite          ( ) + "\n" ;
		sRet += String.format ( formatString ,"Nb arcs colorées    (j2): " ) + this.ctrl.getJoueur2 ( ).getPlateau ( ).getNbVoiesMaritimesColorie  ( ) + "\n" ;
		sRet += String.format ( formatString ,"Nb Points Total     (j2): " ) + this.ctrl.getJoueur2 ( ).getPartie  ( ).calculerScore ( )                      ;

		//écriture du journal 
		try
		{
			PrintWriter pw = new PrintWriter( "journal.txt" );

			pw.write ( "Journal\n\n" );

			List<String> journal  = this.ctrl.getJoueur1 ( ).getPlateau ( ).getJournal ( );

			for ( String s : journal ) 
			{
				pw.println ( s );
			}


			
			this.fin        = System.currentTimeMillis();
			int dureeTotal = (int) ( ( this.fin - this.fin ) / 1000 );

			int mins = (int) ( dureeTotal  / 60 );
			int secs = (int) ( dureeTotal % 60 );

			pw.write ( "Durée totale du jeu : " + mins + " minutes et " + secs + " secondes \n");

			pw.write ( "Points total (j1) : " + this.ctrl.getJoueur1 ( ).getPartie ( ).calculerScore ( ) );
			pw.write ( "Points total (j2) : " + this.ctrl.getJoueur2 ( ).getPartie ( ).calculerScore ( ) );

			Calendar aujourdhui = Calendar.getInstance( );
			aujourdhui.set( Calendar.HOUR_OF_DAY, 0 ); // same for minutes and seconds
			
			pw.write( "\n\nDate : " + aujourdhui );

			pw.close( );
			
		}
		catch( Exception e ) { }

		//Création d'une "Pop-up" pour demander si le joueur veux rejouer ou quitter
		Object[] choix = { "Rejouer","Quitter" };
		int      rep   = JOptionPane.showOptionDialog ( this,sRet, "Game End", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choix, choix[0] );

		if ( rep == 0 ) //Si Rejouer est sélectionné
		{
			this.init( );				//On ferme le fenêtre
			new Controleur ( );			//On relance une partie
		}
		else
			System.exit ( 1 );			//On ferme le scripte
	}

	@Override
	public void majIHM ( ) { super.panelPlateau.repaint ( ); this.panelPlateauJoueurDeux.repaint ( );}
}