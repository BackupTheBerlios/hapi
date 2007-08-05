package hapi.presentation.composants.onglets;

import hapi.application.indicateurs.C_TableauDeBordProcessus;

import java.text.DecimalFormat;

/**
 * @author Vincent
 */
public class OO_TableauDeBordVersionProcessus extends OO_TableauDeBord
{
	private static final long serialVersionUID = 5314358639272705885L;
    private String versionProcessus = null;
	private String dateExport = null;
	private String FORMAT_INDICATEURS = "##0.0";

	public OO_TableauDeBordVersionProcessus(String identifiant, String version, String dateExport)
	{
		super(identifiant);
		this.versionProcessus = version;
		this.dateExport = dateExport;
		this.updateTexte();
	}

	public void updateTexte()
	{
		DecimalFormat df = new DecimalFormat(FORMAT_INDICATEURS);

		this.STV_RUR.setText(" " + df.format(C_TableauDeBordProcessus.getMoyenneRUR(this.identifiant, this.versionProcessus)) + " %");
		this.STV_RCT.setText(" " + df.format(C_TableauDeBordProcessus.getMoyenneRCT(this.identifiant, this.versionProcessus)) + " %");
		this.STV_RUA.setText(" " + df.format(C_TableauDeBordProcessus.getMoyenneRUA(this.identifiant, this.versionProcessus)) + " %");
		this.STV_RUP.setText(" " + df.format(C_TableauDeBordProcessus.getMoyenneRUP(this.identifiant, this.versionProcessus)) + " %");
		this.STV_DateExport.setText(" " + dateExport);

		/*		  
		this.STV_RUR.setText(Double.toString(Math.round(C_TableauDeBordProcessus.getMoyenneRUR(this.identifiant, this.versionProcessus))));
		this.STV_RCT.setText(Double.toString(Math.round(C_TableauDeBordProcessus.getMoyenneRCT(this.identifiant, this.versionProcessus))));
		this.STV_RUA.setText(Double.toString(Math.round(C_TableauDeBordProcessus.getMoyenneRUA(this.identifiant, this.versionProcessus))));
		this.STV_RUP.setText(Double.toString(Math.round(C_TableauDeBordProcessus.getMoyenneRUP(this.identifiant, this.versionProcessus))));
		*/
		//ArrayList donneesModele = C_TableauDeBordProcessus.getMoyenneRangsDesActivites(this.identifiant, this.versionProcessus);
		//this.LS_OA.setModel(C_TableauDeBordProcessus.getModele((ArrayList)donneesModele.get(0), (ArrayList)donneesModele.get(1))); 

	}

	public void operationSurBoutons()
	{}
}
