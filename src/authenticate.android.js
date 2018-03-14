import { DeviceEventEmitter, NativeModules } from 'react-native';
import createError from './createError';

const { ReactNativeFingerprintScanner } = NativeModules;

export default ({ onAttempt }) => {
  return new Promise((resolve, reject) => {
    DeviceEventEmitter.addListener('FINGERPRINT_SCANNER_AUTHENTICATION', (name) => {
      if (name === 'AuthenticationNotMatch' && typeof onAttempt === 'function') {
        onAttempt(createError(name));
      }
    });

    ReactNativeFingerprintScanner.authenticate()
      .then(() => {
        DeviceEventEmitter.removeAllListeners('FINGERPRINT_SCANNER_AUTHENTICATION');
        resolve(true);
      })
      .catch((error) => {
        DeviceEventEmitter.removeAllListeners('FINGERPRINT_SCANNER_AUTHENTICATION');
        reject(createError(error.code, error.message));
      });
  });
}
