package hapi.application.importation;

import hapi.application.ressources.Bundle;
import hapi.donnees.metier.E_Processus;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author boursier
 * 
 */
public class C_Parser
{
	// fichier de d�finition de processus 
	private InputStream fichierXML = null;
	// r�cup�rateur d'�v�nement lors du parsing du fichier
	private DefaultHandler handler = null;

	/**
	 * 
	 * @param fichier : fichier de d�fnition de processus
	 * @param leNomFichier : nom de ce fichier
	 * @param d : r�cup�rateur d'�venement associ�
	 * 			  (ProcessusHandler dans le  cas de parsage d'un
	 * 			   fichier dpe)
	 */
	public C_Parser(InputStream fichier, DefaultHandler d) throws SAXException
	{
		fichierXML = fichier;
		handler = d;
		/*if (handler instanceof ProcessusHandler)
			chargeProcessus();
		else
			chargeMesures();*/
		charge();
	}

	public void charge() throws SAXException
	{
		try
		{
			// pr�paration du parsing du fichier dpe, m�thode SAX
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser analyzer = factory.newSAXParser();
			//handler = new ProcessusHandler(nomFichier);
			// lancer le parsing
			analyzer.parse(fichierXML, handler);
		}
		catch (ParserConfigurationException e)
		{
			throw new SAXException(Bundle.getText("problemeParsage"));
		}
		catch (IOException e1)
		{
			throw new SAXException(Bundle.getText("problemeParsage"));
		}

	}
	/*
		public void chargeMesures()
		{
			try
			{
				// pr�paration du parsing du fichier dpe, m�thode SAX
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser analyzer = factory.newSAXParser();
				//handler = new ProcessusHandler(nomFichier);
				// lancer le parsing
				analyzer.parse(fichierDpe, handler);
			}
			catch (SAXException se)
			{
				se.printStackTrace();
			}
			catch (ParserConfigurationException e)
			{
				e.printStackTrace();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	*/
	/* processus charg� */
	public E_Processus getProcessus()
	{
		if (handler instanceof ProcessusHandler)
			return ((ProcessusHandler) handler).getProcessus();
		return null;
	}
}
