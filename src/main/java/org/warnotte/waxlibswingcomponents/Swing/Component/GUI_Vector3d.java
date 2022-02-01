package org.warnotte.waxlibswingcomponents.Swing.Component;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.vecmath.Vector3d;

public class GUI_Vector3d extends JPanel
{

	private static final long	serialVersionUID	= 1L;
	Vector3d vect = null;
	
	private JTextField jTextField_X = null;
	private JTextField jTextField_Y = null;
	private JTextField jTextField_Z = null;
	private JLabel jLabel_X = null;
	private JLabel jLabel_Y = null;
	private JLabel jLabel = null;
	String titre;
	
	/**
	 * This is the default constructor
	 */
	public GUI_Vector3d(String title, Vector3d vect)
	{
		super();
		this.titre = title;
		this.vect = vect;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.gridy = 3;
		jLabel = new JLabel();
		jLabel.setText("Z");
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.gridy = 1;
		jLabel_Y = new JLabel();
		jLabel_Y.setText("Y");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.gridy = 0;
		jLabel_X = new JLabel();
		jLabel_X.setText("X");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 3;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.gridx = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridx = 1;
		this.setSize(100, 64);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(null, titre, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
		this.add(getJTextField_X(), gridBagConstraints);
		this.add(getJTextField_Y(), gridBagConstraints1);
		this.add(getJTextField_Z(), gridBagConstraints2);
		this.add(jLabel_X, gridBagConstraints11);
		this.add(jLabel_Y, gridBagConstraints21);
		this.add(jLabel, gridBagConstraints3);
	}
	
	public void update_from_current_situation()
	{
		jTextField_X.setText(""+vect.x);
		jTextField_Y.setText(""+vect.y);
		jTextField_Z.setText(""+vect.z);
	}

	/**
	 * This method initializes jTextField_X	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_X()
	{
		if (jTextField_X == null)
		{
			jTextField_X = new JTextField();
			jTextField_X.setText(""+vect.x);
			jTextField_X.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					double v = Double.parseDouble(""+jTextField_X.getText());
					vect.x=v;
				}
			});
		}
		return jTextField_X;
	}

	/**
	 * This method initializes jTextField_Y	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_Y()
	{
		if (jTextField_Y == null)
		{
			jTextField_Y = new JTextField();
			jTextField_Y.setText(""+vect.y);
			jTextField_Y.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					double v = Double.parseDouble(""+jTextField_Y.getText());
					vect.y=v;
				}
			});
		}
		return jTextField_Y;
	}

	/**
	 * This method initializes jTextField_Z	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_Z()
	{
		if (jTextField_Z == null)
		{
			jTextField_Z = new JTextField();
			jTextField_Z.setText(""+vect.z);
			jTextField_Z.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					double v = Double.parseDouble(""+jTextField_Z.getText());
					vect.z=v;
				}
			});
		}
		return jTextField_Z;
	}

}
