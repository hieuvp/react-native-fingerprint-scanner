const ERRORS = {
  // sensor availability
  FingerprintScannerNotSupported: 'Device does not support Fingerprint Scanner.',
  FingerprintScannerNotEnrolled: 'Authentication could not start because Fingerprint Scanner has no enrolled fingers.',
  FingerprintScannerNotAvailable: 'Authentication could not start because Fingerprint Scanner is not available on the device.',

  // auth failures
  AuthenticationNotMatch: 'No match.',
  AuthenticationFailed: 'Authentication was not successful because the user failed to provide valid credentials.',
  AuthenticationTimeout: 'Authentication was not successful because the operation timed out.',
  AuthenticationProcessFailed: 'Sensor was unable to process the image. Please try again.',
  UserCancel: 'Authentication was canceled by the user - e.g. the user tapped Cancel in the dialog.',
  UserFallback: 'Authentication was canceled because the user tapped the fallback button (Enter Password).',
  SystemCancel: 'Authentication was canceled by system - e.g. if another application came to foreground while the authentication dialog was up.',
  PasscodeNotSet: 'Authentication could not start because the passcode is not set on the device.',
  FingerprintScannerUnknownError: 'Could not authenticate for an unknown reason.',
  DeviceLocked: 'Authentication was not successful, the device currently in a lockout of 30 seconds.',
  DeviceLockedPermanent: 'Authentication was not successful, device must be unlocked via password.',
  DeviceOutOfMemory: 'Authentication could not proceed because there is not enough free memory on the device.',
  HardwareError: 'A hardware error occurred.',
};

class FingerprintScannerError extends Error {

  constructor({ name, message, biometric }) {
    super(message);
    this.name = name || this.constructor.name;
    this.biometric = biometric;
    if (typeof Error.captureStackTrace === 'function') {
      Error.captureStackTrace(this, this.constructor);
    } else {
      this.stack = (new Error(message)).stack;
    }
  }
}

export default (name, biometric) => new FingerprintScannerError({ name, message: ERRORS[name], biometric });
