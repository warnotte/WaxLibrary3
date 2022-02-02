package io.github.warnotte.waxlib3.waxlib2.Logs;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.TextArea.JTimeStampedTextArea;

@Plugin(name = "JTextAreaAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject=true)
public class JTextAreaAppender extends AbstractAppender {
 
	// Pourquoi j'ai ca dant tttoools encore ?????!
	//WARNING: sun.reflect.Reflection.getCallerClass is not supported. This will impact performance.
	//2019-06-20 16:43:01,768 main ERROR Appenders contains an invalid element or attribute "JTextAreaAppender"
	//2019-06-20 16:43:01,786 main ERROR Unable to locate appender "JTextAreaAppender" for logger config "root"
	
	
    private static JTextArea jTextArea = new JTimeStampedTextArea();
    
    static boolean  DEBUG = true;
    static boolean  INFO = true;
	static boolean  WARN = true;
	static boolean  ERROR = true;
	static boolean  FATAL = true;
    
	
	protected JTextAreaAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
		super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
		
	}
	
	@PluginFactory
    public static JTextAreaAppender createAppender(@PluginAttribute("name") String name,
                                              @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
                                              @PluginElement("Layout") Layout<?> layout,
                                              @PluginElement("Filters") Filter filter) {

        if (name == null) {
            LOGGER.error("No name provided for JTextAreaAppender");
            return null;
        }

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new JTextAreaAppender(name, filter, layout, false/*ignoreExceptions*/);
    }
	
    /**
     * Format and then append the loggingEvent to the stored
     * JTextArea.
     */
    @Override
    public void append(LogEvent loggingEvent) {
    	
    	Level lvl = loggingEvent.getLevel();
    	
    	if ((lvl.equals(Level.DEBUG)) && (DEBUG==false))
    		return;
    	if ((lvl.equals(Level.INFO)) && (INFO==false))
    		return;
    	if ((lvl.equals(Level.WARN)) && (WARN==false))
    		return;
    	if ((lvl.equals(Level.ERROR)) && (ERROR==false))
    		return;
    	if ((lvl.equals(Level.FATAL)) && (FATAL==false))
    		return;
    	
    	
        final LogEvent event = loggingEvent;

     //   System.err.println("info = "+lvl.toString());
    	String message = new String(getLayout().toByteArray(event));
    //	System.err.println("Message = "+message);
    	
    	        // Append formatted message to textarea using the Swing Thread.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	if (jTextArea!=null){
            		//String message = new String(getLayout().toByteArray(event));
            		jTextArea.append(message);
            		jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
            		 if (true)
            	     {
            			 
            					Throwable throwable = loggingEvent.getThrown();
            	        		if (throwable!=null)
            	        		{
            	        			StringWriter errors = new StringWriter();
            	        			throwable.printStackTrace(new PrintWriter(errors));
            	        			String stack = errors.toString();
            	        			jTextArea.append(stack);
            	            		jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
            	        		}
            	        		
            	        	
            	     }
            		
            	}
            }
        });
    }

	/**
	 * @return the jTextArea
	 */
	public static synchronized JTextArea getjTextArea()
	{
		return jTextArea;
	}

	/**
	 * @return the dEBUG
	 */
	public static synchronized boolean isDEBUG()
	{
		return DEBUG;
	}

	/**
	 * @param dEBUG the dEBUG to set
	 */
	public static synchronized void setDEBUG(boolean dEBUG)
	{
		DEBUG = dEBUG;
	
	}

	/**
	 * @return the iNFO
	 */
	public static synchronized boolean isINFO()
	{
		return INFO;
	}

	/**
	 * @param iNFO the iNFO to set
	 */
	public static synchronized void setINFO(boolean iNFO)
	{
		INFO = iNFO;
	
	}

	/**
	 * @return the wARN
	 */
	public static synchronized boolean isWARN()
	{
		return WARN;
	}

	/**
	 * @param wARN the wARN to set
	 */
	public static synchronized void setWARN(boolean wARN)
	{
		WARN = wARN;
	
	}

	/**
	 * @return the eRROR
	 */
	public static synchronized boolean isERROR()
	{
		return ERROR;
	}

	/**
	 * @param eRROR the eRROR to set
	 */
	public static synchronized void setERROR(boolean eRROR)
	{
		ERROR = eRROR;
	
	}

	/**
	 * @return the fATAL
	 */
	public static synchronized boolean isFATAL()
	{
		return FATAL;
	}

	/**
	 * @param fATAL the fATAL to set
	 */
	public static synchronized void setFATAL(boolean fATAL)
	{
		FATAL = fATAL;
	
	}
	
	


}