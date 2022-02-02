/**
 * 
 */
package io.github.warnotte.waxlib3.waxlib2.TemplateGroupAndSorter.Tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import io.github.warnotte.waxlib3.waxlib2.TemplateGroupAndSorter.SortingOrder;

/**
 * @author Warnotte Renaud
 *
 */
public class mainTest
{
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
	
			List<ChaussureNew> pans = new ArrayList<ChaussureNew>();
			
			String marques[] = {
					"CLARKS",
					"MEPHISTO",
					"GUCCI",
					"CATERPILLAR",
					"NIKE",
					"REEBOOK",
			};
			
			
			long start = System.currentTimeMillis();
			
			Random rand = new Random();
			for (int i = 0 ; i < 100000;i++) {
				String marque = marques[rand.nextInt(marques.length)];
				Couleur couleur = Couleur.values()[rand.nextInt(Couleur.values().length)];
						pans.add(new ChaussureNew(
						marque,
						"MODELE"+rand.nextInt(5),
						rand.nextInt(3), 
						couleur,
						30+rand.nextInt(13), 
						(int)(rand.nextDouble()*30)*3+100
						));
			}
			
			
			
			
			System.err.println("Items = "+pans.size());
			long stop = System.currentTimeMillis();
			long elapsed = stop-start;
			System.err.println("Elapsed for generation = "+elapsed);
		
			List<SortingOrder<ChaussureNew>> classifiers = new ArrayList<>();
			classifiers.add(new SortingOrder<ChaussureNew>(ChaussureNew::getMarque, false));
			classifiers.add(new SortingOrder<ChaussureNew>(ChaussureNew::getModele, false));
			classifiers.add(new SortingOrder<ChaussureNew>(ChaussureNew::getPrix, true));
			classifiers.add(new SortingOrder<ChaussureNew>(ChaussureNew::getPointure, false));
			classifiers.add(new SortingOrder<ChaussureNew>(ChaussureNew::getCouleur, false));
			
			start = System.currentTimeMillis();
			
	//		List<ChaussureNew> pans3 = TemplateGroupAndSorter.GroupAndSort(pans, classifiers);
		
			stop = System.currentTimeMillis();
			elapsed = stop-start;
			System.err.println("Elapsed My Own = "+elapsed);
	
			//displayList(pans3);
			
			Comparator<ChaussureNew> comparateurFixe = new Comparator<ChaussureNew>()
			{
				@Override
				public int compare(ChaussureNew o1, ChaussureNew o2)
				{
					int value1 = o1.marque.compareTo(o2.marque);
					if (value1 == 0)
					{
						int value2 = o1.modele.compareTo(o2.modele);
						if (value2 == 0)
						{
							int value3 = Integer.compare(o1.getPointure(), o2.getPointure());
							if (value3 == 0)
							{
								int value4 = Double.compare(o1.getPrix(), o2.getPrix());
								if (value4 == 0)
								{
									return o1.getCouleur().compareTo(o2.getCouleur());
								}
								return value4;
							}
							return value3;
						}
						return value2;
					}
					return value1;
				}
			};
			
			Collections.shuffle(pans);
			start = System.currentTimeMillis();
			pans.parallelStream().sorted(comparateurFixe).collect(Collectors.toList());
			stop = System.currentTimeMillis();
			elapsed = stop - start;
			System.err.println("Elapsed Collection Sort // fixed = " + elapsed);
			
			Collections.shuffle(pans);
			start = System.currentTimeMillis();
			pans.sort(comparateurFixe);
			stop = System.currentTimeMillis();
			elapsed = stop - start;
			System.err.println("Elapsed Collection Sort fixed = " + elapsed);
	
			//		displayList(pans);
			
			
	}
	
}

