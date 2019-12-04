#if __has_include(<React/RCTBridgeModule.h>) // React Native >= 0.40
#import <React/RCTBridgeModule.h>
#else // React Native < 0.40
#import "RCTBridgeModule.h"
#endif
#import <LocalAuthentication/LocalAuthentication.h>

@interface ReactNativeFingerprintScanner : NSObject <RCTBridgeModule>
	- (NSString *_Nonnull)getBiometryType:(LAContext *_Nonnull)context;
@end
