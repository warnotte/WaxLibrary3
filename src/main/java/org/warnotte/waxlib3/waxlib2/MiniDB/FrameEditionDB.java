package org.warnotte.waxlib3.waxlib2.MiniDB;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.EventListenerList;

import org.warnotte.waxlib3.waxlibswingcomponents.Dialog.DialogDivers;

import com.thoughtworks.xstream.XStream;

public class FrameEditionDB extends JFrame
{

	private static final long	serialVersionUID	= 1L;
	private JPanel				jContentPane		= null;
	private Panel_EditionDB jPanel_EditeurDB = null;
	
	XStream xstream = null;
	DB_Table<?> db_table = null;
	DBTableModelAbstract model = null;
	private JMenuBar jJMenuBar = null;
	private JMenu jMenu_File = null;
	private JMenuItem jMenuItem_Save = null;
	private String XMLfilename = null;
	private JPanel jPanel_BOUTONS = null;
	private JButton jButton_ADD_ROW = null;
	private JButton jButton_DELETE_ROW = null;
	private final boolean	show_buttons;
	private final JTable	jTable;
	

	protected EventListenerList listenerList = new EventListenerList();

    // This methods allows classes to register for MyEvents
    public void addMyEventListener(DBChangedEventListener listener) {
    	//System.err.println(this.getName()+" Adding a listener "+listener); 
        listenerList.add(DBChangedEventListener.class, listener);
    }

    // This methods allows classes to unregister for MyEvents
    public void removeMyEventListener(DBChangedEventListener listener) {
        listenerList.remove(DBChangedEventListener.class, listener);
    }

    // This private class is used to fire MyEvents
    public void fireMyEvent(DBChangedEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i]==DBChangedEventListener.class) {
            	//System.err.println("Transmit the event" +((MyEventListener)listeners[i+1]));
            	((DBChangedEventListener)listeners[i+1]).DBEventOccurred(evt);
            }
        }
    }
	
	/**
	 * This method initializes jPanel_EditeurDB	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private Panel_EditionDB getJPanel_EditeurDB(JTable jTable)
	{
		if (jPanel_EditeurDB == null)
		{
			jPanel_EditeurDB = new Panel_EditionDB(jTable, db_table, model);
		}
		return jPanel_EditeurDB;
	}

	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getJJMenuBar()
	{
		if (jJMenuBar == null)
		{
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getJMenu_File());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu_File	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu_File()
	{
		if (jMenu_File == null)
		{
			jMenu_File = new JMenu();
			jMenu_File.setText("File");
			jMenu_File.add(getJMenuItem_Save());
		}
		return jMenu_File;
	}

	/**
	 * This method initializes jMenuItem_Save	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem_Save()
	{
		if (jMenuItem_Save == null)
		{
			jMenuItem_Save = new JMenuItem();
			jMenuItem_Save.setText("Save");
			jMenuItem_Save.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					try
					{
						Save(xstream);
					} 
					catch (Exception e1)
					{
						DialogDivers.Show_dialog(e1, "Error during save");
						e1.printStackTrace();
						//Logs.getLogger().fatal(e1);
					}
				}
			});
		}
		return jMenuItem_Save;
	}
	
	protected void Save(XStream xstream) throws Exception
	{
		db_table.saveXML(xstream, XMLfilename);
	}
	
	/**
	 * This method initializes jPanel_BOUTONS	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_BOUTONS()
	{
		if (jPanel_BOUTONS == null)
		{
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			jPanel_BOUTONS = new JPanel();
			jPanel_BOUTONS.setLayout(new GridBagLayout());
			jPanel_BOUTONS.add(getJButton_ADD_ROW(), new GridBagConstraints());
			jPanel_BOUTONS.add(getJButton_DELETE_ROW(), gridBagConstraints);
		}
		return jPanel_BOUTONS;
	}

	/**
	 * This method initializes jButton_ADD_ROW	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_ADD_ROW()
	{
		if (jButton_ADD_ROW == null)
		{
			jButton_ADD_ROW = new JButton();
			jButton_ADD_ROW.setText("ADD");
			jButton_ADD_ROW.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					
						try
						{
							AddRow();
						} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1)
						{
							e1.printStackTrace();
							DialogDivers.Show_dialog(e1, e1.toString());
						}
					
					
				}
			});
		}
		return jButton_ADD_ROW;
	}


	/**
	 * This method initializes jButton_DELETE_ROW	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_DELETE_ROW()
	{
		if (jButton_DELETE_ROW == null)
		{
			jButton_DELETE_ROW = new JButton();
			jButton_DELETE_ROW.setText("DELETE");
			jButton_DELETE_ROW.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					DeleteRows();
				}
			});
		}
		return jButton_DELETE_ROW;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
	}

	/**
	 * This is the default constructor
	 */
	public FrameEditionDB(JTable table, DB_Table<?> db_table, DBTableModelAbstract model, XStream xstream, boolean show_load_save)
	{
		super();
		this.jTable = table;
		this.xstream=xstream;
		this.XMLfilename=db_table.fileName;
		this.model = model;
		this.db_table = db_table;
		this.show_buttons = show_load_save;
		initialize();
		this.setTitle("DB Editor - " +XMLfilename);
	}
	
	public FrameEditionDB(JTable table, DB_Table<?> db_table, DBTableModelAbstract model, XStream xstream)
	{
		this(table, db_table, model, xstream, true);
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(960, 512);
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel_EditeurDB(jTable), BorderLayout.CENTER);
			if (show_buttons)
			jContentPane.add(getJPanel_BOUTONS(), BorderLayout.NORTH);
		}
		return jContentPane;
	}

	protected void AddRow() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
	//	int rowIndex = model.getRowCount();
		db_table.addNew();
	//	model.fireTableRowsInserted(firstRow, lastRow);
		model.fireTableDataChanged();
		fireMyEvent(new DBChangedEvent(this,0));
	}


	protected void DeleteRows()
	{
		
		int selected_rows[] = jPanel_EditeurDB.getJTable().getSelectedRows();
		for (int i = 0; i < selected_rows.length; i++)
		{
			
			int selected_row = selected_rows[selected_rows.length-i-1];
			if (selected_row==-1) continue;
			
			long key = (Long)model.getValueAt(selected_row, 0);
			System.err.println("Key detected "+key);
			
		//	Object obj = model.db_table.elements.get((Integer)(int)key);
			Object removed = db_table.removeByID(key);
			
			
			if (removed==null)
				DialogDivers.Show_dialog("Problem during delete");		
	
		}

	//	model.fireTableRowsDeleted(selected_row, selected_row+1);
	//	model.fireTableStructureChanged();		
		model.fireTableDataChanged();
		
	}
}
