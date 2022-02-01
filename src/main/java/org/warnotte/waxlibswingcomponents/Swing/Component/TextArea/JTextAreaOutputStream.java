package org.warnotte.waxlibswingcomponents.Swing.Component.TextArea;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

/**
 * Un OutputStream vers un JTextArea. Utile pour redéfinir System.out
 * et consorts vers un JTextArea.
 * @see javax.swing.JTextArea
 * @see java.io.OutputStream
 * @author Glob
 * @version 0.2
 */
public class JTextAreaOutputStream extends OutputStream {
   private JTextArea m_textArea = null;
//private boolean newly;

   /**
    * Method JTextAreaOutputStream.
    * @param aTextArea le JTextArea qui recevra les caract�res.
    */
   public JTextAreaOutputStream(JTextArea aTextArea) {
      m_textArea = aTextArea;
   }

   /**
    * écrit un caractère dans le JTextArea.
    * Si le caractère est un retour chariot, scrolling.
    * @see java.io.OutputStream#write(int)
    */
   @Override
public void write(int b) throws IOException {
      byte[] bytes = new byte[1];
      bytes[0] = (byte)b;
      String newText = new String(bytes);
      
    /*  if (newly==true)
      {
    	  String date = new Date().toString();
          m_textArea.append(date+" : ");
    	  newly=false;
      }*/
      m_textArea.append(newText);
      //super.write(newText.getBytes());
      if (newText.indexOf('\n') > -1) {
         try {
  //      	newly=true;
        	 
        	// m_textArea.scrollRectToVisible(m_textArea.modelToView(m_textArea.getDocument().getLength()));
        	 m_textArea.scrollRectToVisible(m_textArea.modelToView2D(m_textArea.getDocument().getLength()).getBounds());
         } catch (javax.swing.text.BadLocationException err) {
            err.printStackTrace();
         }
      }
   //   super.write(bytes);
   }

   /**
    * écrit un tableau de bytes dans le JTextArea.
    * Scrolling du JTextArea à la fin du texte ajouté.
    * @see java.io.OutputStream#write(byte[])
    */
   @Override
public final void write(byte[] arg0) throws IOException {
      String txt = new String(arg0);
     // String date = new Date().toString();
     // m_textArea.append(date);
      m_textArea.append("#"+txt);
     
      try {
        //  m_textArea.scrollRectToVisible(m_textArea.modelToView(m_textArea.getDocument().getLength()));
          m_textArea.scrollRectToVisible(m_textArea.modelToView2D(m_textArea.getDocument().getLength()).getBounds());
      } catch (javax.swing.text.BadLocationException err) {
         err.printStackTrace();
      }
      
      super.write(arg0);
   }
}
