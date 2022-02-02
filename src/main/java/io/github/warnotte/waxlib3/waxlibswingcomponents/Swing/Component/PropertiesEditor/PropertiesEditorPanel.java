package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.PropertiesEditor;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import io.github.warnotte.waxlib3.waxlibswingcomponents.Dialog.DialogDivers;

import java.awt.event.KeyEvent;

/**
 * Panel pour editer les classes Properties.
 * @author Warnotte Renaud 2010
 *
 */
public class PropertiesEditorPanel extends JPanel
{

	private static final long	serialVersionUID	= 1L;
	private PropertiesTable jPanel_CENTER = null;
	private JPanel jPanel_SOUTH = null;
	
	Properties props = null;
	
	private JButton jButton_ADD_NEW_PROPERTIES = null;
	private JButton jButton_DELETE_PROPERTIES = null;
	private JButton jButton_DBG = null;
	private JButton jButton_LOAD = null;
	private JButton jButton_SAVE = null;

	/**
	 * This method initializes jPanel_CENTER	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private PropertiesTable getJPanel_CENTER()
	{
		if (jPanel_CENTER == null)
		{
			jPanel_CENTER = new PropertiesTable(props);
			
		}
		return jPanel_CENTER;
	}

	/**
	 * This method initializes jPanel_SOUTH	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_SOUTH()
	{
		if (jPanel_SOUTH == null)
		{
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 4;
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 3;
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 5;
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.weightx = 1.0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridy = 0;
			jPanel_SOUTH = new JPanel();
			jPanel_SOUTH.setLayout(new GridBagLayout());
			jPanel_SOUTH.add(getJButton_ADD_NEW_PROPERTIES(), gridBagConstraints1);
			jPanel_SOUTH.add(getJButton_DELETE_PROPERTIES(), gridBagConstraints);
			jPanel_SOUTH.add(getJButton_DBG(), gridBagConstraints11);
			jPanel_SOUTH.add(getJButton_LOAD(), gridBagConstraints2);
			jPanel_SOUTH.add(getJButton_SAVE(), gridBagConstraints3);
		}
		return jPanel_SOUTH;
	}

	/**
	 * This method initializes jButton_ADD_NEW_PROPERTIES	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_ADD_NEW_PROPERTIES()
	{
		if (jButton_ADD_NEW_PROPERTIES == null)
		{
			jButton_ADD_NEW_PROPERTIES = new JButton();
			jButton_ADD_NEW_PROPERTIES.setToolTipText("Add a new row");
			jButton_ADD_NEW_PROPERTIES.setIcon(new ImageIcon(getClass().getResource("/org/warnotte/Swing/Component/PropertiesEditor/icons/icon_plus.gif")));
			jButton_ADD_NEW_PROPERTIES.setMnemonic(KeyEvent.VK_UNDEFINED);
			jButton_ADD_NEW_PROPERTIES.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							props.put("NEWKEY", "NEWVALUE");
							AbstractTableModel model = (AbstractTableModel)jPanel_CENTER.getModel();
							model.fireTableDataChanged();
						}
					});
					
				}
			});
		}
		return jButton_ADD_NEW_PROPERTIES;
	}

	/**
	 * This method initializes jButton_DELETE_PROPERTIES	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_DELETE_PROPERTIES()
	{
		if (jButton_DELETE_PROPERTIES == null)
		{
			jButton_DELETE_PROPERTIES = new JButton();
			jButton_DELETE_PROPERTIES.setToolTipText("Delete the selected row");
			jButton_DELETE_PROPERTIES.setIcon(new ImageIcon(getClass().getResource("/org/warnotte/Swing/Component/PropertiesEditor/icons/icon_minus.gif")));
			jButton_DELETE_PROPERTIES.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
					int selindex = jPanel_CENTER.getSelectedRow();
					if (selindex==-1)
						return;
					String key = (String) jPanel_CENTER.getModel().getValueAt(selindex, 0);
					if (key!=null)
					{
						props.remove(key);
						
					
						AbstractTableModel model = (AbstractTableModel)jPanel_CENTER.getModel();
						model.fireTableDataChanged();
					}
						}
					});
				}
			});
		}
		return jButton_DELETE_PROPERTIES;
	}


	/**
	 * This is the default constructor
	 */
	public PropertiesEditorPanel(Properties props)
	{
		super();
		this.props = props;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(300, 200);
		setLayout(new BorderLayout());
		add(new JScrollPane(getJPanel_CENTER()), BorderLayout.CENTER);
		add(getJPanel_SOUTH(), BorderLayout.NORTH);
	}

