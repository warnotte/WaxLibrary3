package io.github.warnotte.waxlib3.waxlib2.RuntimeExceptionHandler;

import java.awt.Dialog.ModalExclusionType;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Permet de catcher les ExceptionRuntime et de les envoyer a log4J en Fatal
 * ainsi qu'affichre une petite fen�tre pour terminer le programme.
 * 
 * @author Warnotte Renaud.
 */
public class RunTimeExceptionHandler implements UncaughtExceptionHandler
{
	
	protected static final Logger Logger = LogManager.getLogger("RunTimeExceptionHandler");
	RunTimeExceptionDialog rteh;

	public RunTimeExceptionHandler()
	{
		 
	}

	public void uncaughtException(Thread thread, final Throwable ex)
	{
		// Log into Log4J.
		//if (logs!=null) 
		{
			Logger.fatal("RuntimeException", ex);
			Logger.fatal(ex);
		}
		// Avertir le user.
		// ShowThrowableMessage(ex);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				if (rteh==null)
				{
					rteh = new RunTimeExceptionDialog(ex);
					rteh.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
				}
				else
					rteh.appendException(ex);
				rteh.setVisible(true);
			}
		});

	}

}
