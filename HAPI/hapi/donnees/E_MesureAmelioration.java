/*
 * Créé le 1 oct. 2005
 */
package hapi.donnees;

import java.sql.Date;

/**
 * @author Cédric
 */
public class E_MesureAmelioration
{
	private String idProcessus = null;
	private String dateExport = null;
	private Date dateChangement = null;
	private int nombrePropose = 0;
	private int nombrePris = 0;
	private int presentation = 0;
	private int modele = 0;
	private int documentation = 0;
	
	public E_MesureAmelioration(String idProcessus,String dateExport,Date dateChangement,int nombrePropose,int nombrePris,int presentation,int modele,int documentation)
	{
		this.idProcessus = idProcessus;
		this.dateExport = dateExport;
		this.dateChangement = dateChangement;
		this.nombrePropose = nombrePropose;
		this.nombrePris = nombrePris;
		this.presentation = presentation;
		this.modele = modele;
		this.documentation = documentation;		
	}
	
	/**
	 * @return Renvoie dateExport.
	 */
	public String getDateExport()
	{
		return dateExport;
	}
	
	/**
	 * @return Renvoie modele.
	 */
	public int getModele()
	{
		return modele;
	}
	
	/**
	 * @return Renvoie nombrePris.
	 */
	public int getNombrePris()
	{
		return nombrePris;
	}
	
	/**
	 * @return Renvoie nombrePropose.
	 */
	public int getNombrePropose()
	{
		return nombrePropose;
	}
	
	/**
	 * @return Renvoie presentation.
	 */
	public int getPresentation()
	{
		return presentation;
	}
	
	/**
	 * @return Renvoie idProcessus.
	 */
	public String getIdProcessus()
	{
		return idProcessus;
	}
	
	/**
	 * @return Renvoie dateChangement.
	 */
	public Date getDateChangement()
	{
		return dateChangement;
	}
	
	/**
	 * @return Renvoie documentation.
	 */
	public int getDocumentation()
	{
		return documentation;
	}
}
