package io.github.warnotte.waxlib3.waxlib2.Updater;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JProgressBar;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Toolkit;

public class DownloadProgressFrame extends JFrame
{

	private static final long	serialVersionUID	= 1L;
	private JPanel				jContentPane		= null;
	private JPanel jPanel = null;
	private JProgressBar jProgressBar = null;
	private JTextField jTextField_TOTAL = null;
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.gridy = 0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getJProgressBar(), gridBagConstraints);
			jPanel.add(getJTextField_TOTAL(), gridBagConstraints1);
		}
		return jPanel;
	}

	/**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBar()
	{
		if (jProgressBar == null)
		{
			jProgressBar = new JProgressBar();
			jProgressBar.setStringPainted(true);
			
		}
		return jProgressBar;
	}

	/**
	 * This method initializes jTextField_TOTAL	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_TOTAL()
	{
		if (jTextField_TOTAL == null)
		{
			jTextField_TOTAL = new JTextField();
			jTextField_TOTAL.setHorizontalAlignment(JTextField.CENTER);
			jTextField_TOTAL.setEnabled(false);
		}
		return jTextField_TOTAL;
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
				DownloadProgressFrame thisClass = new DownloadProgressFrame();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public DownloadProgressFrame()
	{
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(300, 70);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("network.png")));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("Download file");
		this.setResizable(false);
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
			jContentPane.add(getJPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	public void setProgress(int Percentvalue, int transfered, int totransfert, int transfertrate, int remainingsec )
	{
		jProgressBar.setValue(Percentvalue);
		transfered=transfered/1024/1024; // Mb.
		totransfert=totransfert/1024/1024; // Mb.
		jTextField_TOTAL.setText(transfered+"M on "+totransfert+"M at "+transfertrate+ "Kbyte/s "+(remainingsec/1000)+"seconds left");
		
		
	}

}
