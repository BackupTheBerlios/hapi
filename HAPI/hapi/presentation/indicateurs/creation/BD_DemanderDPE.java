/*
 * Créé le 24 juin 2005
 */
package hapi.presentation.indicateurs.creation;

import hapi.application.importation.C_ImportationProcessus;
import hapi.application.importation.C_Parser;
import hapi.application.importation.ProcessusHandler;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.metier.C_Processus;
import hapi.application.metier.temporaire.C_ProcessusTemporaire;
import hapi.application.ressources.Bundle;
import hapi.exception.StopActionException;
import hapi.presentation.BD_ImporterProcessus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.xml.sax.SAXException;

/**
 * @author Cédric
 */
public class BD_DemanderDPE
{
	static public boolean doitUtiliserDPECourant(JFrame appui, String idProcessus, String idProjet) throws StopActionException
	{
		return verificationSiUtiliserDPECourant(appui,null,idProcessus,C_ExecutionProcessus.getExecutionProcessus(idProcessus, idProjet).getVersionProcessus());
	}
	
	static public boolean doitUtiliserDPECourant(JDialog appui, String idProcessus, String idProjet) throws StopActionException
	{
		return verificationSiUtiliserDPECourant(null,appui,idProcessus,C_ExecutionProcessus.getExecutionProcessus(idProcessus, idProjet).getVersionProcessus());
	}
	
	
	static private boolean verificationSiUtiliserDPECourant(JFrame appui1, JDialog appui2, String idProcessus, String versionConcernee) throws StopActionException
	{
		//Déclarations
		boolean avertissement = false;
		String dateExport = C_Processus.getProcessus(idProcessus).getDateExport();

		//Initialisation au cas où le dpe est directement le bon
		C_ImportationProcessus.effacerEntitesTemporaires();

		//Vérification de présence en local
		if (demanderDPE(idProcessus, dateExport, versionConcernee))
		{
			existeEnLocal(idProcessus, versionConcernee);
			if (C_ProcessusTemporaire.get() != null)
				dateExport = C_ProcessusTemporaire.get().getDateExport();
		}

		//Tant que la date d'export du processus ne corresponsad pas au projet
		while (demanderDPE(idProcessus, dateExport, versionConcernee))
		{
			//Sauf au premier tour, on prévient l'utilisateur qu'il a pas
			// sélectionné ce qu'il faut
			if (avertissement)
			{
				if (appui1 != null)
					JOptionPane.showMessageDialog(appui1, Bundle.getText("BD_ImporterProcessus_MauvaisDPE"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
				else
					JOptionPane.showMessageDialog(appui2, Bundle.getText("BD_ImporterProcessus_MauvaisDPE"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
			}
			else
				avertissement = true;
			//L'importation se fait dans les entités
			C_ImportationProcessus.effacerEntitesTemporaires();
			//Affichage de la boîte d'importation
			BD_ImporterProcessus laFenetre;
			if (appui1 != null)
				laFenetre = new BD_ImporterProcessus(appui1, false);
			else
				laFenetre = new BD_ImporterProcessus(appui2, false);
			laFenetre.setVisible(true);
			if (laFenetre.getAnnulationComplete())
				throw new StopActionException();

			//Si le processus est utilisable
			if (C_ProcessusTemporaire.get() != null)
				dateExport = C_ProcessusTemporaire.get().getDateExport();
			else
				break;
		}

		return (C_ProcessusTemporaire.get() == null);
	}

	/*static public boolean doitUtiliserDPECourant(JDialog appui, String idProcessus, String idProjet) throws StopActionException
	{
		//Déclarations
		boolean avertissement = false;
		String dateExport = C_Processus.getProcessus(idProcessus).getDateExport();

		//Initialisation au cas où le dpe est directement le bon
		C_ImportationProcessus.effacerEntitesTemporaires();

		//Tant que la date d'export du processus ne corresponsad pas au projet
		while (demanderDPE(idProcessus, dateExport, idProjet))
		{
			//Sauf au premier tour, on prévient l'utilisateur qu'il a pas
			// sélectionné ce qu'il faut
			if (avertissement)
				JOptionPane.showMessageDialog(appui, Bundle.getText("BD_ImporterProcessus_MauvaisDPE"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
			else
				avertissement = true;
			//L'importation se fait dans les entités
			C_ImportationProcessus.effacerEntitesTemporaires();
			//Affichage de la boîte d'importation
			BD_ImporterProcessus laFenetre = new BD_ImporterProcessus(appui, false);
			laFenetre.setVisible(true);
			if (laFenetre.getAnnulationComplete())
				throw new StopActionException();

			//Si le processus est utilisable
			if (C_ProcessusTemporaire.get() != null)
				dateExport = C_ProcessusTemporaire.get().getDateExport();
			else
				break;
		}

		return (C_ProcessusTemporaire.get() == null);
	}*/

	static private void existeEnLocal(String idProcessus, String dateExport)
	{
		String nomDuFichier = idProcessus + dateExport + C_ImportationProcessus.SUFFIXE_SAUVEGARDE + C_ImportationProcessus.EXTENSION_SAUVEGARDE;
		File fichierSauvegarde = new File(C_ImportationProcessus.CHEMIN_SAUVEGARDE + nomDuFichier);

		InputStream fic;
		try
		{
			fic = new FileInputStream(fichierSauvegarde);
			ProcessusHandler handler = new ProcessusHandler();
			new C_Parser(fic, handler);
		}
		catch (FileNotFoundException e)
		{}
		catch (SAXException e1)
		{}
	}
	
	static private 	boolean demanderDPE(String idProcessus, String exportProcessus, String versionConcernee)
	{
		return !(exportProcessus.equals(versionConcernee));
	}
}
