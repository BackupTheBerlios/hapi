package hapi.presentation.indicateurs.creation;

import hapi.application.C_Mesures;
import hapi.application.indicateurs.C_ArbreFaceArbre;
import hapi.application.indicateurs.C_CompilerMesures;
import hapi.application.indicateurs.C_CycleDeVie;
import hapi.application.indicateurs.C_EntiteOUT;
import hapi.application.interfaces.FenetreAssistee;
import hapi.application.modele.NegatifDecimalModel;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.text.DefaultFormatterFactory;

/**
 * @author Natalia
 */
public class OO_Indicateur extends FenetreAssistee implements FenetreHAPI
{
	private static final long serialVersionUID = 6604271407895767805L;
    private JPanel SC_Composant = null;
	private JPanel SC_ValeurCompileeGene = null;
	private JPanel SC_ValeurCompilee = null;
	private JPanel SC_cc = null;
	private JTabbedPane SC_Onglets = null;

	// panels de mise en forme
	private JPanel SC_Haut = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droit = null;
	private JPanel SC_Bas = null;

	private BorderLayout layoutfont = null;
	private BorderLayout layoutcomposants = null;
	private BorderLayout layoutvaleurcompileegene = null;

	private JButton BP_Compiler = null;

	private JLabel STC_ValeurCompilee = null;

	private JFormattedTextField STV_ValeurCompilee = null;

	private JCheckBox CC_ValeurAPrendreEnCompte = null;

