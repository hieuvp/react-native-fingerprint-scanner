import React, { Component } from 'react';
import {
  Image,
  Text,
  TouchableOpacity,
  View
} from 'react-native';

import styles from './Application.container.styles';
import FingerprintPopup from './FingerprintPopup.component';

class Application extends Component {

  constructor(props) {
    super(props);
    this.state = { popupShowed: false };
  }

  handleFingerprintShowed = () => {
    this.setState({ popupShowed: true });
  };

  handleFingerprintDismissed = () => {
    this.setState({ popupShowed: false });
  };

  render() {
    const { popupShowed } = this.state;

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
        >
          <Image source={require('./assets/finger_print.png')} />
        </TouchableOpacity>

        {popupShowed && (
          <FingerprintPopup
            style={styles.popup}
            onBackPressed={this.handleFingerprintDismissed}
          />
        )}

      </View>
    );
  }
}

export default Application;
