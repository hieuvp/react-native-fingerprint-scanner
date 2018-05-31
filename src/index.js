import authenticate from './authenticate';
import isSensorAvailable from './isSensorAvailable';
import release, { restartFingerprint } from './release';

export default {
  authenticate,
  release,
  isSensorAvailable,
  restartFingerprint,
};
