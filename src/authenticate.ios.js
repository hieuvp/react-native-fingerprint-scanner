import { NativeModules } from 'react-native';
import createError from './createError';

const { ReactNativeFingerprintScanner } = NativeModules;

export default ({ description = ' ', cancelTitle = '', fallbackEnabled = true }) => {
  return new Promise((resolve, reject) => {
    ReactNativeFingerprintScanner.authenticate(description, cancelTitle, fallbackEnabled, error => {
      if (error) {
        return reject(createError(error.code, error.message))
      }

      return resolve(true);
    });
  });
}
