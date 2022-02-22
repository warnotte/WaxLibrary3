/**
 * 
 */
package io.github.warnotte.waxlib3.core.IOCSV.tests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import io.github.warnotte.waxlib3.core.IOCSV.CSVFile;
import io.github.warnotte.waxlib3.core.IOCSV.IOCSVTool;

/**
 * @author Warnotte Renaud
 *
 */
public class mainIOCSVTest
{

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, FileNotFoundException, IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		mainIOCSVTest m = new mainIOCSVTest();
		m.test();
	}

	/**
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * 
	 */
	private void test() throws InstantiationException, IllegalAccessException, FileNotFoundException, IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		CSVFile<StiffUpdateOrder> csvfile = IOCSVTool.readCSVFile(getClass().getResource("StiffsOrders.stupdate.csv").getFile(), StiffUpdateOrder.class, true);
		
		//List<StiffUpdateOrder> items = csvfile.getItems();
		// Do something with items ...
		// ...
		
		IOCSVTool.writeCSVFile("e:\\toto.csv", csvfile, true);
		
		
		
	}

}
