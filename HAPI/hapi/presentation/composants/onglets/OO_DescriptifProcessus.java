package hapi.presentation.composants.onglets;

import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Robin & Cécile
 */

public class OO_DescriptifProcessus extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = 3300449587680470210L;
    //panels
	JPanel SC_Composant = null;
	JPanel SC_Infos = null;
	JPanel SC_Descriptif = null;
	JScrollPane SC_Scroll = null;
	JPanel SC_Titre = null;
	JPanel SC_Gauche = null;
	JPanel SC_Droit = null;
	JPanel SC_Haut = null;
	JPanel SC_Bas = null;
	//labels
	JLabel STC_Identifiant = null;
	JLabel STV_Identifiant = null;
	JLabel STC_NomResponsable = null;
	JLabel STV_NomResponsable = null;
	JLabel STC_Nom = null;
	JTextArea STV_Description = null;
	JLabel STC_Description = null;
	JLabel STC_NomAuteur = null;
	JLabel STV_Nom = null;
	JLabel STC_CheminGeneration = null;
	JLabel STV_CheminGeneration = null;
	JLabel STV_EMailAuteur = null;
	JLabel STC_EMailAuteur = null;
	JLabel STV_NomAuteur = null;
	JLabel STC_Version = null;
	JLabel STV_Version = null;

	//layouts
	BorderLayout layoutFond = null;
	BorderLayout layoutComposants = null;
	BorderLayout layoutInfos = null;
	GridLayout layoutTitre = null;
	GridLayout layoutDescriptif = null;

	// idenitifiant du processus courant
	String identifiantProcessus = null;
	
	public OO_DescriptifProcessus(String identifiant)
	{
		super();

		identifiantProcessus = identifiant;
		creationElements();
		operationSurBoutons();
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
		operationMoteur();
		updateTexte();
	}

	public void creationElements()
	{
		SC_Descriptif = new JPanel();
		SC_Titre = new JPanel();
		SC_Infos = new JPanel();
		SC_Composant = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droit = new JPanel();
		SC_Haut = new JPanel();
		SC_Bas = new JPanel();
		SC_Scroll = new JScrollPane();

		layoutFond = new BorderLayout();
		layoutComposants = new BorderLayout();
		layoutTitre = new GridLayout(8, 0);
		layoutDescriptif = new GridLayout(8, 0);
		layoutInfos = new BorderLayout();

		STC_Identifiant = new JLabel();
		STV_Identifiant = new JLabel();
		STC_NomResponsable = new JLabel();
		STV_NomResponsable = new JLabel();
		STC_Nom = new JLabel();
		STV_Description = new JTextArea();
		STC_Description = new JLabel();
		STC_NomAuteur = new JLabel();
		STV_Nom = new JLabel();
		STC_CheminGeneration = new JLabel();
		STV_CheminGeneration = new JLabel();
		STV_EMailAuteur = new JLabel();
		STC_EMailAuteur = new JLabel();
		STV_NomAuteur = new JLabel();
		STV_Version = new JLabel();
		STC_Version = new JLabel();
	}

	public void operationSurBoutons()
	{}

	public void operationSurComposants()
	{
		STV_Description.setEditable(false);
		STV_Description.setBackground(Color.WHITE);
		STV_Description.setLineWrap(true);

		SC_Scroll.setPreferredSize(new Dimension(100, 85));
		// affichage des labels statiques
		STC_Identifiant.setText(Bundle.getText("OO_DescriptifProcessus_identifiant"));
		STC_NomResponsable.setText((Bundle.getText("OO_DescriptifProcessus_nom_responsable")));
		STC_Nom.setText(Bundle.getText("OO_DescriptifProcessus_nom"));
		STC_Description.setText(Bundle.getText("OO_DescriptifProcessus_description"));
		STC_NomAuteur.setText(Bundle.getText("OO_DescriptifProcessus_nom_auteur"));
		STC_CheminGeneration.setText(Bundle.getText("OO_DescriptifProcessus_chemin_generation"));
		STC_EMailAuteur.setText(Bundle.getText("OO_DescriptifProcessus_email_auteur"));
		STC_Version.setText(Bundle.getText("OO_DescriptifProcessus_version"));
	}

	public void operationSurPanel()
	{
		// placement des composants
		SC_Descriptif.setLayout(layoutDescriptif);
		SC_Descriptif.add(STV_Identifiant);
		SC_Descriptif.add(STV_Nom);
		SC_Descriptif.add(STV_Version);
		SC_Descriptif.add(STV_NomResponsable);
		SC_Descriptif.add(STV_NomAuteur);
		SC_Descriptif.add(STV_EMailAuteur);
		SC_Descriptif.add(STV_CheminGeneration);

		SC_Scroll.setViewportView(STV_Description);

		SC_Titre.setLayout(layoutTitre);
		SC_Titre.add(STC_Identifiant);
		SC_Titre.add(STC_Nom);
		SC_Titre.add(STC_Version);
		SC_Titre.add(STC_NomResponsable);
		SC_Titre.add(STC_NomAuteur);
		SC_Titre.add(STC_EMailAuteur);
		SC_Titre.add(STC_CheminGeneration);
		SC_Titre.add(STC_Description);

		SC_Infos.setLayout(layoutInfos);
		SC_Infos.add(SC_Titre, BorderLayout.WEST);
		SC_Infos.add(SC_Descriptif, BorderLayout.CENTER);
		SC_Infos.add(SC_Scroll, BorderLayout.SOUTH);

		SC_Composant.setLayout(layoutComposants);
		SC_Composant.add(SC_Infos, BorderLayout.NORTH);
	}

	public void operationSurFenetre()
	{
		// organisation de l'onglet
		this.setLayout(layoutFond);
		this.add(SC_Composant, BorderLayout.CENTER);
		this.add(SC_Gauche, BorderLayout.WEST);
		this.add(SC_Droit, BorderLayout.EAST);
		this.add(SC_Haut, BorderLayout.NORTH);
		this.add(SC_Bas, BorderLayout.SOUTH);
	}

	public void operationMoteur()
	{}

	public void updateTexte()
	{
		// affichage des labels dynamiques en fonction du processus courant
		ArrayList laListe = C_Processus.getDescriptifProcessus(identifiantProcessus);
		STV_NomAuteur.setText((String) laListe.get(0));
		STV_Identifiant.setText((String) laListe.get(1));
		STV_NomResponsable.setText((String) laListe.get(2));
		STV_Nom.setText((String) laListe.get(3));
		STV_EMailAuteur.setText((String) laListe.get(4));
		STV_Description.setText((String) laListe.get(5));
		STV_CheminGeneration.setText((String) laListe.get(6));
		//Font laFonte = new Font(STV_CheminGeneration.getFont().getFontName(), Font.BOLD, STV_CheminGeneration.getFont().getSize());
		//STV_CheminGeneration.setCursor(new Cursor(Cursor.HAND_CURSOR));
		//STV_CheminGeneration.setForeground(Color.BLUE);
		//STV_CheminGeneration.addMouseListener(GenerationMouseListener);
		
		STV_Version.setText(C_Processus.getDateExportFormatee(identifiantProcessus));
	}
	
	/*private void doURL()
	{
		URL lURL;
		try
		{
			//lURL = new URL(STV_CheminGeneration.getText());
			lURL = new URL("http://atisi.free.fr/");
			lURL.openConnection();
			lURL.toExternalForm();
		}
		catch (MalformedURLException e)
		{
			// TODO Bloc catch auto-généré
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Bloc catch auto-généré
			e.printStackTrace();
		}
	}*/
}