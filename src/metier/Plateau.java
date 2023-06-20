package metier;

import iut.algo.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileInputStream;
import java.awt.Color;

public class Plateau
{
	/* -------------------------------------- */
	/*               Attributs                */
	/* -------------------------------------- */
	
	private List<Ile>          lstIles;
	private List<VoieMaritime> lstVoiesMaritimes;
	private List<Region>       lstRegions;

	private List<String>       journal;

	/* -------------------------------------- */
	/*              Constructeur              */
	/* -------------------------------------- */
	
	public Plateau ( )
	{
		this.lstIles           = new ArrayList<> ( );
		this.lstVoiesMaritimes = new ArrayList<> ( );
		this.lstRegions        = new ArrayList<> ( );
		this.journal           = new ArrayList<> ( );

		this.initialiserPlateau ( );
	}

	/* -------------------------------------- */
	/*                Accesseur               */
	/* -------------------------------------- */

	public List<VoieMaritime> getVoiesMaritimes ( ) { return this.lstVoiesMaritimes; }
	public List<Ile>          getIles           ( ) { return this.lstIles;           }
	public List<Region>       getRegions        ( ) { return this.lstRegions;        }
	public List<String>       getJournal        ( ) { return this.journal;           }

	public void               ajouterAuJournal  ( String s ) { this.journal.add(s);  }

	/* -------------------------------------- */
	/*                 Méthode                */
	/* -------------------------------------- */

	public void initialiserPlateau ( )
	{

		try
		{
			// Prend la première ligne de notre ficher contenant les noeuds du graph
			Scanner sc = new Scanner ( new FileInputStream ( "donnees/Donnees.data" ) );

			//Pour sauter les deux premières lignes
			sc.nextLine ( );
			sc.nextLine ( );
			String line = sc.nextLine ( );

			//Creer les îles
			do
			{
				Decomposeur dec = new Decomposeur ( line );

				String nom,couleur;
				int posX,posY,posXImage,posYImage;

				nom = couleur = "";
				posX = posY = posXImage = posYImage = 0;


				nom     = dec.getString ( 0 );
				couleur = dec.getString ( 1 );

				posX = dec.getInt ( 2 );
				posY = dec.getInt ( 3 );

				posXImage = dec.getInt ( 4 );
				posYImage = dec.getInt ( 5 );
				
				this.lstIles.add ( new Ile ( nom, couleur, posX, posY, posXImage, posYImage ) );

				line = sc.nextLine ( );
			}
			while ( !line.equals ( "" ) );

			//Créer les regions
			line = sc.nextLine( );
			do
			{
				Decomposeur dec = new Decomposeur ( line );

				String nomRegion = dec.getString ( 0 );

				Region region = new Region ( nomRegion );

				//modulaire
				for ( int cpt = 1; cpt > -1 ; cpt++ )
				{
					try 
					{
						String nomIle = dec.getString ( cpt );

						for ( Ile ile : this.lstIles )
							if ( ile.getNom ( ).equals ( nomIle ) )
								ile.setRgn ( region );
						
					} catch ( Exception e ) { break; }
				}		

				this.lstRegions.add ( region );

				line = sc.nextLine ( );
			}
			while ( !line.equals ( "" ) );

			//Créer les voies maritimes
			while ( sc.hasNextLine ( ) )
			{
				Decomposeur dec = new Decomposeur ( sc.nextLine( ) );

				int valeur = 0;
				String nomIle1,nomIle2;
				Ile ile1,ile2;
				ile1 = ile2 = null;

				nomIle1 = dec.getString ( 0 );
				nomIle2 = dec.getString ( 1 );

				for ( Ile ile : this.lstIles )
				{
					if ( ile.getNom ( ).equals ( nomIle1 ) ) ile1 = ile;
					if ( ile.getNom ( ).equals ( nomIle2 ) ) ile2 = ile;
				}

				//modulaire
				try 
				{
					valeur = dec.getInt( 2 );
					
				} catch ( Exception e ) { }
			
					
				VoieMaritime v = VoieMaritime.creerVoieMaritime ( nomIle1 + "-" + nomIle2, ile1, ile2, valeur );

				this.lstVoiesMaritimes.add ( v );
				ile1.ajouterArc ( v );
				ile2.ajouterArc ( v );

			}

		}
		catch ( Exception e ) { e.printStackTrace ( ); }
	}	
	
	/** Méthode qui retourne le nombre d'arc colorié.
	 * @return int (le nombre d'arcs qui sont coloriés)
	 */
	public int getNbVoiesMaritimesColorie ( )
	{
		int nbVoiesMaritimesColorie = 0;
		for ( VoieMaritime voieMaritime : this.lstVoiesMaritimes )
			if ( voieMaritime.getEstColorie ( ) )
				nbVoiesMaritimesColorie++;
		
		return nbVoiesMaritimesColorie;
	}

	/**Méthode qui prend en paramètre deux iles et détermine s'ils croisent une autre voie maritime.
	 * @param noeudDep le noeud de départ
	 * @param noeudArr le noeud d'arrivée
	 * @return un arc
	 */
	public VoieMaritime arcEntre ( Ile noeudDep, Ile noeudArr )
	{
		for ( VoieMaritime voieMaritime : this.lstVoiesMaritimes ) {
			if ( noeudDep == voieMaritime.getIleA()  && noeudArr == voieMaritime.getIleD ( ) ||
				 noeudDep == voieMaritime.getIleD()  && noeudArr == voieMaritime.getIleA ( ) ) return voieMaritime;
		}
		return null;
	}


}