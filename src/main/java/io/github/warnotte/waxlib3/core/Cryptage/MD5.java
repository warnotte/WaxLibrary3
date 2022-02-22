package io.github.warnotte.waxlib3.core.Cryptage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class MD5
{

	/*
	 * Encode la chaine pass� en param�tre avec l�algorithme MD5
	 * @param key : la chaine � encoder
	 * @return la valeur (string) hexad�cimale sur 32 bits
	 */

	public static String encode(String key)
	{

		byte[] uniqueKey = key.getBytes();
		byte[] hash = null;

		try
		{
			// on r�cup�re un objet qui permettra de crypter la chaine
			hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new Error("no MD5 support in this VM");
		}
		StringBuffer hashString = new StringBuffer();
		for (int i = 0; i < hash.length; ++i)
		{
			String hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1)
			{
				hashString.append("0");
				hashString.append(hex.charAt(hex.length() - 1));
			}
			else
			{
				hashString.append(hex.substring(hex.length() - 2));
			}
		}
		return hashString.toString();
	}

	// m�thode principale

	public static void main(String[] args)
	{
		System.out.println("La chaine : P@ssWord, crypt�e via MD5 donne : " + MD5.encode("P@ssWord"));
	}

}