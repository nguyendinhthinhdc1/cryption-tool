/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.File;
import Cryption.*;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Cryption.*;
/**
 *
 * @author COMPUTER
 */
public class Main {
    public static void main(String[] args)
    {
        Encrypt en = Encrypt.getEncrypter(false);
        Decrypt de = Decrypt.getDecrypter(false);

        File src = new File("F:/192/test/file/assignment1.pdf");
        File dst = new File("F:/192/test/encrypt");
        File key = new File("F:/192/test/key.txt");
        File src1 = new File("F:/192/test/encrypt");

        //en.encryptor(src, dst,key);
        de.decryptor(src1, dst, key);
        
        
    }
}
