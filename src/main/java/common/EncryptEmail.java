package common;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptEmail 
{

	private static final String ALGORITHM = "AES";
	private static final byte[] keyValue = 
		    new byte[] {'C','O','M','P','U','T','E','R','C','O','M','P','U','T','E','R'};
	//{ 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };
	
	private static Key generateKey() 
	{
		try
		{
	    Key key = new SecretKeySpec(keyValue, ALGORITHM);
	    return key;
		}
		catch(Exception ex)
		{
			return null;
		}
	}
	
	public static String encrypt(String valueToEnc) 
	{		
		try
		{
	    Key key = generateKey();
	    Cipher c = Cipher.getInstance(ALGORITHM);
	    c.init(Cipher.ENCRYPT_MODE, key);
	    byte[] encValue = c.doFinal(valueToEnc.getBytes());
	    String encryptedValue = new BASE64Encoder().encode(encValue);
	    return encryptedValue;
		}
		catch(Exception ex)
		{
			return "";
		}
	}
	
	public static String decrypt(String encryptedValue)
	{		
		try
		{
	    Key key = generateKey();
	    Cipher c = Cipher.getInstance(ALGORITHM);
	    c.init(Cipher.DECRYPT_MODE, key);
	    byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
	    byte[] decValue = c.doFinal(decordedValue);
	    String decryptedValue = new String(decValue);
	    return decryptedValue;
		}
		catch(Exception ex)
		{
			return "";
		}
	}

}
