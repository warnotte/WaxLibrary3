package io.github.warnotte.waxlib3.waxlib2.Cryptage;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DialogEditionFichier extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private String content = null;
    private JScrollPane jScrollPane = null;
    private JTextArea jTextArea = null;
    private JButton jButton_OK = null;

    public synchronized String getContent() {
	return content;
    }

    public synchronized void setContent(String content) {
	this.content = content;
    }

    /**
     * @param owner
     */
    public DialogEditionFichier(Frame owner, String content) {
	super(owner, "Edition", true);
	this.content = content;
	initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(300, 200);
	this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
	if (jContentPane == null) {
	    jContentPane = new JPanel();
	    jContentPane.setLayout(new BorderLayout());
	    jContentPane.add(getJButton_OK(), BorderLayout.SOUTH);
	    jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
	}
	return jContentPane;
    }

    void jButtonOK_actionPerformed(ActionEvent e) {
	try {
	    setVisible(false);
	} catch (NumberFormatException nfe) {
	}
    }

    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
	if (jScrollPane == null) {
	    jScrollPane = new JScrollPane();
	    jScrollPane.setViewportView(getJTextArea());
	}
	return jScrollPane;
    }

    /**
     * This method initializes jTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JTextArea getJTextArea() {
	if (jTextArea == null) {
	    jTextArea = new JTextArea();
	    jTextArea.setText("" + content);
	}
	return jTextArea;
    }

    /**
     * This method initializes jButton_OK
     * 
     * @return javax.swing.JButton
     */
    private JButton getJButton_OK() {
	if (jButton_OK == null) {
	    jButton_OK = new JButton();
	    jButton_OK.setText("Ok");
	    jButton_OK.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
		    content = jTextArea.getText();
		    setVisible(false);
		}
	    });
	}
	return jButton_OK;
    }

    public static void showDialog(String filename, String keyfile)
	    throws InvalidKeyException, NoSuchAlgorithmException,
	    NoSuchPaddingException, Exception {
	AESEncrypter enc = new AESEncrypter(AESEncrypter
		.getKeyFromFile(keyfile));

	String content = "File is empty";
	String xmlcry ="";
	if (new File(filename).exists() == true) {
	    xmlcry = AESEncrypter.FiletoString(filename);
	    content = enc.decrypt(xmlcry);
	}

	JFrame f = new JFrame();
	// f.setVisible(true);
	DialogEditionFichier dialog = new DialogEditionFichier(f, content);
	dialog.setVisible(true);
	// System.err.println("Fin du dialog : "+dialog.getContent());
	content = dialog.getContent();
	xmlcry = enc.encrypt(content);
	AESEncrypter.StringToFile(filename, xmlcry);
    }

    public static void main(String args[]) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, Exception {
	DialogEditionFichier.showDialog("f:\\upload\\SCHEMA_ATELIER2.CRY","f:\\upload\\key.key");
	System.exit(1);
    }

}
