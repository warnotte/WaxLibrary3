package io.github.warnotte.waxlib3.waxlib2.Cryptage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESEncrypter
{
    static String Algo = "AES";//"DES";
    static String Encodage = "ISO-8859-1";
    
  /*  static byte[] keyBytes = new byte[] {
	    0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
	    0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 
	    0x0f, 0x15, 0x16, 0x17 };
    */
    
   
    static byte[] keyBytes = new byte[] {
    		 (byte) 0xA0,  (byte)0xC0, 0x10,  (byte)0x90,  (byte)0xc0, 0x70, 0x50, 0x30, 
    	    0x40,  (byte)0xc0, 0x0, 0x30,  (byte)0xbf, 0x40, 0x60,  (byte)0x90, 
    	    };

    static SecretKeySpec keyIn = new SecretKeySpec(keyBytes, "AES");
    
    static private String   err8;
    static private String   err9;
    Cipher                  ecipher;
    Cipher                  dcipher;
    private String          err1;
    private String          err2;
   // private String          err3;
    private String          err4;
    private String          err5;
    private String          err6;
   // private String          err7;
    
    
    public AESEncrypter() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
    {
	this(keyIn);
    }
    
    /**
     * @param key Clef de cryptage
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public AESEncrypter(SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
    {
	
            Laodproperties();
            ecipher     = Cipher.getInstance(Algo);
            dcipher     = Cipher.getInstance(Algo);
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
    }
    
    
    /**
     * 
     * @param filename Le fichier avec la clé.
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public AESEncrypter(String filename) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, ClassNotFoundException
    {
            Laodproperties();
            SecretKey         key = KeyGenerator.getInstance(Algo).generateKey();

            FileInputStream   f_out   = new FileInputStream(filename);
            ObjectInputStream obj_out = new ObjectInputStream(f_out);
            key = (SecretKey) obj_out.readObject();
            
            ecipher     = Cipher.getInstance(Algo);
            dcipher     = Cipher.getInstance(Algo);
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
            obj_out.close();
    }
   

    /**Chargement des properties
     */
    private void Laodproperties()
    {
    	err1     = "Erreur d'encryptage du fichier XML BadPaddingException";
        err2     = "Erreur d'encryptage du fichier XML IllegalBlockSizeException";
      //  err3     = "Erreur d'encryptage du fichier XML UnsupportedEncodingException";
        err4     = "Erreur de décryptage du fichier XML BadPaddingException";
        err5     = "Erreur de décryptage du fichier XML IllegalBlockSizeException";
        err6     = "Erreur de décryptage du fichier XML UnsupportedEncodingException";
        //err7     = "Erreur de décryptage du fichier XML IOException";
        err8     = "Erreur de récupèration de la clef de cryptage";
        err9     = "Erreur de génération de la clef de cryptage";
    }

    /**M�thode de cryptage d'un string
     * @param str Texte d�crypt�
     * @return Texte crypt�
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws UnsupportedEncodingException 
     */
    public String encrypt(String str) throws BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {
        try
        {
            byte[] utf8 = str.getBytes(Encodage);
            byte[] enc = ecipher.doFinal(utf8);
            Base64.Encoder mimeEncoder = java.util.Base64.getMimeEncoder();
            return mimeEncoder.encodeToString(enc);
            //return new sun.misc.BASE64Encoder().encode(enc);
        }
        catch (javax.crypto.BadPaddingException e)
        {
            System.err.println(err1 + e.getMessage());
            throw e;
        }
        catch (IllegalBlockSizeException e)
        {
            System.err.println(err2 + e.getMessage());
            throw e;
        }
      /*  catch (UnsupportedEncodingException e)
        {
            System.err.println(err3 + e.getMessage());
            throw e;
        }*/
    }

    /** M�thode de d�cryptage d'un string
     * @param str Texte crypt�
     * @return Texte d�crypt�
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws IOException 
     */
    public String decrypt(String str) throws BadPaddingException, IllegalBlockSizeException, IOException
    {
        try
        {
        	Base64.Decoder mimeDecoder = java.util.Base64.getMimeDecoder();
        	
            byte[] dec  = mimeDecoder.decode(str);
            byte[] utf8 = dcipher.doFinal(dec);
            return new String(utf8, Encodage);
        }
        catch (javax.crypto.BadPaddingException e)
        {
            System.err.println(err4 + e.getMessage());
            throw e;
        }
        catch (IllegalBlockSizeException e)
        {
            System.err.println(err5 + e.getMessage());
            throw e;
        }
        catch (UnsupportedEncodingException e)
        {
            System.err.println(err6 + e.getMessage());
            throw e;
        }
        
    }

    /** Méthode de récupération de la clef de cryptage dans un fichier
     * @param filename Nom du fichier
     * @return Clef de décryptage
     * @throws Exception 
     */
    public static SecretKey getKeyFromFile(String filename) throws Exception
    {
        try
        {
            SecretKey         key = KeyGenerator.getInstance(Algo).generateKey();
            FileInputStream   f_out   = new FileInputStream(filename);
            ObjectInputStream obj_out = new ObjectInputStream(f_out);
            key = (SecretKey) obj_out.readObject();
            obj_out.close();
            return key;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println(err8 + e.getMessage());
            throw e;
        }
    }

    /** Méthode de génération aléatoire de la clef de cryptage dans un fichier
     * @param filename Nom du fichier
     * @throws Exception 
     */
    public static void writeNewKey(String filename) throws Exception
    {
        try
        {
            KeyGenerator toto = KeyGenerator.getInstance(Algo);
            SecretKey          key = toto.generateKey();
            
            FileOutputStream   f_out   = new FileOutputStream(filename);
            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
            obj_out.writeObject(key);
            obj_out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println(err9 + e.getMessage());
            throw e;
        }
    }
    
    public static void main(String[] args) throws Exception {
	
	
    	AESEncrypter enc = new AESEncrypter();
  
	String xmlcry = FiletoString("F:\\Projets\\XXXXX\\R.CRY");
	String xmldecry = enc.decrypt(xmlcry);
	System.err.println(""+xmldecry);

    }
    
    public static String FiletoString(String filename) throws IOException
	{
    	//Files.readString(path)
    	
	   FileInputStream file = new FileInputStream (filename);
	         byte[] b = new byte[file.available ()];
	         file.read(b);
	         file.close ();
	         String result = new String (b, Encodage);
	         return result;
	}
    
    public static String StringToFile(String filename, String txt) throws IOException
    {
    	
    	//Files.writeString(path, csq, options)
    	
		File		    f = new File(filename);
		FileOutputStream fis = new FileOutputStream(f);
		DataOutputStream dis = new DataOutputStream(fis);
		byte []		    b = txt.getBytes();
		dis.write(b);
		String m = new String(b, Encodage);
		dis.flush();
		fis.flush();
		dis.close();
		fis.close();
		
		return m;
    }
	 
}
