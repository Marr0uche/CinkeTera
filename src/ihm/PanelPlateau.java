package ihm;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.*;
import java.util.List;
import java.awt.geom.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import controleur.*;
import metier.*;

public class PanelPlateau extends JPanel implements MouseListener//,ActionListener
{

	/** Un Controleur pour pouvoir accéder au controleur
	 * 
	 */
	private Controleur  ctrl;

	/** Notre liste d'arcs présents dans notre graph 
	 * 
	 */
	private List<VoieMaritime>   lstVoiesMaritimes;

	/** Notre liste de noeuds présents dans notre graph 
	 * 
	 */
	private List<Ile> lstIles;

	/**La couleur du joueur 1 
	 * 
	 */
	private Color clrJ;

	/**
	 * Ratio pour le x
	 */
	private double rX;

	/**
	 * Ratio pour le x
	 */
	private double rY;

	/**
	 * L'arc a colorier
	 */
	private VoieMaritime voieMaritimeAColorier;

	private FramePlateau frame;

	private Joueur j;

	/** Constructeur de PanelPlateau
	 * @param ctrl de type Controleur
	 * @throws IOException
	 * 
	 */
	public PanelPlateau ( Controleur ctrl , FramePlateau frame, Joueur j )
	{
		this.ctrl  = ctrl;
		this.j     = j;
		this.frame = frame;
		this.clrJ  = this.j.getPartie ( ).getCoulLigne ( );
		this.rX    = this.rY = 0.60;

		this.lstVoiesMaritimes     = this.j.getPlateau ( ).getVoiesMaritimes ( );
		this.lstIles               = this.j.getPlateau ( ).getIles           ( );
		this.voieMaritimeAColorier = null;
		
		this.setBackground ( new Color ( 172, 209, 232 ) );
		this.addMouseListener ( this );
	}

	public double r ( ) { return this.rX; }

	public void paintComponent ( Graphics g )
	{
		super.paintComponent ( g );
		Graphics2D g2 = ( Graphics2D ) g;

		this.clrJ = this.j.getPartie ( ).getCoulLigne ( );

		List<String> journal = this.j.getPlateau ( ).getJournal ( );
		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit ( ).getScreenSize ( );
		int xLigne = (int)(tailleEcran.getWidth() * 0.70);
		int yLigne = 80;

		//Dessiner le journal de bord 
		g2.setFont    ( Font.getFont ( Font.SANS_SERIF ) );
		g2.drawRect   ( xLigne, 30, 150, 30              );
		g2.setColor   (  new Color ( 86, 39, 138 )       ); 
		g2.fillRect   ( xLigne, 30, 150, 30              );
		g2.setColor   ( Color.WHITE                      );
		g2.drawString ( "Journal", xLigne + 50, 50       );
		g2.setColor   ( Color.BLACK                      );

		if ( journal.size ( ) <= 20 )
		{
			for ( String ligne : journal )
			{
				g2.drawString ( ligne, xLigne, yLigne );
				yLigne += 20;
			}
		}
		else
		{
			for ( int index = journal.size ( ) - 21; index < journal.size ( ); index++ )
			{
				g2.drawString ( journal.get ( index ), xLigne, yLigne);
				yLigne += 20;
			}
		}		

		//Dessiner le graph
		this.dessinerArcs  ( g2,this.rX,this.rY );
		this.dessinerIles  ( g2,this.rX,this.rY );

		//Dessiner les régions
		this.dessinerRegions ( g2 );
	}

