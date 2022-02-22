package io.github.warnotte.waxlib3.core.TemplateGroupAndSorter;

import java.util.function.Function;

public class SortingOrder<T>
{
	@SuppressWarnings("rawtypes")
	Function<T, Comparable>	classifier;
	boolean					invert	= false;

	/**
	 * @param classifier
	 * @param invert
	 */
	public SortingOrder(@SuppressWarnings("rawtypes") Function<T, Comparable> classifier)
	{
		this(classifier, false);
	}

	/**
	 * @param classifier
	 * @param invert
	 */
	public SortingOrder(@SuppressWarnings("rawtypes") Function<T, Comparable> classifier, boolean invert)
	{
		super();
		this.classifier = classifier;
		this.invert = invert;
	}

	

}