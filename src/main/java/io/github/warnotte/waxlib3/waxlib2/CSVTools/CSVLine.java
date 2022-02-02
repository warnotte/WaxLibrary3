package io.github.warnotte.waxlib3.waxlib2.CSVTools;
/**
 * 
 */


import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author Warnotte Renaud
 *
 */
public class CSVLine
{
	String[] line;
	String[] columnname;
	
	/**
	 * @param _ColumnName
	 * @param line2
	 */
	public CSVLine(String[] _ColumnName, String[] line2)
	{
		this.columnname=_ColumnName;
		this.line=line2;
	}
	public String getString(String columnName) throws CSVColumnNotFoundException
	{
		return getString(getColumnIndex(columnName));
	}
	public String getString(int idxCol) throws CSVColumnNotFoundException
	{
		return line[idxCol];
	}
	public Integer getInteger(String columnName) throws CSVColumnNotFoundException
	{
		return getInteger(getColumnIndex(columnName));
	}
	public Integer getInteger(int idxColumn) throws CSVColumnNotFoundException
	{
		if (line[idxColumn].equalsIgnoreCase("null"))
			return null;
		return (int)Double.parseDouble(line[idxColumn]);
	}
	public BigDecimal getBigDecimal(String columnName) throws CSVColumnNotFoundException
	{
		return getBigDecimal(getColumnIndex(columnName));
	}
	public BigDecimal getBigDecimal(int idxColumn) throws CSVColumnNotFoundException
	{
		if (line[idxColumn].equalsIgnoreCase("null"))
			return null;
		return new BigDecimal(""+line[idxColumn]);
	}
	public Double getDouble(String columnName) throws CSVColumnNotFoundException
	{
		return getDouble(getColumnIndex(columnName));
	}
	public Double getDouble(int idxColumn) throws CSVColumnNotFoundException
	{
		if (line[idxColumn].equalsIgnoreCase("null"))
			return null;
		return Double.parseDouble(line[idxColumn]);
	}
	public Float getFloat(String columnName) throws CSVColumnNotFoundException
	{
		return getFloat(getColumnIndex(columnName));
	}
	public Float getFloat(int idxColumn) throws CSVColumnNotFoundException
	{
		if (line[idxColumn].equalsIgnoreCase("null"))
			return null;
		return Float.parseFloat(line[idxColumn]);
	}
	private int getColumnIndex(String columnName) throws CSVColumnNotFoundException
	{
		for (int i = 0; i < columnname.length; i++)
			if (columnName.equalsIgnoreCase(columnname[i]))
				return i;
		throw new CSVColumnNotFoundException("Column "+columnName+" not found");
	}
	@Override
	public String toString()
	{
		return "CSVLine [line=" + Arrays.toString(line) + ", columnname=" + Arrays.toString(columnname) + "]";
	}

	
}
