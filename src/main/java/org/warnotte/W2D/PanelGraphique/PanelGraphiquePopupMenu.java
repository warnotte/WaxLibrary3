package org.warnotte.W2D.PanelGraphique;

import java.io.File;
import java.io.IOException;

import javax.swing.JPopupMenu;

import org.warnotte.waxlibswingcomponents.Dialog.DialogDivers;

import javax.swing.JMenuItem;

public class PanelGraphiquePopupMenu extends JPopupMenu
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6373430517516331536L;
	PanelGraphiqueBaseBase		panel;
	private JMenuItem			jMenuItem_SaveBMP	= null;
	//private JMenuItem			jMenuItem_SavePDF	= null;

	PanelGraphiquePopupMenu(PanelGraphiqueBaseBase panel)
	{
		this.panel = panel;
		initialize();
	}

	private void initialize()
	{
		this.add(getJMenuItem_SaveBMP());
	
	}

	
	/**
	 * This method initializes jMenuItem_SaveBMP
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem_SaveBMP()
	{
		if (jMenuItem_SaveBMP == null)
		{
			jMenuItem_SaveBMP = new JMenuItem();
			jMenuItem_SaveBMP.setText("Save BMP");
			jMenuItem_SaveBMP.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					try
					{
						SaveBMP();
					} catch (IOException e1)
					{
						DialogDivers.Show_dialog(e1, "Error saving BMP");
						e1.printStackTrace();
					}
				}
			});
		}
		return jMenuItem_SaveBMP;
	}

	protected void SaveBMP() throws IOException
	{
		panel.save(new File("screens.bmp"));

	}
}
