package hapi.presentation.interfaces;

/**
 * Interface de toutes les fenêtres
 * @author Cédric
 */
public interface FenetreHAPI
{

	//Regrouper toutes les opération de création
	abstract void creationElements();
	//Regrouper toutes les infos concernant les boutons de NAVIGATION
	abstract void operationSurBoutons();
	//Regrouper toutes les infos concernant le contenu de la fenêtre
	abstract void operationSurComposants();
	//Regrouper toutes les infos concernant les panels
	abstract void operationSurPanel();
	//Regrouper toutes les infos concernant this
	abstract void operationSurFenetre();
	//Regrouper toutes les infos sur des variables locales
	abstract void operationMoteur() throws Exception;
	//Regrouper toutes les opérations affectant du texte aux composants
	abstract void updateTexte();
}
