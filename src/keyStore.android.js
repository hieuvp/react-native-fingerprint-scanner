import { NativeModules } from 'react-native';
import createError from './createError';

const { ReactNativeFingerprintScanner } = NativeModules;

export default {
  addWithKey: (key, value) => ReactNativeFingerprintScanner.addWithKey(key, value)
    .catch(e => {
      console.log(e);
      throw createError(e.message); }),
  readWithKey: key => ReactNativeFingerprintScanner.readWithKey(key)
    .catch(e => { throw createError(e.message); })
}
