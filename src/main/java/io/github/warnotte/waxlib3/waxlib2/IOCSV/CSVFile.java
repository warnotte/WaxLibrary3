package io.github.warnotte.waxlib3.waxlib2.IOCSV;

import java.util.List;

public class CSVFile<T>
{
	String	header = null;
	List<T>	items;
	
	/**
	 * @return the header
	 */
	public synchronized String getHeader()
	{
		return header;
	}
	/**
	 * @param header the header to set
	 */
	public synchronized void setHeader(String header)
	{
		this.header = header;
	
	}
	/**
	 * @return the items
	 */
	public synchronized List<T> getItems()
	{
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public synchronized void setItems(List<T> items)
	{
		this.items = items;
	
	}
	
	
}