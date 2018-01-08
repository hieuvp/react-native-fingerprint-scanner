import { DeviceEventEmitter, NativeModules } from 'react-native';
import createError from './createError';

const { ReactNativeFingerprintScanner } = NativeModules;

export default ({ onAttempt, key }) => {
  return new Promise((resolve, reject) => {
    DeviceEventEmitter.addListener('FINGERPRINT_SCANNER_AUTHENTICATION', (message) => {
      if (message === 'AuthenticationNotMatch' && typeof onAttempt === 'function') {
        onAttempt(createError(message));
      }
    });
    const auth = key ? ReactNativeFingerprintScanner.readWithKey : ReactNativeFingerprintScanner.authenticate;
    
    auth(key)
      .then((res) => {
        DeviceEventEmitter.removeAllListeners('FINGERPRINT_SCANNER_AUTHENTICATION');
        resolve(res);
      })
      .catch((error) => {
        DeviceEventEmitter.removeAllListeners('FINGERPRINT_SCANNER_AUTHENTICATION');
        reject(createError(error.message));
      });
  });
}
