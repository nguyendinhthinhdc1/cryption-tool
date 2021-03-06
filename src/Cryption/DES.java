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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
 
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
 
public class DES {
 
    private static SecretKeySpec secretKey;
    private static byte[] key;
 
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 8); 
            secretKey = new SecretKeySpec(key, "DES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static byte[] encrypt(byte[] bytesToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            //SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            //return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            return cipher.doFinal(bytesToEncrypt);
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public static byte[] decrypt(byte[] bytesToDecrypt, String secret) 
    {
        try
        {
            setKey(secret);
            //SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            //return new String(cipher.doFinal(Base64.getMimeDecoder().decode(strToDecrypt)));
            return cipher.doFinal(bytesToDecrypt);
            
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
