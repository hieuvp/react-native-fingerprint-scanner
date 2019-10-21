export type AuthenticateIOS = {
  description: string;
  fallbackEnabled: boolean;
};
export type AuthenticateAndroid = { onAttempt: (error: FingerprintScannerError) => void };

export type Biometrics = 'Touch ID' | 'Face ID' | 'Fingerprint';

export type Errors =
  | { name: 'AuthenticationNotMatch'; message: 'No match' }
  | {
      name: 'AuthenticationFailed';
      message: 'Authentication was not successful because the user failed to provide valid credentials';
    }
  | {
      name: 'UserCancel';
      message: 'Authentication was canceled by the user - e.g. the user tapped Cancel in the dialog';
    }
  | {
      name: 'UserFallback';
      message: 'Authentication was canceled because the user tapped the fallback button (Enter Password)';
    }
  | {
      name: 'SystemCancel';
      message: 'Authentication was canceled by system - e.g. if another application came to foreground while the authentication dialog was up';
    }
  | {
      name: 'PasscodeNotSet';
      message: 'Authentication could not start because the passcode is not set on the device';
    }
  | {
      name: 'FingerprintScannerNotAvailable';
      message: '	Authentication could not start because Fingerprint Scanner is not available on the device';
    }
  | {
      name: 'FingerprintScannerNotEnrolled';
      message: '	Authentication could not start because Fingerprint Scanner has no enrolled fingers';
    }
  | {
      name: 'FingerprintScannerUnknownError';
      message: 'Could not authenticate for an unknown reason';
    }
  | {
      name: 'FingerprintScannerNotSupported';
      message: 'Device does not support Fingerprint Scanner';
    }
  | {
      name: 'DeviceLocked';
      message: 'Authentication was not successful, the device currently in a lockout of 30 seconds';
    };

export type FingerprintScannerError = { biometric: Biometrics } & Errors;

export interface FingerPrintProps {
  /**
      ### release(): (Android)
      Stops fingerprint scanner listener, releases cache of internal state in native code.
      - Returns a `void`

      -------------------
      Exemple
      
      ```
      componentWillUnmount() {
        FingerprintScanner.release();
      }
      ```
      */
  release: () => void;

  /**
        ### isSensorAvailable(): (Android, iOS)
        Checks if Fingerprint Scanner is able to be used by now.
        -  Returns a `Promise<Biometrics>`
        - `biometryType`: *String* - The type of biometric authentication supported by the device.
        - `error: FingerprintScannerError { name, message, biometric }` - The name and message of failure and the biometric type in use.
        
        -------------
        Exemple

        ```
          FingerprintScanner
            .isSensorAvailable()
            .then(biometryType => this.setState({ biometryType }))
            .catch(error => this.setState({ errorMessage: error.message }));
        ```

        ------------
      */
  isSensorAvailable: () => Promise<Biometrics>;

  /**
      ### authenticate({ description, fallbackEnabled }): (iOS) 

      - Returns a `Promise`
      - `description: String` - the string to explain the request for user authentication.
      - `fallbackEnabled: Boolean` - default to ***true***, whether to display fallback button (e.g. Enter Password).

      ----------------
      - Example: 
      ```
      FingerprintScanner
        .authenticate({ description: 'Scan your fingerprint on the device scanner to continue' })
        .then(() => {
          this.props.handlePopupDismissed();
          AlertIOS.alert('Authenticated successfully');
        })
        .catch((error) => {
          this.props.handlePopupDismissed();
          AlertIOS.alert(error.message);
        });
      ```     
      -----------------
      
      ### authenticate({ onAttempt }): (Android)

      - Returns a `Promise`
      - `onAttempt: Function` - a callback function when users are trying to scan their fingerprint but failed.

      -----------------
      - Example:
      ```
      FingerprintScanner
        .authenticate({ onAttempt: this.handleAuthenticationAttempted })
        .then(() => {
          this.props.handlePopupDismissed();
          Alert.alert('Fingerprint Authentication', 'Authenticated successfully');
        })
        .catch((error) => {
          this.setState({ errorMessage: error.message });
          this.description.shake();
        });
      ```
      -----------------
     */
  authenticate: (
    platformProps: AuthenticateIOS | AuthenticateAndroid
  ) => Promise<void>;
}

declare const FingerprintScanner: FingerPrintProps;

export default FingerprintScanner;
