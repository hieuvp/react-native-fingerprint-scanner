import { DeviceEventEmitter, NativeModules } from 'react-native';
import createError from './createError';

const { ReactNativeFingerprintScanner } = NativeModules;

export default ({title = '', description = ''}) => {
  return new Promise((resolve, reject) => {

    ReactNativeFingerprintScanner.authenticate(title, description)
      .then((value) => {
        resolve(value);
      })
      .catch((error) => {
        reject(createError(error.message));
      });
  });
}
