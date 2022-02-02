package io.github.warnotte.waxlib3.waxlib2.TemplateCollectionFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class WCollectionFilterStatic {

	
	public static ArrayList<?> getByFieldValue(Collection<?> elements, String Field_name, Object value_to_match, boolean EqualsOrDifferent) throws Exception
	{
		try
		{
			ArrayList<Object> retour_list = new ArrayList<Object>();
			Collection<?> entries = elements;
			
			// Pour chacun des objet de la list.
			for (Iterator<?> iterator = entries.iterator(); iterator.hasNext();)
			{
				Object object = iterator.next();
				Field f;
				f = object.getClass().getDeclaredField(Field_name);
				//boolean oldstate = f.isAccessible();
				boolean oldstate = f.canAccess(object);
				f.setAccessible(true);
				Object valueChamp = f.get(object);
				f.setAccessible(oldstate);
				if (compare(value_to_match, valueChamp)==true)
					retour_list.add(object);
			}
			return retour_list;
		} 
		catch (SecurityException e)
		{
			String rep = "SecurityException : La variable ["+Field_name+"] n'est pas accessible :"+e;
			throw new Exception(rep);
		}
		catch (NoSuchFieldException e)
		{
			String rep = "NoSuchField : La variable ["+Field_name+"] n'existe probablement pas ou mal orthographi� :"+e;
			throw new Exception(rep);
		}
	}
	
	public static ArrayList<?> getByFieldValue(Collection<?> elements, String Field_name, Object value_to_match) throws Exception
	{
		return getByFieldValue(elements, Field_name, value_to_match, true);
	}
	
	/**
	 * Recuperer tout les objets qui vont repondre vrai a la condition du comparateur.
	 * Ceci est a mon avis le meilleurs truc pour etre le plus modulaire possible.
	 * Avec le compatareur, on px facilement creer la condition Where avec des ET et OU, des == , != etc ...
	 * @param comparator
	 * @return
	 */
	public static List<?> getByComparator(Collection<?> elements, Compararable<?> comparator)
	{
		List<Object> retour_list = new ArrayList<Object>();
		Collection<?> entries = elements;
		// Pour chacun des objet de la list.
		for (Iterator<?> iterator = entries.iterator(); iterator.hasNext();)
		{
			Object object = iterator.next();
			if (comparator.isInCriterias(object))
				retour_list.add(object);
		}
		return retour_list;
	}
	
	private static boolean compare(Object valueToMatch, Object valueChamp) throws Exception
	{
		if (valueToMatch.getClass()!=valueChamp.getClass())
			throw new Exception("DB_Base :: I cannot compare these 2 classes " +valueToMatch.getClass()+" , "+valueChamp);
		if (valueToMatch.equals(valueChamp))
			return true;
		return false;
	}
	
}
