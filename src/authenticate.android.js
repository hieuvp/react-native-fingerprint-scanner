import { NativeModules } from 'react-native';
import createError from './createError';

const { ReactNativeFingerprintScanner } = NativeModules;

export default () => {
  return new Promise((resolve, reject) => {
    ReactNativeFingerprintScanner.authenticate()
      .then(() => resolve(true))
      .catch((error) => reject(createError(error.message)));
  });
}
