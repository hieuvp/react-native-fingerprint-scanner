import { NativeModules } from 'react-native';

const { ReactNativeFingerprintScanner } = NativeModules;

export default () => {
  return new Promise(() => {
    ReactNativeFingerprintScanner.invalidate();
  });
}
