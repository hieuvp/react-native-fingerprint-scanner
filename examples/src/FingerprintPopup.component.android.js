import { Component } from 'react';
import PropTypes from 'prop-types';

import FingerprintScanner from 'react-native-fingerprint-scanner';


// Based on https://github.com/hieuvp/react-native-fingerprint-scanner/blob/master/examples/src/FingerprintPopup.component.android.js
class BiometricPopup extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidMount() {
    FingerprintScanner
      .authenticate({ titleText: 'Log in with Biometrics' })
      .then(() => {
        this.props.onAuthenticate();
      });
  }

  componentWillUnmount = () => {
    FingerprintScanner.release();
  }

  render = () => null
}

BiometricPopup.propTypes = {
  onAuthenticate: PropTypes.func.isRequired,
};

export default BiometricPopup;

