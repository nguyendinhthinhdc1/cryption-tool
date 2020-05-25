/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cryption;


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
//    private static byte[] key=null;
//    public static File keyFile;
    
    private Encrypt() 
    {
       // this.key = getKey(keyFile);
    }
//    public static byte[] getKey(File file) throws IOException
//    {
//        byte[] buff = new byte[1024];
//        try {
//            InputStream is = null;
//            is = new FileInputStream(file);
//            is.read(buff);
//           
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Encrypt.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return buff;
//    }
        
    
    public static Encrypt getEncrypter(boolean originalFileDeleted)
    {
        deleteOriginal = originalFileDeleted;
        return encrypter;
    }
    public void encryptor(File src, File dst,File key)
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
                copyEncrypted(src, dst,key);
                System.out.println("Encrypting...");
                if(deleteOriginal) src.delete();
                System.out.println(" 1 files is encrypted");
            } else
            {
                File[] files = src.listFiles();
                System.out.println("Encrypting...");
                for (File f : files)
                {
                    copyEncrypted(f, dst,key);
                    if(deleteOriginal) f.delete();
                }
                System.out.println(files.length + " files are encrypted");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void copyEncrypted(File source, File dest,File key) throws IOException
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
        //InputStream isKey = null;
        OutputStream os = null;
        dest = new File(dest.getPath().concat("/").concat(getRandomName(10, "sopiro")));
        
        try
        {
            is = new FileInputStream(source);
           // isKey = new FileInputStream(key);
            os = new FileOutputStream(dest);

            os.write(new byte[] { (byte) source.getName().length() });
            os.write(stringToByte(source.getName()));

            byte[] buffer = new byte[1024];
    
            int length;
            
            while ((length = is.read(buffer)) > 0)
            {
                encryptBytes(buffer,genKey);
                os.write(buffer, 0, length);
            }

        } finally
        {
            is.close();
            os.close();
        }
    }

    private void encryptBytes(byte[] data,String aesKey) // Encryption Algorithm is written into here
    {
        
        String plainText = new String(data);
        String cipher = AES.encrypt(plainText, aesKey);
        data = cipher.getBytes();
              
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
