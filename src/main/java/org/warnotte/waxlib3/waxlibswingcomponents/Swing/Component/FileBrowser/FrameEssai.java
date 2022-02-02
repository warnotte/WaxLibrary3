package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.FileBrowser;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FrameEssai extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private FileBrowserPanel jPanel = null;
    private JButton jButton = null;

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private FileBrowserPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new FileBrowserPanel();
        }
        return jPanel;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton() {
        if (jButton == null) {
    	jButton = new JButton();
    	jButton.addActionListener(new java.awt.event.ActionListener() {
    	    public void actionPerformed(java.awt.event.ActionEvent e) {
       		for (int i = 0; i < jPanel.getSelectedItems().size(); i++) {
		    System.err.println(i+ " : "+jPanel.getSelectedItems().get(i));
		}
    	    }
    	});
        }
        return jButton;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub

	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		FrameEssai thisClass = new FrameEssai();
		thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thisClass.setVisible(true);
	    }
	});
    }

    /**
     * This is the default constructor
     */
    public FrameEssai() {
	super();
	initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(300, 200);
	this.setContentPane(getJContentPane());
	this.setTitle("JFrame");
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
	if (jContentPane == null) {
	    jContentPane = new JPanel();
	    jContentPane.setLayout(new BorderLayout());
	    jContentPane.add(getJPanel(), BorderLayout.CENTER);
	    jContentPane.add(getJButton(), BorderLayout.NORTH);
	}
	return jContentPane;
    }

}
