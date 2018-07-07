import React, { Component } from 'react';
import {
  Image,
  Text,
  TouchableOpacity,
  View
} from 'react-native';
import FingerprintScanner from 'react-native-fingerprint-scanner';

import styles from './Application.container.styles';
import FingerprintPopup from './FingerprintPopup.component';

class Application extends Component {

  constructor(props) {
    super(props);
    this.state = {
      biometryType: undefined,
      errorMessage: undefined,
      popupShowed: false
    };
  }

  handleFingerprintShowed = () => {
    this.setState({ popupShowed: true });
  };

  handleFingerprintDismissed = () => {
    this.setState({ popupShowed: false });
  };

  componentDidMount() {
    FingerprintScanner
      .isSensorAvailable()
      .then(biometryType => this.setState({ biometryType }))
      .catch(error => this.setState({ errorMessage: error.message }));
  }

  render() {
    const { biometryType, errorMessage, popupShowed } = this.state;

    return (
      <View style={styles.container}>

        <Text style={styles.heading}>
          React Native Fingerprint Scanner
        </Text>
        <Text style={styles.subheading}>
          https://github.com/hieuvp/react-native-fingerprint-scanner
        </Text>

        <TouchableOpacity
          style={styles.fingerprint}
          onPress={this.handleFingerprintShowed}
          disabled={!!errorMessage}
        >
          <Image source={require('./assets/finger_print.png')} />
        </TouchableOpacity>

        {biometryType && (
          <Text style={styles.biometryType}>
            Type of biometric authentication: {biometryType}.
          </Text>
        )}

        {errorMessage && (
          <Text style={styles.errorMessage}>
            {errorMessage}
          </Text>
        )}

        {popupShowed && (
          <FingerprintPopup
            style={styles.popup}
            handlePopupDismissed={this.handleFingerprintDismissed}
          />
        )}

      </View>
    );
  }
}

export default Application;
