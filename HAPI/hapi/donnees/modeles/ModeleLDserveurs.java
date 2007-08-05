/*
 * Auteur Cédric
 *
 */
package hapi.donnees.modeles;

import hapi.donnees.E_Serveur;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

/**
 * Modele des serveurs, liste unique
 */
public class ModeleLDserveurs extends DefaultComboBoxModel
{
    private static final long serialVersionUID = 7083501866215533880L;

    public void initialise(ArrayList lesServeurs)
	{
		for (int i = 0; i < lesServeurs.size(); i++)
		{
			this.addElement(((E_Serveur) lesServeurs.get(i)).getChemin());
			if (((E_Serveur) lesServeurs.get(i)).isSelected())
				this.setSelectedItem(((E_Serveur) lesServeurs.get(i)).getChemin());
		}
	}

	public void addElement(Object arg0)
	{
		//Pour empêcher les doublons
		if (this.getIndexOf(arg0) < 0)
		{
			if (arg0 instanceof String)
				if (!((String) arg0).equals(""))
					super.addElement(arg0);
		}
	}
}
