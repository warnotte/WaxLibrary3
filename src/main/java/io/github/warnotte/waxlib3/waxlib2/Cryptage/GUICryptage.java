package io.github.warnotte.waxlib3.waxlib2.Cryptage;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import io.github.warnotte.waxlib3.waxlibswingcomponents.Dialog.DialogDivers;

public class GUICryptage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JTabbedPane jTabbedPane = null;
    private JPanel jPanel_ENCRYPT = null;
    private JPanel jPanel_DECRYPT = null;
    private JPanel jPanel_EDIT = null;
    private JPanel jPanel_KEYGEN = null;
    private JPanel jPanel_South = null;
    private JTextField jTextField_FILE_KEY = null;
    private JButton jButton_LOAD_KEY = null;
    private JButton jButton_KEYGENERATIOn = null;
    
    AESEncrypter encrypteur = null;  //  @jve:decl-index=0:
    private JButton jButton_CRYPT = null;
    private JButton jButton_DECRYPT = null;
    private JScrollPane jScrollPane = null;
    private JTextArea jTextArea = null;
    private JPanel jPanel = null;
    private JButton jButton_LOAD_FILE_TO_EDIT = null;
    private JButton jButton_SAVE_FILE_TO_EDIT = null;
    private String FileEdited;  //  @jve:decl-index=0:
    
    /**
     * This is the default constructor
     */
    public GUICryptage() {
	super();
	initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(640, 480);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setContentPane(getJContentPane());
	this.setTitle("Crypteur/Decrypteur/Editeur by Warnotte Renaud");
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
	    jContentPane.add(getJTabbedPane(), BorderLayout.CENTER);
	    jContentPane.add(getJPanel_South(), BorderLayout.SOUTH);
	}
	return jContentPane;
    }

    /**
     * This method initializes jTabbedPane	
     * 	
     * @return javax.swing.JTabbedPane	
     */
    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
    	jTabbedPane = new JTabbedPane();
    	//jTabbedPane.setLayout(new BorderLayout());
    	jTabbedPane.addTab("Encryption", new ImageIcon(getClass().getResource("/WaxLibrary/Cryptage/Lock.png")), getJPanel_ENCRYPT(), null);
    	jTabbedPane.addTab("Decryption", new ImageIcon(getClass().getResource("/WaxLibrary/Cryptage/UnLock.png")), getJPanel_DECRYPT(), null);
    	jTabbedPane.addTab("Edition", new ImageIcon(getClass().getResource("/WaxLibrary/Cryptage/Edit.png")), getJPanel_EDIT(), null);
    	jTabbedPane.addTab("Generation cle", new ImageIcon(getClass().getResource("/WaxLibrary/Cryptage/KeyGen.png")), getJPanel_KEYGEN(), null);

    	
        }
        return jTabbedPane;
    }

    /**
     * This method initializes jPanel_ENCRYPT	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel_ENCRYPT() {
        if (jPanel_ENCRYPT == null) {
    	jPanel_ENCRYPT = new JPanel();
    	jPanel_ENCRYPT.setLayout(new GridBagLayout());
    	jPanel_ENCRYPT.add(getJButton_CRYPT(), new GridBagConstraints());
        }
        return jPanel_ENCRYPT;
    }

    /**
     * This method initializes jPanel_DECRYPT	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel_DECRYPT() {
        if (jPanel_DECRYPT == null) {
    	jPanel_DECRYPT = new JPanel();
    	jPanel_DECRYPT.setLayout(new GridBagLayout());
    	jPanel_DECRYPT.add(getJButton_DECRYPT(), new GridBagConstraints());
        }
        return jPanel_DECRYPT;
    }

    /**
     * This method initializes jPanel_EDIT	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel_EDIT() {
        if (jPanel_EDIT == null) {
    	jPanel_EDIT = new JPanel();
    	jPanel_EDIT.setLayout(new BorderLayout());
    	jPanel_EDIT.add(getJScrollPane(), BorderLayout.CENTER);
    	jPanel_EDIT.add(getJPanel(), BorderLayout.NORTH);
        }
        return jPanel_EDIT;
    }

    /**
     * This method initializes jPanel_KEYGEN	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel_KEYGEN() {
        if (jPanel_KEYGEN == null) {
    	jPanel_KEYGEN = new JPanel();
    	jPanel_KEYGEN.setLayout(new GridBagLayout());
    	jPanel_KEYGEN.add(getJButton_KEYGENERATIOn(), new GridBagConstraints());
        }
        return jPanel_KEYGEN;
    }

    /**
     * This method initializes jPanel_South	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel_South() {
        if (jPanel_South == null) {
    	GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
    	gridBagConstraints1.gridx = 1;
    	gridBagConstraints1.fill = GridBagConstraints.BOTH;
    	gridBagConstraints1.gridy = 0;
    	GridBagConstraints gridBagConstraints = new GridBagConstraints();
    	gridBagConstraints.fill = GridBagConstraints.BOTH;
    	gridBagConstraints.weightx = 1.0;
    	jPanel_South = new JPanel();
    	jPanel_South.setLayout(new GridBagLayout());
    	jPanel_South.add(getJTextField_FILE_KEY(), gridBagConstraints);
    	jPanel_South.add(getJButton_LOAD_KEY(), gridBagConstraints1);
        }
        return jPanel_South;
    }

    /**
     * This method initializes jTextField_FILE_KEY	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextField_FILE_KEY() {
        if (jTextField_FILE_KEY == null) {
            jTextField_FILE_KEY = new JTextField();
    	jTextField_FILE_KEY.setEditable(false);
        }
        return jTextField_FILE_KEY;
    }

    /**
     * This method initializes jButton_LOAD_KEY	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton_LOAD_KEY() {
        if (jButton_LOAD_KEY == null) {
    	jButton_LOAD_KEY = new JButton();
    	jButton_LOAD_KEY.setText("Charge Cle");
    	jButton_LOAD_KEY.addActionListener(new java.awt.event.ActionListener() {
    	    public void actionPerformed(java.awt.event.ActionEvent e) {
    		try {
    		choisis_cle(null);
		} catch (InvalidKeyException e1) {
		    DialogDivers.Show_dialog(e1, "Erreur pdt le choix de la cl�", GUICryptage.this);
		    e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
		    DialogDivers.Show_dialog(e1, "Erreur pdt le choix de la cl�", GUICryptage.this);
		    e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
		    DialogDivers.Show_dialog(e1, "Erreur pdt le choix de la cl�", GUICryptage.this);
		    e1.printStackTrace();
		} catch (IOException e1) {
		    DialogDivers.Show_dialog(e1, "Erreur pdt le choix de la cl�", GUICryptage.this);
		    e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			DialogDivers.Show_dialog(e1, "Erreur pdt le choix de la cl�", GUICryptage.this);
		    e1.printStackTrace();
		} catch (Exception e1)
		{
			DialogDivers.Show_dialog(e1, "Erreur pdt le choix de la cl�", GUICryptage.this);
			e1.printStackTrace();
		}
    	    }
    	});
        }
        return jButton_LOAD_KEY;
    }


    /**
     * This method initializes jButton_KEYGENERATIOn	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton_KEYGENERATIOn() {
        if (jButton_KEYGENERATIOn == null) {
    	jButton_KEYGENERATIOn = new JButton();
    	jButton_KEYGENERATIOn.setText("Generation d'une cl�");
    	jButton_KEYGENERATIOn.addActionListener(new java.awt.event.ActionListener() {
    	    public void actionPerformed(java.awt.event.ActionEvent e) {
    		generate_key();
    	    }
    	});
        }
        return jButton_KEYGENERATIOn;
    }
    

    /**
     * This method initializes jButton_CRYPT	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton_CRYPT() {
        if (jButton_CRYPT == null) {
    	jButton_CRYPT = new JButton();
    	jButton_CRYPT.setText("Crypte");
    	jButton_CRYPT.addActionListener(new java.awt.event.ActionListener() {
    	    public void actionPerformed(java.awt.event.ActionEvent e) {
    		try {
		    crypte_fichier();
		} catch (Exception e1) {
		    DialogDivers.Show_dialog(e1, "Erreur durant le cryptage", GUICryptage.this);
		    e1.printStackTrace();
		}
    	    }
    	});
        }
        return jButton_CRYPT;
    }

    

    /**
     * This method initializes jButton_DECRYPT	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton_DECRYPT() {
        if (jButton_DECRYPT == null) {
    	jButton_DECRYPT = new JButton();
    	jButton_DECRYPT.setText("Decrypte");
    	jButton_DECRYPT.addActionListener(new java.awt.event.ActionListener() {
    	    public void actionPerformed(java.awt.event.ActionEvent e) {
    		try {
		    decrypte_fichier();
		} catch (Exception e1) {
		    DialogDivers.Show_dialog(e1, "Erreur durant le decryptage", GUICryptage.this);
		    e1.printStackTrace();
		} 
    	    }

	    
    	});
        }
        return jButton_DECRYPT;
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
        }
        return jTextArea;
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
    	jPanel = new JPanel();
    	jPanel.setLayout(new GridBagLayout());
    	jPanel.add(getJButton_LOAD_FILE_TO_EDIT(), new GridBagConstraints());
    	jPanel.add(getJButton_SAVE_FILE_TO_EDIT(), new GridBagConstraints());
        }
        return jPanel;
    }
    
    
    /**
     * This method initializes jButton_LOAD_FILE_TO_EDIT	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton_LOAD_FILE_TO_EDIT() {
        if (jButton_LOAD_FILE_TO_EDIT == null) {
    	jButton_LOAD_FILE_TO_EDIT = new JButton();
    	jButton_LOAD_FILE_TO_EDIT.setText("Charge un fichier CRY");
    	jButton_LOAD_FILE_TO_EDIT
    		.addActionListener(new java.awt.event.ActionListener() {
    		    public void actionPerformed(java.awt.event.ActionEvent e) {
    			try {
			    load_file_to_edit();
			} catch (Exception e1) {
			    DialogDivers.Show_dialog(e1, "Erreur durant le chargement du fichier crypt�", GUICryptage.this);
			    e1.printStackTrace();
			} 
    		    }
    		});
        }
        return jButton_LOAD_FILE_TO_EDIT;
    }

    /**
     * This method initializes jButton_SAVE_FILE_TO_EDIT	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton_SAVE_FILE_TO_EDIT() {
        if (jButton_SAVE_FILE_TO_EDIT == null) {
    	jButton_SAVE_FILE_TO_EDIT = new JButton();
    	jButton_SAVE_FILE_TO_EDIT.setText("Sauve le fichier");
    	jButton_SAVE_FILE_TO_EDIT
    		.addActionListener(new java.awt.event.ActionListener() {
    		    public void actionPerformed(java.awt.event.ActionEvent e) {
    			try {
			    save_file_to_edit();
			} catch (Exception e1) {
			    DialogDivers.Show_dialog(e1, "Erreur durant le chargement du fichier crypt�", GUICryptage.this);
			    e1.printStackTrace();
			} 
    		    }
    		});
        }
        return jButton_SAVE_FILE_TO_EDIT;
    }

    
    


    protected void generate_key() {
	String filename = DialogDivers.SaveDialog(this, "key",".", "Generate a key");
	
	if (filename!=null)
	{
	    try {
		AESEncrypter.writeNewKey(filename);
		choisis_cle(filename);
	    } catch (Exception e) {
		DialogDivers.Show_dialog(e, "Erreur durant l'ecriture de la cl�", this);
	    }
	}
	else
	    DialogDivers.Show_dialog(null, "Specifiez un fichier valide", this);
	
    }
    
    protected void choisis_cle(String filename) throws Exception {
	File file = null;
	if (filename==null)
	 file = DialogDivers.LoadDialog(this, "key", ".");
	else
	    file = new File(filename);
	if ((file!=null) && (file.exists()))
	{
	    jTextField_FILE_KEY.setText(""+file.getAbsolutePath());
	    encrypteur = new AESEncrypter(file.getAbsolutePath());
	}
	else
	{
	    DialogDivers.Show_dialog(null, "Cle incorrectement choisie.", this);
	    encrypteur=null;
	}
	    
    }

    protected void crypte_fichier() throws Exception {
	if (encrypteur!=null)
	{
	File filein = DialogDivers.LoadDialog(this, "",".", "Load");
	String fileout = DialogDivers.SaveDialog(this, "cry",".",  "Save");
	if ((filein!=null) && (filein.exists()))
	{
	    String content = AESEncrypter.FiletoString(filein.getAbsolutePath());
	    content = encrypteur.encrypt(content);
	    AESEncrypter.StringToFile(fileout, content);
	}
	else
	    DialogDivers.Show_dialog(null, "Veuillez choisir un fichier qui existe pour crypter", this);
	}
	else
	    DialogDivers.Show_dialog(null, "Veuillez choisir une cl� avant...", this);
    }
    
    private void decrypte_fichier() throws Exception {
	if (encrypteur!=null)
	{
	File filein = DialogDivers.LoadDialog(this, "cry", ".");
	String fileout = DialogDivers.SaveDialog(this, "", ".", "Save");
	if ((filein!=null) && (filein.exists()))
	{
	    String content = AESEncrypter.FiletoString(filein.getAbsolutePath());
	    content = encrypteur.decrypt(content);
	    AESEncrypter.StringToFile(fileout, content);
	}
	else
	    DialogDivers.Show_dialog(null, "Veuillez choisir un fichier qui existe pour crypter", this);
	}
	else
	    DialogDivers.Show_dialog(null, "Veuillez choisir une cl� avant...", this);
	
    }


    protected void save_file_to_edit() throws Exception {
	if (FileEdited==null)
	    throw new Exception("Vous n'avez pas encore ouvert de fichier");
	
	if (encrypteur!=null)
	{
	if (FileEdited!=null)
	{
	    String content = jTextArea.getText();
	    content = encrypteur.encrypt(content);
	    AESEncrypter.StringToFile(FileEdited, content);
	}
	else
	    DialogDivers.Show_dialog(null, "Veuillez choisir un fichier qui existe pour crypter", this);
	}
	else
	    DialogDivers.Show_dialog(null, "Veuillez choisir une cl� avant...", this);
	
    }
    
    protected void load_file_to_edit() throws Exception {
	FileEdited = null;
	if (encrypteur!=null)
	{
	File filein = DialogDivers.LoadDialog(this, "cry", ".");
	if ((filein!=null) && (filein.exists()))
	{
	    String content = AESEncrypter.FiletoString(filein.getAbsolutePath());
	    content = encrypteur.decrypt(content);
	    jTextArea.setText(content);
	    FileEdited = filein.getAbsolutePath();
	}
	else
	    DialogDivers.Show_dialog(null, "Veuillez choisir un fichier qui existe pour crypter", this);
	}
	else
	    DialogDivers.Show_dialog(null, "Veuillez choisir une cl� avant...", this);
	
    }
    public static void main(String []args)
    {
	GUICryptage gui = new GUICryptage();
	gui.setVisible(true);
    }
}
