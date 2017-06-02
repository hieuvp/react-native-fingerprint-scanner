#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else

#import <React/RCTBridgeModule.h>

#endif

@interface ReactNativeFingerprintScanner : NSObject <RCTBridgeModule>

@end
