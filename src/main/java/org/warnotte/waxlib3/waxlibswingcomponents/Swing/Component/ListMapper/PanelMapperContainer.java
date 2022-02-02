package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.ListMapper;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;


public class PanelMapperContainer extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private MapPanel jList = null;
    //public DefaultListModel model_ordres =null;
    
    private JButton jButton_ADD = null;
    private JButton jButton_SUPPRIMe = null;
    BandManager manager = null;

    private JPanel jPanel_BOUTONS = null;

    private JButton jButton_Moyenne = null;

    private JButton jButton_InverseOrdre = null;
    
    /**
     * This is the default constructor Medium   
     * @param spriteLoader */
    public PanelMapperContainer(BandManager manager) {
	super();
	this.manager=manager;
	initialize();
	
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(300, 200);
	this.setLayout(new BorderLayout());
	this.add(getJList(), BorderLayout.CENTER);
	this.add(getJPanel_BOUTONS(), BorderLayout.SOUTH);
    }

    /**
     * This method initializes jList	
     * 	
     * @return javax.swing.JList	
     */
    protected MapPanel getJList() {
        if (jList == null) {
            jList = new MapPanel(manager);
            //TexturedMapRenderer tmp = new TexturedMapRenderer(spriteLoader);
            //jList.setMapRenderer(tmp);
        }
        return jList;
    }
    
    

    /**
     * This method initializes jButton_ADD	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton_ADD() {
        if (jButton_ADD == null) {
    	jButton_ADD = new JButton();
    	jButton_ADD.setText("Ajoute");
    	jButton_ADD.addActionListener(new java.awt.event.ActionListener() {
    	    public void actionPerformed(java.awt.event.ActionEvent e) {
    		ajoute_fichiers();
    	    }
    	});
        }
        return jButton_ADD;
    }

    protected void ajoute_fichiers() {
	String name = "Totteopk_"+new Random().nextInt(65536);
	Band element = new Band(name, 0);
	
	jList.getBandManager().ajoute_element(element);
	jList.repaint();
	
    }

    /**
     * This method initializes jButton_SUPPRIMe	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton_SUPPRIMe() {
        if (jButton_SUPPRIMe == null) {
    	jButton_SUPPRIMe = new JButton();
    	jButton_SUPPRIMe.setText("Supprime");
    	jButton_SUPPRIMe.addActionListener(new java.awt.event.ActionListener() {
    	    public void actionPerformed(java.awt.event.ActionEvent e) {
    		Band band = jList.getClickedBand();
    		if (band!=null)
    		{
    		    jList.getBandManager().retire_element(band);
    		    jList.repaint();
    		}
    	    }
    	});
        }
        return jButton_SUPPRIMe;
    }

    /**
     * This method initializes jPanel_BOUTONS	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel_BOUTONS() {
        if (jPanel_BOUTONS == null) {
    	GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
    	gridBagConstraints1.gridx = -1;
    	gridBagConstraints1.gridy = -1;
    	GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
    	gridBagConstraints2.gridx = -1;
    	gridBagConstraints2.gridy = -1;
    	jPanel_BOUTONS = new JPanel();
    	jPanel_BOUTONS.setLayout(new GridBagLayout());
    	jPanel_BOUTONS.add(getJButton_SUPPRIMe(), gridBagConstraints2);
    	jPanel_BOUTONS.add(getJButton_ADD(), gridBagConstraints1);
    	jPanel_BOUTONS.add(getJButton_Moyenne(), new GridBagConstraints());
    	jPanel_BOUTONS.add(getJButton_InverseOrdre(), new GridBagConstraints());
        }
        return jPanel_BOUTONS;
    }

    /**
     * This method initializes jButton_Moyenne	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton_Moyenne() {
        if (jButton_Moyenne == null) {
    	jButton_Moyenne = new JButton();
    	jButton_Moyenne.setText("Moyenne");
    	jButton_Moyenne.addActionListener(new java.awt.event.ActionListener() {
    	    public void actionPerformed(java.awt.event.ActionEvent e) {
    		manager.resizeAllwithAverage();
    		jList.repaint();
    	    }
    	});
        }
        return jButton_Moyenne;
    }

    /**
     * This method initializes jButton_InverseOrdre	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton_InverseOrdre() {
        if (jButton_InverseOrdre == null) {
    	jButton_InverseOrdre = new JButton();
    	jButton_InverseOrdre.setText("Inverse");
    	jButton_InverseOrdre.addActionListener(new java.awt.event.ActionListener() {
    	    public void actionPerformed(java.awt.event.ActionEvent e) {
    		manager.invertBands();
    		jList.repaint();
    	    }
    	});
        }
        return jButton_InverseOrdre;
    }

}
