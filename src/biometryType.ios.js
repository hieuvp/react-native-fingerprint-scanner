import { NativeModules } from 'react-native';
import createError from './createError';

const { ReactNativeFingerprintScanner } = NativeModules;

export default () => {
  return new Promise((resolve, reject) => {
    ReactNativeFingerprintScanner.biometryType((error, result) => {
      if (error) return reject(createError(error.message));
      resolve(result);
    });
  });
}
