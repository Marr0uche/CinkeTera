package metier;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;
import java.util.Collections;

public class Joueur
{
	/* -------------------------------------- */
	/*               Attributs                */
	/* -------------------------------------- */

	private Plateau     plateau;
	private List<Color> couleurs;
	private Partie      partie;

	/* -------------------------------------- */
	/*              Constructeur              */
	/* -------------------------------------- */
	
	/** Unique constructeur de joueur
	 * @param plateau
	 */
	public Joueur ( )
	{
		this.plateau  = new Plateau ( );
		this.couleurs = new ArrayList<> ( Arrays.asList ( Color.BLUE, Color.RED ) );
		
		Collections.shuffle ( this.couleurs );

		this.lancerPartie ( );
	}

	public Joueur ( int numero )
	{
		this.plateau  = new Plateau ( );
		this.couleurs = new ArrayList<> ( Arrays.asList ( Color.BLUE, Color.RED ) );
		
		if ( numero == 2 ) { Collections.shuffle ( this.couleurs ); }

		this.lancerPartie ( numero );
	}

	/* -------------------------------------- */
	/*                Accesseur               */
	/* -------------------------------------- */

	public Plateau            getPlateau       ( ) { return this.plateau;                          }
	/** Acceseur qui retourne la partie du joueur
	 * @return la partie du joueur
	 */
	public Partie             getPartie        ( ) { return this.partie;                           }
	
	/** Accesseur qui permet d'obtenir une couleur du tableau du joueur et de la supprimer en même temps
	 * @return une couleur du tableau
	 */
	public Color              getCouleur       ( ) { return this.couleurs.remove               ( 0 ); }

	/* -------------------------------------- */
	/*                 Méthode                */
	/* -------------------------------------- */

	/** Méthode qui permet de lancer une partie selon la couleur du joueur avec la bonne île de départ
	 */
	public void lancerPartie ( )
	{	
		this.partie = new Partie ( this );
	}

	public void lancerPartie ( int numero )
	{
		this.partie = new Scenario ( this, numero );
	}

	/** Méthode passerelle entre la partie et le joueur
	 * @param voieMaritime voieMaritime selectionnée par le joueur
	 * @return un boolean qui nous indique si le joueur a pu jouer
	 */
	public boolean jouer ( VoieMaritime voieMaritime, boolean bool )
	{
		return this.partie.jouer ( voieMaritime, bool );
	}

	public boolean estJouable ( Ile ile, List<Ile> lstExtremite )
	{
		Carte        carteEnCours = this.getPartie ( ).getCarteEnCours ( );
		VoieMaritime voie         = null;
		
		//Pour toutes les extremités
		for ( Ile extremite : lstExtremite )
		{
			//Pour tous les voisins 
			for ( Ile ileArrivee : extremite.getVoisins ( ) )
			{	
				//On cherche la voie entre notre ile d'extremité et arrivée 
				for ( VoieMaritime voieM : extremite.getEnsVoie ( ) )
					if ( voieM.getIleA ( ) == ileArrivee && voieM.getIleD ( ) == extremite ||
						 voieM.getIleA ( ) == extremite  && voieM.getIleD ( ) == ileArrivee  )
						voie = voieM;
				
				boolean peutJouer = this.jouer ( voie, false );

				//Si la carte en cours n'est pas null et ile == ileArrive e
				if ( carteEnCours != null && ile == ileArrivee )
					if ( ( ileArrivee.getCouleur ( ).equals ( carteEnCours.getCouleurCarte ( ) ) || carteEnCours.getCouleurCarte ( ).equals ( "Multicolore" ) ) &&  peutJouer)
						return true;
			}
		}

		return false;
	}

}