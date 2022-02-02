package org.warnotte.waxlib3.waxlib2.TemplateCollectionFilter;

/**
 * Permet de deriver facilement une classe rapidos dans une fonction pour la recherche en DB.
 * @author Warnotte Renaud.
 *
 */
public abstract class BaseComparator<T> implements Compararable<T>
{
	public abstract boolean isInCriterias(Object o);
}
