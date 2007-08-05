package hapi.presentation.interfaces;

/**
 * Interface de toutes les fen�tres
 * @author C�dric
 */
public interface FenetreHAPI
{

	//Regrouper toutes les op�ration de cr�ation
	abstract void creationElements();
	//Regrouper toutes les infos concernant les boutons de NAVIGATION
	abstract void operationSurBoutons();
	//Regrouper toutes les infos concernant le contenu de la fen�tre
	abstract void operationSurComposants();
	//Regrouper toutes les infos concernant les panels
	abstract void operationSurPanel();
	//Regrouper toutes les infos concernant this
	abstract void operationSurFenetre();
	//Regrouper toutes les infos sur des variables locales
	abstract void operationMoteur() throws Exception;
	//Regrouper toutes les op�rations affectant du texte aux composants
	abstract void updateTexte();
}
