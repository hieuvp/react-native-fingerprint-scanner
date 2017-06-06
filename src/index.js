import { NativeModules } from 'react-native';
import createError from './createError';

const { ReactNativeFingerprintScanner } = NativeModules;

function isSupported() {
  return new Promise((resolve, reject) => {
    ReactNativeFingerprintScanner.isSupported(error => {
      if (error) return reject(createError(error.message));
      resolve(true);
    });
  });
}

function authenticate(reason) {
  const authReason = reason || ' ';
  return new Promise((resolve, reject) => {
    ReactNativeFingerprintScanner.authenticate(authReason, error => {
      if (error) return reject(createError(error.message));
      resolve(true);
    });
  });
}

export default {
  isSupported,
  authenticate,
};
