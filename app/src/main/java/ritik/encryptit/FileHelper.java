package ritik.encryptit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by SuperUser on 31-12-2017.
 */

public class FileHelper {
    Context context;


    public FileHelper(Context context) {
        this.context = context;
    }


    public static void encryptfile(FileInputStream fileInputStream, FileOutputStream fileOutputStream, String password, String salt) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

        byte[] key = (salt + password).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        SecretKeySpec sks = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        CipherOutputStream cos = new CipherOutputStream(fileOutputStream, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = fileInputStream.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        cos.flush();
        cos.close();
        fileInputStream.close();
    }

    public static void decrypt(FileOutputStream fileOutputStream, FileInputStream fileInputStream, String password, String salt) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        byte[] key = (salt + password).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        SecretKeySpec sks = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fileInputStream, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fileOutputStream.write(d, 0, b);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        cis.close();
    }


    public void gen_config_file(config config) throws IOException {

        Gson gson = new Gson();
        String json_config = gson.toJson(config);
        FileOutputStream fos = context.openFileOutput(".config", Context.MODE_PRIVATE);
        fos.write(json_config.getBytes());
        fos.close();
    }

    public void zip(FileInputStream[] fileInputStreams, FileOutputStream fileOutputStream) throws IOException {

        BufferedInputStream origin = null;

        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
        byte data[] = new byte[8];

        for (int i = 0; i < fileInputStreams.length; i++) {
            Log.d("Ritik", "zip: adding file " + i);

            origin = new BufferedInputStream(fileInputStreams[i], 8);
            ZipEntry entry = new ZipEntry("file" + i);
            out.putNextEntry(entry);
            int count;

            while ((count = origin.read(data, 0, 8)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
        }

        out.close();

    }


    public void unzip(FileInputStream fileInputStream, FileOutputStream fileOutputStream, int file) throws IOException {

        //TODO check this function


        ZipInputStream zin = new ZipInputStream(fileInputStream);
        ZipEntry ze = null;
        for (int i = 0; i < file; i++) {
            zin.getNextEntry();
            zin.closeEntry();

        }
        if ((ze = zin.getNextEntry()) != null) {
            for (int c = zin.read(); c != -1; c = zin.read()) {
                fileOutputStream.write(c);
            }
            zin.closeEntry();
            fileOutputStream.close();

        }
        zin.close();


    }

}