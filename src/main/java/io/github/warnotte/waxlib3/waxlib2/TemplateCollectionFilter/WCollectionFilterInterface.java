package io.github.warnotte.waxlib3.waxlib2.TemplateCollectionFilter;

import java.util.ArrayList;

/**
 * Cette merveilleuse interface va servir pour facilement mettre a jour les fonction de la DB.
 * @author Warnotte Renaud 2010
 * @param <T>
 */
public interface WCollectionFilterInterface<T>
{

	public ArrayList<T> getByFieldValue(String Field_name, Object value_to_match, boolean EqualsOrDifferent) throws Exception;
	public ArrayList<T> getByFieldValue(String Field_name, Object value_to_match) throws Exception;
	public ArrayList<T> getByComparator(BaseComparator<T> comparator);
	
	
}
