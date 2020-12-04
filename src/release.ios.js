import { NativeModules } from 'react-native';

const { ReactNativeFingerprintScanner } = NativeModules;

export default () => {
   return ReactNativeFingerprintScanner.invalidate();
}

