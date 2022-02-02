package io.github.warnotte.waxlib3.waxlib2.Archives;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JProgressBar;

public class WaxAutoUnrarer {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		//FilenameFilter ff = new RarFilefilter();
		executeFullDecompress("C:\\Flm", false);
	}

	public static int executeFullDecompress(String path, boolean deletesource) throws IOException, InterruptedException
	{
		return executeFullDecompress(path, deletesource, null);
	}
	
	public static int executeFullDecompress(String path, boolean deletesource, JProgressBar jp) throws IOException, InterruptedException {
		File directory = new File(path);
		jp.setValue(0);
		jp.repaint();
		if ((directory.exists()) && (directory.isDirectory()==true))
		{
			List<File> files= getFileListing(directory);
			jp.setMaximum(files.size()-1);
			jp.setStringPainted(true);
			jp.repaint();
			for (int i =0;i<files.size();i++)
			{
				jp.setValue(i);
				jp.setString((i+1)+" of "+(files.size()));
				jp.repaint();
				File f =files.get(i);
				String cmpfilename = f.getAbsolutePath();
				int separ = cmpfilename.lastIndexOf('\\');
				String filename = cmpfilename.substring(separ+1);
				String dirname = cmpfilename.substring(0, separ);
				
				//System.err.println("File "+f);
				System.err.println("Decompress "+dirname+"\\"+filename);
				RarTools.unrar(dirname, filename, deletesource);
				//Thread.sleep(500);
				
			}
			return files.size();
		}
		else
		{
			System.err.println("Mauvais choix ...");
			return -1;
		}
	}

	/**
	  * Recursively walk a directory tree and return a List of all
	  * Files found; the List is sorted using File.compareTo.
	  *
	  * @param aStartingDir is a valid directory, which can be read.
	  */
	  static public List<File> getFileListing( File aStartingDir ) throws FileNotFoundException{
	    validateDirectory(aStartingDir);
	    List<File> result = new ArrayList<File>();

	    File[] filesAndDirs = aStartingDir.listFiles();
	    List<File> filesDirs = Arrays.asList(filesAndDirs);
	    Iterator<File> filesIter = filesDirs.iterator();
	    File file = null;
	    while ( filesIter.hasNext() ) {
	      file = filesIter.next();
	      // TODO : Regarder si dans le nom y'a pas part02 a part99
	      boolean is_part = isBadPart(file.getAbsolutePath());
	      if (file.getAbsolutePath().endsWith(".rar"))
	    	  if (is_part==false)
	    	      result.add(file); //always add, even if directory
	      if (!file.isFile()) {
	        //must be a directory
	        //recursive call!
	        List<File> deeperList = getFileListing(file);
	        result.addAll(deeperList);
	      }

	    }
	    Collections.sort(result);
	    return result;
	  }

	  static private boolean isBadPart(String filename)
	  {
		  boolean c = false;
		  for (int i=2;i<99;i++)
		  {
			  String num = ""+i;
			  if (num.length()==1) num="0"+num;
			  String part = "part"+num;
			  if (filename.contains(part))
			  	c= true;
		  }
		  return c;
	  }
	  
	  /**
		  * Directory is valid if it exists, does not represent a file, and can be read.
		  */
		  static private void validateDirectory (File aDirectory) throws FileNotFoundException {
		    if (aDirectory == null) {
		      throw new IllegalArgumentException("Directory should not be null.");
		    }
		    if (!aDirectory.exists()) {
		      throw new FileNotFoundException("Directory does not exist: " + aDirectory);
		    }
		    if (!aDirectory.isDirectory()) {
		      throw new IllegalArgumentException("Is not a directory: " + aDirectory);
		    }
		    if (!aDirectory.canRead()) {
		      throw new IllegalArgumentException("Directory cannot be read: " + aDirectory);
		    }
		  }
	
}