	private ActionListener actionCompiler = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doCompilerP();
		}
	};

	private int typeFenetre = -1;
	private int indiceMesure = -1;
	private C_ArbreFaceArbre cArbreFaceArbreP = null;
	private C_EntiteOUT cEntiteOutP = null;
	private int idCycleDeVieP = -1;

	// création de la nouvelle liste des activités de l'itération
	private HashMap listeDesActivitesDeLIterationP = new HashMap();
	// création de la nouvelle liste des produits de l'itération
	private HashMap listeDesProduitsDeLIterationP = new HashMap();
	// création de la nouvelle liste des produits de l'itération
	private HashMap listeDesRolesDeLIterationP = new HashMap();

	public OO_Indicateur()
	{
		this.creationElements();
		this.operationSurBoutons();
		this.operationSurComposants();
		this.operationSurPanel();
		this.updateTexte();
		this.operationSurFenetre();
	}

	public void creationElements()
	{
		SC_Composant = new JPanel();
		SC_ValeurCompilee = new JPanel();
		SC_ValeurCompileeGene = new JPanel();
		SC_Onglets = new JTabbedPane();
		SC_cc = new JPanel();

		// panels de mise en forme
		SC_Haut = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droit = new JPanel();
		SC_Bas = new JPanel();

		layoutfont = new BorderLayout();
		layoutcomposants = new BorderLayout();
		layoutvaleurcompileegene = new BorderLayout();

		BP_Compiler = new JButton();

		STC_ValeurCompilee = new JLabel();

		STV_ValeurCompilee = new JFormattedTextField();

		CC_ValeurAPrendreEnCompte = new JCheckBox();

	}

	public void operationSurBoutons()
	{
		BP_Compiler.setText(Bundle.getText("BD_CreerEvaluation_Compiler"));
		BP_Compiler.setMnemonic('C');
		BP_Compiler.addActionListener(actionCompiler);
	}

	public void operationSurComposants()
	{
		STC_ValeurCompilee.setText(Bundle.getText("BD_CreerEvaluation_ValeurCompilee"));
		STV_ValeurCompilee.setPreferredSize(new Dimension(75, 21));
		NegatifDecimalModel dm = new NegatifDecimalModel(Bundle.formatDecimalCourt);
		STV_ValeurCompilee.setFormatterFactory(new DefaultFormatterFactory(dm));
		//STV_ValeurCompilee.setEnabled(false);

		CC_ValeurAPrendreEnCompte.setText(Bundle.getText("BD_CreerEvaluation_CC"));
	}

	public void operationSurPanel()
	{

		SC_ValeurCompilee.add(STC_ValeurCompilee);
		SC_ValeurCompilee.add(STV_ValeurCompilee);
		SC_ValeurCompilee.add(BP_Compiler);

		SC_cc.setLayout(new BorderLayout());
		SC_cc.add(CC_ValeurAPrendreEnCompte, BorderLayout.EAST);

		SC_ValeurCompileeGene.setLayout(layoutvaleurcompileegene);
		SC_ValeurCompileeGene.add(SC_cc, BorderLayout.NORTH);
		SC_ValeurCompileeGene.add(SC_ValeurCompilee, BorderLayout.EAST);

		SC_Composant.setLayout(layoutcomposants);
		SC_Composant.add(SC_Onglets, BorderLayout.CENTER);
		SC_Composant.add(SC_ValeurCompileeGene, BorderLayout.SOUTH);
	}

	public void operationSurFenetre()
	{
		//Affectation du fond
		this.setLayout(layoutfont);
		//Bordures
		this.add(SC_Haut, BorderLayout.NORTH);
		this.add(SC_Droit, BorderLayout.EAST);
		this.add(SC_Gauche, BorderLayout.WEST);
		this.add(SC_Bas, BorderLayout.SOUTH);
		this.add(SC_Composant, BorderLayout.CENTER);
	}

	public void operationMoteur()
	{}

	public void updateTexte()
	{}

	private void doCompilerP()
	{
		switch (typeFenetre)
		{
			case C_EntiteOUT.ACTIVITE:
				listeDesActivitesDeLIterationP.clear();
				listeDesActivitesDeLIterationP.put("-1", cEntiteOutP.getListeEntiteOUT());
				listeDesActivitesDeLIterationP.putAll(cArbreFaceArbreP.getListeUtilise());

				if (!isValeurSaisieAPrendreEnCompte())
				{
					if (indiceMesure != -1)
						STV_ValeurCompilee.setText(new Float(C_CompilerMesures.compilerRUA(listeDesActivitesDeLIterationP, C_CycleDeVie.getUnCycleDeVie(idCycleDeVieP).getLesActivites())).toString());
					else
						STV_ValeurCompilee.setText(new Float(C_CompilerMesures.compilerRUA(listeDesActivitesDeLIterationP, C_CycleDeVie.getUnCycleDeVie(idCycleDeVieP).getLesActivites())).toString());
				}
				break;
			case C_EntiteOUT.PRODUIT:
				listeDesProduitsDeLIterationP.clear();
				listeDesProduitsDeLIterationP.put("-1", cEntiteOutP.getListeEntiteOUT());
				listeDesProduitsDeLIterationP.putAll(cArbreFaceArbreP.getListeUtilise());

				if (!isValeurSaisieAPrendreEnCompte())
				{
					if (indiceMesure != -1)
						STV_ValeurCompilee.setText(new Float(C_CompilerMesures.compilerRUP(listeDesProduitsDeLIterationP, C_CycleDeVie.getUnCycleDeVie(idCycleDeVieP).getLesProduits())).toString());
					else
						STV_ValeurCompilee.setText(new Float(C_CompilerMesures.compilerRUP(listeDesProduitsDeLIterationP, C_CycleDeVie.getUnCycleDeVie(idCycleDeVieP).getLesProduits())).toString());
				}
				break;
			case C_EntiteOUT.ROLE:
				listeDesRolesDeLIterationP.clear();
				listeDesRolesDeLIterationP.put("-1", cEntiteOutP.getListeEntiteOUT());
				listeDesRolesDeLIterationP.putAll(cArbreFaceArbreP.getListeUtilise());

				if (!isValeurSaisieAPrendreEnCompte())
				{
					if (indiceMesure != -1)
						STV_ValeurCompilee.setText(new Float(C_CompilerMesures.compilerRUR(listeDesRolesDeLIterationP, C_CycleDeVie.getUnCycleDeVie(idCycleDeVieP).getLesRoles())).toString());
					else
						STV_ValeurCompilee.setText(new Float(C_CompilerMesures.compilerRUR(listeDesRolesDeLIterationP, C_CycleDeVie.getUnCycleDeVie(idCycleDeVieP).getLesRoles())).toString());
				}

				break;
			default:
				break;
		}
	}

	private HashMap getListeEntiteDeLIterationP()
	{
		// mise à jour des listes
		this.doCompilerP();
		// en foncion du type des l'entité, retour des listes correspondantes
		switch (typeFenetre)
		{
			case C_EntiteOUT.ACTIVITE:
				return listeDesActivitesDeLIterationP;
			case C_EntiteOUT.PRODUIT:
				return listeDesProduitsDeLIterationP;
			case C_EntiteOUT.ROLE:
				return listeDesRolesDeLIterationP;
			default:
				break;
		}
		return null;
	}

	/*private ArrayList getListeEntiteNONP()
	{
		return new ArrayList(cArbreFaceArbreP.getListeNonUtilise().keySet());
	}*/

	private boolean isValeurSaisieAPrendreEnCompte()
	{
		return CC_ValeurAPrendreEnCompte.isSelected();
	}

	private Double getValeurSaisie()
	{
		String laValeur = STV_ValeurCompilee.getText();
		laValeur = laValeur.replaceAll(",", ".");
		Double valeur = new Double(laValeur);
		if (valeur != null)
		{
			return valeur;
		}
		return null;
	}

	public ArrayList getParametresSaisis()
	{
		ArrayList retour = new ArrayList();

		retour.add(getListeEntiteDeLIterationP());
		retour.add(cArbreFaceArbreP.getListeNonUtilise());
		retour.add(new Boolean(isValeurSaisieAPrendreEnCompte()));
		retour.add(getValeurSaisie());

		return retour;
	}

	public void setParametres(ArrayList lesParametres)
	{
		typeFenetre = ((Integer) lesParametres.get(14)).intValue();
		indiceMesure = ((Integer) lesParametres.get(2)).intValue();
		cArbreFaceArbreP = new C_ArbreFaceArbre((String) lesParametres.get(5), (String) lesParametres.get(6), (String) lesParametres.get(7), (String) lesParametres.get(8), (String) lesParametres.get(9), (String) lesParametres.get(10), (HashMap) lesParametres.get(11), (HashMap) lesParametres.get(12), ((Integer) lesParametres.get(13)).intValue());
		cEntiteOutP = (C_EntiteOUT) lesParametres.get(15);
		idCycleDeVieP = ((Integer) lesParametres.get(1)).intValue();

		SC_Onglets.add(Bundle.getText((String) lesParametres.get(3)), new OO_ArbreFaceArbre(cArbreFaceArbreP));
		SC_Onglets.add(Bundle.getText((String) lesParametres.get(4)), new OO_EntiteOUT(typeFenetre, cEntiteOutP));

		if (indiceMesure != -1)
		{
			switch (typeFenetre)
			{
				case C_EntiteOUT.ACTIVITE:
					STV_ValeurCompilee.setText(new Float(C_CompilerMesures.compilerRUA(C_Mesures.getMesure(((Integer) lesParametres.get(2)).intValue()).getListeActivites(), C_CycleDeVie.getUnCycleDeVie(idCycleDeVieP).getLesActivites())).toString());
					break;
				case C_EntiteOUT.PRODUIT:
					STV_ValeurCompilee.setText(new Float(C_CompilerMesures.compilerRUP(C_Mesures.getMesure(((Integer) lesParametres.get(2)).intValue()).getListeProduits(), C_CycleDeVie.getUnCycleDeVie(idCycleDeVieP).getLesProduits())).toString());
					break;
				case C_EntiteOUT.ROLE:
					STV_ValeurCompilee.setText(new Float(C_CompilerMesures.compilerRUR(C_Mesures.getMesure(((Integer) lesParametres.get(2)).intValue()).getListeRoles(), C_CycleDeVie.getUnCycleDeVie(idCycleDeVieP).getLesRoles())).toString());
					break;
				default:
					break;
			}
		}

		CC_ValeurAPrendreEnCompte.setSelected(((Boolean) lesParametres.get(16)).booleanValue());

		if (!((Boolean) lesParametres.get(16)).booleanValue())
			this.doCompilerP();
		else
			STV_ValeurCompilee.setValue(((Double) lesParametres.get(17)));
	}
}
