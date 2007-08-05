package hapi.donnees.modeles;

import hapi.application.ressources.Bundle;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.DefaultTableModel;

/**
 * @author Vincent
 */
public class ModeleTableOrdreDesActivites extends DefaultTableModel implements Observer
{
    private static final long serialVersionUID = 2696956020591910850L;
    //liste ordonnée des activités à afficher
	private ArrayList ordresActivites = new ArrayList();
	private ArrayList nomsActivites = new ArrayList();

	public ModeleTableOrdreDesActivites(ArrayList ordresAct, ArrayList listeNomsAct)
	{
		super();
		this.ordresActivites = ordresAct;
		this.nomsActivites = listeNomsAct;
	}

	public void update(Observable arg0, Object arg1)
	{}

	public int getColumnCount()
	{
		return 2;
	}

	public String getColumnName(int numCol)
	{
		switch (numCol)
		{
			case 0 :
				return Bundle.getText("OO_TableauDeBord_tableau_col1");
			case 1 :
				return Bundle.getText("OO_TableauDeBord_tableau_col2");
			default :
				return "#";
		}
	}

	public int getRowCount()
	{
		try
		{
			return this.ordresActivites.size();
		}
		catch (NullPointerException npe)
		{
			return 0;
		}
	}

	public Object getValueAt(int indexRow, int indexCol)
	{
		if (indexCol == 0)
			return String.valueOf(this.ordresActivites.get(indexRow));
		return (String) this.nomsActivites.get(indexRow);
	}

	public boolean isCellEditable(int arg0, int arg1)
	{
		return false;
	}

}
