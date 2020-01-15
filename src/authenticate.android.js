import { DeviceEventEmitter, NativeModules } from 'react-native';

const { ReactNativeFingerprintScanner } = NativeModules;

export default ({ titleText="Log In" }) => {
  return new Promise((resolve, reject) => {
    ReactNativeFingerprintScanner.authenticate(titleText)
      .then(() => {
        resolve(true);
      })
      .catch((error) => {
        // translate errors
        reject(createError(error.code, error.message));
      });
  });
}
