/*
 * Auteur Cédric
 *
 */
package Tests;

import hapi.application.C_Configuration;
import hapi.exception.ChampsVideException;
import hapi.exception.FichierConfInexistantException;
import junit.framework.TestCase;

/**
 * Commentaire sur la classe/la méthode
 */
public class C_ConfigurationTest extends TestCase
{

	public C_ConfigurationTest(String arg0)
	{
		super(arg0);
	}

	public void testAddUtilisateur()
	{
		try
		{
			new C_Configuration(true);
		}
		catch (FichierConfInexistantException e)
		{
			assertTrue(false);
		}

		try
		{
			C_Configuration.supprimeUtilisateur("test", 2);
			C_Configuration.supprimeUtilisateur("test", 3);
		}
		catch (Exception e2)
		{
			//assertTrue(false);
		}

		try
		{
			C_Configuration.addUtilisateur("test", "test1", "", 1);
			assertTrue(false);
		}
		catch (ChampsVideException e1)
		{
			assertTrue(true);
		}

		try
		{
			C_Configuration.addUtilisateur("test", "test1", "test2", 1);
			assertTrue(true);
		}
		catch (ChampsVideException e1)
		{
			assertTrue(false);
		}

		try
		{
			assertFalse(C_Configuration.addUtilisateur("test", "test1", "test2", 1));
		}
		catch (ChampsVideException e1)
		{
			assertTrue(false);
		}

		try
		{
			assertTrue(C_Configuration.addUtilisateur("test", "test1", "test2", 2));
		}
		catch (ChampsVideException e1)
		{
			assertTrue(false);
		}
	}

	public void testSupprimeUtilisateur()
	{
		try
		{
			C_Configuration.supprimeUtilisateur("test", 2);
		}
		catch (Exception e)
		{
			assertTrue(false);
		}
		try
		{
			assertTrue(C_Configuration.addUtilisateur("test", "test1", "test2", 1));
		}
		catch (ChampsVideException e1)
		{
			assertTrue(false);
		}
		try
		{
			C_Configuration.supprimeUtilisateur("test", 2);
			C_Configuration.supprimeUtilisateur("test", 3);
			assertTrue(true);
		}
		catch (Exception e2)
		{
			assertTrue(false);
		}

	}

	public void testConvertRole()
	{
		assertTrue((C_Configuration.convertRole("Process supervisor") == 1) || (C_Configuration.convertRole("Responsable processus") == 1));
		assertTrue((C_Configuration.convertRole("Process director") == 2) || (C_Configuration.convertRole("Directeur processus") == 2));
		assertTrue((C_Configuration.convertRole("Process engineer") == 3) || (C_Configuration.convertRole("Ingénieur processus") == 3));
	}
}
