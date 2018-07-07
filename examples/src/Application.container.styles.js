import { Dimensions, StyleSheet } from 'react-native';

const { width } = Dimensions.get('window');

export default StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#00a4de'
  },
  heading: {
    color: '#ffffff',
    fontSize: 22,
    marginTop: 30,
    marginBottom: 5,
  },
  subheading: {
    color: '#ffffff',
    fontSize: 12,
    textAlign: 'center',
    marginTop: 5,
    marginBottom: 30,
  },
  fingerprint: {
    padding: 20,
    marginVertical: 30,
  },
  errorMessage: {
    color: '#ea3d13',
    fontSize: 16,
    textAlign: 'center',
    marginHorizontal: 10,
    marginTop: 30,
  },
  popup: {
    width: width * 0.8,
  }
});
