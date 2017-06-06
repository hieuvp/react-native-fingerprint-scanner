import { NativeModules } from 'react-native';
import createError from './createError';

const { ReactNativeFingerprintScanner } = NativeModules;

export default (reason) => {
  const authReason = reason || ' ';
  return new Promise((resolve, reject) => {
    ReactNativeFingerprintScanner.authenticate(authReason, error => {
      if (error) return reject(createError(error.message));
      resolve(true);
    });
  });
}
