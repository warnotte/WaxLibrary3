/**
 * 
 */
package org.warnotte.waxlib2.IOCSV;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Warnotte Renaud
 *
 */
public class IOCSVTool
{
	
	
	public static <T extends lineToDTO> CSVFile<T> readCSVFile(String filename, Class<T> classl) throws InstantiationException, IllegalAccessException, FileNotFoundException, IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		return readCSVFile(filename, classl, true); 
	}
	
	public static <T extends lineToDTO> CSVFile<T> readCSVFile(String filename, Class<T> classl, boolean hasHeader) throws InstantiationException, IllegalAccessException, FileNotFoundException, IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		
		CSVFile<T> csvfile = new CSVFile<>();
		List<T> list = new ArrayList<T>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line;
		    // read the header
		    if (hasHeader)
		    {
		    	line = br.readLine();
		    	csvfile.header=line;
		    }
		    while ((line = br.readLine()) != null) {
		    	if (line.trim().length()==0)  {
					System.out.println("Info, Empty line detected in "+filename);
					continue;
				}
		    	// This is not standard implementation of CSV
				if (line.trim().startsWith("#")) {
					System.out.println("Info, commented line detected in "+filename);
					continue;
				}

				T m = classl.getConstructor().newInstance();
				m.convertLineToDTO(line);
				list.add(m);
		    }
		}
		
		csvfile.items = list;
		
		return csvfile;
		
	}

	public static <T extends DTOToLine> void writeCSVFile(String filename, CSVFile<T> csvfile, boolean writeheaderifexist) throws FileNotFoundException
	{
		PrintStream ps = new PrintStream(filename);
		
		if (writeheaderifexist)
		if ((csvfile.header!=null) && (csvfile.header.length()!=0))
			ps.println(csvfile.header);
		
		csvfile.getItems().stream().forEach(t -> {
			String[] str = t.convertDTOToLine();
			// Append and add caracter separator.
			String line = String.join(";", str);
			// Write in the file.
			ps.println(line);
		});
		ps.close();
		
		return;
	}
}
