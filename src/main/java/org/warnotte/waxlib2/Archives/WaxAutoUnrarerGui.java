package org.warnotte.waxlib2.Archives;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

public class WaxAutoUnrarerGui extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel jPanel_MAIN = null;

	private JTextField jTextField_CHEMIN = null;

	private JButton jButton_CHOOSE_CHEMIN = null;

	private JButton jButton = null;

	private JPanel jPanel_BOTTON = null;

	private JProgressBar jProgressBar = null;

	private JCheckBox jCheckBox_DELETE_SOURCE = null;
	
	Thread thread_decompression = null;  //  @jve:decl-index=0:

	/**
	 * This is the default constructor
	 */
	public WaxAutoUnrarerGui() {
		super();
		initialize();
		RarTools.logs=jTextArea;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(600, 300);
		this.setContentPane(getJContentPane());
		this.setTitle("Wax Auto Unrarer");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener( new WindowAdapter() {
			   @Override
			public void windowClosing( WindowEvent e ){
			         if (JOptionPane.showConfirmDialog
			             (null,"Are you sure ?")==JOptionPane.YES_OPTION) {
			        	 quit();
			           }
			        }
			     
			   } ); 
	
	}

	protected void quit() {
	//	ConsoleRedirector.stop();
		cancel();
		setVisible(false);
        dispose();
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridLayout gridLayout2 = new GridLayout();
			gridLayout2.setRows(1);
			jContentPane = new JPanel();
			jContentPane.setLayout(gridLayout2);
			jContentPane.add(getJSplitPane(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel_MAIN	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_MAIN() {
		if (jPanel_MAIN == null) {
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.setRows(3);
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 4;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 3;
			jPanel_MAIN = new JPanel();
			//jPanel_MAIN.setPreferredSize(new Dimension(150,200));
			jPanel_MAIN.setLayout(gridLayout1);
			jPanel_MAIN.add(getJPanel_M1(), null);
			jPanel_MAIN.add(getJPanel_M2(), null);
			jPanel_MAIN.add(getJPanel_M0(), null);
		}
		return jPanel_MAIN;
	}

	/**
	 * This method initializes jTextField_CHEMIN	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_CHEMIN() {
		if (jTextField_CHEMIN == null) {
			jTextField_CHEMIN = new JTextField();
			jTextField_CHEMIN.setEditable(false);
		}
		return jTextField_CHEMIN;
	}

	/**
	 * This method initializes jButton_CHOOSE_CHEMIN	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_CHOOSE_CHEMIN() {
		if (jButton_CHOOSE_CHEMIN == null) {
			jButton_CHOOSE_CHEMIN = new JButton();
			jButton_CHOOSE_CHEMIN.setText("...");
			jButton_CHOOSE_CHEMIN.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					choisis_chemin();
				}
			});
		}
		return jButton_CHOOSE_CHEMIN;
	}

	protected void choisis_chemin() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        int returnVal = fc.showOpenDialog(this);
	        
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            if (file.isDirectory()==true)
	            	if (file.exists()==true)
	            	{
	            		// Si c un bon chemin
	            		jButton.setEnabled(true);
	            		String Path = file.getAbsolutePath();
	            		jTextField_CHEMIN.setText(Path);
	            		System.err.println("Chemin correctement choisis");
	            		return;
	            	}
	            	
	            
	        } else {
	        	System.err.println("Chemin pas correctement choisis");
	        	jButton.setEnabled(false);
	            return;
	        }
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Go");
			jButton.setEnabled(false);
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					thread_decompression = new Thread() {
				        @Override
						public void run() {
							try {
								go();
							} catch (IOException e1) {
								show_dialog(e1, "Erreur durante le processus de decompression");
								e1.printStackTrace();
							} catch (InterruptedException e1) {
								show_dialog(e1, "Erreur durante le processus de decompression");
								e1.printStackTrace();
							}
						}};
						thread_decompression.start();
				}
			});
		}
		return jButton;
	}

	protected void go() throws IOException, InterruptedException {
		// Verifier le chemin remplis
		String path = jTextField_CHEMIN.getText();
		if ((path!=null) && (jTextField_RARPATH.getText()!=null))
		{
			jButton.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			RarTools.WinRarPath=jTextField_RARPATH.getText();
			int nbr_cmd = WaxAutoUnrarer.executeFullDecompress(path, jCheckBox_DELETE_SOURCE.isSelected(), jProgressBar);
			jButton.setEnabled(true);
			
			if (jCheckBox.isSelected()==true)
				Cleaner.clean(path);
			
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			show_dialog(null,"Vous avez execut�s "+nbr_cmd+" commandes");
		}
		else
		{
			System.err.println("Erreur le chemin est null");
			show_dialog(null,"Chemin est null");
		}
		
	}

	/**
	 * This method initializes jPanel_BOTTON	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_BOTTON() {
		if (jPanel_BOTTON == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(2);
			jPanel_BOTTON = new JPanel();
			jPanel_BOTTON.setLayout(gridLayout);
			jPanel_BOTTON.add(getJProgressBar(), null);
			jPanel_BOTTON.add(getJPanel_BoutonsCommandes(), null);
		}
		return jPanel_BOTTON;
	}

	/**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
		}
		return jProgressBar;
	}

	/**
	 * This method initializes jCheckBox_DELETE_SOURCE	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox_DELETE_SOURCE() {
		if (jCheckBox_DELETE_SOURCE == null) {
			jCheckBox_DELETE_SOURCE = new JCheckBox();
			jCheckBox_DELETE_SOURCE.setText("Delete *.r*");
			jCheckBox_DELETE_SOURCE.setToolTipText("Will delete all *.r* in the same directory as the .rar file");
		}
		return jCheckBox_DELETE_SOURCE;
	}
	
	public static void show_dialog(Exception e, String dialog)
	{
		if (e==null)
			JOptionPane.showMessageDialog(frame, dialog);
		else
			JOptionPane.showMessageDialog(frame, "Exception : "+e+"\n"+dialog);
	}
	
	static WaxAutoUnrarerGui frame =null;

	private JCheckBox jCheckBox = null;

	private JPanel jPanel_M2 = null;

	private JPanel jPanel_M1 = null;

	private JPanel jPanel_M0 = null;

	private JTextField jTextField_RARPATH = null;

	private JButton jButton_CHOISIS_CHEMIN_RAR = null;

	private JPanel jPanel_BoutonsCommandes = null;

	private JButton jButton_Cancel = null;

	private JScrollPane jScrollPane = null;

	private JTextArea jTextArea = null;

	private JSplitPane jSplitPane = null;

	private JPanel jPanel = null;
	
	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
			jCheckBox.setEnabled(true);
			jCheckBox.setToolTipText("Nettoye les habituelles porcheries venant des ftps");
			jCheckBox.setText("Delete *.sfv *.log *COMPLETE* ");
		}
		return jCheckBox;
	}

	/**
	 * This method initializes jPanel_M2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_M2() {
		if (jPanel_M2 == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.gridwidth = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.gridwidth = 2;
			jPanel_M2 = new JPanel();
			jPanel_M2.setLayout(new BoxLayout(getJPanel_M2(), BoxLayout.Y_AXIS));
			jPanel_M2.setBorder(BorderFactory.createTitledBorder(null, "Nettoyage", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanel_M2.add(getJCheckBox_DELETE_SOURCE(), null);
			jPanel_M2.add(getJCheckBox(), null);
		}
		return jPanel_M2;
	}

	/**
	 * This method initializes jPanel_M1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_M1() {
		if (jPanel_M1 == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = -1;
			gridBagConstraints.weightx = 0.0;
			gridBagConstraints.gridx = -1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = -1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.gridx = -1;
			jPanel_M1 = new JPanel();
			jPanel_M1.setLayout(new GridBagLayout());
			jPanel_M1.setBorder(BorderFactory.createTitledBorder(null, "Path des archives", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanel_M1.add(getJTextField_CHEMIN(), gridBagConstraints1);
			jPanel_M1.add(getJButton_CHOOSE_CHEMIN(), gridBagConstraints);
		}
		return jPanel_M1;
	}

	/**
	 * This method initializes jPanel_M0	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_M0() {
		if (jPanel_M0 == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.gridx = 0;
			jPanel_M0 = new JPanel();
			jPanel_M0.setLayout(new GridBagLayout());
			jPanel_M0.setBorder(BorderFactory.createTitledBorder(null, "Configuration path de Rar.exe", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanel_M0.add(getJTextField_RARPATH(), gridBagConstraints6);
			jPanel_M0.add(getJButton_CHOISIS_CHEMIN_RAR(), gridBagConstraints7);
		}
		return jPanel_M0;
	}

	/**
	 * This method initializes jTextField_RARPATH	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_RARPATH() {
		if (jTextField_RARPATH == null) {
			jTextField_RARPATH = new JTextField();
			jTextField_RARPATH.setEnabled(false);
			//jTextField_RARPATH.setText("C:\\Program Files\\WinRAR\\rar.exe");
			jTextField_RARPATH.setText("rar.exe");
		}
		return jTextField_RARPATH;
	}

	/**
	 * This method initializes jButton_CHOISIS_CHEMIN_RAR	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_CHOISIS_CHEMIN_RAR() {
		if (jButton_CHOISIS_CHEMIN_RAR == null) {
			jButton_CHOISIS_CHEMIN_RAR = new JButton();
			jButton_CHOISIS_CHEMIN_RAR.setText("...");
			jButton_CHOISIS_CHEMIN_RAR.setToolTipText("Choisis l'endroit ou se trouve rar.exe (dans winrar souvent)");
			jButton_CHOISIS_CHEMIN_RAR
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							choisis_chemin_winrar();
						}
					});
		}
		return jButton_CHOISIS_CHEMIN_RAR;
	}

	protected void choisis_chemin_winrar() {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        int returnVal = fc.showOpenDialog(this);
		        
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            if (file.isDirectory()==false)
		            	if (file.exists()==true)
		            	if (file.getAbsolutePath().endsWith(".exe")==true)
		            	{
		            		// Si c un bon chemin
		            		String Path = file.getAbsolutePath();
		            		jTextField_RARPATH.setText(Path);
		            		System.err.println("Chemin de winrar correctement choisis");
		            		return;
		            	}
		            	
		            
		        } else {
		        	System.err.println("Chemin pas correctement choisis");
		            return;
		        }
		
	}

	/**
	 * This method initializes jPanel_BoutonsCommandes	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_BoutonsCommandes() {
		if (jPanel_BoutonsCommandes == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			jPanel_BoutonsCommandes = new JPanel();
			jPanel_BoutonsCommandes.setLayout(new GridBagLayout());
			jPanel_BoutonsCommandes.add(getJButton(), new GridBagConstraints());
			jPanel_BoutonsCommandes.add(getJButton_Cancel(), gridBagConstraints8);
		}
		return jPanel_BoutonsCommandes;
	}

	/**
	 * This method initializes jButton_Cancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_Cancel() {
		if (jButton_Cancel == null) {
			jButton_Cancel = new JButton();
			jButton_Cancel.setText("Cancel");
			jButton_Cancel.setToolTipText("Evitez ce bouton ...");
			jButton_Cancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					cancel();
				}
			});
		}
		return jButton_Cancel;
	}

	protected void cancel() {
		// TODO : Faut absolument verifier que le processus dos soit bien vir�s
		if (thread_decompression!=null)
			if (thread_decompression.isAlive()==true)
			{
				thread_decompression.interrupt();
				//thread_decompression.stop();
				thread_decompression=null;
				RarTools.proc.destroy();
				jProgressBar.setString("Canceled");
				jProgressBar.setValue(jProgressBar.getMaximum());
				setCursor(null);
				jButton.setEnabled(true);
			}
		
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(450,200));
			jScrollPane.setViewportView(getJTextArea());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setAutoscrolls(true);
		}
		return jTextArea;
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerLocation(200);
			jSplitPane.setRightComponent(getJScrollPane());
			jSplitPane.setLeftComponent(getJPanel());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getJPanel_BOTTON(), BorderLayout.SOUTH);
			jPanel.add(getJPanel_MAIN(), BorderLayout.CENTER);
		}
		return jPanel;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		SwingUtilities.invokeLater(new Runnable() {public void run() {
		/*	try {
				ConsoleRedirector.start("waxUnrar.logs");
			} catch (IOException e) {
				
				e.printStackTrace();
				System.exit(-1);
			}*/
			frame = new WaxAutoUnrarerGui();
		frame.setVisible(true);
		
		}});
	}

}
