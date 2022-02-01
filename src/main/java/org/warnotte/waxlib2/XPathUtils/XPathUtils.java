/**
 * 
 */
package org.warnotte.waxlib2.XPathUtils;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document; // The package org.jdom2 is accessible from more than one module: jdom2, jdom2
import org.jdom2.Element; // The package org.jdom2 is accessible from more than one module: jdom2, jdom2
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

/**
 * @author Warnotte Renaud
 *
 */
public class XPathUtils
{
	protected static final Logger parentLogger = LogManager.getLogger("XPathUtils");
	
	protected XPathFactory xpath = XPathFactory.instance();
	
	/**
	 * Decouper un string en un a tableau. la découpe se fait avec les caractère |
	 * Si pas de | alors on retourne une array avec une seul element
	 * @param string
	 * @return
	 */
	public String[] splitOR(String string)
	{
		parentLogger.fatal("I'm in the "+string);
		
	    System.out.println("Warning - | is regexp not usable... need to fix!!!");
		if (string.contains("|")==false)
			return new String [] {string};
			
		return string.split("\\|");
	}

	/**
	 * Trie en fonction d'un chemin XPath avec un type String (//Ship/name/ par
	 * exemple)
	 * 
	 * @param xpath
	 * @return
	 */
	public Comparator<Document> createXPathComparatorString(String XPath)
	{
		return new Comparator<Document>()
		{
			@Override
			public int compare(Document o1, Document o2)
			{
				XPathExpression<Element>	expr1	= xpath.compile(XPath, Filters.element());
				String						text1	= expr1.evaluateFirst(o1).getText();
				XPathExpression<Element>	expr2	= xpath.compile(XPath, Filters.element());
				String						text2	= expr2.evaluateFirst(o2).getText();
				return text1.compareTo(text2);
			}

		};
	}

	/**
	 * Trie en fonction d'un chemin XPath avec un type Valeur (//Ship/length/
	 * par exemple)
	 * 
	 * @param xpath
	 * @return
	 */
	public Comparator<Document> createXPathComparatorNumber(String XPath)
	{
		return new Comparator<Document>()
		{
			@Override
			public int compare(Document o1, Document o2)
			{

				XPathExpression<Element>	expr1	= xpath.compile(XPath, Filters.element());
				Double						text1	= Double.parseDouble(expr1.evaluateFirst(o1).getText());
				XPathExpression<Element>	expr2	= xpath.compile(XPath, Filters.element());
				Double						text2	= Double.parseDouble(expr2.evaluateFirst(o2).getText());

				return text1.compareTo(text2);
			}

		};
	}
	
	public Predicate<Document> createXPathAlwaysTruePredicate()
	{
		return entry -> {
			return true;
		};
	}
	
	public Predicate<Document> createXPathAlwaysFalsePredicate()
	{
		return entry -> {
			return false;
		};
	}

	public Predicate<Document> createXPathPredicateDoubleGreaterThan(String path3, double speed)
	{
		return entry -> {
			Document					doc		= entry;
			XPathExpression<Element>	expr	= xpath.compile(path3, Filters.element());
			String						text	= expr.evaluateFirst(doc).getText();
			double						value	= Double.parseDouble(text);
			if (value > speed)
				return true;
			return false;
		};
	}

	public Predicate<Document> createXPathPredicateDoubleMinMax(String path, double min, double max)
	{
		return entry -> {
			Document					doc		= entry;
			XPathExpression<Element>	expr	= xpath.compile(path, Filters.element());
			Element 					elmt =  expr.evaluateFirst(doc);
			/*
			if (elmt==null) {
				parentLogger.fatal("Path ["+path+ "] doesn't exists!");
				throw new NullPointerException("Path ["+path+ "] doesn't exists!");
			}
			*/			
			String						text	= elmt.getText();
			double						length	= Double.parseDouble(text);
			if ((length >= min) && (length <= max))
				return true;
			return false;
		};
	}
	
	public Predicate<Document> createXPathPredicateString(String path, String nametofind)
	{
		return createXPathPredicateString(path, new String[] {nametofind});
	}
	
	public Predicate<Document> createXPathPredicateString(String path, String[] nametofind)
	{
		return entry -> {
			Document					doc		= entry;
			XPathExpression<Element>	expr	= xpath.compile(path, Filters.element());
			String						text	= expr.evaluateFirst(doc).getText();
			
			for (int i = 0; i < nametofind.length; i++)
			{
                //if (text.contains(nametofind[i])==true)
                //    return true;
				
				//System.err.println(text+" == "+nametofind[i]+" -> "+text.matches(".*"+nametofind[i]+".*"));
				
                if (text.toLowerCase().matches(".*"+nametofind[i].toLowerCase()+".*")==true)
                    return true;
			}
			
			//boolean						ret		= text.contains(nametofind[0]);
			
			// ret = expr.evaluateFirst(doc).getText().startsWith(nametofind);
			// ret = expr.evaluateFirst(doc).getText().endsWith(nametofind);
			// ret = expr.evaluateFirst(doc).getText().equalsIgnoreCase(nametofind);
			
			return false;
		};
	}

	@SafeVarargs
	public static <T> Predicate<T> combineFiltersAND(Predicate<T>... predicates)
	{
		Predicate<T> p = Stream.of(predicates).reduce(x -> true, Predicate::and);
		return p;
	}

	@SafeVarargs
	public static <T> Predicate<T> combineFiltersOR(Predicate<T>... predicates)
	{
		Predicate<T> p = Stream.of(predicates).reduce(x -> false, Predicate::or);
		return p;
	}
	
	public static <T> Predicate<T> combineFiltersAND(List<Predicate<T>> predicates)
	{
		Predicate<T> p = predicates.stream().reduce(x -> true, Predicate::and);
		return p;
	}

	public static <T> Predicate<T> combineFiltersOR(List<Predicate<T>> predicates)
	{
		Predicate<T> p = predicates.stream().reduce(x -> false, Predicate::or);
		return p;
	}

}
