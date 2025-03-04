package io.github.warnotte.waxlib3.core.RuntimeExceptionHandler;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import io.github.warnotte.waxlib3.waxlibswingcomponents.Dialog.DialogDivers;

public class RunTimeExceptionDialog extends JFrame
{
	
	//Thread thread = null;

	private static final long	serialVersionUID	= 1L;
	private JPanel				jContentPane		= null;
	private JPanel jPanel_Informations = null;
	private JPanel jPanel_Boutons = null;
	private JTextField jTextField_CAUSE = null;
	private JTextField jTextField_MESSAGE = null;
	private JTextArea jTextArea_StackTrace = null;
	private JButton jButton_OK = null;
	private JButton jButton_QUIT = null;
	//AudioStream as = null;
	
	String CAUSE;
	String STACK;
	String MESSAGE;
	private JScrollPane jScrollPane = null;
	private JLabel jLabel_Cause = null;
	private JLabel jLabel_Message = null;
	private JLabel jLabel_Trace = null;
	private JPanel jPanel_GRFX = null;

	/**
	 * This method initializes jPanel_Informations	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_Informations()
	{
		if (jPanel_Informations == null)
		{
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 2;
			gridBagConstraints12.fill = GridBagConstraints.BOTH;
			gridBagConstraints12.gridheight = 4;
			gridBagConstraints12.weightx = 0.1;
			gridBagConstraints12.weighty = 1.0;
			gridBagConstraints12.gridy = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridy = 3;
			jLabel_Trace = new JLabel();
			jLabel_Trace.setText("Trace");
			jLabel_Trace.setHorizontalAlignment(SwingConstants.TRAILING);
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.gridx = 0;
			gridBagConstraints41.fill = GridBagConstraints.BOTH;
			gridBagConstraints41.gridy = 2;
			jLabel_Message = new JLabel();
			jLabel_Message.setText("Message");
			jLabel_Message.setHorizontalAlignment(SwingConstants.TRAILING);
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.fill = GridBagConstraints.BOTH;
			gridBagConstraints31.gridy = 0;
			jLabel_Cause = new JLabel();
			jLabel_Cause.setText("Cause");
			jLabel_Cause.setHorizontalAlignment(SwingConstants.TRAILING);
			//jLabel_Image = new JLabel();
			//jLabel_Image.setText("");
			//jLabel_Image.setIcon(new ImageIcon(getClass().getResource("RunTimeExceptionDialogImage.jpg")));
			//jLabel_Image.setPreferredSize(new Dimension(110, 154));
			//jLabel_Image.addMouseListener(new java.awt.event.MouseAdapter()
			//{
			//	public void mousePressed(java.awt.event.MouseEvent e)
			//	{
			//playSound();
			//	}

				
			//});
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 3;
			gridBagConstraints11.gridwidth = 1;
			gridBagConstraints11.weightx = 1.0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.weightx = 1.0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridx = 1;
			gridBagConstraints.weightx = 1.0;
			jPanel_Informations = new JPanel();
			jPanel_Informations.setLayout(new GridBagLayout());
			jPanel_Informations.add(getJTextField_CAUSE(), gridBagConstraints);
			jPanel_Informations.add(getJTextField_MESSAGE(), gridBagConstraints1);
			jPanel_Informations.add(getJScrollPane(), gridBagConstraints11);
			jPanel_Informations.add(jLabel_Cause, gridBagConstraints31);
			jPanel_Informations.add(jLabel_Message, gridBagConstraints41);
			jPanel_Informations.add(jLabel_Trace, gridBagConstraints5);
			jPanel_Informations.add(getJPanel_GRFX(), gridBagConstraints12);
		}
		return jPanel_Informations;
	}

	/**
	 * This method initializes jPanel_Boutons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_Boutons()
	{
		if (jPanel_Boutons == null)
		{
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridy = 0;
			jPanel_Boutons = new JPanel();
			jPanel_Boutons.setLayout(new GridBagLayout());
			
			jPanel_Boutons.add(getJButton_QUIT(), gridBagConstraints4);
			jPanel_Boutons.add(getJButton_OK(), gridBagConstraints3);
		}
		return jPanel_Boutons;
	}

	/**
	 * This method initializes jTextField_CAUSE	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_CAUSE()
	{
		if (jTextField_CAUSE == null)
		{
			jTextField_CAUSE = new JTextField();
			jTextField_CAUSE.setText(CAUSE);
		}
		return jTextField_CAUSE;
	}

	/**
	 * This method initializes jTextField_MESSAGE	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_MESSAGE()
	{
		if (jTextField_MESSAGE == null)
		{
			jTextField_MESSAGE = new JTextField();
			jTextField_MESSAGE.setText(MESSAGE);
		}
		return jTextField_MESSAGE;
	}

	/**
	 * This method initializes jTextArea_StackTrace	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea_StackTrace()
	{
		if (jTextArea_StackTrace == null)
		{
			jTextArea_StackTrace = new JTextArea();
			jTextArea_StackTrace.setText(STACK);
		}
		return jTextArea_StackTrace;
	}

	/**
	 * This method initializes jButton_OK	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_OK()
	{
		if (jButton_OK == null)
		{
			jButton_OK = new JButton();
			jButton_OK.setText("Continue");
			jButton_OK.setToolTipText("Tente de continuer l'execution du programme");
			jButton_OK.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					dispose();
					setVisible(false);
					dispose();
				}
			});
		}
		return jButton_OK;
	}

	/**
	 * This method initializes jButton_QUIT	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_QUIT()
	{
		if (jButton_QUIT == null)
		{
			jButton_QUIT = new JButton();
			jButton_QUIT.setText("Quit");
			jButton_QUIT.setToolTipText("Ceci quitte le programme de manière brusque");
			jButton_QUIT.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					setAlwaysOnTop(false);
					int ans = DialogDivers.Show_YesNoDialog("Attention vous allez arreter tout le programme");
					if (ans == JOptionPane.YES_OPTION)
					{
						//LogManager.shutdown();
					    System.exit(-1);
					}
					setAlwaysOnTop(true);
				}
			});
		}
		return jButton_QUIT;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane()
	{
		if (jScrollPane == null)
		{
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTextArea_StackTrace());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jPanel_GRFX	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_GRFX()
	{
		if (jPanel_GRFX == null)
		{
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = -1;
			gridBagConstraints2.gridheight = 4;
			jPanel_GRFX = new GrfxPanel();
			//jPanel_GRFX.setLayout(new GridBagLayout());
			jPanel_GRFX.addMouseListener(new java.awt.event.MouseAdapter()
			{
				@Override
				public void mousePressed(java.awt.event.MouseEvent e)
				{
					playSound();
				}
			});
			//jPanel_GRFX.add(jLabel_Image, gridBagConstraints2);
		}
		return jPanel_GRFX;
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
			/*	try
				{
					@SuppressWarnings("unused")
					Logs l = new Logs("Test RuntimeFrame");
				} catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				Exception e = new Exception("Petit singe");
				
				RunTimeExceptionDialog thisClass = new RunTimeExceptionDialog(e);
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
				
				e = new Exception("Deuxieme message");
				thisClass.appendException(e);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public RunTimeExceptionDialog(Throwable ex)
	{
		super();
		appendException(ex);
		initialize();
		/*thread = new Thread(){
			@Override
			public void run()
			{
				try
				{
				while(true)
				{
				
					
					jPanel_GRFX.repaint();
					Thread.sleep(40);
					
				
				}
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		};
		thread.start();
			*/
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(960, 400);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("RunTime Exception Catcher");
		this.setAlwaysOnTop(true);
		
		//playSound();
	}
	
	/**
	 * Joue le Homer sounds.
	 */
	private void playSound()
	{/*
		//InputStream in;
		try
		{
		//	if (new File("Doh.wav").exists()==true)
			{
			as = new AudioInputStream(this.getClass().getResourceAsStream("Doh.wav"));
			AudioPlayer.player.start(as);
			}
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	AudioPlayer.player.stop(as);*/

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
			jContentPane.add(getJPanel_Informations(), BorderLayout.CENTER);
			jContentPane.add(getJPanel_Boutons(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	
	public void appendException(Throwable ex)
	{
		StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    ex.printStackTrace(pw);
	    
		this.CAUSE=ex.toString();
		this.STACK=sw.toString();
		this.MESSAGE=ex.getMessage();
	
		getJTextArea_StackTrace().append(STACK);
		getJTextField_CAUSE().setText(CAUSE);
		getJTextField_MESSAGE().setText(MESSAGE);
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="17,9"
