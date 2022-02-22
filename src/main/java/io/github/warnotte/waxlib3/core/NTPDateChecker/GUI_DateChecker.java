package io.github.warnotte.waxlib3.core.NTPDateChecker;
import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * 
 */

/**
 * @author Warnotte Renaud
 *
 */
public class GUI_DateChecker extends JFrame
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5252258160010431572L;
	private JPanel	contentPane;
	private JPanel panel_check;
	private JLabel lblCheckingLicenseValidity;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					GUI_DateChecker frame = new GUI_DateChecker();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI_DateChecker()
	{
		initialize();
	}
	private void initialize() {
		setTitle("License checker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 61);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		contentPane.add(getPanel_check(), "name_263441459660737");
	}

	private JPanel getPanel_check() {
		if (panel_check == null) {
			panel_check = new JPanel();
			panel_check.add(getLblCheckingLicenseValidity());
		}
		return panel_check;
	}
	private JLabel getLblCheckingLicenseValidity() {
		if (lblCheckingLicenseValidity == null) {
			lblCheckingLicenseValidity = new JLabel("Checking license validity...");
		}
		return lblCheckingLicenseValidity;
	}

	/**
	 * @param string
	 */
	public void setMessage(String string)
	{
		lblCheckingLicenseValidity.setText(string);
		lblCheckingLicenseValidity.repaint();
	}
}
