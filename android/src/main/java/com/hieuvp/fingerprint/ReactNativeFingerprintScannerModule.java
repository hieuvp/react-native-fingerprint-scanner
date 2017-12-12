package com.hieuvp.fingerprint;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.Context;
import android.os.Build;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ActivityEventListener;

public class ReactNativeFingerprintScannerModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private final ReactApplicationContext mReactContext;
    private KeyguardManager mKeyguardManager;
    private Promise authPromise;

    // Constant values to be used in the module
    private static final int AUTH_REQUEST = 562385; // Random number to verify the activity result returned to the listener is ours
    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_AUTH_CANCELLED = "LAErrorUserCancel";
    private static final String E_FAILED_TO_SHOW_AUTH = "E_FAILED_TO_SHOW_AUTH";
    private static final String E_ONE_REQ_AT_A_TIME = "E_ONE_REQ_AT_A_TIME";

    /**
        Activity Event Listener that returns whether or not the user successfully verified their local credentials
     */
    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
      if (requestCode != AUTH_REQUEST || authPromise == null) return;

      if (resultCode == Activity.RESULT_CANCELED) {
        authPromise.reject(E_AUTH_CANCELLED, "UserCancel");
      } else if (resultCode == Activity.RESULT_OK) {
        authPromise.resolve(true);
      }

      authPromise = null;
    }
  };

    /**
        Constructor for module, called exactly once for each application start
        This means it is not initialized when an app comes to the foreground from the background
     */
    public ReactNativeFingerprintScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
        // Add the activity listener that will return the authentication result
        mReactContext.addActivityEventListener(mActivityEventListener);
        mKeyguardManager = (KeyguardManager) this.mReactContext.getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Override
    public String getName() {
        return "ReactNativeFingerprintScanner";
    }

    @Override
    public void onHostResume() {
        // Brought in from implements LifecycleEventListener
    }

    @Override
    public void onHostPause() {
        // Brought in from implements LifecycleEventListener
    }

    @Override
    public void onHostDestroy() {
        // Brought in from implements LifecycleEventListener
    }

    /**
        Authentication method that will create the KeyguardManager intent to verify the user's device credentials
     */
    @ReactMethod
    public void authenticate(String title, String description, final Promise promise) {
        // Create the Confirm Credentials screen. You can customize the title and description. Or
        // we will provide a generic one for you if you leave it null
        Activity currentActivity = getCurrentActivity();

        // The calling activity (ReverseRisk) has somehow disappeared or been deleted
         if (authPromise != null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
         }

        // There is already an intent to verify the credentials running
        if (currentActivity == null) {
            promise.reject(E_ONE_REQ_AT_A_TIME, "One auth request at a time");
            return;
        }

        // Store the promise to resolve/reject when Activity returns data
        authPromise = promise;

        try {
            // Create and start the native Android intent for verifying device credentials
            final Intent authIntent = mKeyguardManager.createConfirmDeviceCredentialIntent(title, description);
            currentActivity.startActivityForResult(authIntent, AUTH_REQUEST);
        } catch (Exception e) {
            authPromise.reject(E_FAILED_TO_SHOW_AUTH, e);
            authPromise = null;
        }
    }

    /**
        This should be used to release resources but we don't have any resources that we want to release
        Maybe we could clear the ActivityListener when going between OnPause and OnResume but nothing points to needing to
     */
    @ReactMethod
    public void release() {
        // Nothing to do on release but included to make coding for Android simpler
    }

    /**
        Initial method that needs to be called before an authentication can take place. 
        This ensures the device is setup to perform local credential verification
     */
    @ReactMethod
    public void isSensorAvailable(final Promise promise) {
        // Current implementation of our gradle file points our minSDK to 21 but the KeyGuard isn't available past SDK 23
        // This is kind of messy but it's the best way to make sure the code is backwards compatible
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                // Determine if a PIN, Pattern, or Fingerprint is setup in the Security to unlock the device
                boolean available = mKeyguardManager.isDeviceSecure();
                promise.resolve(available); // Uneccessary but easier to debug this way

            } catch (Exception e) {
                // An error occured on the KeyguardManager, so bubble it up
                promise.reject("KeyguardManagerError", e);
            }
        } else {

            promise.resolve(false);
        }
    }
}
