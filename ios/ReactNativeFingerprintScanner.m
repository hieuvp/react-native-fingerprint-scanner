#import "ReactNativeFingerprintScanner.h"

#if __has_include(<React/RCTUtils.h>) // React Native >= 0.40
#import <React/RCTUtils.h>
#else // React Native < 0.40
#import "RCTUtils.h"
#endif

@implementation ReactNativeFingerprintScanner

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(isSensorAvailable: (RCTResponseSenderBlock)callback)
{
    LAContext *context = [[LAContext alloc] init];
    NSError *error;

    if ([context canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:&error]) {
        callback(@[[NSNull null], [self getBiometryType:context]]);
    } else {
        NSString *errorReason;

        switch (error.code) {
            case LAErrorTouchIDNotAvailable:
                errorReason = @"FingerprintScannerNotAvailable";
                break;

            case LAErrorTouchIDNotEnrolled:
                errorReason = @"FingerprintScannerNotEnrolled";
                break;

            default:
                errorReason = @"FingerprintScannerNotSupported";
                break;
        }
        callback(@[RCTMakeError(errorReason, nil, nil)]);
        return;
    }
}

RCT_EXPORT_METHOD(authenticate: (NSString *)reason
                  fallback: (BOOL)fallbackEnabled
                  callback: (RCTResponseSenderBlock)callback)
{
    LAContext *context = [[LAContext alloc] init];
    NSError *error;

    // Toggle fallback button
    if (!fallbackEnabled) {
        context.localizedFallbackTitle = @"";
    }

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
                         errorReason = @"AuthenticationFailed";
                         break;

                     case LAErrorUserCancel:
                         errorReason = @"UserCancel";
                         break;

                     case LAErrorUserFallback:
                         errorReason = @"UserFallback";
                         break;

                     case LAErrorSystemCancel:
                         errorReason = @"SystemCancel";
                         break;

                     case LAErrorPasscodeNotSet:
                         errorReason = @"PasscodeNotSet";
                         break;

                     case LAErrorTouchIDNotAvailable:
                         errorReason = @"FingerprintScannerNotAvailable";
                         break;

                     case LAErrorTouchIDNotEnrolled:
                         errorReason = @"FingerprintScannerNotEnrolled";
                         break;

                     default:
                         errorReason = @"FingerprintScannerUnknownError";
                         break;
                 }

                 NSLog(@"Authentication failed: %@", errorReason);
                 callback(@[RCTMakeError(errorReason, nil, nil)]);
                 return;
             }

             if (success) {
                 // Authenticated Successfully
                 callback(@[[NSNull null], @"Authenticated with Fingerprint Scanner."]);
                 return;
             }

             callback(@[RCTMakeError(@"AuthenticationFailed", nil, nil)]);
         }];

    } else {
        // Device does not support FingerprintScanner
        callback(@[RCTMakeError(@"FingerprintScannerNotSupported", nil, nil)]);
        return;
    }
}

- (NSString *)getBiometryType:(LAContext *)context
{
    if (@available(iOS 11, *)) {
        return context.biometryType == LABiometryTypeFaceID ? @"Face ID" : @"Touch ID";
    }

    return @"Touch ID";
}

@end
