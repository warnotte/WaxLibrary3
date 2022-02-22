package io.github.warnotte.waxlib3.core.MiniDB;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Dimension;

public class Panel_EditionDB extends JPanel
{

	private static final long	serialVersionUID	= 1L;
	private JTable jTable = null;
	DBTableModelAbstract model = null;
	DB_Table<?> db_table;
	
	/**
	 * This is the default constructor
	 */
	public Panel_EditionDB(JTable jtable, DB_Table<?> db_table, DBTableModelAbstract model)
	{
		super();
		this.jTable = jtable;
		this.model = model;
		this.db_table = db_table;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setLayout(new BorderLayout());
		this.setSize(640, 480);
		this.setPreferredSize(new Dimension(640, 640));
		this.add(new JScrollPane(jTable), BorderLayout.CENTER);
	
	}

	public JTable getJTable()
	{
		return jTable;
	}


	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	/*public JTable getJTable()
	{
		if (jTable == null)
		{
			jTable = new JTable(model);
			jTable.setAutoCreateRowSorter(true);
		}
		return jTable;
	}*/

}
