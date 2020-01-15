import { DeviceEventEmitter, NativeModules } from 'react-native';
import createError from './createError';

const { ReactNativeFingerprintScanner } = NativeModules;

export default ({ onAttempt }) => {
  return new Promise((resolve, reject) => {
    ReactNativeFingerprintScanner.authenticate()
      .then(() => {
        resolve(true);
      })
      .catch((error) => {
        // translate errors
        reject(createError(error.code, error.message));
      });
  });
}
