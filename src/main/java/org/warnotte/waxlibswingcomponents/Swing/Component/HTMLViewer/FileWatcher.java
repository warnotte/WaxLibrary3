package org.warnotte.waxlibswingcomponents.Swing.Component.HTMLViewer;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.TimerTask;

public class FileWatcher extends TimerTask {
  private long timeStamp;
  private File file;
  String buffer = "";
  
  DataInputStream dis =null;
  HTMLAutoRefreshFileViewer parent = null;
  

  public FileWatcher( File file, HTMLAutoRefreshFileViewer autoRefreshFileViewer ) throws IOException {
    this.file = file;
    this.parent = autoRefreshFileViewer;
    FileInputStream fis = new FileInputStream(file);
    BufferedInputStream bis = new BufferedInputStream(fis);
     dis = new DataInputStream(bis);
    
     int Lenght = (int) file.length();
     
     byte data [] = new byte[Lenght];
     dis.read(data, 0, Lenght);
     buffer = new String(data);
     /*
     while (true)
		{
			try
			{
				byte c = dis.readByte();
				if (c == -1)
					break;
				String b = "" + (char) c;
				buffer = buffer + b;
			} 
			catch (IOException e1)
			{
				break;
				// e1.printStackTrace();
			}
		}*/
    
    parent.refresh(buffer);
	buffer="";
    this.timeStamp = file.lastModified();
  }

  @Override
public final void run() {
    long timeStamp = file.lastModified();

    if( this.timeStamp != timeStamp ) {
      this.timeStamp = timeStamp;
      onChange(file);
    }
  }

  protected void onChange( File file )
  {
	
		  while(true)
		  {
		  try
		  
		{	byte c = dis.readByte();
		if (c==-1)
			break;
			  String b = ""+(char)c;
			  buffer=buffer+b;
		} 
		  catch (IOException e1)
		{
		 break;
			//e1.printStackTrace();
		}}
				  
	  System.err.println(""+buffer);
	  parent.refresh(buffer);
	  buffer="";
	  
  }
  
  public synchronized String getBuffer()
  {
	  return buffer;
  }
  
  public static void main(String args[])
  {
	/*  TimerTask task = new FileWatcher( new File("F:/Projets/BoatSlicer/logs/logs.html") ) {
	      protected void onChange( File file ) {
	        // here we code the action on a change
	        System.out.println( "File "+ file.getName() +" have change !" );
	      }
	    };
	    
	  Timer timer = new Timer();
	  // repeat the check every second
	  timer.schedule( task , new Date(), 250 );*/
  }
}
