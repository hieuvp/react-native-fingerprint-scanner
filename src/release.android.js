import { DeviceEventEmitter, NativeModules } from 'react-native';

const { ReactNativeFingerprintScanner } = NativeModules;

export default () => {
  DeviceEventEmitter.removeAllListeners('FINGERPRINT_SCANNER_AUTHENTICATION');
  ReactNativeFingerprintScanner.release();
}

export let restartFingerprint = () => {
  return new Promise((resolve, reject) => {
    ReactNativeFingerprintScanner.restartFingerprint()
      .then(() => {
        DeviceEventEmitter.removeAllListeners('FINGERPRINT_SCANNER_AUTHENTICATION');
        resolve(true);
      })
      .catch((error) => {
        DeviceEventEmitter.removeAllListeners('FINGERPRINT_SCANNER_AUTHENTICATION');
        reject(createError(error.message));
      });
  });
}