package ihm;

import javax.swing.JFrame;

import java.awt.*;
import controleur.*;

public class FrameCarte extends JFrame
{
	/*-------------*/
	/*--Attributs--*/
	/*-------------*/

	/** Un Controleur pour pouvoir accéder au controleur
	 * 
	 */
	private Controleur   ctrl;
	private FramePlateau frame;
	private PanelCarte   panelCarte;

	/*----------------*/
	/*--Constructeur--*/
	/*----------------*/

	/** Constructuer de FrameCarte qui crée un panelGraphe et panelAction
	 * @param ctrl le controleur
	 * 
	 */
	public FrameCarte ( Controleur ctrl, FramePlateau frame )
	{
		this.ctrl  = ctrl;
		this.frame = frame;
		
		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit ( ).getScreenSize ( );
		Image icon = Toolkit.getDefaultToolkit ( ).getImage ( "donnees/imagesCartes/dos_carte.png" );

		this.setTitle    ( "Cartes"                                                                       );
		this.setLocation ( 0                               , ( int ) ( tailleEcran.getHeight ( ) * 0.86 ) );
		this.setSize     ( ( int ) tailleEcran.getWidth ( ), ( int ) ( tailleEcran.getHeight ( ) * 0.14 ) );
		
		/*Création des composants*/
		this.panelCarte = new PanelCarte ( this.ctrl, this );

		/*Placement des composants*/
		this.add ( this.panelCarte );

		this.setDefaultCloseOperation ( EXIT_ON_CLOSE );
		this.setUndecorated           ( true          );
		this.setVisible               ( true          );
		this.setIconImage             ( icon          ); 
	}

	public void majCartes ( ) 
	{ 
		this.revalidate ( ); 
		this.panelCarte.repaint ( );
	}

	public FramePlateau getFramePlateau ( ) { return this.frame; }
}