	/** Méthode qui dessine les arcs de la liste
	 * @param g2 de type Graphics2D
	 */
	private void dessinerArcs ( Graphics2D g2,double rX,double rY )
	{
		float lineWidth = 3f;
		float[] dashPattern = { 5f, 5f, 5f }; //le motif de pointillé

		for ( VoieMaritime voieMaritime : lstVoiesMaritimes)
		{
			Ile depart  = voieMaritime.getIleD ( );
			Ile arrivee = voieMaritime.getIleA ( );

			g2.setColor ( Color.DARK_GRAY );

			if ( voieMaritime != this.voieMaritimeAColorier )
			{
				Color arcColor = voieMaritime.getColorArc ( ) == null ? Color.DARK_GRAY : voieMaritime.getColorArc ( );
				g2.setColor  ( arcColor );
				g2.setStroke ( new BasicStroke ( voieMaritime.getColorArc ( ) == null ? 5 : 7 ) );	//Dessine les arcs coloriés avec un stroke plus épais
			}

			//Représenter les arcs bonus
			if ( voieMaritime.getValeur ( ) != 0 && !voieMaritime.getEstColorie (  ) )
				g2.setColor ( Color.LIGHT_GRAY );

			if ( this.j.getPartie ( ).jouer ( voieMaritime,false ) )
			{
				g2.setColor  ( this.clrJ                                                                                      );
				g2.setStroke ( new BasicStroke ( lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, dashPattern, 0 ) );
				g2.setColor  ( this.clrJ                                                                                      );
			}
			
			g2.drawLine ( ( int ) ( depart.getPosX ( ) * rX ), ( int ) ( depart.getPosY ( ) * rY ), ( int ) ( arrivee.getPosX ( ) * rX ), ( int ) ( arrivee.getPosY ( ) * rY ) );
			
			this.frame.majFrameCarte ( );
		}
	}

	/** Méthode qui dessine les noeuds de la liste
	 * @param g2 de type Graphics2D
	 * 
	 */
	private void dessinerIles ( Graphics2D g2,double rX,double rY )
	{
		//Avoir la couleur du Joueur
		this.clrJ = this.j.getPartie ( ).getCoulLigne ( );

		//Avoir les Iles jouables
		List<Ile> lstExtremite = this.j.getPartie ( ).getEnsExtremites ( );
		List<Ile> ligneR       = this.j.getPartie ( ).getLigneR        ( );
		List<Ile> ligneB       = this.j.getPartie ( ).getLigneB        ( );
		List<Ile> ligne;

		if ( this.clrJ == Color.RED )
			ligne = ligneR;
		else
			ligne = ligneB;

		for ( Ile ile : this.lstIles )
		{
			BufferedImage imgIle = null;
			try 
			{
				imgIle = ImageIO.read ( new File ( ".\\donnees/images\\" + ile.getNom ( ) +".png" ) );
			}
			catch ( Exception e )
			{
				e.printStackTrace ( );
			}
			
			int width    = imgIle.getWidth  (                                     );
			int height   = imgIle.getHeight (                                     );
			int[] pixels = imgIle.getRGB    ( 0, 0, width, height, null, 0, width );

			// Parcourir les pixels et les assombrire pour changer l'image
			for ( int i = 0; i < pixels.length; i++ )
			{
				int rgb   = pixels[i];
				int alpha = ( rgb >> 24 ) & 0xFF;
				int r     = ( rgb >> 16 ) & 0xFF;
				int g     = ( rgb >> 8  ) & 0xFF;
				int b     = rgb & 0xFF;

				r *= 0.50;
				b *= 0.50;
				g *= 0.50;

				pixels [i] = ( alpha << 24 ) | ( r << 16 ) | ( g << 8 ) | b;
			}

			// Créer une nouvelle image avec les pixels modifiés
			BufferedImage modifiedImage = new BufferedImage ( width, height, BufferedImage.TYPE_INT_ARGB );
			modifiedImage.setRGB ( 0, 0, width, height, pixels, 0, width );

			//Si la ligne R ou la ligne B contient l'ile, et que c'est une extremité , ou c'est jouable on remet l'image au normale
			if ( ligne.contains ( ile ) && lstExtremite.contains ( ile ) || this.j.estJouable ( ile,lstExtremite ) )
				modifiedImage = imgIle;

			//En cas de biffurcation
			if (ligne.contains ( ile ) && ( this.j.getPartie( ).estBiffurcation( ) && this.j.getPartie().getNumTours() != 0 ) || this.j.estJouable ( ile,ligne ))
				modifiedImage = imgIle;
			
			g2.drawImage ( modifiedImage, ( int ) ( ile.getPosXImage        ( ) * this.rX ),
										  ( int ) ( ile.getPosYImage        ( ) * this.rY ),
										  ( int ) ( modifiedImage.getWidth  ( ) * this.rX ),
										  ( int ) ( modifiedImage.getHeight ( ) * this.rY ), this );
		}

		//Les noms des îles
		for ( Ile ile : this.lstIles )
		{	
			g2.setColor ( Color.WHITE );
			if ( ( this.clrJ == Color.RED  && ile.getNom ( ).equals ( "Ticó"  ) ) ||
				 ( this.clrJ == Color.BLUE && ile.getNom ( ).equals ( "Mutaa" ) )    )
				g2.setColor ( clrJ );

			g2.drawString ( ile.getNom ( ), ( int ) ( ( ile.getPosX ( ) - 20 ) * rX ), ( int ) ( ile.getPosY ( ) * rY ) );
		}

		g2.setColor ( Color.BLACK );
	}



