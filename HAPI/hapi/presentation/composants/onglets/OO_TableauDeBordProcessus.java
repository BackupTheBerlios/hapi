package hapi.presentation.composants.onglets;

import hapi.application.metier.C_Processus;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * @author Vincent
 */
public class OO_TableauDeBordProcessus extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = 5042462563670010032L;
    private String identifiantProcessus = null;
	private JTabbedPane SC_Onglet = null;
	
	//creation d'un onglet pour chaque version du processus
	ArrayList lesVersionsDuProcess = null;
	ArrayList lesVersionsFormateesDuProcess = null;
	ArrayList lesNumerosDuProcess = null;	

	public OO_TableauDeBordProcessus(String idProcessus)
	{
		super();
		this.identifiantProcessus = idProcessus;
		//Création des éléments
		creationElements();
		operationSurPanel();
		operationSurFenetre();
	}

	public void creationElements()
	{
		//creation d'un onglet pour chaque version du processus
		lesVersionsDuProcess = C_Processus.getProcessus(this.identifiantProcessus).getListeDateExport();
		lesVersionsFormateesDuProcess = C_Processus.getProcessus(this.identifiantProcessus).getListeDateExportFormatee();
		lesNumerosDuProcess = C_Processus.getProcessus(this.identifiantProcessus).getListeNumeroVersion();
		
		this.SC_Onglet = new JTabbedPane();
		this.setLayout(new BorderLayout());		
	}

	public void operationMoteur()
	{}

	public void operationSurBoutons()
	{}

	public void operationSurComposants()
	{}

	public void operationSurFenetre()
	{
		this.add(this.SC_Onglet, BorderLayout.CENTER);
	}

	public void operationSurPanel()
	{
		for (int i = 0; i < lesVersionsDuProcess.size() && i < lesVersionsFormateesDuProcess.size(); i++)
			this.SC_Onglet.add((String) lesNumerosDuProcess.get(i), new OO_TableauDeBordVersionProcessus(this.identifiantProcessus, (String) lesVersionsDuProcess.get(i),(String) lesVersionsFormateesDuProcess.get(i)));
	}

	public void updateTexte()
	{}

}
