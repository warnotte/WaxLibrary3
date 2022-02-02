/**
 * 
 */
package org.warnotte.waxlib3.waxlibswingcomponents.FileChooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.warnotte.waxlib3.waxlibswingcomponents.Dialog.DialogDivers;

/**
 * @author Warnotte Renaud.
 */
public class FileChooser extends JPanel
{

	public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				ToolTipManager.sharedInstance().setDismissDelay(1000);
				ToolTipManager.sharedInstance().setReshowDelay(1500);

				JFrame frame = new JFrame();
				frame.setTitle("FileChooser - Demo");
				frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
				FileChooser comp = new FileChooser("xml");
				//	comp.setBackground(Color.BLUE);
				frame.getContentPane().add(comp);
				FileChooser comp2 = new FileChooser(new File(""), true);
				//	comp2.setBackground(Color.GREEN);
				frame.getContentPane().add(comp2);
				FileChooser comp3 = new FileChooser(new File(""), true);
				comp3.setBackground(Color.GREEN);
				frame.getContentPane().add(comp3);
				frame.setSize(320, 240);
				frame.setVisible(true);

			}
		});
	}

	static String						LABEL_BUTTON_OPEN		= "Open";
	static String						LABEL_NOFILE_SELECTED	= "No file selected - Drag & Drop one or use Open";
	static String						LABEL_IACCEPTFILEONLY	= "I accept file only";
	static String						IACCEPTDIRECTORYONLY	= "I accept directory only";
	static String						LABEL_NO_FILE_SELECTED	= "No file selected";
	static String						LABEL_CHOOSE_A_FILE		= "Choose a file";
	
	static
	{
		if (Locale.getDefault().getLanguage().toLowerCase().equals("fr"))
		{
			LABEL_BUTTON_OPEN 		= "Ouvrir";
			LABEL_NOFILE_SELECTED 	= "Pas de fichier selectionn� - Cliquer glisser un fichier ou utiliser " + LABEL_BUTTON_OPEN;
			LABEL_IACCEPTFILEONLY 	= "Je n'accepte que les fichiers";
			IACCEPTDIRECTORYONLY 	= "Je n'accepte que les répertoires";
			LABEL_NO_FILE_SELECTED 	= "Pas de fichier selectionné";
			LABEL_CHOOSE_A_FILE 	= "Choisissez un fichier";
		}
	}

	private final List<ActionListener>	listenersA				= new ArrayList<ActionListener>();					//  @jve:decl-index=0:

	final JLabel						lblDisplay;

	/**
	 * 
	 */
	private static final long			serialVersionUID		= 5843308298964284553L;
	File								file					= null;
	String[]							extensionPattern		= new String[] { "*" };
	boolean								directoryOnly			= false;

	public FileChooser()
	{
		this(null, "*");
	}

	public FileChooser(String extensionPattern)
	{
		this(null, extensionPattern);
	}

	public FileChooser(String[] extensionPattern)
	{
		this(null, extensionPattern);
	}

	public FileChooser(File file, boolean directoryOnly)
	{
		this(file, new String[] { "*" }, directoryOnly);
	}

	public FileChooser(File file, String extensionPattern)
	{
		this(file, new String[] { extensionPattern }, false);
	}

	public FileChooser(File file, String[] extensionPattern)
	{
		this(file, extensionPattern, false);
	}

	public FileChooser(File file, String extensionPattern, boolean directoryOnly)
	{
		this(file, new String[] { extensionPattern }, directoryOnly);
	}

	public FileChooser(File file, String[] extensionPattern, boolean directoryOnly)
	{
		super();

		this.file = file;
		this.directoryOnly = directoryOnly;
		this.extensionPattern = extensionPattern;
		setLayout(new BorderLayout(0, 0));

		lblDisplay = new JLabel(LABEL_NOFILE_SELECTED);
		lblDisplay.setName("Label_file_loading");
		add(lblDisplay, BorderLayout.WEST);

		if (file != null)
			lblDisplay.setText(file.getName());

		lblDisplay.setTransferHandler(new MyTransferHandler());
		this.setTransferHandler(new MyTransferHandler());

		JButton btnOpen = new JButton(LABEL_BUTTON_OPEN);
		btnOpen.setName("Button_open_file");
		btnOpen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String directory = ".";
				if (FileChooser.this.file != null)
				{
					if (FileChooser.this.directoryOnly == true)
						directory = FileChooser.this.file.getAbsolutePath();
					else
						directory = FileChooser.this.file.getParent();

				}
				
				File filename = FCDialog(new JFrame(), FileChooser.this.extensionPattern, directory, LABEL_CHOOSE_A_FILE);
				if (filename != null)
				{
					setFile(filename);
					fireActionEvent(new ActionEvent(FileChooser.this, 0, "FileChanged"));
				}
			}
		});
		add(btnOpen, BorderLayout.EAST);

	}

	public File FCDialog(JFrame frame, String[] extensions, String directory, String title)
	{
		File selFile = null;
		JFileChooser fc = new JFileChooser(directory);
		if (directoryOnly == false)
		{
			if (extensions != null)
				if (extensions.length != 0)
				{
					String affichage = "";
					for (int i = 0; i < extensions.length; i++)
					{
						affichage += "*." + extensions[i];
						if (i + 1 < extensions.length)
							affichage += ", ";
					}
					FileFiltre mfi = new FileFiltre(extensions, affichage);
					//fc.addChoosableFileFilter(mfi);
					fc.setFileFilter(mfi);
				}

			/*
			 * String desc = extension[0]; if (extension.length>1) { for (int i
			 * = 1; i < extension.length; i++) { desc += desc +
			 * "; "+extension[i]; } } FileFiltre mfi = new FileFiltre(extension,
			 * "*." + desc); fc.addChoosableFileFilter(mfi);
			 */
		}

		if (directoryOnly == true)
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		fc.setDialogTitle(title);

		// Show open dialog; this method does not return until the dialog is
		// closed
		if (directoryOnly == false)
		{
			fc.showSaveDialog(frame);
		} else
		{
			fc.showOpenDialog(frame);
		}

		selFile = fc.getSelectedFile();

		if (selFile != null)
		{
			try
			{
				return new File(selFile.getPath());
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * * DOCUMENT ME!
	 * 
	 * @param listener
	 *            DOCUMENT ME!
	 */
	public void addActionListener(ActionListener listener)
	{
		listenersA.add(listener);
	}

	/**
	 * DOCUMENT ME!
	 */
	public void removeAllActionListener()
	{
		listenersA.removeAll(listenersA); // TODO : pas sure
	}

	/*
	 * DOCUMENT ME!
	 * @param event DOCUMENT ME!
	 */
	public void fireActionEvent(ActionEvent event)
	{
		int total = listenersA.size();
		for (int i = 0; i < total; i++)
		{
			ActionListener l = listenersA.get(i);
			l.actionPerformed(event);
		}
	}

	public synchronized File getFile()
	{
		return file;
	}

	public synchronized void setFile(File file)
	{
		FileChooser.this.file = file;
		if (file != null)
		{
			String name = file.getName();
			if (directoryOnly == true)
				name = file.getAbsolutePath();
			lblDisplay.setText(name);
			lblDisplay.setToolTipText(FileChooser.this.file.getAbsoluteFile().toString());
		} else
		{
			lblDisplay.setText(LABEL_NO_FILE_SELECTED);
			lblDisplay.setToolTipText(LABEL_NO_FILE_SELECTED);
		}
	}

	public void setText(String text)
	{
		lblDisplay.setText(text);
	}

	class MyTransferHandler extends TransferHandler
	{
		/**
		 * 
		 */
		private static final long	serialVersionUID	= -2975649037959349396L;

		@Override
		public boolean canImport(TransferHandler.TransferSupport support)
		{
			if (!support.isDrop())
			{
				return false;
			}

			if (support.isDataFlavorSupported(DataFlavor.imageFlavor))
			{
				System.err.println("Image not accepted!");
			}

			// we only import File
			if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			{
				/*
				 * List files; Transferable transferable =
				 * support.getTransferable(); // Transferable transferable =
				 * support.getTransferable(); if (transferable==null) return
				 * false; try { files = (List)
				 * transferable.getTransferData(DataFlavor.javaFileListFlavor);
				 * if (files.size()!=0) { File f = (File) files.get(0); } }
				 * catch (UnsupportedFlavorException e) { // TODO Auto-generated
				 * catch block e.printStackTrace(); } catch (IOException e) { //
				 * TODO Auto-generated catch block e.printStackTrace(); }
				 */

				return true;

			}
			return false;
		}

		@Override
		public boolean importData(TransferHandler.TransferSupport support)
		{
			if (canImport(support) == false)
				return false;
			try
			{
				Transferable tr = support.getTransferable();
				List<?> files = (List<?>) (tr.getTransferData(DataFlavor.javaFileListFlavor));
				if (files.size() != 0)
				{
					File f = (File) files.get(0);
					if ((directoryOnly == true) && (f.isDirectory() == false))
					{
						DialogDivers.Show_dialog(IACCEPTDIRECTORYONLY);
						return false;
					}
					if ((directoryOnly == false) && (f.isDirectory() == true))
					{
						DialogDivers.Show_dialog(LABEL_IACCEPTFILEONLY);
						return false;
					}

					setFile(f);
					fireActionEvent(new ActionEvent(FileChooser.this, 0, "FileChanged"));
				}
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return false;

		}
	}

}
