package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.JList;

import java.util.List;

import javax.swing.AbstractListModel;

public class SwapListModel<T> extends AbstractListModel<T> {
	/**
	* 
	*/
	private static final long serialVersionUID = 373590131644263713L;
	List<T> list_to_present = null;

	public SwapListModel(List<T> list_to_present) {
		this.list_to_present = list_to_present;
	}

	public int getSize() {
		return list_to_present.size();
	}

	public T getElementAt(int n) {
		return list_to_present.get(n);
	}

	public T get(int n) {
		return list_to_present.get(n);
	}

	public void remove(int underpos) {
		list_to_present.remove(underpos);
	}

	public void add(int dir, T selected) {
		list_to_present.add(dir, selected);
	}

}
