package org.warnotte.waxlib2.TemplateCollectionFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Cette merveilleuse classe va permettre de faire rapidement une recherche parmis une collection pass�e
 * en paremetre et retourne une liste des elements qui matche le crit�re.
 * @author Warnotte Renaud 2010
 * @param <T>
 */
public class WCollectionFilterBase<T>
{

	/**
	 * Fonction de recherche +- evolu�e qui retourne une liste des eleements dont une variable dans la classe
	 * s'apellant Field_name, a une valeur qui est egale a Object_to_match.
	 * @param Field_name nom de la variable de la classe.
	 * @param value_to_match La valeur du champs recherch�e.
	 * @param EqualsOrDifferent == ou alors !=
	 * @return ArrayList<T>
	 * @throws Exception 
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */

	@SuppressWarnings("unchecked")
	protected ArrayList<T> getByFieldValue(Collection<T> elements, String Field_name, Object value_to_match, boolean EqualsOrDifferent) throws Exception
	{
		return (ArrayList<T>) WCollectionFilterStatic.getByFieldValue(elements, Field_name, value_to_match, EqualsOrDifferent);
	}
	
	@SuppressWarnings("unchecked")
	protected ArrayList<T> getByFieldValue(Collection<T> elements, String Field_name, Object value_to_match) throws Exception
	{
		return (ArrayList<T>) WCollectionFilterStatic.getByFieldValue(elements, Field_name, value_to_match);
	}
	
	/**
	 * Recuperer tout les objets qui vont repondre vrai a la condition du comparateur.
	 * Ceci est a mon avis le meilleurs truc pour etre le plus modulaire possible.
	 * Avec le compatareur, on px facilement creer la condition Where avec des ET et OU, des == , != etc ...
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> getByComparator(Collection<T> elements, Compararable<T> comparator)
	{
		return (List<T>) WCollectionFilterStatic.getByComparator(elements, comparator);
	}
}
