using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Com.Reactlibrary.RNReactNativeFingerprintScanner
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNReactNativeFingerprintScannerModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNReactNativeFingerprintScannerModule"/>.
        /// </summary>
        internal RNReactNativeFingerprintScannerModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNReactNativeFingerprintScanner";
            }
        }
    }
}
