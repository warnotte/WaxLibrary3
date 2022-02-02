package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.PropertiesEditor;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TestPropertiesEditor extends JFrame
{

	private static final long	serialVersionUID	= 1L;
	private JPanel				jContentPane		= null;  //  @jve:decl-index=0:
	private PropertiesEditorPanel jTree = null;
	Test_propertiesEditor test = new Test_propertiesEditor();
	/**
	 * This method initializes jTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private PropertiesEditorPanel getJTree()
	{
		if (jTree == null)
		{
			jTree = new PropertiesEditorPanel(test.getProperties());
		}
		return jTree;
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
				TestPropertiesEditor thisClass = new TestPropertiesEditor();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public TestPropertiesEditor()
	{
		super();
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
		this.setTitle("JFrame");
		jContentPane = new JPanel();
		this.add(jContentPane);
		jContentPane.add(getJTree(), BorderLayout.CENTER);
		setTitle("Test de l'éditeur de properties");
	}

	

}  //  @jve:decl-index=0:
