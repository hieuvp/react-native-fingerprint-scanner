package main.java.com.hieuvp.keystorehelper;

import javax.crypto.Cipher;
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

  public byte[] encryptText(final String alias, final String textToEncrypt)
          throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
          NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
          InvalidAlgorithmParameterException, SignatureException, BadPaddingException,
          IllegalBlockSizeException {

      final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));

      iv = cipher.getIV();

      return (encryption = cipher.doFinal(textToEncrypt.getBytes("UTF-8")));
  }

  private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
          NoSuchProviderException, InvalidAlgorithmParameterException {

      final KeyGenerator keyGenerator = KeyGenerator
              .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);

      keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
              KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
              .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
              // .setUserAuthenticationRequired(true)
              .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
              .build());

      return keyGenerator.generateKey();
  }

  public byte[] getEncryption() {
      return encryption;
  }

  public byte[] getIv() {
      return iv;
  }
}