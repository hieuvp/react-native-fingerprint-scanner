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

  onPress = () => {

  };

  render() {
    return (
      <View style={styles.container}>

        <Text style={styles.heading}>
          React Native Fingerprint Scanner
        </Text>
        <Text style={styles.subheading}>
          https://github.com/hieuvp/react-native-fingerprint-scanner
        </Text>

        <TouchableOpacity style={styles.fingerprint} onPress={this.onPress}>
          <Image source={require('./assets/finger_print.png')} />
        </TouchableOpacity>

        <FingerprintPopup />

      </View>
    );
  }
}

export default Application;
