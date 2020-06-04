/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
/**
 *
 * @author COMPUTER
 */
public class Decrypt
{
    private static Decrypt decrypter = new Decrypt();
    private static boolean deleteOriginal;	
    private Decrypt()
    {
    }

    public static Decrypt getDecrypter(boolean originalFileDeleted)
    {
        deleteOriginal = originalFileDeleted;		
        return decrypter;
    }

    public void decryptor(File src, File dst,File key)
    {
        if (!dst.exists())
                dst.mkdir();
        if (!dst.isDirectory())
                return;
        try
        {
            if (!src.isDirectory())
            {
                copyDecrypted(src, dst,key);
                System.out.println("Decryting...");
                if(deleteOriginal) src.delete();
                System.out.println("1 files is decrytped");
            } else
            {
                File[] files = src.listFiles();
                System.out.println("Decryting...");

                for (File f : files)
                {
                    copyDecrypted(f, dst,key);
                    if(deleteOriginal) f.delete();
                }
                System.out.println(files.length + " files are decrytped");
            }
        } catch (IOException e)
        {
                e.printStackTrace();
        }
    }

    public void copyDecrypted(File source, File dest,File key) throws IOException
    {

        FileInputStream fis = new FileInputStream(key);
        byte[] keyBytes = new byte[fis.available()];
        fis.read(keyBytes);
        fis.close();
        
        String genKey= new String(keyBytes);    
        InputStream is = null;
        OutputStream os = null;

        try
        {
            is = new FileInputStream(source);
            int sizename = is.read() * 2;
            byte[] name = new byte[sizename];
            is.read(name);
            String fileName = bytesToString(name);
            os = new FileOutputStream(dest.getPath().concat("/").concat(fileName));
            byte[] buffer = new byte[(int)source.length()-sizename-1];
            System.out.println(source.length());
            System.out.println(sizename);
            
            is.read(buffer);
            os.write(AES.decrypt(buffer, genKey));
            
            

        } finally
        {
            is.close();
            os.close();
        }
    }

    public String bytesToString(byte[] data)
    {
            StringBuilder res = new StringBuilder();

            for (int i = 0; i < data.length / 2; i++)
            {
                    char c = (char) ((data[i * 2] << 8) | data[i * 2 + 1]);
                    res.append(c);
            }
            return res.toString();
    }
   

}
