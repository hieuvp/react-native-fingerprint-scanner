import { DeviceEventEmitter, NativeModules, Platform } from 'react-native';

const { ReactNativeFingerprintScanner } = NativeModules;

export default () => {
  if (Platform.Version < 23) {
    DeviceEventEmitter.removeAllListeners('FINGERPRINT_SCANNER_AUTHENTICATION');
  }

  ReactNativeFingerprintScanner.release();
}
