package hapi.application.metier.temporaire;

import hapi.donnees.metier.E_Processus;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_ProcessusTemporaire
{
	private static E_Processus leProcessus = null;

	public synchronized static E_Processus get()
	{
		return leProcessus;
	}

	public synchronized static void setProcessus(E_Processus leProcessTmp)
	{
		leProcessus = leProcessTmp;
	}
}
