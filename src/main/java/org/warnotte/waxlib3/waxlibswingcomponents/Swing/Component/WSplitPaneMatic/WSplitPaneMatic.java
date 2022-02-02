package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.WSplitPaneMatic;

import javax.swing.JSplitPane;


public class WSplitPaneMatic extends JSplitPane
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8350226284375783444L;
	boolean AutoSplit = false;
	
	public WSplitPaneMatic()
	{
		this.setDividerSize(3);
	}
	
	public void setAutoSplit(boolean value)
	{
		this.AutoSplit=value;
	}
	public boolean getAutoSplit()
	{
		return AutoSplit;
	}
}
