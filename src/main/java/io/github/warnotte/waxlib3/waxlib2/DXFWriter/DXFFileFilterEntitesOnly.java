/**
 * 
 */
package io.github.warnotte.waxlib3.waxlib2.DXFWriter;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * @author Warnotte Renaud
 * Cette petite routine permet de vider des fichiers DXF des repertoire pour n'avoir que la section entity (l'interieur)
 */
public class DXFFileFilterEntitesOnly
{

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		// Nettoye les fichier avec les blocks. (px contenir tout un fichier "qui sort d'autocad")
		// processdirectory("Datas//Models//DXFPlans//DXFBlocksDetails");
		// Nettoye les fichier de plans (ne doit plus contenir que l'interieur des entities)
		//processdirectoryFondPlan("Datas//Models//Novarka//DXFPlans//");
		processdirectoryFondPlan("Datas//Models//Premontage//DXFPlans//");
		
	}

	/**
	 * @param directory
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	private static void processdirectory(String directory) throws Exception
	{
		FilenameFilter filter = new FilenameFilter()
		{
			//@Override
			public boolean accept(File dir, String name)
			{
				if (name.toLowerCase().endsWith("dxf"))
						return true;
				return false;
			}
		};
		File dir = new File(directory);
		if (dir.isDirectory()==false) throw new Exception("This is not a directory");
		String files [] = dir.list(filter);
		for (int i = 0; i < files.length; i++)
		{
			System.err.println("Processing "+files[i]);
			
			File f = new File(directory+"//"+files[i]);
			boolean ret = processFile(f, directory);
			if (ret == false)
				throw new Exception("Failed for file "+f);
		}
	}
	
	private static void processdirectoryFondPlan(String directory) throws Exception
	{
		FilenameFilter filter = new FilenameFilter()
		{
			//@Override
			public boolean accept(File dir, String name)
			{
				if (name.toLowerCase().endsWith("dxf"))
						return true;
				return false;
			}
		};
		File dir = new File(directory);
		if (dir.isDirectory()==false) throw new Exception("This is not a directory");
		String files [] = dir.list(filter);
		for (int i = 0; i < files.length; i++)
		{
			System.err.println("Processing "+files[i]);
			
			File f = new File(directory+"//"+files[i]);
			boolean ret = processFileFondPlan(f, directory);
			if (ret == false)
				throw new Exception("Failed for file "+f);
		}
	}

	static boolean first=false;
	/**
	 * @param f
	 * @param directory 
	 * @throws IOException 
	 */
	private static boolean processFile(File f, String directory) throws IOException
	{
		 BufferedReader dis = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		String line = "";
		
		// Cree un temporaire
		File fnew = new File("Datas\\Models\\Novarka\\DXFPlans\\DXFBlocksDetails\\temp.tmp");
		PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(fnew)));
		
		line = dis.readLine();
		boolean start = false;
	//	boolean end = false;
		first=false;
		// Le remplis
		while (line!=null)
		{
			if (start==true)
				if (line.trim().toUpperCase().contains("ENDSEC"))
					break;
			
			if (line.trim().toUpperCase().contains("ENTITIES"))
			{
				line = dis.readLine(); // Read the first 0
				line = dis.readLine();
				start=true;
			}
			if (start==true)
			{
				String str = getGrublizeg(line, dis);
				if (str.length()!=0)
					if (accepted(line))
						ps.println(str);
					else
						System.err.println("Command not accepted : "+line);
			}
			line = dis.readLine();
		}
		
		dis.close();
		ps.close();
		
		// Supprime l'originale
		if (f.delete()==true)
		{
			// Rename le temporaire
			if (fnew.renameTo(f)==true)
				return true;
			return false;
		}
		return false;
		
	}
	
	
	/**
	 * @param f
	 * @param directory 
	 * @throws IOException 
	 */
	private static boolean processFileFondPlan(File f, String directory) throws IOException
	{
		 BufferedReader dis = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		String line = "";
		
		// Cree un temporaire
		File fnew = new File("Datas\\Models\\Novarka\\DXFPlans\\DXFBlocksDetails\\temp.tmp");
		PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(fnew)));
		
		line = dis.readLine();
		//boolean end = false;
		first=false;
		// Le remplis
		while (line!=null)
		{
				String str = getGrublizeg(line, dis);
				if (str.length()!=0)
					if (accepted(line))
						ps.println(str);
					else
						System.err.println("Command not accepted : "+line);
			line = dis.readLine();
		}
		
		dis.close();
		ps.close();
		
		// Supprime l'originale
		if (f.delete()==true)
		{
			// Rename le temporaire
			if (fnew.renameTo(f)==true)
				return true;
			return false;
		}
		return false;
		
	}

	/**
	 * @param line
	 * @return
	 */
	private static boolean accepted(String line)
	{
		String allowed [] = new String [] {"LINE", 
				/*"ELLIPSE", "CIRCLE", */ "MTEXT", "TEXT"};
		line=line.toUpperCase().trim();
		for (int i = 0; i < allowed.length; i++)
		{
			if (line.equals(allowed[i]))
				return true;
		}
		return false;
	}

	/**
	 * @param ps
	 * @return 
	 * @throws IOException 
	 */
	private static String getGrublizeg(String entitytype, BufferedReader dis) throws IOException
	{
		String CMD = null;
		String VAL = null;
		
		//String entitytype = dis.readLine(); // Command
	//	System.err.println("Entity command : "+entitytype);
		//String line = null;
		String ret = entitytype+"\r\n";
		//int cpt = 0;
		do
		{
			CMD=dis.readLine().trim();
			if (CMD.trim().equals("0")==true)
			{
				ret+=CMD;
				break;
			}
			//if (line==null)
		//		break;
			VAL=dis.readLine().trim();
			
			//System.err.println(CMD+"=="+VAL);
			
			// Forbidden command.
			if (
					CMD.contains("330")  || 
					CMD.contains("370")  ||
					CMD.contains("1005") ||
					CMD.contains("1070") || 
					CMD.contains("1071") 
				)
			{
				continue;
			}
			ret += CMD+"\r\n";
			ret += VAL+"\r\n";
		//	if (cpt%2==0)
		//		if (CMD.trim().equals("0")==true) 
					
		//	cpt++;
		}
		while(true); // end of entity.
		
		return ret;
		
	}

}
