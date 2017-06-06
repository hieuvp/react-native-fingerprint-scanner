package com.hieuvp.fingerprint;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint.FingerprintIdentifyExceptionListener;

public class ReactNativeFingerprintScannerModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext mReactContext;
    private FingerprintIdentify mFingerprintIdentify;

    public ReactNativeFingerprintScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return "ReactNativeFingerprintScanner";
    }

    private FingerprintIdentify getFingerprintIdentify() {
        if (mFingerprintIdentify == null) {
            mFingerprintIdentify = new FingerprintIdentify(getCurrentActivity(),
                    new FingerprintIdentifyExceptionListener() {
                        @Override
                        public void onCatchException(Throwable exception) {
                        }
                    });
        }
        return mFingerprintIdentify;
    }

    @ReactMethod
    public void isSensorAvailable(final Promise promise) {
        if (!getFingerprintIdentify().isHardwareEnable()) {
            promise.reject("RCTFingerprintScannerNotSupported", "RCTFingerprintScannerNotSupported");
        } else if (!getFingerprintIdentify().isRegisteredFingerprint()) {
            promise.reject("LAErrorFingerprintScannerNotEnrolled", "LAErrorFingerprintScannerNotEnrolled");
        } else if (!getFingerprintIdentify().isFingerprintEnable()) {
            promise.reject("LAErrorFingerprintScannerNotAvailable", "LAErrorFingerprintScannerNotAvailable");
        } else {
            promise.resolve(true);
        }
    }
}
