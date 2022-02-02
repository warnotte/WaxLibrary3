package io.github.warnotte.waxlib3.waxlib2.CSVTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * @author Warnotte Renaud.
 *
 */
public class CSVLoader
{
	public static String CharSeparator = ";";
	private final String[] _ColumnName;
	private final int NbrColumn;
	private BufferedReader dis;
	
	URL file;
	
	public CSVLoader(File file) throws IOException
	{
		this(file.toURI().toURL());
	}
	public CSVLoader(Path path) throws IOException
	{
		this(path.toUri().toURL());
	}
	public CSVLoader(URL file) throws IOException
	{
		this.file=file;
		dis= new BufferedReader(new InputStreamReader(file.openStream()));
		assert (dis) != null;
		String head_ColumnName = dis.readLine();
		_ColumnName=head_ColumnName.split(CharSeparator);
		NbrColumn=_ColumnName.length;
	}
	
	/**
	 * Get the number of line present in the file (including header if csv file).
	 * @return
	 * @throws IOException
	 */
	public int getNbrLines() throws IOException
	{
		assert file != null;
		java.io.LineNumberReader lineNumberReader = new java.io.LineNumberReader(new InputStreamReader(file.openStream(), Charset.forName("UTF-8")));
		lineNumberReader.skip(Long.MAX_VALUE);
		int lines = lineNumberReader.getLineNumber();
		lineNumberReader.close();
		return lines;
	}
	
	
	/**
	 * Get the header of the file
	 * @return
	 */
	public String getHeader()
	{
		String ColumnName = "";
		for (int i = 0; i < _ColumnName.length; i++)
			ColumnName+=_ColumnName[i]+";";
		return ColumnName;
	}
	
	/**
	 * Get the header column name at index i
	 * @param i
	 * @return
	 */
	public String getHeaderString(int i)
	{
		assert _ColumnName != null;
		assert _ColumnName[i] != null;
		return _ColumnName[i];
		
	}
	/**
	 * Retourne le nombre de colonnes.
	 * @return
	 */
	public int getNbrColumn()
	{
		return NbrColumn;
	}
	
	/**
	 * Get another line from the CSV file (constructor place the position of the file on the first entry, not on the header).
	 * @return CSVLine or null if nothing or EOF found.
	 * @throws IOException 
	 */
	public CSVLine getNext() throws IOException
	{
		assert (dis) != null;
		String line = dis.readLine();
		
		if (line==null) return null;
	
		String []datas=line.split(CharSeparator);
		CSVLine csv_line = new CSVLine(_ColumnName, datas);
		return csv_line;
	}

	/**
	 * @throws IOException 
	 * 
	 */
	public void close() throws IOException
	{
		dis.close();
	}

	/**
	 * Affiche une ligne.
	 * @param csvline
	 * @throws CSVColumnNotFoundException 
	 */
	public void displayLine(CSVLine csvline) throws CSVColumnNotFoundException
	{
		System.err.println("Entry : "+csvline.hashCode());
		for (int i = 0; i < getNbrColumn(); i++)
		{
			String colName = getHeaderString(i);
			String data = csvline.getString(colName);
			System.err.println(colName + " = "+data);
		}
		System.err.println("------------");
	}

	/**
	 * Rebobine au debut du fichier et lit la premiere ligne (le header).
	 * @throws IOException 
	 * 
	 */
	public void rewind() throws IOException
	{
		dis= new BufferedReader(new InputStreamReader(file.openStream()));
		assert (dis) != null;
		// Skip la ligne des titres
		dis.readLine();
	}
	

}
