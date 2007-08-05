package hapi.donnees;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import java.util.Date;

import javax.swing.ImageIcon;


/**
 * @author Stéphane
 */
public class E_Tache implements InterfaceMetier
{
	private String idTache;
	private String nomTache;
	private double tempsPasse;
	private Date dateFinReelle = null;
	private ImageIcon icone;

	public E_Tache(String id, String nom, double tpsPasse, Date dateFin, ImageIcon lIcone)
	{
		idTache = id;
		nomTache = nom;
		tempsPasse = tpsPasse;
		dateFinReelle = dateFin;
		icone = lIcone;
	}

	public double getTempsPasse()
	{
		return tempsPasse;
	}
	public Date getDateFinReelle()
	{
		return dateFinReelle;
	}

	public String getNom()
	{
		return nomTache;
	}

	public String getIdentifiant()
	{
		return nomTache;
	}

	public ImageIcon getIcone()
	{
		return icone;
	}

    public String getIdTache()
    {
        return idTache;
    }
}
