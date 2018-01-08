package main.java.com.hieuvp.keystorehelper;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class EnCryptor {

  private static final String TRANSFORMATION = "AES/GCM/NoPadding";
  private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

  private byte[] encryption;
  private byte[] iv;

  public EnCryptor() {
  }

  public byte[] encryptText(final Cipher cipher, final String textToEncrypt)
          throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
          NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
          InvalidAlgorithmParameterException, SignatureException, BadPaddingException,
          IllegalBlockSizeException {

      return (encryption = cipher.doFinal(textToEncrypt.getBytes("UTF-8")));
  }

  public Cipher getCipher(final String alias) {
    try {
        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias, true));

        iv = cipher.getIV();
        return cipher;
    } catch (Throwable e) {
        return null;
    }
  }

  private SecretKey getSecretKey(final String alias, boolean invalidatedByBiometricEnrollment) throws NoSuchAlgorithmException,
          NoSuchProviderException, InvalidAlgorithmParameterException {

      final KeyGenerator keyGenerator = KeyGenerator
              .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);

    KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(alias,
              KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
              .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
              .setUserAuthenticationRequired(true)
              .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
        }
        keyGenerator.init(builder.build());

      return keyGenerator.generateKey();
  }

  public byte[] getEncryption() {
      return encryption;
  }

  public byte[] getIv() {
      return iv;
  }
}