	public void dessinerRegions ( Graphics2D g2 )
	{
		g2.setStroke ( new BasicStroke ( 1 )     );
		g2.setColor  ( new Color ( 86, 39, 138 ) );
		
		g2.drawLine ( ( int ) ( 0   * this.rX ), ( int ) ( 377 * this.rY ), ( int ) ( 785  * this.rX ), ( int ) ( 377 * this.rY ) );
		g2.drawLine ( ( int ) ( 785 * this.rX ), ( int ) ( 377 * this.rY ), ( int ) ( 1037 * this.rX ), ( int ) ( 0   * this.rY ) );
		g2.drawLine ( ( int ) ( 785 * this.rX ), ( int ) ( 377 * this.rY ), ( int ) ( 1400 * this.rX ), ( int ) ( 900 * this.rY ) );
		g2.drawLine ( ( int ) ( 450 * this.rX ), ( int ) ( 0   * this.rY ), ( int ) ( 450  * this.rX ), ( int ) ( 900 * this.rY ) );
	}

	/** Méthode qui permet de récupérer l'arc sélectionné
	 * @return Arc sélectionné
	 * 
	 */
	public VoieMaritime getVoieMaritimeAColorier ( ) { return this.voieMaritimeAColorier; }

	public void mouseClicked ( MouseEvent e )
	{
		for ( VoieMaritime voieMaritime : this.lstVoiesMaritimes ) 
		{
			Ile ileD = voieMaritime.getIleD ( );
			Ile ileA = voieMaritime.getIleA ( );

			Line2D line = new Line2D.Double ( ileD.getPosX ( ) * rX, ileD.getPosY ( ) * rY, ileA.getPosX ( ) * rX, ileA.getPosY ( ) * rY );

			//Si on clique bien sur un arc
			if ( line.intersects ( e.getX ( ), e.getY ( ), 10, 10 ) ) 
			{
				this.voieMaritimeAColorier = voieMaritime;
				
				//Si ce n'est pas possible
				if ( !this.j.jouer ( voieMaritime, true ) )
					JOptionPane.showMessageDialog ( this.frame, "Erreur de sélection", "Erreur", JOptionPane.ERROR_MESSAGE ); //Affiche que la sélection est mauvaise

				
				this.voieMaritimeAColorier = null;
				this.repaint ( );

				if ( this.ctrl.getJoueur2 ( ) != null && this.j.getPartie ( ).getFinPartie ( ) )
                {
                    this.j.getPlateau().ajouterAuJournal(" -> Le jeu est fini! Bien joué!!" );
                    this.frame.finPartieInit ( );
                }

				//Si la partie est fini pour un joueur 
				if ( this.j.getPartie ( ).getFinPartie ( ) && this.ctrl.getJoueur2 ( ) == null )
				{
					this.j.getPlateau ( ).ajouterAuJournal( " -> Le jeu est fini! Bien joué!!" );
					this.frame.finPartieInit ( );
				}

				//Message pour dire qu'il y a une biffurcation
				if ( this.j.getPartie ( ).estBiffurcation ( ) && this.j.getPartie ( ).getNumTours ( ) != 0 )
				{
					String precision;
					if (j == this.ctrl.getJoueur1 ( ) ) precision  = "pour le joueur 1 ";
					else                                precision  = "pour le joueur 2 ";
					
					this.j.getPlateau().ajouterAuJournal(" -> La biffurcation est arrivée " + precision );
					JOptionPane.showMessageDialog ( this.frame, "Évènement - Biffurcation", "Biffurcation " + precision, JOptionPane.INFORMATION_MESSAGE ); //Affiche qu'il y a une biffurcation
				}
					
				this.frame.majFrameCarte ( );
				return;
			}
		}
	}

	public void mousePressed  ( MouseEvent e ) { /*méthode pas utilisé*/ }
	public void mouseReleased ( MouseEvent e ) { /*méthode pas utilisé*/ }
	public void mouseEntered  ( MouseEvent e ) { /*méthode pas utilisé*/ }
	public void mouseExited   ( MouseEvent e ) { /*méthode pas utilisé*/ }
	
}