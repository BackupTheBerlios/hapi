/*
 * Créé le 20 sept. 2005
 */
package hapi.application.interfaces;

import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * @author Cédric
 */
public abstract class FenetreAssistee extends JPanel
{
	private int numeroFenetre;
	public int getNumeroFenetre()
	{
		return numeroFenetre;
	}
	public void setNumeroFenetre(int numero)
	{
		numeroFenetre = numero;
	}
	public abstract ArrayList getParametresSaisis();
	public abstract void setParametres(ArrayList lesParametres);
}
