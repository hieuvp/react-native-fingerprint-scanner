package com.hieuvp.fingerprint;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import main.java.com.hieuvp.fingerprint.fingerprint.SecureFingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.aosp.FingerprintManagerCompat.CryptoObject;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint.FingerprintIdentifyExceptionListener;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint.FingerprintIdentifyListener;

import main.java.com.hieuvp.keystorehelper.DeCryptor;
import main.java.com.hieuvp.keystorehelper.EnCryptor;

public class ReactNativeFingerprintScannerModule extends ReactContextBaseJavaModule
        implements LifecycleEventListener {
    public static final int MAX_AVAILABLE_TIMES = Integer.MAX_VALUE;

    // public static final KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");

    private final ReactApplicationContext mReactContext;
    private SecureFingerprintIdentify mFingerprintIdentify;

    private EnCryptor encryptor;
    private DeCryptor decryptor;
    private CryptoObject mCryptoObject;

    public ReactNativeFingerprintScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
        encryptor = new EnCryptor();

        try {
            decryptor = new DeCryptor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "ReactNativeFingerprintScanner";
    }

    @Override
    public void onHostResume() {
    }

    @Override
    public void onHostPause() {
    }

    @Override
    public void onHostDestroy() {
        this.release();
    }

    private SecureFingerprintIdentify getFingerprintIdentify() {
        if (mFingerprintIdentify == null) {
            mReactContext.addLifecycleEventListener(this);
            mFingerprintIdentify = new SecureFingerprintIdentify(getCurrentActivity(),
                    mCryptoObject,
                    new FingerprintIdentifyExceptionListener() {
                        @Override
                        public void onCatchException(Throwable exception) {
                            mReactContext.removeLifecycleEventListener(
                                    ReactNativeFingerprintScannerModule.this);
                        }
                    });
        }
        return mFingerprintIdentify;
    }

    private String getErrorMessage() {
        if (!getFingerprintIdentify().isHardwareEnable()) {
            return "FingerprintScannerNotSupported";
        } else if (!getFingerprintIdentify().isRegisteredFingerprint()) {
            return "FingerprintScannerNotEnrolled";
        } else if (!getFingerprintIdentify().isFingerprintEnable()) {
            return "FingerprintScannerNotAvailable";
        }
        return null;
    }

    @ReactMethod
    public void authenticate(final Promise promise) {
        final String errorMessage = getErrorMessage();
        if (errorMessage != null) {
            promise.reject(errorMessage, errorMessage);
            ReactNativeFingerprintScannerModule.this.release();
            return;
        }

        getFingerprintIdentify().resumeIdentify();
        getFingerprintIdentify().startIdentify(MAX_AVAILABLE_TIMES, new FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                promise.resolve(true);
                ReactNativeFingerprintScannerModule.this.release();
            }

            @Override
            public void onNotMatch(int availableTimes) {
                mReactContext.getJSModule(RCTDeviceEventEmitter.class)
                        .emit("FINGERPRINT_SCANNER_AUTHENTICATION", "AuthenticationNotMatch");
            }

            @Override
            public void onFailed(boolean isError) {
                promise.reject("AuthenticationFailed", "AuthenticationFailed");
                ReactNativeFingerprintScannerModule.this.release();
            }

            @Override
            public void onStartFailedByDeviceLocked() {
                promise.reject("DeviceLocked", "DeviceLocked");
                ReactNativeFingerprintScannerModule.this.release();
            }
        });
    }

    @ReactMethod
    public void addWithKey(final String key, final String value, final Promise promise) {
        mCryptoObject = new CryptoObject(encryptor.getCipher(key));
        final String errorMessage = getErrorMessage();
        if (errorMessage != null) {
            promise.reject(errorMessage, errorMessage);
            ReactNativeFingerprintScannerModule.this.release();
            return;
        }

        getFingerprintIdentify().resumeIdentify();
        getFingerprintIdentify().startIdentify(MAX_AVAILABLE_TIMES, new FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                try {
                    byte[] encryptedText = encryptor.encryptText(mCryptoObject.getCipher(), value);
                    promise.resolve(new String(encryptedText, "UTF-8"));
                } catch (Exception e) {
                    promise.reject(e);
                }
                ReactNativeFingerprintScannerModule.this.release();
            }

            @Override
            public void onNotMatch(int availableTimes) {
                mReactContext.getJSModule(RCTDeviceEventEmitter.class)
                        .emit("FINGERPRINT_SCANNER_AUTHENTICATION", "AuthenticationNotMatch");
            }

            @Override
            public void onFailed(boolean isError) {
                promise.reject("AuthenticationFailed", "AuthenticationFailed");
                ReactNativeFingerprintScannerModule.this.release();
            }

            @Override
            public void onStartFailedByDeviceLocked() {
                promise.reject("DeviceLocked", "DeviceLocked");
                ReactNativeFingerprintScannerModule.this.release();
            }
        });
    }

    @ReactMethod
    public void readWithKey(final String key, final Promise promise) {
        mCryptoObject = new CryptoObject(decryptor.getCipher(key, encryptor.getIv()));
        final String errorMessage = getErrorMessage();
        if (errorMessage != null) {
            promise.reject(errorMessage, errorMessage);
            ReactNativeFingerprintScannerModule.this.release();
            return;
        }

        getFingerprintIdentify().resumeIdentify();
        getFingerprintIdentify().startIdentify(MAX_AVAILABLE_TIMES, new FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                try {
                    final String decryptedText = decryptor
                    .decryptData(mCryptoObject.getCipher(), encryptor.getEncryption());
                    promise.resolve(decryptedText);
                } catch (Exception e) {
                    promise.reject(e);
                }
                ReactNativeFingerprintScannerModule.this.release();
            }

            @Override
            public void onNotMatch(int availableTimes) {
                mReactContext.getJSModule(RCTDeviceEventEmitter.class)
                        .emit("FINGERPRINT_SCANNER_AUTHENTICATION", "AuthenticationNotMatch");
            }

            @Override
            public void onFailed(boolean isError) {
                promise.reject("AuthenticationFailed", "AuthenticationFailed");
                ReactNativeFingerprintScannerModule.this.release();
            }

            @Override
            public void onStartFailedByDeviceLocked() {
                promise.reject("DeviceLocked", "DeviceLocked");
                ReactNativeFingerprintScannerModule.this.release();
            }
        });
    }

    @ReactMethod
    public void release() {
        getFingerprintIdentify().cancelIdentify();
        mFingerprintIdentify = null;
        mReactContext.removeLifecycleEventListener(this);
    }

    @ReactMethod
    public void isSensorAvailable(final Promise promise) {
        String errorMessage = getErrorMessage();
        if (errorMessage != null) {
            promise.reject(errorMessage, errorMessage);
        } else {
            promise.resolve(true);
        }
    }
}
