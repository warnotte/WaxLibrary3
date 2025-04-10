package io.github.warnotte.waxlib3.waxlibswingcomponents.Dialog;


import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import io.github.warnotte.waxlib3.waxlibswingcomponents.FileChooser.FileFiltre;

/**
 * Classe qui va contenir des dialog divers telles que le SaveAs bien connu ou
 * le Open
 * 
 * @author Warnotte Renaud.
 */
public class DialogDivers
{

	public static void main(String args[]) throws Exception
	{
		DialogDivers.LoadDialog(null, new String[]{"xml", "mpp", "id3"}, "F:\\projets\\Datas\\Models");
		DialogDivers.SaveDialog(null, "*.xml");
		DialogDivers.Show_OkDialog("Salut", "Titre");
		
		

	}
	
	public static File ChooseDirectoryDialog(String directory)
	{
		return ChooseDirectoryDialog(null, directory);
	}
	
	public static File ChooseDirectoryDialog(JFrame frame, String directory)
	{
		File selFile = null;
		JFileChooser fc = new JFileChooser(directory);
		fc.setDialogTitle("Choose directory");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);

		if (frame==null) new JFrame();
		// Show open dialog; this method does not return until the dialog is
		// closed
		int retour = fc.showOpenDialog(frame);
		
		if (retour == JFileChooser.CANCEL_OPTION )
			return null;
		if (retour == JFileChooser.ERROR_OPTION )
			return null;
		
