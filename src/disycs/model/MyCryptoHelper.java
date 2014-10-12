package disycs.model;

import java.security.NoSuchAlgorithmException;

public class MyCryptoHelper {
	private static byte[] computeHash(String x)    
	  {
	     java.security.MessageDigest d =null;
	     try {
			d = java.security.MessageDigest.getInstance("SHA-1");
			d.reset();
		    d.update(x.getBytes());
		    return  d.digest();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	     
	  }
	  
	private static String byteArrayToHexString(byte[] b){
	     StringBuffer sb = new StringBuffer(b.length * 2);
	     for (int i = 0; i < b.length; i++){
	       int v = b[i] & 0xff;
	       if (v < 16) {
	         sb.append('0');
	       }
	       sb.append(Integer.toHexString(v));
	     }
	     return sb.toString().toUpperCase();
	  }
	
	public static String computePasswordHash(String password){
		return byteArrayToHexString(computeHash(password));
	}
}
