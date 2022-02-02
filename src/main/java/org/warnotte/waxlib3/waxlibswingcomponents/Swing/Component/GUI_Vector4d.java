package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.vecmath.Vector4d;

public class GUI_Vector4d extends JPanel
{

	private static final long	serialVersionUID	= 1L;
	Vector4d vect = null;
	
	private JTextField jTextField_X = null;
	private JTextField jTextField_Y = null;
	private JTextField jTextField_Z = null;
	private JTextField jTextField_W = null;
	private JLabel jLabel_X = null;
	private JLabel jLabel_Y = null;
	private JLabel jLabel_Z = null;
//	private JLabel jLabel_W = null;
	String titre;
	private JLabel jLabel_W1 = null;
	
	/**
	 * This is the default constructor
	 */
	public GUI_Vector4d(String title, Vector4d vect)
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
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.gridx = 3;
		gridBagConstraints22.fill = GridBagConstraints.BOTH;
		gridBagConstraints22.gridy = 4;
		jLabel_W1 = new JLabel();
		jLabel_W1.setText("W");
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.fill = GridBagConstraints.BOTH;
		gridBagConstraints12.gridy = 4;
		gridBagConstraints12.weightx = 1.0;
		gridBagConstraints12.gridx = 5;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 3;
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.gridy = 3;
		jLabel_Z = new JLabel();
		jLabel_Z.setText("Z");
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 3;
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.gridy = 1;
		jLabel_Y = new JLabel();
		jLabel_Y.setText("Y");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 3;
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.gridy = 0;
		jLabel_X = new JLabel();
		jLabel_X.setText("X");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 3;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.gridx = 5;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.gridx = 5;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridx = 5;
		this.setSize(110, 85);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(null, titre, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
		this.add(getJTextField_X(), gridBagConstraints);
		this.add(getJTextField_Y(), gridBagConstraints1);
		this.add(getJTextField_Z(), gridBagConstraints2);
		this.add(jLabel_X, gridBagConstraints11);
		this.add(jLabel_Y, gridBagConstraints21);
		this.add(jLabel_Z, gridBagConstraints3);
		this.add(getJTextField_W(), gridBagConstraints12);
		this.add(jLabel_W1, gridBagConstraints22);
	}
	
	public void update_from_current_situation()
	{
		jTextField_X.setText(""+vect.x);
		jTextField_Y.setText(""+vect.y);
		jTextField_Z.setText(""+vect.z);
		jTextField_W.setText(""+vect.w);
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

	private JTextField getJTextField_W()
	{
		if (jTextField_W == null)
		{
			jTextField_W = new JTextField();
			jTextField_W.setText(""+vect.w);
			jTextField_W.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					double v = Double.parseDouble(""+jTextField_W.getText());
					vect.w=v;
				}
			});
		}
		return jTextField_W;
	}

}
