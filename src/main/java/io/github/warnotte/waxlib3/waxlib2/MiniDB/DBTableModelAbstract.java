package io.github.warnotte.waxlib3.waxlib2.MiniDB;

import javax.swing.table.AbstractTableModel;


public abstract class DBTableModelAbstract extends AbstractTableModel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1274468057173821593L;
	protected DB_Table<?> db_table;
	protected String[] columnNames = null;
	
	protected DBTableModelAbstract(DB_Table<?> db_table)
	{
		this.db_table=db_table;
	}
	
	@Override
	public String getColumnName(int col) { 
		if (columnNames==null)
		{
			System.err.println("You forget to set columnName");
			return "Void";
		}
		return columnNames[col].toString();
    }
	
	public abstract int getColumnCount();

	public int getRowCount()
	{
		return db_table.toList().size();
	}

	public abstract Object getValueAt(int rowIndex, int columnIndex);

	/**
	 * @param id
	 * @param i
	 * @return
	 */
	public static double arrondir(double in, int decimal) {
		double v = ((Math.round(in * (Math.pow(10, decimal)))) / (Math.pow(10, decimal)));
		return v;
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col > 0) {
            return true;
        } else {
            return false;
        }
    }
}
