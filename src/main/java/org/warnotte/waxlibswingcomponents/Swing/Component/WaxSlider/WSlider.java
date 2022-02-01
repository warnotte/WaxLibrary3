package org.warnotte.waxlibswingcomponents.Swing.Component.WaxSlider;

import javax.swing.JSlider;


public class WSlider extends JSlider 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5256389372159595448L;
	public float divider = 1;

	public WSlider(int min, int max, int divider1)
	{
		this.setMinimum(min);
		this.setMaximum(max);
		divider = divider1;
	}

	public synchronized final float getDivider()
	{
		return divider;
	}

	public synchronized final void setDivider(float divider)
	{
		this.divider = divider;
	}
	
}
