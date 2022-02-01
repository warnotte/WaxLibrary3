package org.warnotte.waxlibswingcomponents.Swing.Component.TextArea;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class JTimeStampedTextArea extends JTextArea
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -321064999596596052L;
	boolean						newLine				= true;

	boolean						timeStamp			= true;

	SimpleDateFormat			sdf					= new SimpleDateFormat("EEEEEEEE HH:mm:ss");

	@Override
	public void append(String str2)
	{
		final String str = str2;
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{

				if (timeStamp)
					if (newLine == true)
					{
						String date = sdf.format(new Date());
						JTimeStampedTextArea.super.append(date + " ║ "); // ║☺☻
						newLine = false;
					}

				JTimeStampedTextArea.super.append(str);

				if (timeStamp)
					if (str.contains("\n") == true)
						newLine = true;
			}
		});
	}

	/**
	 * @return the timeStamp
	 */
	public synchronized boolean isTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public synchronized void setTimeStamp(boolean timeStamp)
	{
		this.timeStamp = timeStamp;

	}

	/**
	 * @return the sdf
	 */
	public synchronized SimpleDateFormat getDateFormat()
	{
		return sdf;
	}

	/**
	 * @param sdf
	 *            the sdf to set
	 */
	public synchronized void setDateFormat(SimpleDateFormat sdf)
	{
		this.sdf = sdf;

	}

}
