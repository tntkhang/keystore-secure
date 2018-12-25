package com.github.tntkhang.keystore_secure;

import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

import khangtran.preferenceshelper.PrefHelper;

/**
 * Created by KhangTran on 3/31/2017.
 */

public class KeystoreSecure {
    private static KeystoreSecure instance;

    private static final boolean IS_M = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String RSA_MODE =  "RSA/ECB/PKCS1Padding";

    private static KeyStore keyStore;

    public static KeystoreSecure init(Context context) {
        if (instance == null) {
            instance = new KeystoreSecure(context);
        }
        return instance;
    }

    private KeystoreSecure(Context context) {
        PrefHelper.initHelper(context);
    }

    public static void encrypt(Context context, String key, String value) {
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);

            KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEY_STORE);
            if (IS_M) {
                KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(key,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                        .setRandomizedEncryptionRequired(false)
                        .build();

                kpg.initialize(spec);
            } else {
                // Generate a key pair for encryption
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 30);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                        .setAlias(key)
                        .setSubject(new X500Principal("CN=" + key))
                        .setSerialNumber(BigInteger.TEN)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();

                kpg.initialize(spec);
            }
            kpg.generateKeyPair();
            save(key, value);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String decrypt(String key) {
        return get(key);
    }


    private static String get(String key) {
        String encryptedKeyB64 = PrefHelper.getStringVal(key, null);
        try {
            byte[] encryptedKey = Base64.decode(encryptedKeyB64, Base64.DEFAULT);
            return new String(rsaDecrypt(encryptedKey, key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void save(String key, String value) {
        try {
            byte[] secureByte = value.getBytes();
            byte[] byteEncrypted = rsaEncrypt(secureByte, key);
            String encryptedKey = Base64.encodeToString(byteEncrypted, Base64.DEFAULT);
            PrefHelper.setVal(key, encryptedKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] rsaEncrypt(byte[] secret, String key) throws Exception {
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(key, null);
        Cipher inputCipher = Cipher.getInstance(RSA_MODE);
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
        cipherOutputStream.write(secret);
        cipherOutputStream.close();

        return outputStream.toByteArray();
    }

    private static byte[] rsaDecrypt(byte[] encrypted, String key) throws Exception {
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(key, null);
        Cipher output = Cipher.getInstance(RSA_MODE);
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(encrypted), output);
        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte)nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i);
        }
        return bytes;
    }
}
