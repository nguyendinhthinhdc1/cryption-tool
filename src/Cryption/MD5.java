/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cryption;

/**
 *
 * @author COMPUTER
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class MD5 {
    
    public static String getHash(File file) throws FileNotFoundException, IOException { 

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] inputBytes = new byte[(int)file.length()];
            fileInputStream.read(inputBytes);
            
            // Create MD5 Hash 
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5"); 
            digest.update(inputBytes); 
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++) {
                    String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
        }
        return "";

   }
    
}
