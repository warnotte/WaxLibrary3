package io.github.warnotte.waxlib3.waxlib2.TemplateCollectionFilter.Tests;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.warnotte.waxlib3.waxlib2.TemplateCollectionFilter.BaseComparator;
import io.github.warnotte.waxlib3.waxlib2.TemplateCollectionFilter.WCollectionFilter;

/**
 * Cette petite classe permet de stocker des object selon un identifiant unique dans une DB "virtuelle" en m�moire.
 * Il y'a des fonctions de recherche assez souples pour permettre tout sorte de recherches.
 * @author Warnotte Renaud
 * @param <T> L'objet template (le type en qlq sorte) � utiliser pour cette BD.
 */
public class BaseEssai<T> extends WCollectionFilter<T>
{
	HashMap<Integer, T> elements = new HashMap<Integer, T> ();
	
	public void put(int i, T element)
	{
		elements.put(i, element);
	}
	
	/**
	 * Recupere l'element a l'id donn�.
	 * @param ID l'identifiant du truc.
	 * @return
	 */
	public T getByID(int ID)
	{
		return elements.get(ID);
	}

	public ArrayList<T> getByComparator(BaseComparator<T> comparator)
	{
		return (ArrayList<T>) getByComparator(elements.values(), comparator);
	}

	
	public ArrayList<T> getByFieldValue(String Field_name, Object valueToMatch, boolean EqualsOrDifferent) throws Exception
	{
		return getByFieldValue(elements.values(), Field_name, valueToMatch, EqualsOrDifferent);
	}

	
	public ArrayList<T> getByFieldValue(String Field_name, Object valueToMatch) throws Exception
	{
		return getByFieldValue(elements.values(), Field_name, valueToMatch);
	}

	
	
	
	

}
