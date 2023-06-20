package metier;

import java.util.ArrayList;
import java.util.List;

public class Ile
{
	/* -------------------------------------- */
	/*               Attributs                */
	/* -------------------------------------- */

	private int posX;
	private int posY;

	private int posXImage;
	private int posYImage;

	private String nom;
	private String couleur;
	private Region region;

	private List<VoieMaritime> ensVoie;

	/* -------------------------------------- */
	/*              Constructeur              */
	/* -------------------------------------- */

	/** Constructeur unique d'une île
	 * @param nom
	 * @param couleur
	 * @param posX
	 * @param posY
	 * @param xImage Pour la partie IHM
	 * @param yImage Pour la partie IHM
	 */
	public Ile ( String nom, String couleur, int posX, int posY, int xImage, int yImage )
	{
		this.nom       = nom;
		this.couleur   = couleur;
		this.posX      = posX;
		this.posY      = posY;
		this.posXImage = xImage;
		this.posYImage = yImage;
		this.region    = null;

		this.ensVoie  = new ArrayList<> ( );
	}

	/* -------------------------------------- */
	/*                Accesseur               */
	/* -------------------------------------- */

	/** Acceseur qui retourne le nom de l'île
	 * @return le nom de l'île
	 */
	public String             getNom       ( ) { return this.nom;       }
	/** Acceseur qui retourne la positionX de l'île
	 * @return la positionX de l'île
	 */
	public int                getPosX      ( ) { return this.posX;      }
	/** Acceseur qui retourne la positionY de l'île
	 * @return la positionY de l'île
	 */
	public int                getPosY      ( ) { return this.posY;      }
	/** Acceseur qui retourne la region de l'île
	 * @return la region de l'île
	 */
	public Region             getRegion    ( ) { return this.region;    }
	/** Acceseur qui retourne l'ensemble des voies liées à une île
	 * @return ensemble de voies d'un île
	 */
	public List<VoieMaritime> getEnsVoie   ( ) { return this.ensVoie;   }
	/** Acceseur qui retourne la couleur de l'île
	 * @return la couleur de l'île
	 */
	public String             getCouleur   ( ) { return this.couleur;   }
	/** Acceseur qui retourne la positionX de l'image de l'île
	 * @return la positionX de l'image de l'île
	 */
	public int                getPosXImage ( ) { return this.posXImage; }
	/** Acceseur qui retourne la positionY de l'image de l'île
	 * @return la positionY de l'image de l'île
	 */
	public int                getPosYImage ( ) { return this.posYImage; }

	public List<Ile>          getVoisins   ( )
	{
		List<Ile> voisins = new ArrayList<> ( );

		for ( VoieMaritime voieMaritime : this.ensVoie )
		{
			Ile depart  = voieMaritime.getIleD ( );
			Ile arrivee = voieMaritime.getIleA ( );

			if (depart  != this) voisins.add ( depart  );
			else                 voisins.add ( arrivee );
		}

		return voisins;
	}

	/* -------------------------------------- */
	/*              Modificateur              */
	/* -------------------------------------- */

	/** Modificateur permettant d'ajouter une voie liée à une île
	 * @param arc 
	 */
	public void ajouterArc ( VoieMaritime voie ) { this.ensVoie.add ( voie );                           }

	/** Modificateur permettant de modifiée la region qui est initialisée à null
	 * @param region 
	 */
	public void setRgn     ( Region region	   ) { this.region = region; region.ajouterIle ( this );   }

	/* -------------------------------------- */
	/*                 Méthode                */
	/* -------------------------------------- */

	/** toString
	 * @return une chaine descrivant l'île
	 */
	public String toString ( )
	{
		String sRet = "";

		sRet += "L'ile "                     + this.getNom ( );
		sRet += " appartient à la région : " + this.region.getNomRegion ( );

		return sRet;
	}
	
}