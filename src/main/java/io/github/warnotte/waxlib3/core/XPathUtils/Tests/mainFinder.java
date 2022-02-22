/**
 * 
 */
package io.github.warnotte.waxlib3.core.XPathUtils.Tests;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;

import org.jdom2.Document; // The package org.jdom2 is accessible from more than one module: jdom2, jdom2
import org.jdom2.JDOMException; // The package org.jdom2 is accessible from more than one module: jdom2, jdom2

import io.github.warnotte.waxlib3.core.XPathUtils.DBXML;
import io.github.warnotte.waxlib3.core.XPathUtils.XPathUtils;

/**
 * @author Warnotte Renaud Juin 2019
 */
public class mainFinder
{
	DBXML dbxml;
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static void main(String[] args) throws JDOMException, IOException
	{
		mainFinder m = new mainFinder();
		long start = System.currentTimeMillis();
		List<Entry<File, Document>> files = m.search();
		
		files.stream().parallel().forEachOrdered(e -> {
			System.out.println("" + e.getKey());
		});
	
		System.out.printf("Found %d matches\r\n", files.size());
		
		long	stop	= System.currentTimeMillis();
		long	elapsed	= stop - start;
		System.out.println("Total Search time Elapsed : " + elapsed);
	}
	

	public mainFinder() throws JDOMException, IOException
	{
		dbxml = new DBXML();
		dbxml.loadXMLS("files//outdel");
		
	}

	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private List<Entry<File, Document>> search()
	{
		// Question pour ALO 12 juin 2019
		// Question 1 : Les predicats, uniquement du AND ?
		// Question 2 : Concerne tout le XML ? A savoir concerne-t-il les données sous
		// forme de liste ? (Si oui alors caca)
		// Question 3 : Le type de comparateur son bons ?

		// TODO : Quelques idées sur ce truc.
		// Predicat sur les String : CONTAINS, STARTWITH, ENDWITH, EQUALS
		// Predicat sur les Chiffres : ==, >=, <=, !=, >= && <=, <= && >=

		// Posibilité d'inverser les predicats

		// ... fill DB
		
	    //XPUPathUtils_Trajecto XPU = new XPUPathUtils_Trajecto();
	    XPathUtils XPU = new XPathUtils();
        
		// TODO : String regxep = ".*tank.*|.*push.*";
		String boat_name = "pushed convoy|tanker";
		
		
		String description_ABC = "1 rudder and 2 propellers|variable pitch|rapid vessel 3 jets";
		String description_D = "pas de propulseurs transversaux|bow and stern thrusters";
		
		double	minL	= 85;
		double	maxL	= 300;
		double	minB	= 15;
		double	maxB	= 30;
		double	minT	= 0;
		double	maxT	= 30;
		double	minCb	= 0;
		double	maxCb	= 3;

		boolean	enable_name		= true;
		boolean	enable_descABC	= true;
		boolean	enable_D		= true;
		boolean	enable_L		= true;
		boolean	enable_B		= true;
		boolean	enable_Cb		= true;
		boolean	enable_Dw		= true;
		
		Predicate<Document>	pred_name					= XPU.createXPathPredicateString("//ship/name", XPU.splitOR(boat_name));
		Predicate<Document>	pred_descriptionABC			= XPU.createXPathPredicateString("//ship/description", XPU.splitOR(description_ABC));
		Predicate<Document>	pred_descriptionD			= XPU.createXPathPredicateString("//ship/description", XPU.splitOR(description_D));
		Predicate<Document>	pred_L						= XPU.createXPathPredicateDoubleMinMax("//ship/length", minL, maxL);
		Predicate<Document>	pred_B						= XPU.createXPathPredicateDoubleMinMax("//ship/width", minB, maxB);
		Predicate<Document>	pred_T						= XPU.createXPathPredicateDoubleMinMax("//ship/draught", minT, maxT);
		Predicate<Document>	pred_Cb						= XPU.createXPathPredicateDoubleMinMax("//ship/blockCoefficient", minCb, maxCb);
		// deadweight = la masse volumique en tonnes/m3 dixit Adrien C. (en fait non il manque la masse volumique).
		//Predicate<Document> pred_Dw                   = XPU.createXPathPredicateCustomMinMax(-9999, 25000);
		Predicate<Document> pred_always_true 			= XPU.createXPathAlwaysTruePredicate();
		
		// TODO : Split that with the enable boolean in action. OR use an if false -> XPU.createXPathAlwaysTruePredicate()
		Predicate<Document> pred_group_final 			= XPathUtils.combineFiltersAND(pred_name, /*pred_descriptionABC, pred_descriptionD,*/ pred_L, pred_B, pred_T, pred_Cb/*, pred_Dw*/);

		// Trier par NOM/TAILLE (prends un peu de temps CPU)
		Comparator<Document> compareByAll = XPU.createXPathComparatorString("//ship/name").thenComparing(XPU.createXPathComparatorNumber("//ship/length"));
		
		List<Entry<File, Document>> files = dbxml.findXMLThatMathPredicates(pred_group_final, compareByAll);
		
		return files;
	}

}
