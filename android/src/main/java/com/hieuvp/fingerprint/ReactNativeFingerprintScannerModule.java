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
    public static final int DEFAULT_MAX_AVAILABLE_TIMES = 5;

    private final ReactApplicationContext mReactContext;
    private FingerprintIdentify mFingerprintIdentify;

    public ReactNativeFingerprintScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
        mReactContext.addLifecycleEventListener(this);
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
        this.release();
    }

    @Override
    public void onHostDestroy() {
        this.release();
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

    private String getErrorMessage() {
        if (!getFingerprintIdentify().isHardwareEnable()) {
            return "RCTFingerprintScannerNotSupported";
        } else if (!getFingerprintIdentify().isRegisteredFingerprint()) {
            return "LAErrorFingerprintScannerNotEnrolled";
        } else if (!getFingerprintIdentify().isFingerprintEnable()) {
            return "LAErrorFingerprintScannerNotAvailable";
        }
        return null;
    }

    @ReactMethod
    public void authenticate(final Promise promise) {
        final String errorMessage = getErrorMessage();
        if (errorMessage != null) {
            promise.reject(errorMessage, errorMessage);
            return;
        }

        getFingerprintIdentify().resumeIdentify();
        getFingerprintIdentify().startIdentify(DEFAULT_MAX_AVAILABLE_TIMES, new FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                promise.resolve(true);
            }

            @Override
            public void onNotMatch(int availableTimes) {
                mReactContext.getJSModule(RCTDeviceEventEmitter.class)
                        .emit("FINGERPRINT_SCANNER_AUTHENTICATION", "LAErrorAuthenticationNotMatch");
            }

            @Override
            public void onFailed() {
                promise.reject("LAErrorAuthenticationFailed", "LAErrorAuthenticationFailed");
            }
        });
    }

    @ReactMethod
    public void release() {
        getFingerprintIdentify().cancelIdentify();
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
