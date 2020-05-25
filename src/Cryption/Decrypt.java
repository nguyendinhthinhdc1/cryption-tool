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
        String genKey="";
        try {
            Scanner myReader = new Scanner(key);
            while (myReader.hasNextLine()) {
            genKey = myReader.nextLine();
            }
             myReader.close();
        } catch (FileNotFoundException e) {
             System.out.println("An error occurred.");
             e.printStackTrace();
        }
        
        InputStream is = null;
        OutputStream os = null;

        try
        {
            is = new FileInputStream(source);
            byte[] buffer = new byte[1024];
            byte[] name = new byte[is.read() * 2];
            is.read(name);
            String fileName = bytesToString(name);
            os = new FileOutputStream(dest.getPath().concat("/").concat(fileName));
            int length;
            while ((length = is.read(buffer)) > 0)
            {
                decryptBytes(buffer,genKey);
                os.write(buffer, 0, length);
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

    private void decryptBytes(byte[] data,String aesKey) // Decryption Algorithm is written into here
    {
        String cipher = new String(data);
        String plain = AES.encrypt(cipher, aesKey);
        data = plain.getBytes();
            
    }

    public void copy(File source, File dest) throws IOException
    {
        InputStream is = null;
        OutputStream os = null;

        try
        {
            dest = new File(dest.getPath().concat("/").concat(source.getName()));

            is = new FileInputStream(source);
            os = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            int tl = 0;

            while ((length = is.read(buffer)) > 0)
            {
                tl += length;
                os.write(buffer, 0, length);
            }

            System.out.println(tl + " bytes");
        } finally
        {
            is.close();
            os.close();
        }
    }
}
