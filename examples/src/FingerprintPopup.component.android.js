import React, { Component, PropTypes } from 'react';
import {
  Alert,
  Image,
  Text,
  TouchableOpacity,
  View,
  ViewPropTypes
} from 'react-native';
import FingerprintScanner from 'react-native-fingerprint-scanner';

import ShakingText from './ShakingText.component';
import styles from './FingerprintPopup.component.styles';

class FingerprintPopup extends Component {

  constructor(props) {
    super(props);
    this.state = { errorMessage: undefined };
  }

  componentWillReceiveProps(nextProps) {
    const { pin,  value, popupShowed } = nextProps;
    if (popupShowed==true && this.props.popupShowed==false) {
      this.setState({errorMessage: ''});
      if (value) {
        FingerprintScanner
          .addWithKey(pin, value)
          .then((res) => {
            this.props.handlePopupDismissed({res});
          })
          .catch((error) => {
            this.setState({ errorMessage: JSON.stringify(error) });
          });
      }
      else {
        FingerprintScanner
          .readWithKey(pin)
          .then((res) => {
            this.props.handlePopupDismissed({res});
          })
          .catch((error) => {
            this.setState({ errorMessage: JSON.stringify(error) });
          });
        }
    }
  }

  componentWillUnmount() {
    FingerprintScanner.release();
  }

  handleAuthenticationAttempted = (error) => {
    this.setState({ errorMessage: error.message });
    this.description.shake();
  };

  render() {
    const { errorMessage } = this.state;
    const { style, handlePopupDismissed, popupShowed } = this.props;
    if (!popupShowed) return null;
    return (
      <View style={styles.container}>
        <View style={[styles.contentContainer, style]}>

          <Image
            style={styles.logo}
            source={require('./assets/finger_print.png')}
          />

          <Text style={styles.heading}>
            Fingerprint{'\n'}Authentication
            {errorMessage}
          </Text>

          <TouchableOpacity
            style={styles.buttonContainer}
            onPress={handlePopupDismissed}
          >
            <Text style={styles.buttonText}>
              BACK TO MAIN
            </Text>
          </TouchableOpacity>

        </View>
      </View>
    );
  }
}

FingerprintPopup.propTypes = {
  style: ViewPropTypes.style,
  handlePopupDismissed: PropTypes.func.isRequired,
};

export default FingerprintPopup;
