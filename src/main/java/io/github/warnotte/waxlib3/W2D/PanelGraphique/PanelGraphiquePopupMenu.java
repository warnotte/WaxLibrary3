package io.github.warnotte.waxlib3.W2D.PanelGraphique;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import io.github.warnotte.waxlib3.waxlibswingcomponents.Dialog.DialogDivers;

public class PanelGraphiquePopupMenu extends JPopupMenu
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6373430517516331536L;
	PanelGraphiqueBaseBase		panel;
	private JMenu				jMenu_Export		= null;
	private JMenuItem			jMenuItem_SavePNG	= null;
	private JMenuItem			jMenuItem_SavePDF	= null;
	private JMenuItem			jMenuItem_SaveSVG	= null;

	PanelGraphiquePopupMenu(PanelGraphiqueBaseBase panel)
	{
		this.panel = panel;
		initialize();
	}

	private void initialize()
	{
		jMenuItem_SavePNG = new JMenuItem();
		jMenuItem_SavePNG.setText("Save PNG");
		jMenuItem_SavePNG.addActionListener(a -> {
			SavePNG();
		});
		jMenuItem_SavePDF = new JMenuItem();
		jMenuItem_SavePDF.setText("Save PDF");
		jMenuItem_SavePDF.addActionListener(a -> {
			SavePDF();
		});
		jMenuItem_SaveSVG = new JMenuItem();
		jMenuItem_SaveSVG.setText("Save SVG");
		jMenuItem_SaveSVG.addActionListener(a -> {
			SaveSVG();
		});

		jMenu_Export = new JMenu("Export view");
		jMenu_Export.add(jMenuItem_SavePNG);
		//jMenu_Export.add(jMenuItem_SavePDF);
		//jMenu_Export.add(jMenuItem_SaveSVG);

		this.add(jMenu_Export);

	}

	protected void SavePNG()
	{
		String filename = DialogDivers.SaveDialog(new JFrame(), "png");
		panel.save(new File(filename));
	}

	protected void SavePDF()
	{
		String filename = DialogDivers.SaveDialog(new JFrame(), "pdf");
		panel.savePDF(new File(filename));
	}

	protected void SaveSVG()
	{
		String filename = DialogDivers.SaveDialog(new JFrame(), "svg");
		panel.saveSVG(new File(filename));
	}
}
