package br.com.marrs.ischool.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Daniel Souza de lima e-mail:daniesouza@gmail.com
 * 
 */

public class MD5CheckSum {

	public static String gerarCodigoHash(String senha) throws NoSuchAlgorithmException{
		
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");  
		messageDigest.update(senha.getBytes(),0, senha.length());  
		String hashedPass = new BigInteger(1,messageDigest.digest()).toString(16);  
		if (hashedPass.length() < 32) {
		   hashedPass = "0" + hashedPass; 
		}
		
		return hashedPass;
	}
	
	public static byte[] createChecksum(String filename) throws Exception {
		InputStream fis =  new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}
	
	public static byte[] createChecksum(byte[] file) throws Exception {
		MessageDigest complete = MessageDigest.getInstance("MD5");
		complete.update(file);
		return complete.digest();
	}	

	public static String getMD5Checksum(byte[] file) throws Exception {
		byte[] b = createChecksum(file);
		String result = "";
		for (int i=0; i < b.length; i++) {
			result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}	
	
	public static String getMD5Checksum(String filename) throws Exception {
		byte[] b = createChecksum(filename);
		String result = "";
		for (int i=0; i < b.length; i++) {
			result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}
	
	public static void main(String args[]) {
		try {
			System.out.println(gerarCodigoHash("Eu sou foda joe matador pqp aahahahah aha hah ahaha haha hah"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
