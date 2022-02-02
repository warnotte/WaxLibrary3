package io.github.warnotte.waxlib3.waxlib2.PDF.ExtractionToTXT;


import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.faceless.pdf2.PDF;

public class ExportPDFtoTXT {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		List<File> fichiers = null;
			try
			{
				fichiers = getFileListing(new File("F:\\upload\\MarineStructure"));
			} catch (FileNotFoundException e1)
			{
				
				e1.printStackTrace();
			}
		if (fichiers==null) 
			throw new Exception("Repertoire source des pdfs invalide"); 
		if (fichiers.size()<=0) 
			throw new Exception("Repertoire source des pdfs ne contient rien"); 
		
		int i =0;
		Vector<String> emails = new Vector<String>();
		
		for(File file1 : fichiers) // pour chaque String current dans a
		{
			i++;
			File infile = file1;
			StringBuffer page0 = extractFirstPage_with_midas(infile.getAbsolutePath());
			Vector<String> emailsTMP = extractEmails(page0);
			System.err.println("PDF numero "+i+" "+infile.getAbsoluteFile());
			if (emails!=null)
			for (int k = 0; k < emailsTMP.size(); k++)
			{
				String email = emailsTMP.get(k);
				System.err.println("Email "+k+" :"+email);
				//System.err.println(email);
				if (emails.contains(email)==false)
				emails.add(email);
			}
		}
		
		System.err.println(emails.size()+" email(s)");
		for (int j = 0; j < emails.size(); j++)
		{
			System.err.println(""+emails.get(j));
		}
	}
	
	 private static Vector<String> extractEmails(StringBuffer page0)
	{
		 Vector<String> v = new Vector<String>();
		 String strRe=new String("[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,4}");
	//	 String strRe=new String("das");
				// the regular expression
				// 'tv' can be omitted because it is
				// covered under [a-z]{2}

		Pattern p = Pattern.compile(strRe,Pattern.CASE_INSENSITIVE |
		                            Pattern.UNICODE_CASE | Pattern.MULTILINE);
				// compile it
		
		 @SuppressWarnings("unused")
		boolean theEnd = false, printed = false;
			
					// get the results and print
		//	boolean comma=true;
			String content=page0.toString();
			Matcher m = p.matcher(content);
			while (!theEnd) {
				theEnd = !m.find();
				if (!theEnd) {
					
						String str = new String(content.substring(
				                 m.start(),m.end()));
						if (v.contains(str)==false)
						v.add(str);
						//System.err.println(content.substring( method.start(),method.end()));
					printed=true;
				}
			}
		return v;
	}

	@SuppressWarnings("deprecation")
	private static  StringBuffer extractFirstPage_with_midas(String infile) throws Exception {
			
	    	String txt_filename = infile.substring(0, infile.indexOf("."))+".txt";
	   // 	String path = infile.substring(0, infile.lastIndexOf("\\"));
	    	String commande = "pdftotext.exe \""+infile+"\" -f 0 -l 1 -layout";
	    	//System.err.println("Launch cmd ["+commande+"]");
	    	Process proc = Runtime.getRuntime().exec(commande);
	    	//Thread.sleep(1000);
	    	/*int r =*/ proc.waitFor();
	    	
	    	File f = new File(txt_filename);
	    	if (f.canRead()==false)
	    		throw new Exception("Cannot read "+txt_filename);
	    	FileInputStream fis = new FileInputStream(f);
	    	DataInputStream dis = new DataInputStream(fis);
	    	
	    	String page0 = "";
	    	String line = dis.readLine();
	    	while (line != null)
			{
	    		//System.err.println("line ["+line+"\\n]");
	    		page0+=line+"\n";
	    		line = dis.readLine();
			}
	    	dis.close();
	    	fis.close();
	    	
	  //  	f.delete();
	 		return new StringBuffer(page0);
		}
	 /*
	  * Files found; the List is sorted using File.compareTo.
	  *
	  * @param aStartingDir is a valid directory, which can be read.
	  */
	   public static List<File> getFileListing( File aStartingDir ) throws FileNotFoundException{
	    validateDirectory(aStartingDir);
	    List<File> result = new ArrayList<File>();

	    File[] filesAndDirs = aStartingDir.listFiles();
	    List<File> filesDirs = Arrays.asList(filesAndDirs);
	    Iterator<File> filesIter = filesDirs.iterator();
	    File file = null;
	    while ( filesIter.hasNext() ) {
	      file = filesIter.next();
	      // TODO : Regarder si dans le nom y'a pas part02 a part99
	      if (file.getAbsolutePath().endsWith(".pdf"))
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

	   /**
		  * Directory is valid if it exists, does not represent a file, and can be read.
		  */
		   private static void validateDirectory (File aDirectory) throws FileNotFoundException {
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
