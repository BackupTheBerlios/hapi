/*
 * Created on 25 f�vr. 2005
 *
 */
package hapi.donnees.modeles;

import java.util.ArrayList;

/**
 * @author boursier
 *
 */
public class ModeleTableOrdonnerActivites extends ModeleTable
{
    private static final long serialVersionUID = -2795637759107087075L;
    /**
	 * Permet de conserver les clefs des activites affich�es
	 */
	private ArrayList idActivites = null;

	public ModeleTableOrdonnerActivites()
	{
		idActivites = new ArrayList();
	}

	public ArrayList getIdActivites()
	{
		return idActivites;
	}
}
