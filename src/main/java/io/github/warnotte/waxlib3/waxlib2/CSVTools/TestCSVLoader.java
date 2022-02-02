package io.github.warnotte.waxlib3.waxlib2.CSVTools;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;



/**
 * @author Warnotte Renaud
 *
 */
public class TestCSVLoader
{
	
	/**
	 * @throws IOException 
	 * @throws CSVColumnNotFoundException 
	 * 
	 */
	public static void main(String[] args) throws IOException, CSVColumnNotFoundException
	{
		test2();
		testReadCSV("docs/1_Plates.txt");
		/*testReadCSV("docs/2_Profiles.txt");
		testReadCSV("docs/3_Stiffeners.txt");
		testReadCSV("docs/4_Assemblies.txt");
		testReadCSV("docs/5_Blocks.txt");
		testReadCSV("docs/6_Ship.txt");
		testReadCSV("docs/8_Piping.txt");*/
	}

	/**
	 * @throws IOException 
	 * @throws CSVColumnNotFoundException 
	 * 
	 */
	private static void test2() throws IOException, CSVColumnNotFoundException
	{
		CSVLoader loader = new CSVLoader(new File("data.csv"));
		CSVLine line = null; 
		line = loader.getNext();
		double a = line.getDouble(0);
		double b = line.getDouble(1);
		//double c = line.getDouble(2);
	//	double d = line.getDouble(3);
		System.err.printf("%f %f %f %f\r\n", line.getDouble(0), line.getDouble(1), line.getDouble(2), line.getDouble(3));
		line = loader.getNext();
		System.err.printf("%f %f\r\n", line.getDouble(0), line.getDouble(1));
		line = loader.getNext();
		System.err.printf("%f %f %f %f\r\n", line.getDouble(0), line.getDouble(1), line.getDouble(2), line.getDouble(3));
		
		System.err.printf("A=%f b=%f ...\r\n", a,b);
	
	}

	/**
	 * @param fileName
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws CSVColumnNotFoundException 
	 */
	private static void testReadCSV(String fileName) throws IOException, MalformedURLException, CSVColumnNotFoundException
	{
		System.err.println("FILE : "+fileName);
		CSVLoader loader = new CSVLoader(new File(fileName).toURI().toURL());
		String header = loader.getHeader();
		int NbrCols = loader.getNbrColumn();
		System.err.println("Nbr Colonnes : "+NbrCols);
		
		System.err.println("Header : "+header);
		CSVLine csvline = loader.getNext();
		int cpt=0;
		while(csvline!=null)
		{
			loader.displayLine(csvline);
			csvline = loader.getNext();
			cpt++;
		}
		System.err.println("Read finished, nbr line read : "+cpt);
		loader.close();
	}

	
}

