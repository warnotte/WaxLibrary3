package org.warnotte.waxlib3.waxlib2.Archives;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.JTextArea;

/**
 * 
 * @author Warnotte Renaud 2007
 *
 */
public class RarTools {

	static String  WinRarPath = "f:\\Program Files\\WinRAR\\rar.exe";
	static Process proc=null;
	public static JTextArea logs = null;

/**
  * 
  * @param path
  * @param filename avec .RAR
  * @param delete_archive_file
  * @return
  * @throws IOException
  * @throws InterruptedException
  */
	public static int unrar(String path, String filename, boolean delete_archive_file) throws IOException, InterruptedException
	{
		// Extraction process
		String CMD = "\""+WinRarPath+"\" x -o- \""+path+"\\"+filename+"\" \""+path+"\"";
		execute_commande(CMD);
		
		// Deletion process
		if (delete_archive_file==true)
		{
			
			filename = filename.substring(0, filename.lastIndexOf("."));
			
			CMD = "del \""+path+"\\"+filename+".r*\"";
			System.err.println("Will exec ["+CMD+"]");
			execute_commande(CMD);
		}
		
		
		return 0;
	}

	private static void execute_commande(String CMD) throws IOException, InterruptedException {
		File f = new File("c:\\TEMP.bat");
		FileOutputStream fos = new FileOutputStream(f);
		PrintStream ps = new PrintStream(fos);
		ps.println(CMD);
		ps.flush();
		ps.close();
		fos.flush();
		fos.close();
		
		System.err.println("Would like to execute ["+CMD+"]");
		proc = Runtime.getRuntime().exec("c:\\TEMP.bat");
		
		Runnable r = new Runnable(){
		     public void run(){
		        try {
					myinputmethod(proc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }
		   };
		  
		   r.run();
		  	
		proc.waitFor();
		
		f.delete();
		r=null;
		
	}
	
	
	 
	private static void myinputmethod(Process p) throws IOException{
		final BufferedInputStream in = new BufferedInputStream (p.getInputStream());
		final BufferedReader br = new BufferedReader ( new InputStreamReader (in ));
		//byte []buffer=null;
		String s =null;
		
		 while ( ( s = br.readLine () ) != null ) {
			 if (logs!=null)
			 {
				logs.insert(s+"\r\n", logs.getText().length());
			 	logs.setCaretPosition(logs.getText().length()-1);
			 }
			 
             System.err.println(s);
		 };
		 System.err.println("myinputMethod finie");
	}
}
