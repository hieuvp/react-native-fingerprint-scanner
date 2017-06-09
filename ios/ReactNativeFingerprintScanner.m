#import "ReactNativeFingerprintScanner.h"

#if __has_include(<React/RCTUtils.h>) // React Native >= 0.40
#import <React/RCTUtils.h>
#else // React Native < 0.40
#import "RCTUtils.h"
#endif

#import <LocalAuthentication/LocalAuthentication.h>

@implementation ReactNativeFingerprintScanner

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(isSensorAvailable: (RCTResponseSenderBlock)callback)
{
    LAContext *context = [[LAContext alloc] init];
    NSError *error;

    if ([context canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:&error]) {
        callback(@[[NSNull null], @true]);
    } else {
        // Device does not support FingerprintScanner
        callback(@[RCTMakeError(@"RCTFingerprintScannerNotSupported", nil, nil)]);
        return;
    }
}

RCT_EXPORT_METHOD(authenticate: (NSString *)reason
                  callback: (RCTResponseSenderBlock)callback)
{
    LAContext *context = [[LAContext alloc] init];
    NSError *error;

    // Device has FingerprintScanner
    if ([context canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:&error]) {
        // Attempt Authentication
        [context evaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics
                localizedReason:reason
                          reply:^(BOOL success, NSError *error)
         {
             // Failed Authentication
             if (error) {
                 NSString *errorReason;

                 switch (error.code) {
                     case LAErrorAuthenticationFailed:
                         errorReason = @"LAErrorAuthenticationFailed";
                         break;

                     case LAErrorUserCancel:
                         errorReason = @"LAErrorUserCancel";
                         break;

                     case LAErrorUserFallback:
                         errorReason = @"LAErrorUserFallback";
                         break;

                     case LAErrorSystemCancel:
                         errorReason = @"LAErrorSystemCancel";
                         break;

                     case LAErrorPasscodeNotSet:
                         errorReason = @"LAErrorPasscodeNotSet";
                         break;

                     case LAErrorTouchIDNotAvailable:
                         errorReason = @"LAErrorFingerprintScannerNotAvailable";
                         break;

                     case LAErrorTouchIDNotEnrolled:
                         errorReason = @"LAErrorFingerprintScannerNotEnrolled";
                         break;

                     default:
                         errorReason = @"RCTFingerprintScannerUnknownError";
                         break;
                 }

                 NSLog(@"Authentication failed: %@", errorReason);
                 callback(@[RCTMakeError(errorReason, nil, nil)]);
                 return;
             }

             // Authenticated Successfully
             callback(@[[NSNull null], @"Authenticated with Fingerprint Scanner."]);
         }];

    } else {
        // Device does not support FingerprintScanner
        callback(@[RCTMakeError(@"RCTFingerprintScannerNotSupported", nil, nil)]);
        return;
    }
}

@end
