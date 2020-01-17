package com.hieuvp.fingerprint;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt.AuthenticationCallback;
import androidx.biometric.BiometricPrompt.PromptInfo;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.UiThreadUtil;


@ReactModule(name="ReactNativeFingerprintScanner")
public class ReactNativeFingerprintScannerModule
        extends ReactContextBaseJavaModule
        implements LifecycleEventListener
{
    public static final int MAX_AVAILABLE_TIMES = Integer.MAX_VALUE;
    public static final String TYPE_BIOMETRICS = "Biometrics";

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

        public AuthCallback(final Promise promise) {
            super();
            this.promise = promise;
        }

        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            this.promise.reject(biometricPromptErrName(errorCode), TYPE_BIOMETRICS);
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            this.promise.resolve(true);
        }
    }

    public BiometricPrompt getBiometricPrompt(final Promise promise) {
        // memoize so can be accessed to cancel
        if (biometricPrompt != null) {
            return biometricPrompt;
        }

        // listen for onHost* methods
        mReactContext.addLifecycleEventListener(this);

        AuthCallback authCallback = new AuthCallback(promise);
        FragmentActivity fragmentActivity = (FragmentActivity) getCurrentActivity();
        Executor executor = Executors.newSingleThreadExecutor();
        biometricPrompt = new BiometricPrompt(
            fragmentActivity,
            executor,
            authCallback
        );

        return biometricPrompt;
    }

    private void biometricAuthenticate(final String titleText, final Promise promise) {
        UiThreadUtil.runOnUiThread(
            new Runnable() {
                @Override
                public void run() {
                    BiometricPrompt bioPrompt = getBiometricPrompt(promise);

                    PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setDeviceCredentialAllowed(false)
                        .setConfirmationRequired(false)
                        .setNegativeButtonText("Cancel")
                        .setTitle(titleText)
                        .build();

                    bioPrompt.authenticate(promptInfo);
                }
            });
    }

    // the below constants are consistent across BiometricPrompt and BiometricManager
    private String biometricPromptErrName(int errCode) {
        switch (errCode) {
            case BiometricPrompt.ERROR_CANCELED:
                return "SystemCancel";
            case BiometricPrompt.ERROR_HW_NOT_PRESENT:
                return "FingerprintScannerNotSupported";
            case BiometricPrompt.ERROR_HW_UNAVAILABLE:
                return "FingerprintScannerNotAvailable";
            case BiometricPrompt.ERROR_LOCKOUT:
                return "DeviceLocked";
            case BiometricPrompt.ERROR_LOCKOUT_PERMANENT:
                return "DeviceLocked";
            case BiometricPrompt.ERROR_NEGATIVE_BUTTON:
                return "UserCancel";
            case BiometricPrompt.ERROR_NO_BIOMETRICS:
                return "FingerprintScannerNotEnrolled";
            case BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL:
                return "PasscodeNotSet";
            case BiometricPrompt.ERROR_NO_SPACE:
                return "DeviceOutOfMemory";
            case BiometricPrompt.ERROR_TIMEOUT:
                return "AuthenticationTimeout";
            case BiometricPrompt.ERROR_UNABLE_TO_PROCESS:
                return "AuthenticationProcessFailed";
            case BiometricPrompt.ERROR_USER_CANCELED:  // actually 'user elected another auth method'
                return "UserFallback";
            case BiometricPrompt.ERROR_VENDOR:
                // hardware-specific error codes
                return "HardwareError";
            default:
                return "FingerprintScannerUnknownError";
        }
    }

    private String getSensorError() {
        BiometricManager biometricManager = BiometricManager.from(mReactContext);
        int authResult = biometricManager.canAuthenticate();

        if (authResult == BiometricManager.BIOMETRIC_SUCCESS) {
            return null;
        }
        if (authResult == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            return "FingerprintScannerNotSupported";
        } else if (authResult == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            return "FingerprintScannerNotEnrolled";
        } else if (authResult == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE) {
            return "FingerprintScannerNotAvailable";
        }

        return null;
    }

    @ReactMethod
    public void authenticate(final Promise promise, String titleText) {
        final String errorName = getSensorError();
        if (errorName != null) {
            promise.reject(errorName, TYPE_BIOMETRICS);
            ReactNativeFingerprintScannerModule.this.release();
            return;
        }

        biometricAuthenticate(titleText, promise);
    }

    @ReactMethod
    public void release() {
        if (biometricPrompt != null) {
            biometricPrompt.cancelAuthentication();  // if release called from eg React
        }
        biometricPrompt = null;
        mReactContext.removeLifecycleEventListener(this);
    }

    @ReactMethod
    public void isSensorAvailable(final Promise promise) {
        String errorName = getSensorError();
        if (errorName != null) {
            promise.reject(errorName, TYPE_BIOMETRICS);
        } else {
            promise.resolve(TYPE_BIOMETRICS);
        }
    }
}
