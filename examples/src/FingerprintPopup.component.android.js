import React, { Component, PropTypes } from 'react';
import {
  Image,
  Text,
  TouchableOpacity,
  View,
  ViewPropTypes
} from 'react-native';

import styles from './FingerprintPopup.component.styles';

class FingerprintPopup extends Component {

  render() {
    const { style, handlePopupDismissed } = this.props;
    return (
      <View style={styles.container}>
        <View style={[styles.contentContainer, style]}>

          <Image
            style={styles.logo}
            source={require('./assets/finger_print.png')}
          />

          <Text style={styles.heading}>
            Fingerprint{'\n'}Authentication
          </Text>
          <Text style={styles.subheading}>
            Scan your fingerprint on the{'\n'}device scanner to continue
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
