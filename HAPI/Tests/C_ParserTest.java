/*
 * Created on 3 déc. 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package Tests;

import hapi.application.importation.C_Parser;
import hapi.application.importation.ProcessusHandler;
import hapi.application.metier.C_Activite;
import hapi.application.metier.C_Composant;
import hapi.application.metier.C_Definition;
import hapi.application.metier.C_ElementPresentation;
import hapi.application.metier.C_Etat;
import hapi.application.metier.C_Guide;
import hapi.application.metier.C_Interface;
import hapi.application.metier.C_PaquetagePresentation;
import hapi.application.metier.C_Processus;
import hapi.application.metier.C_Produit;
import hapi.application.metier.C_Role;
import hapi.application.metier.C_TypeGuide;
import hapi.application.metier.C_TypeProduit;
import hapi.donnees.metier.E_Activite;
import hapi.donnees.metier.E_Composant;
import hapi.donnees.metier.E_Definition;
import hapi.donnees.metier.E_ElementPresentation;
import hapi.donnees.metier.E_Etat;
import hapi.donnees.metier.E_Guide;
import hapi.donnees.metier.E_Interface;
import hapi.donnees.metier.E_PaquetagePresentation;
import hapi.donnees.metier.E_Processus;
import hapi.donnees.metier.E_Produit;
import hapi.donnees.metier.E_Role;
import hapi.donnees.metier.E_TypeGuide;
import hapi.donnees.metier.E_TypeProduit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

/**
 * @author boursier
 *
 */
public class C_ParserTest extends TestCase
{

	public C_ParserTest(String arg0)
	{
		super(arg0);
	}

