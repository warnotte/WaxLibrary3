package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.WSplitPaneMatic;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public class TestMultiSplitPane extends JFrame
{

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private WSplitPaneMatic SPLIT_A = null;
	private JPanel PANEL_A = null;
	private JPanel jPanel_SECOND = null;
	private WSplitPaneMatic SPLIT_B = null;
	private WSplitPaneMatic SPLIT_C = null;
	private JPanel PANEL_B = null;

	
	private JPanel PANEL_C = null;

	private JPanel PANEL_D = null;

	Thread_SplitPane thread_mouse =null;  //  @jve:decl-index=0:

	private WSplitPaneMatic WSplitPaneMatic = null;

	private WSplitPaneMatic WSplitPaneMatic1 = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JPanel jPanel3 = null;

	private WSplitPaneMatic WSplitPaneMatic2 = null;

	private JLabel jLabel = null;

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jLabel = new JLabel();
			jLabel.setText("JLabel");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getSPLIT_A(), gridBagConstraints);
		}
		return jPanel;
	}

	/**
	 * This method initializes WSplitPaneMatic	
	 * 	
	 * @return javax.swing.WSplitPaneMatic	
	 */
	private WSplitPaneMatic getSPLIT_A()
	{
		if (SPLIT_A == null)
		{
			SPLIT_A = new WSplitPaneMatic();
			SPLIT_A.setAutoSplit(false);
			SPLIT_A.setName("Split A");
			SPLIT_A.setDividerSize(3);
			SPLIT_A.setDividerLocation(0.5d);
			
			SPLIT_A.setLeftComponent(jLabel);
			SPLIT_A.setRightComponent(getJPanel_SECOND());
			SPLIT_A.setDividerLocation(0.50f);
			
			
		}
		return SPLIT_A;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPANEL_A()
	{
		if (PANEL_A == null)
		{
			PANEL_A = new JPanel();
			PANEL_A.setName("PANEL A");
			//PANEL_A.setLayout(new GridBagLayout());
			PANEL_A.setBackground(Color.red);
		}
		return PANEL_A;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_SECOND()
	{
		if (jPanel_SECOND == null)
		{
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.gridx = 0;
			jPanel_SECOND = new JPanel();
			jPanel_SECOND.setLayout(new GridBagLayout());
			jPanel_SECOND.add(getSPLIT_B(), gridBagConstraints1);
		}
		return jPanel_SECOND;
	}

	/**
	 * This method initializes WSplitPaneMatic1	
	 * 	
	 * @return javax.swing.WSplitPaneMatic	
	 */
	private WSplitPaneMatic getSPLIT_B()
	{
		if (SPLIT_B == null)
		{
			SPLIT_B = new WSplitPaneMatic();
			SPLIT_B.setName("Split B");
			SPLIT_B.setOrientation(JSplitPane.VERTICAL_SPLIT);
			SPLIT_B.setDividerLocation(0.5d);
			SPLIT_B.setDividerSize(3);
			SPLIT_B.setBottomComponent(getPANEL_B());
			SPLIT_B.setTopComponent(getSPLIT_C());
		}
		return SPLIT_B;
	}

	/**
	 * This method initializes WSplitPaneMatic2	
	 * 	
	 * @return javax.swing.WSplitPaneMatic	
	 */
	private WSplitPaneMatic getSPLIT_C()
	{
		if (SPLIT_C == null)
		{
			SPLIT_C = new WSplitPaneMatic();
			SPLIT_C.setName("SPLIT C");
			SPLIT_C.setDividerLocation(0.50d);
			SPLIT_C.setDividerSize(3);
			SPLIT_C.setRightComponent(getPANEL_D());
			SPLIT_C.setLeftComponent(getPANEL_C());
		}
		return SPLIT_C;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPANEL_B()
	{
		if (PANEL_B == null)
		{
			PANEL_B = new JPanel();
			PANEL_B.setLayout(new GridBagLayout());
			PANEL_B.setName("PANEL B");
			PANEL_B.setBackground(Color.green);
		}
		return PANEL_B;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPANEL_C()
	{
		if (PANEL_C == null)
		{
			PANEL_C = new JPanel();
			PANEL_C.setName("PANEL C");
			PANEL_C.setLayout(new GridBagLayout());
			PANEL_C.setBackground(Color.blue);
		}
		return PANEL_C;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPANEL_D()
	{
		if (PANEL_D == null)
		{
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.weightx = 1.0;
			PANEL_D = new JPanel();
			PANEL_D.setName("PANEL D");
			PANEL_D.setLayout(new GridBagLayout());
			PANEL_D.setBackground(new Color(255, 153, 153));
			PANEL_D.add(getWSplitPaneMatic(), gridBagConstraints2);
		}
		return PANEL_D;
	}

	/**
	 * This method initializes WSplitPaneMatic	
	 * 	
	 * @return javax.swing.WSplitPaneMatic	
	 */
	private WSplitPaneMatic getWSplitPaneMatic()
	{
		if (WSplitPaneMatic == null)
		{
			WSplitPaneMatic = new WSplitPaneMatic();
			WSplitPaneMatic.setDividerSize(3);
			WSplitPaneMatic.setAutoSplit(false);
			WSplitPaneMatic.setDividerLocation(0.5d);
			WSplitPaneMatic.setTopComponent(getWSplitPaneMatic1());
			WSplitPaneMatic.setBottomComponent(getJPanel3());
			WSplitPaneMatic.setOrientation(JSplitPane.VERTICAL_SPLIT);
		}
		return WSplitPaneMatic;
	}

	/**
	 * This method initializes WSplitPaneMatic1	
	 * 	
	 * @return javax.swing.WSplitPaneMatic	
	 */
	private WSplitPaneMatic getWSplitPaneMatic1()
	{
		if (WSplitPaneMatic1 == null)
		{
			WSplitPaneMatic1 = new WSplitPaneMatic();
			WSplitPaneMatic1.setDividerSize(3);
			WSplitPaneMatic1.setDividerLocation(0.5d);
			WSplitPaneMatic1.setLeftComponent(getJPanel1());
			WSplitPaneMatic1.setRightComponent(getJPanel2());
		}
		return WSplitPaneMatic1;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1()
	{
		if (jPanel1 == null)
		{
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.setBackground(new Color(102, 153, 0));
			
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2()
	{
		if (jPanel2 == null)
		{
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GridBagLayout());
			jPanel2.setBackground(new Color(51, 51, 0));
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel3()
	{
		if (jPanel3 == null)
		{
			jPanel3 = new JPanel();
			jPanel3.setLayout(new GridBagLayout());
			jPanel3.setBackground(Color.white);
		}
		return jPanel3;
	}

	/**
	 * This method initializes WSplitPaneMatic2	
	 * 	
	 * @return javax.swing.WSplitPaneMatic	
	 */
	private WSplitPaneMatic getWSplitPaneMatic2()
	{
		if (WSplitPaneMatic2 == null)
		{
			WSplitPaneMatic2 = new WSplitPaneMatic();
			WSplitPaneMatic2.setOrientation(JSplitPane.VERTICAL_SPLIT);
			WSplitPaneMatic2.setDividerLocation(0.5d);
			WSplitPaneMatic2.setDividerSize(3);
			WSplitPaneMatic2.setBottomComponent(getPANEL_A());
			WSplitPaneMatic2.setTopComponent(getJPanel());
		}
		return WSplitPaneMatic2;
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
				TestMultiSplitPane thisClass = new TestMultiSplitPane();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public TestMultiSplitPane()
	{
		super();
		initialize();
		

		Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
		      @Override
			protected void dispatchEvent(AWTEvent e) {
		        if (e instanceof MouseEvent) {
		          if (e.getID()==MouseEvent.MOUSE_MOVED) {
		        	  System.err.println("----------------");
			          // get the mouse click point relative to the content pane
		        	  Point p = ((MouseEvent)e).getPoint();
		        	  Point containerPoint = SwingUtilities.convertPoint(TestMultiSplitPane.this, p,getContentPane());

		        	  //find the component that under this point
		        	  Component component = SwingUtilities.getDeepestComponentAt(getContentPane(),containerPoint.x,containerPoint.y);
			        
		      		
		        	  thread_mouse.setCible(component,5);
		        	  
		         // System.err.println("LAST HACK = "+component.getName());

		           
		          } else if (e.getID()==MouseEvent.MOUSE_CLICKED) {
		              // get the mouse click point relative to the content pane
		        	  Point p = ((MouseEvent)e).getPoint();
		        	  Point containerPoint = SwingUtilities.convertPoint(TestMultiSplitPane.this, p,getContentPane());

		        	  //find the component that under this point
		        	  Component component = SwingUtilities.getDeepestComponentAt(getContentPane(),containerPoint.x,containerPoint.y);
			        
		        	 
		      		
		        	  thread_mouse.setCible(component,5);
		          }
		        }
		        super.dispatchEvent(e);
		        getContentPane().doLayout();
		    
		      }
		    });
	}

	

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(800,600);
		this.setPreferredSize(new Dimension(800, 600));
		this.setContentPane(getJContentPane());
		this.setTitle("Wax Split Pane test demo");
		if (thread_mouse==null)
		 {
			 thread_mouse = new Thread_SplitPane(getContentPane(),null);
			 thread_mouse.start();
  	  	}
 		
   	  
		this.validate();
		this.pack();
		
		
	/*	thread_mouse.setCible(jPanel,5000);
		thread_mouse.setCible(PANEL_A,5000);
		
		thread_mouse.setCible(jPanel_SECOND,5000);
		thread_mouse.setCible(PANEL_C,5000);
		thread_mouse.setCible(PANEL_B,5000);
		thread_mouse.setCible(PANEL_D,5000);
		thread_mouse.setCible(jPanel3,5000);
		thread_mouse.setCible(jPanel2,5000);
		thread_mouse.setCible(jPanel1,5000);*/
		
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
			jContentPane.add(getWSplitPaneMatic2(), BorderLayout.CENTER);
			
		}
		return jContentPane;
	}

}
