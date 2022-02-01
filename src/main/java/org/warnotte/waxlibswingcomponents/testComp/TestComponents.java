/**
 * 
 */
package org.warnotte.waxlibswingcomponents.testComp;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.warnotte.waxlibswingcomponents.FileChooser.FileChooser;
import org.warnotte.waxlibswingcomponents.Swing.Component.JFontChooserCombo;
import org.warnotte.waxlibswingcomponents.Swing.Component.FileBrowser.FileBrowserPanel;
import org.warnotte.waxlibswingcomponents.Swing.Component.WaxSlider.WRoundSlider;
import org.warnotte.waxlibswingcomponents.Swing.Component.WaxSpinnerDisplay.PanelCompteurTournant;

import javax.swing.event.ChangeEvent;

/**
 * @author Warnotte Renaud
 *
 */
public class TestComponents extends JFrame
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4015400085687675901L;
	private final JPanel	contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					TestComponents frame = new TestComponents();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public TestComponents() throws IOException
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		final PanelCompteurTournant panelCompteurTournant = new PanelCompteurTournant(4, 10);
		
		final WRoundSlider roundSlider = new WRoundSlider();
		roundSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				System.err.println("Toto");
				panelCompteurTournant.setValue(""+roundSlider.getValue());
			}
			
		});
		contentPane.add(roundSlider, BorderLayout.SOUTH);
		
		FileChooser fileChooser = new FileChooser();
		roundSlider.add(fileChooser, BorderLayout.EAST);
		
		FileBrowserPanel fileBrowserPanel = new FileBrowserPanel();
		contentPane.add(fileBrowserPanel, BorderLayout.CENTER);
		
		JFontChooserCombo jFontChooserCombo = new JFontChooserCombo(new Font("Arial", Font.BOLD, 14));
		contentPane.add(jFontChooserCombo, BorderLayout.WEST);
		
		
		contentPane.add(panelCompteurTournant, BorderLayout.NORTH);
		
	}

}
