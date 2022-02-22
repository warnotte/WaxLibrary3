package io.github.warnotte.waxlib3.core.DXFWriter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public abstract class PrintStreamWriter implements WriterInterface {

	PrintStream ps = null;
	
	public void createFile(String filename) throws IOException
	{
		File file = new File(filename);
		ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)), true, "ISO-8859-1");
		ps.println("0");
		ps.println("SECTION");
		ps.println("2");
		String header = FiletoString("Datas\\DXFHelpers\\header.dxf");
		ps.print(header);
		this.EndSection();
		ps.println("SECTION");
		ps.println("2");
		String tables = FiletoString("Datas\\DXFHelpers\\tables.dxf");
		ps.println(tables);
		ps.println("0");
	}
	
	public void createSection(String named)
	{
		ps.println("SECTION");
		ps.println("2");
		ps.println(""+named);
		ps.println("0");
	}
	
	public void EndSection()
	{
		ps.println("ENDSEC");
		ps.println("0");
	}
	
	public void closeFile()
	{
		ps.println("EOF");
		ps.close();
	}
	
	static String FiletoString(String filename) throws IOException
	{
		//return Files.readString(Paths.get(filename));
		
		FileInputStream file = new FileInputStream (filename);
		byte[] b = new byte[file.available ()];
		file.read(b);
		file.close ();
		String result = new String (b);
		return result;
	}

}