	public void testC_Parser()
	{
		FileInputStream fichier = null;
		try
		{
			fichier = new FileInputStream("." + File.separator + "lib" + File.separator + "Processus IEPP2.1.dpe");
			//			lancement du parsage
			new C_Parser(fichier, new ProcessusHandler());
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Erreur :" + e.getMessage());
		}
		catch (SAXException saxe)
		{
			System.out.println("Erreur :" + saxe.getMessage());
		}

		// vérification des éléments parsés
		//		 processus
		assertTrue(C_Processus.size() == 1);
		System.out.println(C_Processus.getProcessus().keySet().toArray()[0]);

		E_Processus eproc = C_Processus.getProcessus("PUBe-PUBe");
		String id_processus = "PUBe-PUBe";
		System.out.println(eproc.getNomSansVersion());
		assertTrue(eproc.getNomSansVersion().equals("PUBe"));
		assertTrue(eproc.getNomAuteur().equals("Dupont"));
		assertTrue(eproc.getEmailAuteur().equals("dupont@free.fr"));
		assertTrue(eproc.getDateExport().equals("20050124123455"));
		assertTrue(eproc.getCheminGeneration().equals("./WebSite/"));
		assertTrue(eproc.getDescription().equals(""));
		assertTrue(eproc.getIdComposants().get(0).equals("PUBe-Expression_des_exigences"));
		assertTrue(eproc.getIdPaquetagePresentation().get(0).equals("_101"));

		// composant
		assertTrue(C_Composant.getComposantsDuProcessus(id_processus).size() == 2);

		E_Composant ec = C_Composant.getComposant(id_processus, "PUBe-Expression_des_exigences");
		assertTrue(ec.getNom().equals("Expression des exigences"));
		assertTrue(ec.getNomAuteur().equals("Dupuits"));
		assertTrue(ec.getEmailAuteur().equals("dupuits@free.fr"));
		assertTrue(ec.getVersion().equals("1.0"));
		assertTrue(ec.getDatePlacement().equals("20050121130000"));
		assertTrue(ec.getElementPresentationId().equals("_601"));
		assertTrue(ec.getInterfaceFournie().equals("_502"));
		assertTrue(ec.getInterfaceRequise().equals("_501"));
		assertTrue(((String) ec.getIdProduits().get(0)).equals("_301"));
		assertTrue(((String) ec.getIdRoles().get(0)).equals("_201"));
		assertTrue(((String) ec.getIdDefinitionTravail().get(0)).equals("_401"));

		// activite
		assertTrue(C_Activite.getActivitesDuProcessus(id_processus).size() == 5);
		E_Activite ea = C_Activite.getActivite(id_processus, "_801");
		assertTrue(ea.getNom().equals("Collecter les besoins"));
		assertTrue(ea.getParticipationRole().equals("_201"));
		assertTrue(ea.getAgregatDefinitionTravail().equals("_401"));
		assertTrue(ea.getElementPresentationId().equals("_616"));
		System.out.println(((String) ea.getProduitsEntree().get(0)));
		assertTrue(((String) ea.getProduitsEntree().get(0)).equals("_305"));
		assertTrue(((String) ea.getProduitsSortie().get(0)).equals("_302"));

		// definition de travail
		assertTrue(C_Definition.getDefinitionsDuProcessus(id_processus).size() == 3);

		E_Definition ed = C_Definition.getDefinition(id_processus, "_402");
		assertTrue(ed.getAgregatComposant().equals("PUBe-Expression_des_exigences"));
		assertTrue(ed.getElementPresentationId().equals("_614"));
		assertTrue(ed.getIdActivites().get(0).equals("_802"));
		assertTrue(ed.getNom().equals("Gérer les exigences"));

		//produit
		assertTrue(C_Produit.getProduitsDuProcessus(id_processus).size() == 7);

		E_Produit eprod = C_Produit.getProduit(id_processus, "_303");
		assertTrue(eprod.getAgregatComposant().equals("PUBe-Gestion_de_projet"));
		assertTrue(eprod.getElementPresentationId().equals("_609"));
		assertTrue(eprod.getIdResponsabilite().equals("_204"));
		assertTrue(eprod.getTypeProduitId().equals("_3010"));
		assertTrue(eprod.getNom().equals("Plan d'itération"));
		assertTrue(eprod.getIdEtat().size() == 0);
		assertTrue(eprod.getInterfaceId().size() == 0);
		assertTrue(eprod.getIdEntreeActivite().get(1).equals("_805"));
		assertTrue(eprod.getIdSortieActivite().get(1).equals("_805"));

		// interface
		assertTrue(C_Interface.getInterfacesDuProcessus(id_processus).size() == 4);

		E_Interface einter = C_Interface.getInterface(id_processus, "_501");
		assertTrue(einter.getInterfaceFournieComposant().equals(""));
		assertTrue(einter.getInterfaceRequiseComposant().equals("PUBe-Expression_des_exigences"));
		assertTrue(einter.getInterfaceProduit().get(0).equals("_305"));

		// type de produit
		assertTrue(C_TypeProduit.getTypesProduitDuProcessus(id_processus).size() == 1);

		E_TypeProduit etprod = C_TypeProduit.getTypeProduit(id_processus, "_3010");
		assertTrue(etprod.getNom().equals("Document"));

		// type de  guide
		assertTrue(C_TypeGuide.getTypesGuideDuProcessus(id_processus).size() == 2);

		E_TypeGuide etguide = C_TypeGuide.getTypeGuide(id_processus, "_1100");
		assertTrue(etguide.getNom().equals("Exemple"));

		// guide
		assertTrue(C_Guide.getGuidesDuProcessus(id_processus).size() == 2);

		E_Guide eguide = C_Guide.getGuide(id_processus, "_702");
		assertTrue(eguide.getNom().equals("Exemple POG"));
		assertTrue(eguide.getElementPresentationId().equals("_623"));
		assertTrue(eguide.getTypeGuideId().equals("_1100"));

		// etat
		assertTrue(C_Etat.getEtatsDuProcessus(id_processus).size() == 1);

		E_Etat eetat = C_Etat.getEtat(id_processus, "_901");
		assertTrue(eetat.getNom().equals("Commencé"));

		// role
		assertTrue(C_Role.getRolesDuProcessus(id_processus).size() == 4);

		E_Role erole = C_Role.getRole(id_processus, "_203");
		assertTrue(erole.getNom().equals("Equipier"));
		assertTrue(erole.getAgregatComposant().equals("PUBe-Gestion_de_projet"));
		assertTrue(erole.getElementPresentationId().equals("_605"));
		assertTrue(erole.getIdParticipationsActivite().get(0).equals("_804"));
		assertTrue(erole.getIdResponsabilitesProduit().size() == 0);

		// paquetage de présentation
		assertTrue(C_PaquetagePresentation.getPaquetagesPresentationDuProcessus(id_processus).size() == 1);

		E_PaquetagePresentation apaq = C_PaquetagePresentation.getPaquetagePresentation(id_processus, "_101");
		assertTrue(apaq.getNom().equals("A propos"));
		assertTrue(apaq.getDossierContenu().equals("./A propos/contenu/"));
		assertTrue(apaq.getDossierIcone().equals("./applet/images/"));
		assertTrue(apaq.getElementPresentationId().equals("_622"));
		assertTrue(apaq.getAgregeElementPresentation().get(0).equals("_625"));

		// élément de présentation
		assertTrue(C_ElementPresentation.getElementsPresentationDuProcessus(id_processus).size() == 23);

		E_ElementPresentation eelem = C_ElementPresentation.getElementPresentation(id_processus, "_601");
		assertTrue(eelem.getNom().equals("Expression des exigences"));
		assertTrue(eelem.getCheminIcone().equals("./applet/images/TreeComponent.gif"));
		assertTrue(eelem.getCheminContenu().equals("./Expression_des_exigences/contenu/dis_exi.htm"));
		assertTrue(eelem.getDescription().equals(""));
		assertTrue(eelem.getCheminPage().equals("./Expression_des_exigences/Expression_des_exigences_32.html"));
		assertTrue(eelem.getGuideId().size() == 0);

	}

}
