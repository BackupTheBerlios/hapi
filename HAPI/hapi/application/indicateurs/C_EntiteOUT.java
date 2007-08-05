package hapi.application.indicateurs;

import hapi.donnees.metier.interfaces.InterfaceMetier;
import hapi.donnees.modeles.ModeleTable;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Robin EYSSERIC
 */
public class C_EntiteOUT
{
	public final static int ACTIVITE = 0;
	public final static int ROLE = 1;
	public final static int PRODUIT = 2;	
	
	private ModeleTable modeleTable = null;

	public C_EntiteOUT(ArrayList liste)
	{
		modeleTable = new ModeleTable();

		modeleTable.addColumn("X");

		if (liste != null)
		{
			for (int i = 0; i < liste.size(); i++)
			{
				ajouterEntite(((InterfaceMetier) liste.get(i)).getNom());
			}
		}
	}

	/**
	 * @return
	 */
	public ModeleTable getModeleTable()
	{
		return modeleTable;
	}

	public void ajouterEntite(String entite)
	{
		Vector entites = new Vector();
		entites.add(entite);
		modeleTable.addRow(entites);
	}

	public void supprimerEntite(int ligne)
	{
		modeleTable.removeRow(ligne);
	}

	public ArrayList getListeEntiteOUT()
	{
		ArrayList entites = new ArrayList();

		for (int i = 0; i < modeleTable.getRowCount(); i++)
		{
			entites.add(modeleTable.getValueAt(i, 0));
		}

		return entites;
	}

}
