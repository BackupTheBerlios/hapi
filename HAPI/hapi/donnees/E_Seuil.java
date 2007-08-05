/*
 * Created on 1 févr. 2005
 *
 */
package hapi.donnees;

/**
 * @author Natalia
 *
 */
public class E_Seuil
{
	// identifiant du seuil
	private int id = 0;
	// identifiant de l'indicateur auquel il correspond
	private int id_indicateur = 0;
	// identifiant du processus pour lequel il 
	// est valide
	private String id_processus = null;
	// valeur minimum
	private Double min = null;
	// valeur maximum
	private Double max = null;

	public E_Seuil()
	{}

	// getters et setters
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getId_processus()
	{
		return id_processus;
	}
	public void setId_processus(String id_processus)
	{
		this.id_processus = id_processus;
	}
	public int getId_indicateur()
	{
		return id_indicateur;
	}
	public void setId_indicateur(int id_indicateur)
	{
		this.id_indicateur = id_indicateur;
	}
	public Double getMax()
	{
		return max;
	}
	public void setMax(Double max)
	{
		this.max = max;
	}
	public Double getMin()
	{
		return min;
	}
	public void setMin(Double min)
	{
		this.min = min;
	}
}
