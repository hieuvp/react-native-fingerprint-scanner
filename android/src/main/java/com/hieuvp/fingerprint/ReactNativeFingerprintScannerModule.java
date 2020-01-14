package com.hieuvp.fingerprint;

import android.os.Build;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

@ReactModule(name="ReactNativeFingerprintScanner")
public class ReactNativeFingerprintScannerModule extends ReactContextBaseJavaModule
        implements LifecycleEventListener {
    public static final int MAX_AVAILABLE_TIMES = Integer.MAX_VALUE;
    public static final String TYPE_FINGERPRINT = "Fingerprint";

    private final ReactApplicationContext mReactContext;
    private BiometricPrompt biometricPrompt;

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

    public class AuthCallback extends BiometricPrompt.AuthenticationCallback {
        private Promise promise;

        public AuthCallback(Promise promise) {
            super();
            this.promise = promise;
        }

        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            this.promise.reject("Error authenticating biometrics" , "Error authenticating biometrics");
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            this.promise.resolve(true);
        }
    }

    public BiometricPrompt getBiometricPrompt() {
        if (biometricPrompt != null) {
            return biometricPrompt;
        }

        // create and memoize if DNE
        mReactContext.addLifecycleEventListener(this);  // TODO: need this?
        AuthenticationCallback authCallback = new AuthCallback(promise);
        FragmentActivity fragmentActivity = (FragmentActivity) getCurrentActivity();
        Executor executor = Executors.newSingleThreadExecutor();
        biometricPrompt = new BiometricPrompt(
            fragmentActivity,
            executor,
            authCallback
        );

        return biometricPrompt;
    }

    private void biometricAuthenticate(String titleText, Promise promise) {
        UiThreadUtil.runOnUiThread(
            new Runnable() {
                @Override
                public void run() {
                    bioPrompt = getBiometricPrompt();

                    PromptInfo promptInfo = new PromptInfo.Builder()
                        .setDeviceCredentialAllowed(false)
                        .setConfirmationRequired(false)
                        .setNegativeButtonText("Cancel")
                        .setTitle(titleText)
                        .build();

                    bioPrompt.authenticate(promptInfo);
                }
            });
    }

    // TODO: use biometrioc manager to eval
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
            promise.reject(errorMessage, TYPE_FINGERPRINT);
            ReactNativeFingerprintScannerModule.this.release();
            return;
        }

        biometricAuthenticate();
    }

    @ReactMethod
    public void release() {
        getBiometricPrompt().cancelAuthentication();  // if release called from eg React
        biometricPrompt = null;
        mReactContext.removeLifecycleEventListener(this);
    }

    @ReactMethod
    public void isSensorAvailable(final Promise promise) {
        String errorMessage = getErrorMessage();
        if (errorMessage != null) {
            promise.reject(errorMessage, TYPE_FINGERPRINT);
        } else {
            promise.resolve(TYPE_FINGERPRINT);
        }
    }
}
