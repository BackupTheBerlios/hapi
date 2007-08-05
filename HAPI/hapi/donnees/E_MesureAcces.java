/*
 * Créé le 29 sept. 2005
 */
package hapi.donnees;

import java.sql.Date;

/**
 * @author Cédric
 */
public class E_MesureAcces
{
	private int id = -1;
	private Date mois;
	private int nombre;
	
	public E_MesureAcces(int lId,Date leMois,int leNombre)
	{
		id = lId;
		mois = leMois;
		nombre = leNombre;
	}
	
	public E_MesureAcces(Date leMois,int leNombre)
	{
		mois = leMois;
		nombre = leNombre;
	}	
	
	/**
	 * @return Renvoie id.
	 */
	public int getId()
	{
		return id;
	}
	/**
	 * @return Renvoie mois.
	 */
	public Date getMois()
	{
		return mois;
	}
	/**
	 * @return Renvoie nombre.
	 */
	public int getNombre()
	{
		return nombre;
	}
	
	/**
	 * @param id id à définir.
	 */
	public void setId(int id)
	{
		this.id = id;
	}
}
