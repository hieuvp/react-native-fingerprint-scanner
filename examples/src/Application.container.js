import React, { Component } from 'react';
import {
  Image,
  Text,
  TouchableOpacity,
  View,
  TextInput,
  Button,
  Alert
} from 'react-native';
import FingerprintScanner from 'react-native-fingerprint-scanner';

import styles from './Application.container.styles';
import FingerprintPopup from './FingerprintPopup.component';

class Application extends Component {

  constructor(props) {
    super(props);
    this.state = {
      errorMessage: undefined,
      popupShowed: false
    };
  }

  handleFingerprintShowed = () => {
    this.setState({ popupShowed: true });
  };

  handleFingerprintDismissed = ({res}) => {
    this.setState({ popupShowed: false }, () => {
      if (res) {
        console.log(res);
      };
    });
  };

  componentDidMount() {
    FingerprintScanner
      .isSensorAvailable()
      .catch(error => this.setState({ errorMessage: error.message }));
  }

  _setKey = () => {
    FingerprintScanner.addWithKey('pin', this.state.text).then((res) => {
      this.setState({ text: res });
    }, (e) => console.log(e));
  }

  render() {
    const { errorMessage, popupShowed } = this.state;

    return (
      <View style={styles.container}>

        <Text style={styles.heading}>
          React Native Fingerprint Scanner
        </Text>
        <Text style={styles.subheading}>
          https://github.com/hieuvp/react-native-fingerprint-scanner
        </Text>
        <TextInput
          style={styles.input}
          onChangeText={(text) => this.setState({text})}
          value={this.state.text}
        />
        <TouchableOpacity
          style={styles.fingerprint}
          onPress={this.handleFingerprintShowed}
          disabled={!!errorMessage}
        >
          <Image source={require('./assets/finger_print.png')} />
        </TouchableOpacity>

        {errorMessage && (
          <Text style={styles.errorMessage}>
            {errorMessage}
          </Text>
        )}

        <FingerprintPopup
          popupShowed={popupShowed}
          style={styles.popup}
          pin="pin"
          value={this.state.text}
          handlePopupDismissed={this.handleFingerprintDismissed}
        />

      </View>
    );
  }
}

export default Application;
