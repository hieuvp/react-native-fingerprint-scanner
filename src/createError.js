const ERRORS = {
  LAErrorAuthenticationFailed: 'Authentication was not successful because the user failed to provide valid credentials.',
  LAErrorUserCancel: 'Authentication was canceled by the user - e.g. the user tapped Cancel in the dialog.',
  LAErrorUserFallback: 'Authentication was canceled because the user tapped the fallback button (Enter Password).',
  LAErrorSystemCancel: 'Authentication was canceled by system - e.g. if another application came to foreground while the authentication dialog was up.',
  LAErrorPasscodeNotSet: 'Authentication could not start because the passcode is not set on the device.',
  LAErrorFingerprintScannerNotAvailable: 'Authentication could not start because Fingerprint Scanner is not available on the device.',
  LAErrorFingerprintScannerNotEnrolled: 'Authentication could not start because Fingerprint Scanner has no enrolled fingers.',
  RCTFingerprintScannerUnknownError: 'Could not authenticate for an unknown reason.',
  RCTFingerprintScannerNotSupported: 'Device does not support Fingerprint Scanner.',
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
