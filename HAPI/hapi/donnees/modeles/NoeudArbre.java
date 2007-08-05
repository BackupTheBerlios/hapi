package hapi.donnees.modeles;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 * @author Robin & Cécile
 */
public class NoeudArbre extends DefaultMutableTreeNode
{
    private static final long serialVersionUID = 9099663222204997823L;
    private InterfaceMetier leType = null;

	public NoeudArbre(InterfaceMetier leNoeud)
	{
		leType = leNoeud;
	}

	public InterfaceMetier getInfos()
	{
		return leType;
	}

	public String toString()
	{
		return leType.getNom();
	}

	public boolean equals(NoeudArbre unNoeud)
	{
		return leType.equals(unNoeud.getInfos());
	}
}
