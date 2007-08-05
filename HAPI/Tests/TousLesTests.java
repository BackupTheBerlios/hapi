/*
 * Auteur C�dric
 *
 */
package Tests;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Commentaire sur la classe/la m�thode
 */
public class TousLesTests
{

	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(TousLesTests.class);
	}

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for Tests");
		//$JUnit-BEGIN$

		//Ajouter la ligne suivante pour chaque classe de tests
		//suite.addTest(new TestSuite(<nom de la classe>.class));

		/* Apr�s avoir effectu� un test, il est imp�ratif de supprimer 
		 sur le compte m1isb5 de marine : 
		 - le fichier testJUnitClassFTP.txt
		 - le dossier JUnitCreationRepFTP
		 Si ces suppressions ne sont pas faites, le cas de test �choue.
		 Des m�thodes suppl�mentaires seront impl�ment�es prochainement
		 (supprimerRepertoire , ...) */
		suite.addTest(new TestSuite(C_FTPTest.class));
		suite.addTest(new TestSuite(C_LocalTest.class));
		//suite.addTest(new TestSuite(C_ParserTest.class));
		//suite.addTest(new TestSuite(C_ImporterProcessus.class));
		suite.addTest(new TestSuite(C_ConfigurationTest.class));

		//$JUnit-END$
		return suite;
	}
}
