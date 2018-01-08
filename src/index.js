import authenticate from './authenticate';
import isSensorAvailable from './isSensorAvailable';
import keyStore from './keyStore';
import release from './release';

export default {
  authenticate,
  release,
  isSensorAvailable,
  ...keyStore
};
