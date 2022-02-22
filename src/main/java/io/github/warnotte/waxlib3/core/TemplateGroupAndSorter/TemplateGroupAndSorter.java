package io.github.warnotte.waxlib3.core.TemplateGroupAndSorter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TemplateGroupAndSorter {

	/**
	 * Groupe et trie un liste d'objets sur base d'une liste des variable a triée et d'un sens.
	 * Ceci est plus lent qu'en utilisant un comparateur fixé si on a pas un nombre de Coeur suffisant.
	 * EX : Avec 10.000.000 de "chaussure" sur le Xeon je vais plus rapidement que sur mon I5 (4 coeurs) avec ma méthode comparé au sort non parallel de java.
	 * 	  								   
	 * @param pans
	 * @param classifiers
	 * @return
	 */
	public static <T> List<T> GroupAndSort(List<T> pans, List<SortingOrder<T>> classifiers)
	{
		Stream<T> stream = GroupAndSortStream( pans,  classifiers);
		return stream.collect(Collectors.toList());
	}
	
	/**
	 * Groupe et trie un liste d'objets sur base d'une liste des variable a triée et d'un sens.
	 * Ceci est plus lent qu'en utilisant un comparateur fixé
	 * @param pans
	 * @param classifiers
	 * @return
	 */
	public static <T> Stream<T> GroupAndSortStream(List<T> pans, List<SortingOrder<T>> classifiers)
	{
		Comparator<T> compL = createComparator(classifiers);
		return pans.parallelStream().sorted(compL);
	}

	/**
	 * @param classifiers
	 * @return
	 */
	private static <T> Comparator<T> createComparator(List<SortingOrder<T>> classifiers)
	{
		Comparator<T> compL = new Comparator<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public int compare(T o1, T o2) {
				int resultat=0;
				int cssize = classifiers.size();
				for (int i = 0; i < cssize-1; i++)
				{
					SortingOrder<T> so = classifiers.get(i);
					
					resultat = so.classifier.apply(o1).compareTo(so.classifier.apply(o2));
					if (so.invert==true) resultat=invert(resultat);
					if (i==cssize-1)
						break;
					if (resultat != 0) return resultat;
				}
				
				SortingOrder<T> so = classifiers.get(cssize-1);
				resultat = so.classifier.apply(o1).compareTo(so.classifier.apply(o2));
				if (so.invert==true) resultat=invert(resultat);
				return invert(resultat);
			}
			
			public int invert(int value)
			{
				if (value==0) return value;
				return value*=-1;
			}
		};
		return compL;
	}



	
	
}
