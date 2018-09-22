/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.marrs.ischool.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 *
 * @author danielhenriquelima
 */
public class Crypto {
    
    private static final String engine = "AES";
    private static final String crypto = "AES/CBC/PKCS5Padding";
    
    static String key = "1234567890987654";
    static String _iv = "1234567890987654";

    public static byte[] cipher(byte[] data, int mode)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException{
    	
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), engine);
        IvParameterSpec iv = new IvParameterSpec(_iv.getBytes());
        Cipher c = Cipher.getInstance(crypto);
        c.init(mode, sks, iv);
        return c.doFinal(data);
    }

    public static byte[] encrypt(byte[] data) throws Exception{
        return cipher(data, Cipher.ENCRYPT_MODE);
    }

    public static byte[] decrypt(byte[] data)  throws Exception {
        return data == null ? new byte[0] : cipher(data, Cipher.DECRYPT_MODE);
    }
    
    public static String encriptSenhaMD5(String senha) 
    {     
          try{ 
              return MD5CheckSum.gerarCodigoHash(senha);   
          } 
          catch (NoSuchAlgorithmException ns){     
        	  ns.printStackTrace();
              System.out.println("Exception: "+ns.toString());                  
          }  
          
          return senha;      
     }
    
    
    public static String gerarSalt(){
    	
		String set ="0123456789abcdefghijklmnopqurstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ";
		String salt = "";
		for (int  i = 0; i < 10; i++) {
			double p = Math.floor(Math.random() * set.length());
			salt += set.charAt((int) p);
		}
		return salt;
    }
    
    public static String removeSalt(String args){  	
    	if(args == null || args.length() <=10){
    		return null;
    	}
    	
    	return args.substring(10,args.length());
    }
    
	public static Object deserializeBytes(byte[] bytes) throws IOException, ClassNotFoundException
	{
	    ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
	    ObjectInputStream ois = new ObjectInputStream(bytesIn);
	    Object obj = ois.readObject();
	    ois.close();
	    
	    return obj;
	}


	public static byte[] serializeObject(Object obj) throws IOException
	{
	    ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(bytesOut);
	    oos.writeObject(obj);
	    oos.flush();
	    byte[] bytes = bytesOut.toByteArray();
	    bytesOut.close();
	    oos.close();
	    return bytes;
	}
    
    public static void main(String args[]){
    	String hash = gerarSalt();
    	
    	System.out.println(hash);
    }
}
