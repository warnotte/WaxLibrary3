/*
 * FrameTest.java
 *
 * Created on 17 août 2007, 16:40
 */

package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.WaxSlider;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

// TODO : 
/**
 * 1)Implementation du keylistener gauche droite
 * 2)
 * 3)
 * 4)
 * 5)
 * ...
 * 
 */

/**
 *
 * @author  Fran
 */
public class FrameTest_PLAT extends javax.swing.JFrame{
    
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -9110284552563018558L;
	WFlatSlider pbt;
	WFlatSlider pbt2;

    /** Creates new form FrameTest */
    public FrameTest_PLAT() {
    	
        initComponents();
        
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
        gridBagConstraints42.gridx = 0;
        gridBagConstraints42.fill = GridBagConstraints.BOTH;
        gridBagConstraints42.gridy = 2;
        GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
        gridBagConstraints32.fill = GridBagConstraints.BOTH;
        gridBagConstraints32.gridy = 1;
        gridBagConstraints32.weightx = 1.0;
        gridBagConstraints32.gridx = 4;
        GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
        gridBagConstraints23.fill = GridBagConstraints.BOTH;
        gridBagConstraints23.gridy = 1;
        gridBagConstraints23.weightx = 1.0;
        gridBagConstraints23.gridx = 2;
        GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
        gridBagConstraints13.gridx = 0;
        gridBagConstraints13.fill = GridBagConstraints.BOTH;
        gridBagConstraints13.gridy = 6;
        GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
        gridBagConstraints41.gridx = 1;
        gridBagConstraints41.fill = GridBagConstraints.BOTH;
        gridBagConstraints41.gridy = 5;
        jLabel_Angle = new JLabel();
        GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
        gridBagConstraints31.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints31.gridy = 5;
        gridBagConstraints31.weightx = 1.0;
        gridBagConstraints31.gridx = 2;
        GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
        gridBagConstraints22.gridx = 1;
        gridBagConstraints22.fill = GridBagConstraints.BOTH;
        gridBagConstraints22.gridy = 4;
        GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        gridBagConstraints12.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints12.gridy = 4;
        gridBagConstraints12.weightx = 1.0;
        gridBagConstraints12.gridx = 2;
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.gridx = 0;
        gridBagConstraints21.fill = GridBagConstraints.BOTH;
        gridBagConstraints21.gridy = 1;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.fill = GridBagConstraints.BOTH;
        gridBagConstraints11.gridy = 4;
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridx = 1;
        gridBagConstraints7.fill = GridBagConstraints.BOTH;
        gridBagConstraints7.gridy = 3;
        jLabel_Space = new JLabel();
        jLabel_Space.setText("Space");
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 3;
        gridBagConstraints6.fill = GridBagConstraints.BOTH;
        gridBagConstraints6.gridy = 0;
        jLabel_Y = new JLabel();
        jLabel_Y.setText("Max");
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.gridx = 1;
        gridBagConstraints5.fill = GridBagConstraints.BOTH;
        gridBagConstraints5.gridy = 0;
        jLabel_X = new JLabel();
        jLabel_X.setText("Min");
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints4.gridy = 3;
        gridBagConstraints4.ipady = 8;
        gridBagConstraints4.weightx = 1.0;
        gridBagConstraints4.gridx = 2;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.ipady = 8;
        gridBagConstraints3.weightx = 1.0;
        gridBagConstraints3.gridx = 4;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.ipady = 8;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.gridx = 2;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.ipadx = 110;
        gridBagConstraints1.gridy = 3;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.ipadx = 113;
        gridBagConstraints.gridy = 5;
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jSlider_Max = new javax.swing.JSlider();
        pbt = new WFlatSlider(0,1000);
        pbt2 = new WFlatSlider(-2500,2500,0);
        pbt2.setDivider(1000);
        pbt.setSize(320,200);
        pbt.setValue(50);
    	pbt.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
            	int val = (int) pbt.getValue();
            	jTextField1.setText(""+val);
            	
           }
       });

    	jLabel_CircleProportion = new JLabel();
     //   jLabel_CircleProportion.setText("Proportion="+pbt.getMaximumAngle());
        
    	jSlider_Max.setMaximum(100);
    	jSlider_Max.setMinimum(0);
    	jSlider_Max.setValue(pbt.getMaximum());
    	//jLabel_Angle.setText("AngleOffset="+pbt.getAngleOffset());
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(320, 200));
        this.setPreferredSize(new Dimension(320, 200));
        this.setSize(new Dimension(320, 200));
        jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));

        
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jTextField1.setMinimumSize(new java.awt.Dimension(100, 100));
        jTextField1.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel2.add(jTextField1);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new GridBagLayout());

        jPanel3.add(getJSlider_Min(), gridBagConstraints2);
        jPanel3.add(jSlider_Max, gridBagConstraints3);
        jPanel3.add(jLabel_X, gridBagConstraints5);
        jPanel3.add(jLabel_Y, gridBagConstraints6);
        jPanel3.add(jLabel_Space, gridBagConstraints7);
        jPanel3.add(jLabel_CircleProportion, gridBagConstraints22);
        jPanel3.add(jLabel_Angle, gridBagConstraints41);
        jPanel3.add(getJTextField_MIN(), gridBagConstraints23);
        jPanel3.add(getJTextField_MAX(), gridBagConstraints32);
        jPanel1.add(pbt, null);
        jPanel1.add(pbt2, null);
        jSlider_Max.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
           	 jSlider1StateChanged(evt);
           	jLabel_Y.setText("Max = "+jSlider_Max.getValue());
           }
       });

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);
        
        ///jPanel1.add(pbt1);
        //jPanel1.add(pbt2);
    	
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
    	pbt.setMaximum(jSlider_Max.getValue());
    	pbt.setMinimum(jSlider_Min.getValue());
    	System.err.println("Ajustement du max et min du rotatif ["+jSlider_Min.getValue()+","+jSlider_Max.getValue()+"]");
    	jTextField1.setText(""+pbt.getValue());
    	
    }//GEN-LAST:event_jSlider1StateChanged
    
    /**
	 * This method initializes jSlider_Min	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJSlider_Min() {
		if (jSlider_Min == null) {
			jSlider_Min = new JSlider();
			jSlider_Min.setMinimum(-100);
			jSlider_Min.setValue(pbt.getMinimum());
			jLabel_X.setText("Min = "+pbt.getMinimum());
			jSlider_Min.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					jSlider1StateChanged(e);
					jLabel_X.setText("Min = "+jSlider_Min.getValue());
				}
			});
		}
		return jSlider_Min;
	}

	/**
	 * This method initializes jTextField_MIN	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_MIN()
	{
		if (jTextField_MIN == null)
		{
			jTextField_MIN = new JTextField();
			jTextField_MIN.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					jLabel_X.setText("Min = "+jTextField_MIN.getText());
					pbt.setMinimum(Integer.parseInt(jTextField_MIN.getText()));
				}
			});
		}
		return jTextField_MIN;
	}

	/**
	 * This method initializes jTextField_MAX	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_MAX()
	{
		if (jTextField_MAX == null)
		{
			jTextField_MAX = new JTextField();
			jTextField_MAX.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					jLabel_Y.setText("Max = "+jTextField_MAX.getText());
					pbt.setMaximum(Integer.parseInt(jTextField_MAX.getText()));
				}
			});
		}
		return jTextField_MAX;
	}

	

	/**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameTest_PLAT().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSlider jSlider_Max;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
	private JSlider jSlider_Min = null;
	private JLabel jLabel_X = null;
	private JLabel jLabel_Y = null;
	private JLabel jLabel_Space = null;
	private JLabel jLabel_CircleProportion = null;
	private JLabel jLabel_Angle = null;
	private JTextField jTextField_MIN = null;
	private JTextField jTextField_MAX = null;

   
    
}