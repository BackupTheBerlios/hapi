/*
 * Auteur Cédric
 *
 */
package hapi.donnees.modeles;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

/**
 * Modèle de la liste
 */
public class ModeleListe extends AbstractListModel implements Observer
{
    private static final long serialVersionUID = -5687162555661914361L;
    private ArrayList lesDonnees = new ArrayList();

	public void clearDonnees()
	{
		lesDonnees.clear();
		fireContentsChanged(this, 0, lesDonnees.size() - 1);
	}

	public void addDonnees(ArrayList info)
	{
		lesDonnees.clear();
		lesDonnees = info;
	}

	public void addDonnees(Object info)
	{
		lesDonnees.add(info);
		fireContentsChanged(this, 0, lesDonnees.size() - 1);
	}

	public int getSize()
	{
		return lesDonnees.size();
	}

	public Object getElementAt(int position)
	{
		if (position >= lesDonnees.size() || position < 0)
			return null;
		else
			return lesDonnees.get(position);
	}
	
	public int getIndexOf(Object element)
	{
		return lesDonnees.indexOf(element);
	}	

	public void update(Observable arg0, Object arg1)
	{}

}
