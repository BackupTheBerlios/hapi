/*
 * Créé le 29 sept. 2005
 */
package hapi.donnees;

import java.sql.Date;

/**
 * @author Cédric
 */
public class E_MesureFormation
{
	private int id = -1;
	private Date dateMesure;
	private int effectifProcessus;
	private int effectifMesure;
	private double note;
	
	public E_MesureFormation(int lId,Date laDateMesure,int lEffectifProcessus,int lEffectifMesure,double laNote)
	{
		id = lId;
		dateMesure = laDateMesure;
		effectifProcessus = lEffectifProcessus;
		effectifMesure = lEffectifMesure;
		note = laNote;
	}
	
	public E_MesureFormation(Date laDateMesure,int lEffectifProcessus,int lEffectifMesure,double laNote)
	{
		dateMesure = laDateMesure;
		effectifProcessus = lEffectifProcessus;
		effectifMesure = lEffectifMesure;
		note = laNote;
	}	
	
	
	/**
	 * @return Renvoie dateMesure.
	 */
	public Date getDateMesure()
	{
		return dateMesure;
	}
	/**
	 * @return Renvoie effectifMesure.
	 */
	public int getEffectifMesure()
	{
		return effectifMesure;
	}
	/**
	 * @return Renvoie effectifProcessus.
	 */
	public int getEffectifProcessus()
	{
		return effectifProcessus;
	}
	/**
	 * @return Renvoie id.
	 */
	public int getId()
	{
		return id;
	}
	/**
	 * @return Renvoie note.
	 */
	public double getNote()
	{
		return note;
	}
	
	/**
	 * @param id id à définir.
	 */
	public void setId(int id)
	{
		this.id = id;
	}	
}
