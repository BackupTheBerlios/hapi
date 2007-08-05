package hapi.donnees.modeles;

import java.util.ArrayList;

/**
 * @author Fabien PASCAL
 */
public class ModeleTableHistoriqueModifications extends ModeleTable
{
    private static final long serialVersionUID = 8062181223241431022L;
    /**
	 * Permet de conserver les clefs primaires des demandes
	 * dans le modele sans les afficher
	 */
	private ArrayList idModifications = null;

	public ModeleTableHistoriqueModifications()
	{
		idModifications = new ArrayList();
	}

	public ArrayList getIdModifications()
	{
		return idModifications;
	}

}