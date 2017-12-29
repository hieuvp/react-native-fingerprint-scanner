package main.java.com.hieuvp.fingerprint.fingerprint;

import android.content.Context;
import android.os.Build;
import android.support.v4.os.CancellationSignal;

import com.wei.android.lib.fingerprintidentify.aosp.FingerprintManagerCompat;
import com.wei.android.lib.fingerprintidentify.aosp.FingerprintManagerCompat.CryptoObject;
import com.wei.android.lib.fingerprintidentify.impl.AndroidFingerprint;

public class SecureAndroidFingerprint extends AndroidFingerprint {

  private CancellationSignal mCancellationSignal;
  private FingerprintManagerCompat mFingerprintManagerCompat;
  private CryptoObject mCryptoObject;

  public SecureAndroidFingerprint(Context context, CryptoObject cryptoObject, FingerprintIdentifyExceptionListener exceptionListener) {
      super(context, exceptionListener);

      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
          return;
      }

      try {
          mFingerprintManagerCompat = FingerprintManagerCompat.from(context);
          setHardwareEnable(mFingerprintManagerCompat.isHardwareDetected());
          setRegisteredFingerprint(mFingerprintManagerCompat.hasEnrolledFingerprints());
      } catch (Throwable e) {
          onCatchException(e);
      }

      mCryptoObject = cryptoObject;
  }

  @Override
  protected void doIdentify() {
      try {
          mCancellationSignal = new CancellationSignal();
          mFingerprintManagerCompat.authenticate(mCryptoObject, 0, mCancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
              @Override
              public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                  super.onAuthenticationSucceeded(result);
                  onSucceed();
              }

              @Override
              public void onAuthenticationFailed() {
                  super.onAuthenticationFailed();
                  onNotMatch();
              }

              @Override
              public void onAuthenticationError(int errMsgId, CharSequence errString) {
                  super.onAuthenticationError(errMsgId, errString);
                  onFailed(errMsgId == 7); // FingerprintManager.FINGERPRINT_ERROR_LOCKOUT
              }
          }, null);
      } catch (Throwable e) {
          onCatchException(e);
          onFailed(false);
      }
  }
}