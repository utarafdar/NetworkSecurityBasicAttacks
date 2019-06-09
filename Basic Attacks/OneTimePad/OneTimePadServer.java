import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 
public class OneTimePadServer
{
 
    private static Socket socket;
    
    
    
    public static String encrypt(String msg) throws FileNotFoundException, UnsupportedEncodingException
    {   //generate random variable using linux /dev/random
    	//linux generate hash //
        String command = "od -An -N2 -i /dev/random";
            String randomNumber="";
                  try {

              Process proc = Runtime.getRuntime().exec(command);
              BufferedReader stdInput = new BufferedReader(new
              InputStreamReader(proc.getInputStream()));
              String s = null;
			  //store the hash in a string for comparision later//
              while ((s = stdInput.readLine()) != null)
                {
            	  randomNumber=s;
                }
              }
                  catch (IOException e) {
              System.out.println( "exception");
              e.printStackTrace();
          }
            
           //store the random number in one time pad
           //in this case a local file
                  PrintWriter writer = new PrintWriter("onetimepad.txt", "UTF-8");
                  writer.println(randomNumber);                 
                  writer.close();      
                  System.out.println(randomNumber);
          //get MD5 hash of the random number
                  String md5 ="";
            try {
				 md5=md5Hash(randomNumber) ;
				 System.out.println("md5 "+md5);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}     
            //convert message to hex string
         String   hexstr=convertAsciiHex(msg);
            //xor the md5 of random variable  and message 
            //to get final encrytped message.
         System.out.println("encrypted hex "+hexstr);
                 String encryptedMessage = xorHex(hexstr, md5); 
    	return encryptedMessage;
    }
    
    public static String decrypt(String msg)
    { //first read random number from OTP
    	//i.e read file
    	FileInputStream fis = null;
        try {
                fis = new FileInputStream("onetimepad.txt");
        } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        String randomNumber ="";
        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        try {
			 randomNumber = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//get md5 of random number
        String md5 ="";
        try {System.out.println("random number "+randomNumber.replace("\n", "").replace("\r", "").replace("\\s+", ""));
			 md5=md5Hash(randomNumber) ;
			 System.out.println("md5 "+md5 );
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
        //xor md5 with encrypted message
        String decryptedHex = xorHex(msg, md5);
        System.out.println("decrytped hex "+decryptedHex );
        //convert the hexstring to string
        //to finally decrypt the message
        
    	return HexString2ASCII(decryptedHex);
    }
    
    public static String HexString2ASCII(String input)
    {
    	StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i+=2) {
            String str = input.substring(i, i+2);
            output.append((char)Integer.parseInt(str, 16));
        }
        return output.toString();
    }
    
    
    public static String xorHex(String a, String b) {
	    // TODO: Validation
	    char[] chars = new char[a.length()];
	    for (int i = 0; i < chars.length; i++) {
	        chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
	    }
	    return new String(chars);
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
    
    
    public static String md5Hash(String password) throws NoSuchAlgorithmException
	{
		    MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(password.getBytes());
	        
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

    
    
    
    public static void main(String[] args)
    {
        try
        {
 
            int port = 25000;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port 25000");
 
            //Server is running always. This is done using this while(true) loop
            while(true)
            {
                //Reading the message from the client
                socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String number = br.readLine();
                System.out.println("Message received from client is "+number);
                System.out.println("decryption "+decrypt(number));
 
                
                String returnMessage=encrypt("hello OTP client");
                System.out.println("message to be sent is : hello OTP client");
 
                //Sending the response back to the client.
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(returnMessage+"\n");
                System.out.println("Message sent to the client is "+returnMessage);
                bw.flush();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch(Exception e){}
        }
    }
}
//http://www.careerbless.com/samplecodes/java/beginners/socket/SocketBasic1.php
//http://www.mkyong.com/java/java-md5-hashing-example/
//http://stackoverflow.com/questions/2885173/how-to-create-a-file-and-write-to-a-file-in-java
//http://stackoverflow.com/questions/4785654/convert-a-string-of-hex-into-ascii-in-java
