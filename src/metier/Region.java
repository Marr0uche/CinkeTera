package metier;

import java.util.ArrayList;
import java.util.List;


public class Region
{
	/* -------------------------------------- */
	/*               Attributs                */
	/* -------------------------------------- */

	private List<Ile> ensIle;
	private String    nomRegion;

	/* -------------------------------------- */
	/*              Constructeur              */
	/* -------------------------------------- */

	/**
	 * Constructure de la class Region
	 * 
	 * @param nomRegion nom de la région
	 */

	public Region ( String nomRegion )
	{
		this.ensIle    = new ArrayList<> ( );
		this.nomRegion = nomRegion;
	}

	/* -------------------------------------- */
	/*                Accesseur               */
	/* -------------------------------------- */

	/** Accesseur qui renvoie le nombre d'iles dans la région
	 * @return le nombre d'iles
	 */
	public int       getNbIle     ( ) { return this.ensIle.size ( ); }

	/** Accesseur qui renvoie la liste des régions
	 * @return ArrayList des iles
	 */
	public List<Ile> getEnsIles   ( ) { return this.ensIle;          }

	/** Accesseur qui renvoie le nom de la region
	 * @return le nom de la région
	 */
	public String    getNomRegion ( ) { return this.nomRegion;       }

	/* -------------------------------------- */
	/*                 Méthode                */
	/* -------------------------------------- */

	/** Méthode qui ajoute une ile
	 * @param ile ile a ajoute
	 * @return true si l'ile est ajoutee
	 */
	public boolean ajouterIle ( Ile ile )
	{
		if ( this.ensIle.contains ( ile ) ) return false;
		if ( ile == null                  ) return false;

		this.ensIle.add ( ile );
		return true;
	}


	/** Méthode qui supprime une île
	 * @param ile ile a supprime
	 * @return true si l'ile est supprimée
	 */
	public boolean supprimerIle ( Ile ile )
	{
		if ( this.ensIle.contains ( ile ) ) return false;
		if ( ile == null                  ) return false;

		this.ensIle.remove ( this.ensIle.lastIndexOf ( ile ) );
		return true;
	}

	/** toString
	 * @return le nom de la région et ses îles
	 */
	public String toString ( )
	{
		String sRet = "";
		String sIle = "";

		sRet += "La région " + this.nomRegion;

		switch ( this.ensIle.size ( ) )
		{
			case 0  -> sRet += "ne contient aucune île.";
			case 1  -> sRet += "contient l'île ";
			default -> sRet += "contient les îles ";
		}

		for ( Ile i : this.ensIle )
			sIle += i.getNom ( ) + " ";

		sRet += sIle;
		return sRet;
	}

}