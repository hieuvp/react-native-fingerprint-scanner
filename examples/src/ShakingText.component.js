import PropTypes from 'prop-types';
import React, { Component } from 'react';
import {
  Animated,
  Text
} from 'react-native';

class ShakingText extends Component {

  componentWillMount() {
    this.shakedValue = new Animated.Value(0);
  }

  get animatedStyle() {
    return {
      transform: [
        {
          translateY: this.shakedValue.interpolate({
            inputRange: [0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1],
            outputRange: [0, 10, -15, 12, -9, 18, -7, 10, -11, 5, 0],
          }),
        },
        {
          translateX: this.shakedValue.interpolate({
            inputRange: [0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1],
            outputRange: [0, 2, -3, 4, -4, 3, -3, 4, -5, 2, 0],
          }),
        },
      ],
    };
  }

  shake = () => {
    this.shakedValue.setValue(0);
    Animated.spring(this.shakedValue, {
      toValue: 1,
      friction: 3,
      tension: 10,
    }).start(() => this.shakedValue.setValue(0));
  };

  render() {
    return (
      <Animated.Text
        {...this.props}
        style={[this.animatedStyle, this.props.style]}
      />
    );
  }
}

ShakingText.propTypes = {
  children: PropTypes.oneOfType([
    PropTypes.arrayOf(PropTypes.node),
    PropTypes.node
  ]),
  style: Text.propTypes.style,
};

export default ShakingText;