	/**
	 * This method initializes jButton_DBG	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_DBG()
	{
		if (jButton_DBG == null)
		{
			jButton_DBG = new JButton();
			jButton_DBG.setToolTipText("Debug values in console ...");
			jButton_DBG.setIcon(new ImageIcon(getClass().getResource("/org/warnotte/Swing/Component/PropertiesEditor/icons/icon_debug.png")));
			jButton_DBG.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					debug();
				}
			});
		}
		return jButton_DBG;
	}
	
	protected void debug()
	{
		Properties p = props;
		Set<?> set = p.entrySet();
		Object [] objs = set.toArray();
		for (int i = 0; i < objs.length; i++)
		{
			Entry<?, ?> ent = (Entry<?, ?>)objs[i];
			System.err.println(i+") "+ ent.getKey()+"== "+ent.getValue());
		}
	}

	/**
	 * This method initializes jButton_LOAD	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_LOAD()
	{
		if (jButton_LOAD == null)
		{
			jButton_LOAD = new JButton();
			jButton_LOAD.setToolTipText("Load a props.xml file");
			jButton_LOAD.setIcon(new ImageIcon(getClass().getResource("/org/warnotte/Swing/Component/PropertiesEditor/icons/icon_load.png")));
			jButton_LOAD.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					try
					{
						Load();
					} catch (InvalidPropertiesFormatException e1)
					{
						DialogDivers.Show_dialog(e1, "Error loading props");
						e1.printStackTrace();
					} catch (IOException e1)
					{
						DialogDivers.Show_dialog(e1, "Error loading props");
						e1.printStackTrace();
					} catch (HeadlessException e1)
					{
						DialogDivers.Show_dialog(e1, "Error loading props");
						e1.printStackTrace();
					} catch (Exception e1)
					{
						DialogDivers.Show_dialog(e1, "Error loading props");
						e1.printStackTrace();
					}
				}
			});
		}
		return jButton_LOAD;
	}

	
	/**
	 * This method initializes jButton_SAVE	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_SAVE()
	{
		if (jButton_SAVE == null)
		{
			jButton_SAVE = new JButton();
			jButton_SAVE.setToolTipText("Save a props.xml file");
			jButton_SAVE.setIcon(new ImageIcon(getClass().getResource("/org/warnotte/Swing/Component/PropertiesEditor/icons/icon_save.png")));
			jButton_SAVE.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					try
					{
						Save();
					} catch (IOException e1)
					{
						DialogDivers.Show_dialog(e1, "Error saving props");
						e1.printStackTrace();
					}
				}
			});
		}
		return jButton_SAVE;
	}
	

	protected void Load() throws HeadlessException, Exception
	{
		File f = DialogDivers.LoadDialog(new JFrame(), "props.xml");
		if (f!=null)
		{
			Load(f.getAbsolutePath());
		}
	}
	
	protected void Load(String filename) throws InvalidPropertiesFormatException, IOException
	{
		FileInputStream fos = new FileInputStream(new File(filename));
		props.loadFromXML(fos);
		fos.close();
		repaint();
	}
	
	protected void Save() throws IOException
	{
		String file = DialogDivers.SaveDialog(new JFrame(), "props.xml", ".");
		if (file!=null)
			Save(file);
	}
	
	protected void Save(String filename) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(new File(filename));
		props.storeToXML(fos, "File written by OBJ2GUI::PropertiesEditorPanel - Warnotte Renaud 2010");
		fos.close();
	}


}
