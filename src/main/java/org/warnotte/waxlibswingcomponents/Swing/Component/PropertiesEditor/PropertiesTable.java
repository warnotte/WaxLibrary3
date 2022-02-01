package org.warnotte.waxlibswingcomponents.Swing.Component.PropertiesEditor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Properties;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class PropertiesTable extends JTable implements KeyListener
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5632130045179527427L;

	public PropertiesTable(Properties props)
	{
		PropertiesTableModelAdapter model = new PropertiesTableModelAdapter(props); 
		setModel(model);
		JTableHeader header = this.createDefaultTableHeader();
		TableColumn c1 = header.getColumnModel().getColumn(0);
		TableColumn c2 = header.getColumnModel().getColumn(1);
		c1.setHeaderValue("Key");
		c2.setHeaderValue("Value");
		this.setTableHeader(header);
		this.addKeyListener(this);
	}

	public void keyPressed(KeyEvent e)
	{
		System.err.println("KeyP");
		
	}

	public void keyReleased(KeyEvent e)
	{
		System.err.println("KeyR");
		repaint();
	}

	public void keyTyped(KeyEvent e)
	{
		System.err.println("KeyTyped");
	}
}
