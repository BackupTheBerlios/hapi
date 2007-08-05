/*
 * Fichier ModeleSpinner.java
 * Auteur C�dric
 *
 */
package hapi.application.modele;

import java.util.Observable;
import java.util.Observer;

import javax.swing.SpinnerNumberModel;

/**
 * Mod�le su spinner du niveau de maturit�
 */
public class ModeleSpinner extends SpinnerNumberModel implements Observer
{
    private static final long serialVersionUID = 1364967149602153380L;

    public ModeleSpinner(int valMini,int valMaxi)
	{
		super();
		super.setMaximum(new Integer(valMaxi));
		super.setMinimum(new Integer(valMini));
	}

	public void update(Observable arg0, Object arg1)
	{}

}
