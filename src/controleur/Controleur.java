package controleur;
/** SAE 2.02
  * date : le 06/06/2023
  * @author : Alizéa Lebaron, Mathys Poret, Maximilien Lesterlin ,Mohamad Marrouche, Eleonore Bouloché
  */

import metier.*;
import ihm.*;

import java.util.ArrayList;

public class Controleur
{
	private	FramePlateau frameJoueur;
	private Joueur       j1;
	private Joueur       j2;

	/** Contructeur qui initialise le jeu
	 */
	public Controleur ( )
	{
		new FrameAccueil ( this );
	}

	public Joueur getJoueur1 ( ) { return this.j1; }
	public Joueur getJoueur2 ( ) { return this.j2; }

	public void  lancerFrame      ( )
	{
		this.j1             = new Joueur  ( );
		this.frameJoueur    = new FramePlateau ( this);
	}

	public void  lancerScenario   ( int numero )
	{
		this.j1             = new Joueur       ( numero ); 
		this.frameJoueur    = new FramePlateau ( this   );
	}
	
	public void  lancerDeuxJoueurs   ( )
	{
		this.j1             = new Joueur  ( ); 
		this.j2             = new Joueur  ( ); 

		this.j2.getPartie ( ).setLstPaquet ( new ArrayList<> ( this.j1.getPartie ( ).getLstPaquets ( ) ) );
		this.j2.getPartie ( ).setPaquet    ( this.j2.getPartie ( ).getLstPaquets ( ).get ( 0 )    );

		this.frameJoueur    = new FrameDeuxJoueurs ( this, j1, j2 );

	}
	public void majIHM ( ) { this.frameJoueur.majIHM ( ); }

	public static void main ( String[] arg ) { new Controleur ( ); }
}
