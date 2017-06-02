import React, { Component } from 'react';
import {
  Text,
  View
} from 'react-native';

import styles from './Application.container.styles';

class Application extends Component {

  render() {
    return (
      <View style={styles.container}>
        <Text>Welcome to React Native!</Text>
      </View>
    );
  }
}

export default Application;
