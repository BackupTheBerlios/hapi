/*
 * Créé le 18 sept. 2005
 */
package hapi.donnees;

import hapi.application.ressources.Bundle;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * @author Cédric
 */
public class E_NiveauMaturite
{
	private int niveau = 0;
	private String commentaire = new String();
	private Date datePassage = null;
	private Date dateObjectif = null;
	
	public E_NiveauMaturite(int leNiveau,String leCommentaire, Date laDatePassage, Date laDateObjectif)
	{
		niveau = leNiveau;
		commentaire = leCommentaire;
		datePassage = laDatePassage;
		dateObjectif = laDateObjectif;
	}
	
	/**
	 * @return Renvoie commentaire.
	 */
	public String getCommentaire()
	{
		return commentaire;
	}
	
	/**
	 * @param commentaire commentaire à définir.
	 */
	public void setCommentaire(String commentaire)
	{
		this.commentaire = commentaire;
	}
	
	/**
	 * @return Renvoie dateObjectif.
	 */
	public Date getDateObjectif()
	{
		return dateObjectif;
	}
	
	/**
	 * @return Renvoie dateObjectif.
	 */
	public String getDateObjectifFormateeHTML()
	{
		SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.DATE_MODEL);
		return ((dateObjectif==null)?"&nbsp;":sfDate.format(dateObjectif));
	}	
	
	/**
	 * @param dateObjectif dateObjectif à définir.
	 */
	public void setDateObjectif(Date dateObjectif)
	{
		this.dateObjectif = dateObjectif;
	}
	
	/**
	 * @return Renvoie datePassage.
	 */
	public Date getDatePassage()
	{
		return datePassage;
	}
	
	/**
	 * @return Renvoie datePassage.
	 */
	public String getDatePassageFormateeHTML()
	{
		SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.DATE_MODEL);
		return ((datePassage==null)?"&nbsp;":sfDate.format(datePassage));
	}	
	
	/**
	 * @param datePassage datePassage à définir.
	 */
	public void setDatePassage(Date datePassage)
	{
		this.datePassage = datePassage;
	}
	
	/**
	 * @return Renvoie niveau.
	 */
	public int getNiveau()
	{
		return niveau;
	}
}
