/**
 * Metro configuration for React Native
 * https://github.com/facebook/react-native
 *
 * @format
 */

const path = require('path');

// https://github.com/facebook/metro/issues/1#issuecomment-501143843
// https://github.com/facebook/metro/issues/7
module.exports = {
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: false
      }
    })
  },
  resolver: {
    /* This configuration allows you to build React-Native modules and
     * test them without having to publish the module. Any exports provided
     * by your source should be added to the "target" parameter. Any import
     * not matched by a key in target will have to be located in the embedded
     * app's node_modules directory.
     */
    extraNodeModules: new Proxy(
      /* The first argument to the Proxy constructor is passed as
       * "target" to the "get" method below.
       * Put the names of the libraries included in your reusable
       * module as they would be imported when the module is actually used.
       */
      {
        'react-native-fingerprint-scanner': path.resolve(__dirname, '../src/')
      },
      {
        get: (target, name) =>
        {
          if (target.hasOwnProperty(name))
          {
            return target[name];
          }
          return path.join(process.cwd(), `node_modules/${name}`);
        }
      }
    )
  },
  projectRoot: path.resolve(__dirname),
  watchFolders: [
    path.resolve(__dirname, '../src')
  ]
};
