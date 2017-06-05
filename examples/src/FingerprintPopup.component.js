import React, {
  Component,
  PropTypes
} from 'react';
import {
  Text,
  View
} from 'react-native';

import styles from './FingerprintPopup.component.styles';

class FingerprintPopup extends Component {

  render() {
    const { style } = this.props;
    return (
      <View style={styles.container}>
        <View style={[styles.contentContainer, style]}>
          <Text>
            Fingerprint Authentication
          </Text>
          <Text>
            Scan your fingerprint on the device scanner to continue
          </Text>
        </View>
      </View>
    );
  }
}

FingerprintPopup.propTypes = {
  style: View.propTypes.style,
  onBackPressed: PropTypes.func
};

export default FingerprintPopup;