		selFile = fc.getSelectedFile();
		if (selFile != null)
			return selFile;
		return null;
	}
	
	public static File LoadDialog(JFrame frame, String extension[]) throws Exception
	{
		return LoadDialog(frame, extension, "./");
	}
	public static File LoadDialog(JFrame frame, String extension) throws Exception
	{
		return LoadDialog(frame, extension, "./");
	}

	public static File[] LoadMultipleDialog(JFrame frame, String extension) throws Exception
	{
		return LoadMultipleDialog(frame, extension, "./");
	}

	public static File LoadDialog(JFrame frame, String extension, String directory) throws Exception
	{
		return LoadDialog(frame, extension, directory, "Chargement d'un fichier *." + extension);
	}
	public static File LoadDialog(JFrame frame, String[] extension, String directory) throws Exception
	{
		
		String ext = "";
		for (int i = 0; i < extension.length; i++)
		{
			ext+="*."+extension[i];
			if (i<extension.length-1) ext+=", ";
		}
		
		return LoadDialog(frame, extension, directory, "Chargement d'un fichier " + ext);
	}
	
	public static File[] LoadMultipleDialog(JFrame frame, String extension, String directory) throws Exception
	{
		return LoadMultipleDialog(frame, new String [] {extension}, directory, "Chargement d'un fichier *." + extension);
	}

	public static File LoadDialog(JFrame frame, String extension, String directory, String title) throws Exception
	{
		return LoadDialog(frame, new String []{extension}, directory, title);
	}
	
	public static File LoadDialog(JFrame frame, String []extensions, String directory, String title) throws Exception
	{
		File selFile = null;
		JFileChooser fc = new JFileChooser(directory);
		fc.setDialogTitle(title);
		
		if (extensions!=null)
			if (extensions.length!=0)
			{
				String affichage = "";
				for (int i = 0; i < extensions.length; i++)
				{
					affichage += "*."+extensions[i];
					if (i+1<extensions.length)
					affichage += ", ";
					
				}
				FileFiltre mfi = new FileFiltre(extensions, affichage);
				//fc.addChoosableFileFilter(mfi);
				fc.setFileFilter(mfi);
			}


		// Show open dialog; this method does not return until the dialog is
		// closed
		int retour = fc.showOpenDialog(frame);
		
		if (retour == JFileChooser.CANCEL_OPTION )
			return null;
		if (retour == JFileChooser.ERROR_OPTION )
			return null;
		
		selFile = fc.getSelectedFile();
		if (selFile != null)
		{
			if (selFile.exists()==false)
				throw new Exception("File not Found");
			return selFile;
		}
		return null;

		
	}
	
	public static File[] LoadMultipleDialog(JFrame frame, String[] extensions, String directory, String title) throws Exception
	{
		File[] selFile = null;
		JFileChooser fc = new JFileChooser(directory);
		fc.setMultiSelectionEnabled(true);
		fc.setDialogTitle(title);
		
		if (extensions!=null)
			if (extensions.length!=0)
			{
				String affichage = "";
				for (int i = 0; i < extensions.length; i++)
				{
					affichage += "*."+extensions[i];
					if (i+1<extensions.length)
					affichage += ", ";
					
				}
				FileFiltre mfi = new FileFiltre(extensions, affichage);
				//fc.addChoosableFileFilter(mfi);
				fc.setFileFilter(mfi);
			}


		// Show open dialog; this method does not return until the dialog is
		// closed
		int retour = fc.showOpenDialog(frame);
		
		if (retour == JFileChooser.CANCEL_OPTION )
			return null;
		if (retour == JFileChooser.ERROR_OPTION )
			return null;
		
		selFile = fc.getSelectedFiles();
		if (selFile != null)
		{
			// TODO : Faire le test ou alors retourner null ?
			if (selFile.length==0)
				throw new Exception("Pas de fichier(s) choisi(s) / No file choosen");
			return selFile;
			
		}
		return null;

		
	}

	public static String SaveDialog(JFrame frame, String extension)
	{
		return SaveDialog(frame, extension, "./", "Sauvegarde sous");
	}

	public static String SaveDialog(JFrame frame, String extension, String directory)
	{
		return SaveDialog(frame, extension, directory, "Sauvegarde sous", null);
	}
	
	public static String SaveDialog(JFrame frame, String extension, String directory, String title)
	{
		return SaveDialog(frame, new String [] {extension}, directory, title, null);
	}
	public static String SaveDialog(JFrame frame, String extension, String directory, String title, String defaultFilename)
	{
		return SaveDialog(frame, new String [] {extension}, directory, title, defaultFilename);
	}
	
	/*
	 * 
	 * Offre une boite de dialogue sauvegarder sous avec demande d'ecrasement
	 * @param extension
	 * @param frame
	 * @return NULL ou un nom de fichier avec son path complet
	 */
	public static String SaveDialog(JFrame frame, String []extensions, String directory, String title, String defaultFilename)
	{
		File selFile = null;
		JFileChooser fc = new JFileChooser(directory);
		if (defaultFilename!=null)
			fc.setSelectedFile(new File(defaultFilename));
		
	//	FileFiltre mfi = new FileFiltre(new String[] { extension }, "*." + extension);
		//if (extension.endsWith("*")==false)
		//	fc.addChoosableFileFilter(mfi);
		fc.setDialogTitle(title);
		
		if (extensions!=null)
			if (extensions.length!=0)
			{
				for (int i = 0; i < extensions.length; i++)
				{
					FileFiltre mfi2 = new FileFiltre(new String[]{extensions[i]}, "*."+extensions[i]);
					fc.addChoosableFileFilter(mfi2);
					if (i==0)
						fc.setFileFilter(mfi2);
				}
			}


		

		// Show open dialog; this method does not return until the dialog is
		// closed
		int retour = fc.showSaveDialog(frame);
		
		if (retour == JFileChooser.CANCEL_OPTION )
			return null;
		if (retour == JFileChooser.ERROR_OPTION )
			return null;
		
		selFile = fc.getSelectedFile();

		if (selFile != null)
		{
			try
			{
				String str = selFile.getPath();
				

				String extension = ((FileFiltre)fc.getFileFilter()).getLesSuffixes()[0];
				
				if (str.toLowerCase().endsWith(extension) == false)
					str = str + "." + extension;
				
				// Verifier que ca existe pas encore
				File f = new File(str);
				// int check =1;

				if (f.exists() == true)
				{

					String message = "Voulez-vous écraser le fichier déja existant (" + str + ") avant ?";
					int answer = JOptionPane.showConfirmDialog(frame, message);
					if (answer == JOptionPane.YES_OPTION)
					{
						return f.getAbsoluteFile().toString();
					} else if (answer == JOptionPane.NO_OPTION)
					{
						return null;
					} else if (answer == JOptionPane.CANCEL_OPTION)
					{
						return null;
					}
				}
				return f.getAbsoluteFile().toString();

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void Show_dialog(Exception e, String dialog)
	{
		Show_dialog(e, dialog, new JFrame());
	}
	public static void Show_dialog(String dialog)
	{
		Show_dialog(null, dialog, new JFrame());
	}
	public static void Show_dialog(String dialog, String title)
	{
		Show_dialog(null, dialog, title, new JFrame());
	}

	public static void Show_dialog(Exception e, String dialog, JFrame frame)
	{
		Show_dialog(e, dialog, dialog, frame);
	}

	public static void Show_dialog(final Exception e, final String dialog, final String title, final JFrame frameM)
	{
	/*	SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{*/
				JFrame frame;
				if (frameM == null)
					frame = new JFrame();
				else
					frame = frameM;
				if (e == null)
					JOptionPane.showMessageDialog(frame, dialog, dialog, 1);
				else
					JOptionPane.showMessageDialog(frame, dialog + "\r\n\r\nException :" + e, dialog, 0); //$NON-NLS-1$
		/*	}
		});*/
	}
	

	/**
	 * @param frame
	 */
	public static void center(JFrame frame)
	{
		// Semble déconner.
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point center = ge.getCenterPoint();
		// Rectangle bounds = ge.getMaximumWindowBounds();
		int w = frame.getWidth();
		int h = frame.getHeight();
		int x = center.x - w / 2, y = center.y - h / 2;
		frame.setLocation(x, y);
		frame.validate();

	}

	/**
	 * Return true if yes.
	 * 
	 * @param string
	 * @return
	 */
	public static boolean Show_YesNoDialog_OLD(String string)
	{

		JFrame frame = new JFrame();
		int value = JOptionPane.showConfirmDialog(frame, string);
		if (value == JOptionPane.YES_OPTION)
			return true;
		return false;
	}
	
	/**
	 * Return JOptionPane.YES_OPTION, ...
	 * 
	 * @param JOptionPane constant.
	 * @return
	 */
	public static int Show_YesNoDialog(String string)
	{
		JFrame frame = new JFrame();
		int value = JOptionPane.showConfirmDialog(frame, string);
		return value;
	}

	public static void Show_OkDialog(final String message)
	{
		Show_OkDialog( message, "Success");
	}
	
	public static void Show_OkDialog(final String message, final String title)
	{
		/*SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{*/
		Object[] options = { "OK" };
		JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		/*	}
		});*/
	}

	public static void Show_ErrorDialog(final String string)
	{
		/*SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{*/

		
				Object[] options = { "Doh!" };
				JOptionPane.showOptionDialog(null, string, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
			/*}
		});*/
	}
	
	public static double Show_HowMuchDialog(JFrame frame, String string, double default_value)
	{
		CustomDialog dia = new CustomDialog(frame, string, default_value);
		dia.setSize(new Dimension(160,125));
		dia.setVisible(true);
		
		double text = dia.getValidateddouble();
		System.err.println("Read "+text);
		return text;
	}
	
	public static CustomDialog Show_HowMuchDialog(JFrame frame, String string, double default_value, boolean checkBoxValue, String checkBoxText)
	{
		CustomDialog dia = new CustomDialog(frame, string, default_value, true, checkBoxValue, checkBoxText);
		dia.setSize(new Dimension(160,125));
		dia.setVisible(true);
		return dia;
	}
	
}
