package hapi.donnees.metier;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import java.util.ArrayList;

import javax.swing.ImageIcon;


/*import java.util.ArrayList;*/

/**
 * @author boursier
 */
public class E_Produit implements InterfaceMetier
{
	// nom du produit
	private String nom = new String();
	// identifiant du produit
	private String identifiant = new String();
	// identifiant du composant auquel il est lié
	private String agregatComposant = new String();
	// identifiant de rôle qui lui est lié
	private String idResponsabilite = new String();
	// type du produit
	private String typeProduitId = new String();
	// element de présentation
	private String elementPresentationId = new String();

	// liste des identifiants des interfaces
	private ArrayList interfaceId = null;
	// liste des identifiants d'état
	private ArrayList idEtat = null;

	// liste des activités dont le produits est en entrée
	private ArrayList idEntreeActivite = null;
	// liste des activités dont le produits est en sortie
	private ArrayList idSortieActivite = null;

	private ImageIcon icone;

	public E_Produit(ImageIcon lIcone)
	{
		idEtat = new ArrayList();
		interfaceId = new ArrayList();
		idEntreeActivite = new ArrayList();
		idSortieActivite = new ArrayList();
		icone = lIcone;
	}

	public void ajouterIdEtat(String id)
	{
		idEtat.add(id);
	}
	
	public void replaceIdEtat(String ancien, String id)
	{
		idEtat.remove(ancien);
		idEtat.add(id);
	}	

	public void ajouterIdInterface(String id)
	{
		interfaceId.add(id);
	}
	
	public void replaceIdInterface(String ancien,String id)
	{
		interfaceId.remove(ancien);
		interfaceId.add(id);
	}	

	public void ajouterIdEntreeActivite(String id)
	{
		idEntreeActivite.add(id);
	}
	
	public void replaceIdEntreeActivite(String ancien, String id)
	{
		idEntreeActivite.remove(ancien);
		idEntreeActivite.add(id);
	}	

	public void ajouterIdSortieActivite(String id)
	{
		idSortieActivite.add(id);
	}
	
	public void replaceIdSortieActivite(String ancien, String id)
	{
		idSortieActivite.remove(ancien);
		idSortieActivite.add(id);
	}	
	/**
	 * getters et setters
	 * 
	 */
	public void setNom(String n)
	{
		nom = n;
	}

	public String getNom()
	{
		return nom;
	}

	public String toString()
	{
		return nom;
	}

	/**
	 * @return
	 */
	public String getAgregatComposant()
	{
		return agregatComposant;
	}

	/**
	 * @return
	 */
	public String getIdentifiant()
	{
		return identifiant;
	}

	/**
	 * @param i
	 */
	public void setAgregatComposant(String s)
	{
		agregatComposant = s;
	}

	/**
	 * @param i
	 */
	public void setIdentifiant(String i)
	{
		identifiant = i;
	}

	public ArrayList getInterfaces()
	{
		return interfaceId;
	}

	public ArrayList getEtats()
	{
		return idEtat;
	}

	public String getIdResponsabilite()
	{
		return idResponsabilite;
	}

	public void setIdResponsabilite(String i)
	{
		idResponsabilite = i;
	}

	public String getTypeProduitId()
	{
		return typeProduitId;
	}
	public void setTypeProduitId(String typeProduitId)
	{
		this.typeProduitId = typeProduitId;
	}
	public String getElementPresentationId()
	{
		return elementPresentationId;
	}
	public void setElementPresentationId(String elementPresentationId)
	{
		this.elementPresentationId = elementPresentationId;
	}
	public ArrayList getIdEtat()
	{
		return idEtat;
	}

	public ArrayList getInterfaceId()
	{
		return interfaceId;
	}
	public ArrayList getIdEntreeActivite()
	{
		return idEntreeActivite;
	}
	public ArrayList getIdSortieActivite()
	{
		return idSortieActivite;
	}

	public ImageIcon getIcone()
	{
		return icone;
	}
}
