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
        NSString *code;
        NSString *message;

        switch (error.code) {
            case LAErrorBiometryNotAvailable:
                code = @"FingerprintScannerNotAvailable";
                message = [self getBiometryType:context];
                break;
                
            case LAErrorBiometryNotEnrolled:
                code = @"FingerprintScannerNotEnrolled";
                message = [self getBiometryType:context];
                break;

            case LAErrorBiometryLockout:
                code = @"DeviceLockedPermanent";
                message = [self getBiometryType:context];
                break;
            
            case LAErrorPasscodeNotSet:
                code = @"PasscodeNotSet";
                message = [self getBiometryType:context];
                break;

            default:
                code = @"FingerprintScannerNotSupported";
                message = nil;
                break;
        }

        callback(@[RCTJSErrorFromCodeMessageAndNSError(code, message, nil)]);
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
    if ([context canEvaluatePolicy:LAPolicyDeviceOwnerAuthentication error:&error]) {
        // Attempt Authentication
        [context evaluatePolicy:LAPolicyDeviceOwnerAuthentication
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

                     case LAErrorBiometryNotAvailable:
                         errorReason = @"FingerprintScannerNotAvailable";
                         break;

                     case LAErrorBiometryNotEnrolled:
                         errorReason = @"FingerprintScannerNotEnrolled";
                         break;

                     case LAErrorBiometryLockout:
                         errorReason = @"DeviceLockedPermanent";
                         break;

                     default:
                         errorReason = @"FingerprintScannerUnknownError";
                         break;
                 }

                 NSLog(@"Authentication failed: %@", errorReason);
                 callback(@[RCTJSErrorFromCodeMessageAndNSError(errorReason, errorReason, nil)]);
                 return;
             }

             if (success) {
                 // Authenticated Successfully
                 callback(@[[NSNull null], @"Authenticated with Fingerprint Scanner."]);
                 return;
             }

             callback(@[RCTJSErrorFromCodeMessageAndNSError(@"AuthenticationFailed", @"AuthenticationFailed", nil)]);
         }];

    } else {
        if (error) {
            NSString *errorReason;

            switch (error.code) {
                case LAErrorBiometryNotAvailable:
                    errorReason = @"FingerprintScannerNotAvailable";
                    break;

                case LAErrorBiometryNotEnrolled:
                    errorReason = @"FingerprintScannerNotEnrolled";
                    break;

                case LAErrorBiometryLockout:
                    errorReason = @"DeviceLockedPermanent";
                    break;

                case LAErrorPasscodeNotSet:
                    errorReason = @"PasscodeNotSet";
                    break;

                default:
                    errorReason = @"FingerprintScannerNotSupported";
                    break;
            }

            NSLog(@"Authentication failed: %@", errorReason);
            callback(@[RCTJSErrorFromCodeMessageAndNSError(errorReason, errorReason, nil)]);
            return;
        }
        // Device does not support FingerprintScanner
        callback(@[RCTJSErrorFromCodeMessageAndNSError(@"FingerprintScannerNotSupported", @"FingerprintScannerNotSupported", nil)]);
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
