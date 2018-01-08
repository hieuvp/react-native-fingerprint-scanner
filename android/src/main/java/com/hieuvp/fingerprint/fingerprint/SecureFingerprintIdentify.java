package main.java.com.hieuvp.fingerprint.fingerprint;

import android.content.Context;

import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;
import com.wei.android.lib.fingerprintidentify.aosp.FingerprintManagerCompat.CryptoObject;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint.FingerprintIdentifyExceptionListener;
import main.java.com.hieuvp.fingerprint.fingerprint.SecureAndroidFingerprint;

public class SecureFingerprintIdentify {

    private BaseFingerprint mFingerprint;
    private BaseFingerprint mSubFingerprint;

    public SecureFingerprintIdentify(Context context) {
        this(context, null, null);
    }

    public SecureFingerprintIdentify(Context context, CryptoObject cryptoObject, FingerprintIdentifyExceptionListener exceptionListener) {
        SecureAndroidFingerprint androidFingerprint = new SecureAndroidFingerprint(context, cryptoObject, exceptionListener);
        if (androidFingerprint.isHardwareEnable()) {
            mSubFingerprint = androidFingerprint;
            if (androidFingerprint.isRegisteredFingerprint()) {
                mFingerprint = androidFingerprint;
                return;
            }
        }
        return;
    }

    // DO
    public void startIdentify(int maxAvailableTimes, BaseFingerprint.FingerprintIdentifyListener listener) {
        if (!isFingerprintEnable()) {
            return;
        }

        mFingerprint.startIdentify(maxAvailableTimes, listener);
    }

    public void cancelIdentify() {
        if (mFingerprint != null) {
            mFingerprint.cancelIdentify();
        }
    }

    public void resumeIdentify() {
        if (!isFingerprintEnable()) {
            return;
        }

        mFingerprint.resumeIdentify();
    }

    // GET & SET
    public boolean isFingerprintEnable() {
        return mFingerprint != null && mFingerprint.isEnable();
    }

    public boolean isHardwareEnable() {
        return isFingerprintEnable() || (mSubFingerprint != null && mSubFingerprint.isHardwareEnable());
    }

    public boolean isRegisteredFingerprint() {
        return isFingerprintEnable() || (mSubFingerprint != null && mSubFingerprint.isRegisteredFingerprint());
    }
}