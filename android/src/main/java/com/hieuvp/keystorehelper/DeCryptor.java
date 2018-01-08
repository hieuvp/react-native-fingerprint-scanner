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
import java.security.cert.CertificateException;
import java.security.UnrecoverableEntryException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class DeCryptor {

  private static final String TRANSFORMATION = "AES/GCM/NoPadding";
  private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

  private KeyStore keyStore;

  public DeCryptor() throws CertificateException, NoSuchAlgorithmException, KeyStoreException,
          IOException {
      initKeyStore();
  }

  private void initKeyStore() throws KeyStoreException, CertificateException,
          NoSuchAlgorithmException, IOException {
      keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
      keyStore.load(null);
  }

  public Cipher getCipher(final String alias, final byte[] encryptionIv) {
    try {
        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        final GCMParameterSpec spec = new GCMParameterSpec(128, encryptionIv);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec);
        return cipher;
    } catch (Throwable e) {
        return null;
    }
  }

  public String decryptData(final Cipher cipher, final byte[] encryptedData)
          throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
          NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
          BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

      return new String(cipher.doFinal(encryptedData), "UTF-8");
  }

  private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
          UnrecoverableEntryException, KeyStoreException {
      return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();
  }
}