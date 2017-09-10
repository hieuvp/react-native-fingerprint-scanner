import { NativeModules } from 'react-native';
import createError from './createError';

const { ReactNativeFingerprintScanner } = NativeModules;

export default ({ description = ' ', fallbackEnabled = true, fallbackTitle = '' }) => {
  return new Promise((resolve, reject) => {
    ReactNativeFingerprintScanner.authenticate(description, fallbackEnabled, fallbackTitle, error => {
      if (error) return reject(createError(error.message));
      resolve(true);
    });
  });
}
