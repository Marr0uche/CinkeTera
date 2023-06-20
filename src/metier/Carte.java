package metier;

public class Carte
{
	/* -------------------------------------- */
	/*               Attributs                */
	/* -------------------------------------- */

	private static int nbCartePrimaire   = 0;
	private static int nbCarteSecondaire = 0;

	private char   typeCarte;
	private String couleurCarte;
	private int    idCarte;

	/* -------------------------------------- */
	/*              Constructeur              */
	/* -------------------------------------- */

	/** Constructeur unique de Carte
	 * @param typeCarte
	 * @param couleurCarte
	 */
	public Carte ( char typeCarte, String couleurCarte )
	{
		this.typeCarte    = typeCarte;
		this.couleurCarte = couleurCarte;

		if ( typeCarte == 'P' ) this.idCarte = ++ Carte.nbCartePrimaire;
		else                    this.idCarte = ++ Carte.nbCarteSecondaire;
	}

	/* -------------------------------------- */
	/*                Accesseur               */
	/* -------------------------------------- */

	/** Acceseur qui retoune le type de carte
	 * @return type de la carte
	 */
	public char   getTypeCarte    ( ) { return this.typeCarte;    }
	
	/** Accesseur qui retourne la couleur de la carte
	 * @return couleur de la carte
	 */
	public String getCouleurCarte ( ) { return this.couleurCarte; }

	/** Accesseur qui retourne l'identifiant / numéro de la carte
	 * @return ID de la carte
	 */
	public int    getIdCarte      ( ) { return this.idCarte;      }

	/* -------------------------------------- */
	/*                Méthodes                */
	/* -------------------------------------- */

	/** Redéfinition de la méthode toString
	 * @return La chaîne de caractère descriptive de la carte
	 */
	public String toString ( )
	{
		return "Carte [typeCarte=" + this.typeCarte + ", couleurCarte=" + this.couleurCarte + "]";
	}
}

