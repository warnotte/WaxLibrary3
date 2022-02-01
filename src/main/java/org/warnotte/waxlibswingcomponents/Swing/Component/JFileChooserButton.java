package org.warnotte.waxlibswingcomponents.Swing.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;

import org.warnotte.waxlibswingcomponents.Dialog.DialogDivers;

/**
 * Crée un bouton d'un certaine couleur offrant la possibilite de changer celle ci (JWColor, mais pas color qui ne contient pas de set)
 * @author wax
 *
 */
public class JFileChooserButton extends JButton implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9158407806747255454L;
	private File file = null;
	
	
	public JFileChooserButton(File file)
	{
		super();
		this.setFile(file);
		this.setText("F");
		this.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		String filename = DialogDivers.SaveDialog(null, "*", ".");
	    if (filename != null) {
	    	setFile(new File(filename));
	    } 
	}
	public void setFile(File col)
	{
		this.file = col;
	}
	public File getFile()
	{
		return file;
	}
}
