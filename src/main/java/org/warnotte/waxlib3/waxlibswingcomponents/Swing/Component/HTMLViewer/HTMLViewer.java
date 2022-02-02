 /**
 * 
 */
package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.HTMLViewer;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;

import org.warnotte.waxlib3.waxlibswingcomponents.Dialog.DialogDivers;

/*
 * @author wax
 */
public class HTMLViewer extends JFrame
{

	private static final long	serialVersionUID	= 1L;
	private JPanel				jContentPane		= null;
	private JEditorPane jTextArea = null;
	private JScrollPane jScrollPane = null;
	private JPanel jPanel_CONTROL = null;
	private JCheckBox jCheckBox_LOCK = null;
	private JButton jButton_Clear_Console = null;
	private JButton jButton_EmptyFile = null;
	String texte = "";
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JEditorPane getJTextArea()
	{
		if (jTextArea == null)
		{
			jTextArea = new JEditorPane();
			jTextArea.setEditable(false);
			jTextArea.setContentType("text/html");
		}
		return jTextArea;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane()
	{
		if (jScrollPane == null)
		{
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTextArea());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jPanel_CONTROL	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_CONTROL()
	{
		if (jPanel_CONTROL == null)
		{
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 0;
			jPanel_CONTROL = new JPanel();
			jPanel_CONTROL.setLayout(new GridBagLayout());
			jPanel_CONTROL.add(getJCheckBox_LOCK(), new GridBagConstraints());
			jPanel_CONTROL.add(getJButton_Clear_Console(), new GridBagConstraints());
			jPanel_CONTROL.add(getJButton_EmptyFile(), gridBagConstraints);
		}
		return jPanel_CONTROL;
	}

	/**
	 * This method initializes jCheckBox_LOCK	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox_LOCK()
	{
		if (jCheckBox_LOCK == null)
		{
			jCheckBox_LOCK = new JCheckBox();
			jCheckBox_LOCK.setText("ScrollLock");
			jCheckBox_LOCK.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					
				}
			});
		}
		return jCheckBox_LOCK;
	}

	/**
	 * This method initializes jButton_Clear_Console	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_Clear_Console()
	{
		if (jButton_Clear_Console == null)
		{
			jButton_Clear_Console = new JButton();
			jButton_Clear_Console.setText("Clear");
			jButton_Clear_Console.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					clear();
				}
			});
		}
		return jButton_Clear_Console;
	}

	protected void clear()
	{
		jTextArea.setText("");
	}

	/**
	 * This method initializes jButton_EmptyFile	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_EmptyFile()
	{
		if (jButton_EmptyFile == null)
		{
			jButton_EmptyFile = new JButton();
			jButton_EmptyFile.setText("Empty File");
			jButton_EmptyFile.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					if (DialogDivers.Show_YesNoDialog("Supprimer le contenu ?")==JOptionPane.YES_OPTION)
					{
						// TODO : Vider reelement le fichier.
						clear();
					}
				}
			});
		}
		return jButton_EmptyFile;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				HTMLViewer thisClass;
				try
				{
					thisClass = new HTMLViewer("");
					thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					thisClass.setVisible(true);
				}
				catch (IOException e)
				{
					DialogDivers.Show_dialog(e, "Perduuuu !!!");
					e.printStackTrace();
				}
				
			}
		});
	}
	
	/**
	 * This is the default constructor
	 * @throws IOException 
	 */
	public HTMLViewer(String text) throws IOException
	{
		
		super();
		this.texte=text;
		initialize();
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("Wax File Viewer");
		refresh(texte);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
			jContentPane.add(getJPanel_CONTROL(), BorderLayout.NORTH);
		}
		return jContentPane;
	}

	public void refresh(String buffer)
	{
		int v = jTextArea.getDocument().getLength();
		AttributeSet attr = ((HTMLEditorKit) jTextArea.getEditorKit()).getInputAttributes();
		try
		{
			jTextArea.getDocument().insertString(v, buffer, attr);	
		}
		catch (BadLocationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (jCheckBox_LOCK.isSelected()==false)
			jTextArea.setCaretPosition( jTextArea.getDocument().getLength() );
		
	}

}
