package io.github.warnotte.waxlib3.W2D.PanelGraphique.tests.spirograph;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class TLableModel extends DefaultTableModel {

	Manager manager;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4763379999233442908L;

	public TLableModel(Manager manager) {
		this.manager = manager;
	}

	@Override
	public int getRowCount() {
		return 2;
	}

	@Override
	public int getColumnCount() {
		if (manager==null)
			return 0;
		return manager.getModel().getDivsX().length+1;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return ""+columnIndex;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex>0)
		return true;
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Model model = manager.getModel();
		if (columnIndex==0)
		{
			if (rowIndex==0) return "Divider";
			if (rowIndex==1) return "Scale";
		}
		columnIndex-=1;
		if (rowIndex==0) {
			return model.getDivsX()[columnIndex];
		}
		if (rowIndex==1) {
			return model.getScalX()[columnIndex];
		}
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Model model = manager.getModel();
		columnIndex-=1;
		if (rowIndex==0) {
			model.getDivsX()[columnIndex] = Double.parseDouble(""+aValue);
		}
		if (rowIndex==1) {
			model.getScalX()[columnIndex] = Double.parseDouble(""+aValue);
		}
		fireTableDataChanged();
	}


}
