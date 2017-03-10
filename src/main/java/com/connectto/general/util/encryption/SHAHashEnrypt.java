package com.connectto.general.util.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by htdev001 on 5/27/14.
 */
public class SHAHashEnrypt {
    /*public static void main(String[] args) throws EncryptException {
        String passwordToHash = "password";

        String securePassword = get_MD5_SecurePassword(passwordToHash);
        System.out.println(securePassword+ " , "+securePassword.length());

        securePassword = get_SHA_1_SecurePassword(passwordToHash);
        System.out.println(securePassword+ " , "+securePassword.length());

        securePassword = get_SHA_256_SecurePassword(passwordToHash);
        System.out.println(securePassword+ " , "+securePassword.length());

        securePassword = get_SHA_384_SecurePassword(passwordToHash);
        System.out.println(securePassword+ " , "+securePassword.length());

        securePassword = get_SHA_512_SecurePassword(passwordToHash);
        System.out.println(securePassword+ " , "+securePassword.length());
    }*/

    public static String get_MD5_SecurePassword(String passwordToHash) throws EncryptException {

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(passwordToHash.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }


    }

    public static String get_MD5_SecurePassword(String passwordToHash, String salt) throws EncryptException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }
    }


    public static String get_SHA_1_SecurePassword(String passwordToHash) throws EncryptException {
        try {
            String salt = getSalt();
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }
    }

    public static String get_SHA_256_SecurePassword(String passwordToHash) throws EncryptException {
        try {
            String salt = getSalt();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }
    }

    public static String get_SHA_384_SecurePassword(String passwordToHash) throws EncryptException {
        try {
            String salt = getSalt();
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }

    }

    public static String get_SHA_512_SecurePassword(String passwordToHash) throws EncryptException {
        try {
            String salt = getSalt();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }
    }

    //Add salt
    public static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }


    public static void main(String[] args) {
        try {
            String md5 = get_MD5_SecurePassword("1");
            ;
            System.out.println(getSalt());
        } catch (EncryptException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
