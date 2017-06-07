# React Native Fingerprint Scanner

[![Version](https://img.shields.io/npm/v/react-native-fingerprint-scanner.svg)](https://www.npmjs.com/package/react-native-fingerprint-scanner)
[![NPM](https://img.shields.io/npm/dm/react-native-fingerprint-scanner.svg)](https://www.npmjs.com/package/react-native-fingerprint-scanner)

React Native Fingerprint Scanner is a [React Native](http://facebook.github.io/react-native/) library for authenticating users with Fingerprint Scanner (TouchID).

#### iOS Version
The usage of the TouchID is based on a framework, named **Local Authentication**.
It provides a **Default View** that prompts the user to place a finger to the iPhone’s button for scanning.
<div>
<img src="https://github.com/hieuvp/react-native-fingerprint-scanner/raw/master/screenshots/ios-availability.png" height="600">
<img src="https://github.com/hieuvp/react-native-fingerprint-scanner/raw/master/screenshots/ios-authentication.gif" height="600">
</div>

#### Android Version
<div>
<img src="https://github.com/hieuvp/react-native-fingerprint-scanner/raw/master/screenshots/android-availability.png" height="600">
<img src="https://github.com/hieuvp/react-native-fingerprint-scanner/raw/master/screenshots/android-authentication.gif" height="600">
</div>

## Getting started

`$ npm install react-native-fingerprint-scanner --save`

### Mostly automatic installation

`$ react-native link react-native-fingerprint-scanner`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-fingerprint-scanner` and add `ReactNativeFingerprintScanner.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libReactNativeFingerprintScanner.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.hieuvp.fingerprint.ReactNativeFingerprintScannerPackage;` to the imports at the top of the file
  - Add `new ReactNativeFingerprintScannerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-fingerprint-scanner'
  	project(':react-native-fingerprint-scanner').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-fingerprint-scanner/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
    compile project(':react-native-fingerprint-scanner')
  	```


## Usage
```javascript
import ReactNativeFingerprintScanner from 'react-native-fingerprint-scanner';
```
