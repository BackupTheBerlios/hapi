/*
 * Créé le 23 sept. 2005
 */
package hapi.donnees;

import java.sql.Date;

/**
 * @author Cédric
 */
public class E_RevueProcessus
{
	/**
	 * @param dateProchaineRevue dateProchaineRevue à définir.
	 */
	public void setDateProchaineRevue(Date dateProchaineRevue)
	{
		this.dateProchaineRevue = dateProchaineRevue;
	}
	// Identifiant
	private int idRevue = -1;

	// Identifiant du processus lié
	private String idProcessus = null;

	// Date de la revue
	private Date dateRevue = null;
	private Date dateProchaineRevue = null;

	// Action
	private String action = null;
	
	// Decision
	private String decision = null;
	
	public E_RevueProcessus(int idRevue,Date dateRevue, Date dateProchaineRevue, String decision, String action, String idProcessus)
	{
		this.action= action;
		this.idRevue = idRevue;
		this.decision = decision;
		this.dateRevue = dateRevue;
		this.dateProchaineRevue = dateProchaineRevue;
		this.idProcessus = idProcessus;	
	}
	
	public E_RevueProcessus(Date dateRevue, Date dateProchaineRevue, String decision, String action, String idProcessus)
	{
		this.action= action;
		this.decision = decision;
		this.dateRevue = dateRevue;
		this.dateProchaineRevue = dateProchaineRevue;
		this.idProcessus = idProcessus;	
	}	

	/**
	 * @return action
	 */
	public String getAction()
	{
		return action;
	}

	/**
	 * @return dateRevue
	 */
	public Date getDateRevue()
	{
		return dateRevue;
	}

	/**
	 * @return decision
	 */
	public String getDecision()
	{
		return decision;
	}

	/**
	 * @return idProcessus
	 */
	public String getIdProcessus()
	{
		return idProcessus;
	}

	/**
	 * @return idRevue
	 */
	public int getIdRevue()
	{
		return idRevue;
	}

	/**
	 * @param string action
	 */
	public void setAction(String string)
	{
		action = string;
	}

	/**
	 * @param date dateRevue
	 */
	public void setDateRevue(Date date)
	{
		dateRevue = date;
	}

	/**
	 * @param string decision
	 */
	public void setDecision(String string)
	{
		decision = string;
	}

	/**
	 * @param string idProcessus
	 */
	public void setIdProcessus(String string)
	{
		idProcessus = string;
	}

	/**
	 * @param int idRevue
	 */
	public void setIdRevue(int i)
	{
		idRevue = i;
	}

	/**
	 * @return Renvoie dateProchaineRevue.
	 */
	public Date getDateProchaineRevue()
	{
		return dateProchaineRevue;
	}
}