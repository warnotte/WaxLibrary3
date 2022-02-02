package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.CurveEditor;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TestMainFrame extends JFrame
{

	private static final long	serialVersionUID	= 1L;
	private JPanel				jContentPane		= null;
	private CurvePanel jPanel_Curve = null;

	/**
	 * This method initializes jPanel_Curve	
	 * 	
	 * @return javax.swing.JPanel	
	 * @throws Exception 
	 */
	private CurvePanel getJPanel_Curve() throws Exception
	{
		if (jPanel_Curve == null)
		{
			jPanel_Curve = new CurvePanel();
		}
		return jPanel_Curve;
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
				TestMainFrame thisClass;
				try {
					thisClass = new TestMainFrame();
					thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					thisClass.setVisible(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * This is the default constructor
	 * @throws Exception 
	 */
	public TestMainFrame() throws Exception
	{
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @throws Exception 
	 */
	private void initialize() throws Exception
	{
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 * @throws Exception 
	 */
	private JPanel getJContentPane() throws Exception
	{
		if (jContentPane == null)
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel_Curve(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

}
