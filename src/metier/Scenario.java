package metier;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.FileInputStream;
import iut.algo.*;

public class Scenario extends Partie
{
	/* -------------------------------------- */
	/*               Attributs                */
	/* -------------------------------------- */

	private int          numScenario;
	private List<Carte>  paquetManche1;
	private List<Carte>  paquetManche2;

	/* -------------------------------------- */
	/*              Constructeur              */
	/* -------------------------------------- */

	public Scenario ( Joueur j, int num )
	{
		super ( j );

		this.numScenario = num;

		this.paquetManche1 = new ArrayList<> ( );
		this.paquetManche2 = new ArrayList<> ( );

		this.creerScenario ( num );
	}

	/* -------------------------------------- */
	/*                Accesseur               */
	/* -------------------------------------- */

	public int getNumScenario ( ) { return this.numScenario; }

	/* -------------------------------------- */
	/*                 Méthode                */
	/* -------------------------------------- */

	public void creerScenario ( int num )
	{

		try 
		{
			// Prend la première ligne de notre ficher contenant les noeuds du graph

			Scanner sc = new Scanner ( new FileInputStream ( "donnees/scenario"+ num +".data" ) );

			String line = sc.nextLine ( );

			int cpt = 1; // cpt des paquet selon les manches
			do
			{
				// gestion des paquets prédéfinis
				Decomposeur dec = new Decomposeur ( line );
				
				List<Carte> paquet = ( cpt == 1 ) ? this.paquetManche1 : this.paquetManche2;

				for ( int i = 0; i > -1 ; i++ )
				{
					try 
					{
						String defCarte = dec.getString ( i );

						char   type    = defCarte.charAt    ( 0 );
						String couleur = defCarte.substring ( 1 );

						paquet.add ( new Carte ( type, couleur ) );
						
					} 
					catch ( Exception e ) { break; }
				}
				
				line = sc.nextLine ( );
				cpt++;
			}
			while ( !line.equals ( "" ) );

			line = sc.nextLine ( );
			
			super.setLstPaquet ( new ArrayList<> ( Arrays.asList ( new Paquet ( this.paquetManche1 ), new Paquet (  this.paquetManche2 ) ) ) );
			super.setPaquet    ( super.getLstPaquets ( ).get ( 0 ) );

			// Permet de piocher la première carte
			super.tourSuivant ( );

			do
			{
				Decomposeur dec = new Decomposeur ( line );

				String nomIle1 = "";
				String nomIle2 = "";

				nomIle1 = dec.getString ( 0 );
				nomIle2 = dec.getString ( 1 );

				for ( VoieMaritime v : super.joueur.getPlateau ( ).getVoiesMaritimes ( ) )
					if ( v.getIleA ( ).getNom ( ).equals ( nomIle1 ) && v.getIleD ( ).getNom ( ).equals ( nomIle2 ) ||
						 v.getIleD ( ).getNom ( ).equals ( nomIle1 ) && v.getIleA ( ).getNom ( ).equals ( nomIle2 )   )
							super.jouer ( v, true );

				super.tourSuivant ( );
				line = sc.nextLine ( );

			}
			while ( !line.equals ( "" ) );

		}
		catch ( Exception e ) { }

	}

}

