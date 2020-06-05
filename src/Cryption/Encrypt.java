/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cryption;


import GUI.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author COMPUTER
 */
public class Encrypt
{
    private static Encrypt encrypter = new Encrypt();
    private static boolean deleteOriginal;
    //Form.algorithm alg ;
//    private static byte[] key=null;
//    public static File keyFile;
    
    private Encrypt() 
    {
    }

    public static Encrypt getEncrypter(boolean originalFileDeleted)
    {
        deleteOriginal = originalFileDeleted;
        return encrypter;
    }

    public void encryptor(File src, File dst,File key,GUI.Form.algorithm alg,GUI.Form form )
    {
     
        if (!dst.exists())
            dst.mkdir();
        if (!dst.isDirectory())
            return;
        try
        {
            if (!src.isDirectory())
            {
                //File newFile = src.getAbsoluteFile();
                form.updateAreaText("Crypting.....");
                copyEncrypted(src, dst,key,alg);     
                if(deleteOriginal) src.delete();
                form.updateAreaText(" 1 files is encrypted");
            } else
            {
                File[] files = src.listFiles();
                form.updateAreaText("Encrypting...");
                for (File f : files)
                {
                    copyEncrypted(f, dst,key,alg);
                    if(deleteOriginal) f.delete();
                }
                if(files.length==1)
                {
                    form.updateAreaText(" 1 files is encrypted");
                }
                else{
                    form.updateAreaText(files.length + " files are encrypted");
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void copyEncrypted(File source, File dest,File key,GUI.Form.algorithm alg) throws IOException
    {

        FileInputStream fis = new FileInputStream(key);
        byte[] keyBytes = new byte[fis.available()];
        fis.read(keyBytes);
        fis.close();
        String genKey= new String(keyBytes);
        InputStream is = null;
        OutputStream os = null;
        dest = new File(dest.getPath().concat("/").concat(getRandomName(10, "crypt")));
        
        try
        {
            is = new FileInputStream(source);
           // isKey = new FileInputStream(key);
            os = new FileOutputStream(dest);
            os.write(new byte[] { (byte) source.getName().length() });
            os.write(stringToByte(source.getName()));

            byte[] buffer = new byte[(int)source.length()];
            is.read(buffer);
            if(alg.equals(Form.algorithm.aes)){
                os.write(AES.encrypt(buffer, genKey));
            }
            else
            {
                os.write(DES.encrypt(buffer, genKey));
            }

        } finally
        {
            is.close();
            os.close();
        }
    }

    public byte[] stringToByte(String data)
    {
        char[] ca = data.toCharArray();
        byte[] res = new byte[ca.length * 2]; // Character.BYTES = 2;
        for (int i = 0; i < res.length; i++)
        {
            res[i] = (byte) ((ca[i / 2] >> (8 - (i % 2) * 8)) & 0xff);
        }
        return res;
    }
    
    
    
    public String getRandomName(int length, String extend)
    {
        Random r = new Random();
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < length; i++)
        {

            char c = 'a';
            int width = 'z' - 'a';
            if (r.nextInt(3) == 0)
            {
                c = 'A';
                width = 'Z' - 'A';
            }
            if (r.nextInt(3) == 1)
            {
                c = '0';
                width = '9' - '0';
            }
            res.append((char) (c + r.nextInt(width)));
        }
        res.append(".").append(extend);
        return res.toString();
    }

}
