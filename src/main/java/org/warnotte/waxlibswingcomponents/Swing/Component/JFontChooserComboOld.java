package org.warnotte.waxlibswingcomponents.Swing.Component;

import java.awt.GraphicsEnvironment;

import javax.swing.JComboBox;

public class JFontChooserComboOld extends JComboBox<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9093729231879874314L;

	public JFontChooserComboOld()
	{
		super();
		remplis_combo();
	}

	private void remplis_combo() {
		
		// Get all font family names
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    String fontNames[] = ge.getAvailableFontFamilyNames();
	    //Font [] ge.getAllFonts();
	    
	    // Iterate the font family names
	    for (int i=0; i<fontNames.length; i++) {
	    	this.insertItemAt(""+fontNames[i], i);
	    }
	    this.setSelectedIndex(0);
	}
}
