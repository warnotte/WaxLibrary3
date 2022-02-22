package io.github.warnotte.waxlib3.core.NTPDateChecker;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;

/**
 * 
 */

/**
 * @author Warnotte Renaud
 *
 */
public class DateChecker
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		DateChecker.checkValidity("21/12/2016");
	}
	
	public static void checkValidity(String date)
	{
		Logger.getLogger("TOTO").severe("TEOKEPKTP");
		Date serverTime=null;
		GUI_DateChecker gui = new GUI_DateChecker();
		gui.setMessage("Checking license validity...");
		gui.setVisible(true);
		LogManager.shutdown();
		try
		{
			serverTime = NtpClient.getServerTime();	
		}
		// If not host is answering, tell client 
		catch (NoNTPHostAnswering e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(gui, "Couldn't connect to server, check your internet connection", "Error", 0); //$NON-NLS-1$
			gui.setVisible(false);
			System.exit(-1);
		}
		
		gui.setMessage("Date retrieved : "+serverTime);
		System.err.println("--> "+serverTime);
		
		Date LocalDate=null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			LocalDate = sdf.parse(date);
		} 
		catch (ParseException e)
		{
			JOptionPane.showMessageDialog(gui, "Parsing date error", "Error", 0); //$NON-NLS-1$
			gui.setVisible(false);
			System.exit(-1);
		}
		
		if (serverTime.after(LocalDate))
		{
			JOptionPane.showMessageDialog(gui, "License expired", "Error", 0); //$NON-NLS-1$
			gui.setVisible(false);
			System.exit(-1);
		}
		
		gui.setVisible(false);
	}
}
