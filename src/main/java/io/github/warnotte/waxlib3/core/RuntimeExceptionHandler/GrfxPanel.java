
package io.github.warnotte.waxlib3.core.RuntimeExceptionHandler;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GrfxPanel extends JPanel {

	private static final long	serialVersionUID	= 1L;
	private JLabel				jLabel				= null;
	ImageIcon					ic					= null;

	/**
	 * This is the default constructor
	 */
	public GrfxPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		// font = new Font("Impact", 0, 32);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText("");
		// jLabel_Image.setText("");
		ic = new ImageIcon(getClass().getResource("/images/RuntimeExceptionHandler/RunTimeExceptionDialogImage.jpg"));
		jLabel.setIcon(ic);
		// this.setSize(200, 300);
		this.setLayout(new GridBagLayout());
		// this.setPreferredSize(new Dimension(120, 200));
		this.add(jLabel, gridBagConstraints);
	}

}
