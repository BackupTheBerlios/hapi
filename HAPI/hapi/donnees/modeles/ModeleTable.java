/*
 * Auteur Cédric
 *
 */
package hapi.donnees.modeles;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.DefaultTableModel;

/**
 * Modele de table non modifiable
 */
public class ModeleTable extends DefaultTableModel implements Observer
{
    private static final long serialVersionUID = -3436574532562879375L;

    public boolean isCellEditable(int row, int column)
	{
		return false;
	}

	public void update(Observable arg0, Object arg1)
	{}
}
