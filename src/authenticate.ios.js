import { NativeModules } from 'react-native';
import createError from './createError';

const { ReactNativeFingerprintScanner } = NativeModules;

export default ({ description }) => {
  const authReason = description || ' ';
  return new Promise((resolve, reject) => {
    ReactNativeFingerprintScanner.authenticate(authReason, error => {
      if (error) return reject(createError(error.message));
      resolve(true);
    });
  });
}
