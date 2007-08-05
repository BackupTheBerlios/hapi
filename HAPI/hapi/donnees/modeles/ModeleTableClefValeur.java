/*
 * Créé le 7 sept. 2005
 */
package hapi.donnees.modeles;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.DefaultTableModel;

/**
 * @author Cédric
 */
public class ModeleTableClefValeur extends DefaultTableModel implements Observer
{
    private static final long serialVersionUID = -590530947020134638L;

    public boolean isCellEditable(int row, int column)
	{
		if (column == 1)
			return true;
		else
			return false;
	}

	public void update(Observable arg0, Object arg1)
	{}
	
	public ArrayList getValeurs()
	{
		ArrayList retour = new ArrayList();
		for (int i=0;i<getRowCount();i++)
			retour.add(getValueAt(i,1));
		
		return retour;			
	}
}