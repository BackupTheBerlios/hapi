/*
 * Auteur C�dric
 *
 */
package hapi.application.modele;

import hapi.donnees.modeles.ModeleListe;

import java.util.ArrayList;

/**
 * Fabrique de mod�le de listes
 */
public class ListModelFactory
{
	ModeleListe leModele = new ModeleListe();

	public void clearDonnees()
	{
		leModele.clearDonnees();
	}

	public void addDonnees(ArrayList info)
	{
		leModele.addDonnees(info);
	}

	public void addDonnees(Object info)
	{
		leModele.addDonnees(info);
	}

	public int getSize()
	{
		return leModele.getSize();
	}

	public Object getElementAt(int position)
	{
		return leModele.getElementAt(position);
	}

	public ModeleListe getModel()
	{
		return leModele;
	}

}
