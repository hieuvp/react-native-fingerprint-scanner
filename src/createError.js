const ERRORS = {
  AuthenticationNotMatch: 'No match.',
  AuthenticationFailed: 'Authentication was not successful because the user failed to provide valid credentials.',
  UserCancel: 'Authentication was canceled by the user - e.g. the user tapped Cancel in the dialog.',
  UserFallback: 'Authentication was canceled because the user tapped the fallback button (Enter Password).',
  SystemCancel: 'Authentication was canceled by system - e.g. if another application came to foreground while the authentication dialog was up.',
  PasscodeNotSet: 'Authentication could not start because the passcode is not set on the device.',
  FingerprintScannerNotAvailable: 'Authentication could not start because Fingerprint Scanner is not available on the device.',
  FingerprintScannerNotEnrolled: 'Authentication could not start because Fingerprint Scanner has no enrolled fingers.',
  FingerprintScannerUnknownError: 'Could not authenticate for an unknown reason.',
  FingerprintScannerNotSupported: 'Device does not support Fingerprint Scanner.',
};

class FingerprintScannerError extends Error {

  constructor({ name, message }) {
    super(message);
    this.name = name || this.constructor.name;
    if (typeof Error.captureStackTrace === 'function') {
      Error.captureStackTrace(this, this.constructor);
    } else {
      this.stack = (new Error(message)).stack;
    }
  }
}

export default (name) => new FingerprintScannerError({ name, message: ERRORS[name] });
