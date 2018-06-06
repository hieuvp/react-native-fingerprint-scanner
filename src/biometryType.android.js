import { NativeModules } from 'react-native';
import createError from './createError';

const { ReactNativeFingerprintScanner } = NativeModules;

export default () => {
  return new Promise((resolve, reject) => {
    ReactNativeFingerprintScanner.biometryType()
      .then(result => resolve(result))
      .catch(error => reject(createError(error.message)));
  });
}
