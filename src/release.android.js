import { DeviceEventEmitter, NativeModules } from 'react-native';

const { ReactNativeFingerprintScanner } = NativeModules;

export default () => {
  DeviceEventEmitter.removeAllListeners('FINGERPRINT_SCANNER_AUTHENTICATION');
  ReactNativeFingerprintScanner.release();
}
