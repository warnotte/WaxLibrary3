package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.WaxSpinnerDisplay;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.io.IOException;
import javax.swing.JLabel;

public class test_PanelCompteur extends JFrame
{

	private static final long	serialVersionUID	= 1L;
	private JPanel				jContentPane		= null;
	PanelCompteurTournant panelcompteur = null;
	
	private JTextField jTextField_L = null;
	private JPanel	jPanel1;
	private JLabel jLabel_Label = null;


	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1()
	{
		if (jPanel1 == null)
		{
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			jLabel_Label = new JLabel();
			jLabel_Label.setText("Entrer la valeur");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0;
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(jLabel_Label, gridBagConstraints1);
			jPanel1.add(getJTextField_L(), gridBagConstraints);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jTextField_L	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_L()
	{
		if (jTextField_L == null)
		{
			jTextField_L = new JTextField();
			jTextField_L.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					// TODO : !!!! si jamais tu rentres un mauvais chiffre ...
					panelcompteur.setValue(jTextField_L.getText());
				}
			});
		}
		return jTextField_L;
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
				test_PanelCompteur thisClass;
				try
				{
					thisClass = new test_PanelCompteur();
					thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					thisClass.setVisible(true);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		});
	}

	/**
	 * This is the default constructor
	 * @throws IOException 
	 */
	public test_PanelCompteur() throws IOException
	{
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @throws IOException 
	 */
	private void initialize() throws IOException
	{
		this.setSize(640, 240);
		panelcompteur = new PanelCompteurTournant(4, 128);
		this.setContentPane(getJContentPane());
		this.setTitle("Frame de test du compteur tournant by wax78");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 * @throws IOException 
	 */
	private JPanel getJContentPane() throws IOException
	{
		if (jContentPane == null)
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel1(), BorderLayout.NORTH);
			jContentPane.add(panelcompteur, BorderLayout.CENTER);
		}
		return jContentPane;
	}

}
