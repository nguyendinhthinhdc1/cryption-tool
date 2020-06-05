/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cryption;

import GUI.*;
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

    public void decryptor(File src, File dst,File key,GUI.Form.algorithm alg,GUI.Form form)
    {
        if (!dst.exists())
                dst.mkdir();
        if (!dst.isDirectory())
                return;
        try
        {
            if (!src.isDirectory())
            {
                form.updateAreaText("Decryting...");
                copyDecrypted(src, dst,key,alg);
                if(deleteOriginal) src.delete();
                System.out.println("1 files is decrytped");
            } else
            {
                File[] files = src.listFiles();
                form.updateAreaText("Decryting...");

                for (File f : files)
                {
                    copyDecrypted(f, dst,key,alg);
                    if(deleteOriginal) f.delete();
                }
                if(files.length==1)
                {
                    form.updateAreaText(" 1 files is decrypted");
                }
                else{
                    form.updateAreaText(files.length + " files are decrypted");
                }
            }
        } catch (IOException e)
        {
                e.printStackTrace();
        }
    }

    public void copyDecrypted(File source, File dest,File key,GUI.Form.algorithm alg) throws IOException
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
            if(alg.equals(Form.algorithm.aes)){
                os.write(AES.decrypt(buffer, genKey));
            }
            else
            {
                os.write(DES.decrypt(buffer, genKey));
            }
            
            

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
