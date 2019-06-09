import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class RadiusAttack {
	
	
	private static String xorHex(String a, String b) {
	    // TODO: Validation
	    char[] chars = new char[a.length()];
	    for (int i = 0; i < chars.length; i++) {
	        chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
	    }
	    return new String(chars);
	}

	private static byte[] xor(byte[] a, byte[] b) {
		byte[] c = new byte[b.length];
		
		int i = 0;
		for (byte b1 : a)
		    c[i] = (byte) (b1 ^  b[i++]);
		
		return c;
	    
	}

	
	private static int fromHex(char c) {
	    if (c >= '0' && c <= '9') {
	        return c - '0';
	    }
	    if (c >= 'A' && c <= 'F') {
	        return c - 'A' + 10;
	    }
	    if (c >= 'a' && c <= 'f') {
	        return c - 'a' + 10;
	    }
	    throw new IllegalArgumentException();
	}

	private static char toHex(int nybble) {
	    if (nybble < 0 || nybble > 15) {
	        throw new IllegalArgumentException();
	    }
	    return "0123456789ABCDEF".charAt(nybble);
	}
	
	 public static String convertAsciiHex(String rawIn)
	 {
	   /**
	    * convert ascii string to HEX string
	    */
	    String rawBin ="";
	    try
	    {  
	       char [] chars = rawIn.toCharArray(); //convert string to individual chars
	       for(int j=0; j<chars.length; j++)
	       {
		 rawBin += Integer.toHexString(chars[j]);  //convert char to hex value
	       }
	    }  
	    catch(NumberFormatException e)
	    {
		return rawIn; 
	    }
	    return rawBin;
	}
	
	private static byte[] md5Hash(byte[] password) throws NoSuchAlgorithmException
	{
		    MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(password);
	        
	        byte byteData[] = md.digest();
	        
	        //convert the byte to hex 
	        StringBuffer hexString = new StringBuffer();
	    	for (int i=0;i<byteData.length;i++) {
	    		String hex=Integer.toHexString(0xff & byteData[i]);
	   	     	if(hex.length()==1) hexString.append('0');
	   	     	hexString.append(hex);
	    	}
	    	//System.out.println("Digest(in hex format):: " + hexString.toString()); 
		return byteData;
		
	}
	private static String md5HashHex(byte[] password) throws NoSuchAlgorithmException
	{
		    MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(password);
	        
	        byte byteData[] = md.digest();
	        
	        //convert the byte to hex 
	        StringBuffer hexString = new StringBuffer();
	    	for (int i=0;i<byteData.length;i++) {
	    		String hex=Integer.toHexString(0xff & byteData[i]);
	   	     	if(hex.length()==1) hexString.append('0');
	   	     	hexString.append(hex);
	    	}
	    	//System.out.println("Digest(in hex format):: " + hexString.toString()); 
		return hexString.toString();
		
	}
	
	public static byte[] hexStringToByteArray(String s) {
		//if (value < 0) value += 256
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	       
	       
	       
	    }
	    return data;
	}
	
    public static void main(String[] args)throws Exception
    {//String userPassword=convertAsciiHex("chequohpingathuu");
      //User-Password (encrypted): 4a94bb35c146aad7696346ebc65aec00
    	String userPasswordAttr="12ad6fdfdc5cf772521b069c96d6e4dc";//"4a94bb35c146aad7696346ebc65aec00";
      String requestAuthenticator="b8e4467714e29aa4d3d5af0457d7d6d2";//8c7403a7641fab16c3ef651d45a3e4f4";
      //String responseAuthenticator="bf152ed3675746711feef62e8a7f6584";

        FileInputStream fis = null;
        try {
                fis = new FileInputStream("C://Users//UMAIR//workspace//NetSec//src//Dictionary.txt");
        } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line = null;
        String[] words;
        try {                  
		//read each word from the dictionary file//
                while ((line = br.readLine()) != null)
                {
                    //getting the exact word without extra white spaces//
                    words=      line.split("\\s+");
                    for (String word :words)
                    { //System.out.println("chequohpingathuu".getBytes().length+"  "+md5Hash(word+requestAuthenticator).length);
                    
                     
                     // String md5=md5Hash(word+requestAuthenticator);
                      //String passwd= xorHex(userPassword,convertAsciiHex("chequohpingathuu"));
                    	// System.out.println(userPassword);
                       //System.out.println(passwd+"   "+md5);
                    	//if(res.equalsIgnoreCase(userPasswordAttr))
                    byte[] array1and2 = new byte[word.getBytes(StandardCharsets.UTF_8).length + hexStringToByteArray(requestAuthenticator).length];
                    System.arraycopy(word.getBytes(StandardCharsets.UTF_8), 0, array1and2, 0, word.getBytes().length);
                     System.arraycopy(hexStringToByteArray(requestAuthenticator), 0, array1and2, word.getBytes().length, hexStringToByteArray(requestAuthenticator).length);
                      
                    	
                    	//try
                    	
                    	 //byte[] array1and2 = new byte[hexStringToByteArray(requestAuthenticator).length+word.getBytes(StandardCharsets.UTF_8).length ];
                         //System.arraycopy(hexStringToByteArray(requestAuthenticator), 0, array1and2, 0,hexStringToByteArray(requestAuthenticator).length);
                          //System.arraycopy(word.getBytes(StandardCharsets.UTF_8), 0, array1and2, hexStringToByteArray(requestAuthenticator).length, word.getBytes(StandardCharsets.UTF_8).length);
                           //word.getBytes(StandardCharsets.UTF_8)
                           //hexStringToByteArray(requestAuthenticator)                    	
                    	
                      //  System.out.println(word+" "+requestAuthenticator+ " "+md5HashHex(array1and2) );
                    	
                    	
                    	//ArrayUtils.addAll(word.getBytes(),hexStringToByteArray(requestAuthenticator));
                     // System.out.println(Arrays.toString(word.getBytes(StandardCharsets.UTF_8))+"  "+Arrays.toString(hexStringToByteArray(requestAuthenticator))+" ->"+Arrays.toString(array1and2));
                      
                     // System.out.println(Arrays.toString(xor("chequohpingathuu".getBytes(), "chequohpingathuu".getBytes())));
                      byte[] res=xor("chequohpingathuu".getBytes(StandardCharsets.UTF_8), md5Hash(array1and2));
                     // System.out.println(Arrays.toString(userPasswordAttr.getBytes()));
                      //System.out.println(Arrays.toString(hexStringToByteArray(userPasswordAttr)));
                      //System.out.println(Arrays.toString(res));
                     // System.out.println();
                      //byte[] res1=xor("chequohpingathuu".getBytes(StandardCharsets.UTF_8), hexStringToByteArray(userPasswordAttr));
                      //System.out.println(Arrays.toString(res1));
                      byte[] md5 = md5Hash((word+requestAuthenticator).getBytes());
                      byte[] xor={113, -59, 10, -82, -87, 51, -97, 2, 59, 117, 97, -3, -30, -66, -111, -87};
                      
                      if(Arrays.toString(xor).equals(Arrays.toString(md5)) )
                    		  {
                    	      System.out.println(word);
                    		  }
                          
                      
                      if(Arrays.toString(res).equals(Arrays.toString(hexStringToByteArray(userPasswordAttr)))) 
                    //  if(Arrays.toString(res).equals(Arrays.toString(userPasswordAttr.getBytes())) )
                      {
                        	System.out.println("key = "+word);
                        	//break;
                        }
                    }
                }
     
              //  System.out.println(xorHex(md5Hash("123456"),md5Hash("123456") ));
           }
        catch (IOException e1)
           {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            }
    	
     
}
}
//http://www.mkyong.com/java/java-md5-hashing-example/
//http://stackoverflow.com/questions/9840675/xor-hex-string-in-java-of-different-length
/*
String to Hex:

Integer.decode(hexString);
Hex to String:

Integer.toHexString(integer);
*/