import { NativeModules } from 'react-native';
import createError from 'react-native-fingerprint-scanner/src/createError';

const { ReactNativeFingerprintScanner } = NativeModules;

export default () => {
  return new Promise((resolve, reject) => {
    ReactNativeFingerprintScanner.authenticateDevice(error => {
      if (error) {
        return reject(createError(error.code, error.message))
      }

      return resolve(true);
    });
  });
}
