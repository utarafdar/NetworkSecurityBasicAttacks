import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
public class CrackPassword {

        public static void main(String[] args) {
                // TODO Auto-generated method stub
                // read words from dictionary file//


                FileInputStream fis = null;
                try {
                        fis = new FileInputStream("dictionary.txt");
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
                            {
                            //  System.out.print(word+"::");

                                 //linux generate hash //
                                String command = "openssl passwd -apr1 -salt /pE9u4cQ "+word;
                                    String pswd="";
                                          try {

                                      Process proc = Runtime.getRuntime().exec(command);
                                      BufferedReader stdInput = new BufferedReader(new
                                      InputStreamReader(proc.getInputStream()));
                                      String s = null;
									  //store the hash in a string for comparision later//
                                      while ((s = stdInput.readLine()) != null)
                                        {
                                          pswd=s;
                                        }
                                      }
                                          catch (IOException e) {
                                      System.out.println( "exception");
                                      e.printStackTrace();
                                  }
                        //                System.out.print(pswd+"::::");
						//compare the hash generated with the hash corresponding to netsec user's password hash//
                                          if (pswd.equals("$apr1$/pE9u4cQ$ZfQfXfZ8NWh2gfFpIx22T0"))
                                          {
                                                  System.out.println("cracked");
                                                  System.out.println("password is :"+word);
                                                  System.exit(0);
                                          }





                            }
                          //  System.out.println();


                        }
                } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                }

                try {
                        br.close();
                } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                }

           }// main function

                }


