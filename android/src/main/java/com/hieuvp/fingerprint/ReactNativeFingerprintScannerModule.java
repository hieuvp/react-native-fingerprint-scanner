package com.hieuvp.fingerprint;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint.FingerprintIdentifyExceptionListener;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint.FingerprintIdentifyListener;

public class ReactNativeFingerprintScannerModule extends ReactContextBaseJavaModule
        implements LifecycleEventListener {
    public static final int MAX_AVAILABLE_TIMES = Integer.MAX_VALUE;

    private final ReactApplicationContext mReactContext;
    private FingerprintIdentify mFingerprintIdentify;

    public ReactNativeFingerprintScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
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

    private FingerprintIdentify getFingerprintIdentify() {
        mReactContext.addLifecycleEventListener(this);
        mFingerprintIdentify = new FingerprintIdentify(getCurrentActivity(),
                new FingerprintIdentifyExceptionListener() {
                    @Override
                    public void onCatchException(Throwable exception) {
                        mReactContext.removeLifecycleEventListener(
                                ReactNativeFingerprintScannerModule.this);
                    }
                });
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
            public void onFailed() {
                promise.reject("AuthenticationFailed", "AuthenticationFailed");
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
