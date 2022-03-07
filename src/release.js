import { Platform } from 'react-native';

export default () => {
  return new Promise((_, reject) => {
    reject(new Error(`react-native-fingerprint-scanner is not available for ${Platform.OS}`))
  });
}
