package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.PropertiesEditor;

import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

import org.warnotte.waxlib3.waxlibswingcomponents.Dialog.DialogDivers;

public class PropertiesTableModelAdapter extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1282020558230174176L;
	Properties props = null;
	
	public PropertiesTableModelAdapter(Properties props)
	{
		this.props = props;
	}
	@SuppressWarnings("unused")
	private PropertiesTableModelAdapter()
	{
		
	}

	

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return String.class;
	}

	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public int getRowCount()
	{
		return props.size();
	}

	@SuppressWarnings("unchecked")
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Set<?> set = props.entrySet();
		if (set.size()==0) return null;
		if (rowIndex>=set.size()) return null;
		Object [] objs = set.toArray();
		
		Entry<Object, Object> ent = (Entry<Object, Object>) objs[rowIndex];
		String value = "";
		if (columnIndex==0)
			value = (String) ent.getKey();
		if (columnIndex==1)
			value = (String) ent.getValue();
		return value;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		Set<?> set = props.entrySet();
		if (set.size()==0) return ;
		if (rowIndex>set.size()) return ;
		
		
		Object [] objs = set.toArray();
		Entry<Object,Object> ent = (Entry<Object,Object>)objs[rowIndex];
	//	String value = "";
		if (columnIndex==0)
		{
			// Faut supprimer l'ancienne clé
			String ancienne_cle = (String)ent.getKey();
			String ancienne_value = (String)ent.getValue();;
			Object exist = props.get(aValue);
			if (exist==null)
			{
				props.remove(ancienne_cle);
				props.put(aValue, ancienne_value);
				
			}
			else
				DialogDivers.Show_dialog("Another key already exist with that name.");
		}
		if (columnIndex==1)
			ent.setValue(aValue);
	}


	/*public void removeTableModelListener(TableModelListener l)
	{
		
		
	}

	public void addTableModelListener(TableModelListener l)
	{
		
		
	}
	*/
	
	
}