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
            case LAErrorTouchIDNotAvailable:
                code = @"FingerprintScannerNotAvailable";
                message = [self getBiometryType:context];
                break;

            case LAErrorTouchIDNotEnrolled:
                code = @"FingerprintScannerNotEnrolled";
                message = [self getBiometryType:context];
                break;

            case LAErrorTouchIDLockout:
                code = @"AuthenticationLockout";
                message = [self getBiometryType:context];
                break;

            case LAErrorAuthenticationFailed:
                code = @"AuthenticationFailed";
                message = [self getBiometryType:context];
                break;

            case LAErrorUserCancel:
                code = @"UserCancel";
                message = [self getBiometryType:context];
                break;

            case LAErrorUserFallback:
                code = @"UserFallback";
                message = [self getBiometryType:context];
                break;

            case LAErrorSystemCancel:
                code = @"SystemCancel";
                message = [self getBiometryType:context];
                break;

            case LAErrorPasscodeNotSet:
                code = @"PasscodeNotSet";
                message = [self getBiometryType:context];
                break;

            default:
                code = @"AuthenticationNotMatch";
                message = nil;
                break;
        }
        NSLog(@"Authentication failed: %@", code);
        callback(@[RCTJSErrorFromCodeMessageAndNSError(code, code, nil)]);
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

                     case LAErrorTouchIDLockout:
                         errorReason = @"AuthenticationLockout";
                         break;

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
                         errorReason = @"AuthenticationNotMatch";
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
        // Device does not support FingerprintScanner
        // callback(@[RCTJSErrorFromCodeMessageAndNSError(@"FingerprintScannerNotSupported", @"FingerprintScannerNotSupported", nil)]);
        NSString *errorReason;

        switch (error.code) {
            case LAErrorTouchIDNotAvailable:
                errorReason = @"FingerprintScannerNotAvailable";
                break;

            case LAErrorTouchIDNotEnrolled:
                errorReason = @"FingerprintScannerNotEnrolled";
                break;

            case LAErrorTouchIDLockout:
                errorReason = @"AuthenticationLockout";
                break;

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

            default:
                errorReason = @"AuthenticationNotMatch";
                break;
        }
        callback(@[RCTJSErrorFromCodeMessageAndNSError(errorReason, errorReason, nil)]);
        return;
    }
}

RCT_EXPORT_METHOD(authenticateDevice: (RCTResponseSenderBlock)callback)
{
    LAContext *context = [[LAContext alloc] init];
    [context evaluatePolicy:LAPolicyDeviceOwnerAuthentication localizedReason: @" " reply:^(BOOL success, NSError * _Nullable error) {
        if(error) {
            NSString *errorReason = @"UserDeviceCancel";
            NSLog(@"Authentication failed: %@", errorReason);
            callback(@[RCTJSErrorFromCodeMessageAndNSError(errorReason, errorReason, nil)]);
        } else {
         callback(@[[NSNull null], @"Authentication unlock."]);
        }
    }];
}

- (NSString *)getBiometryType:(LAContext *)context
{
    if (@available(iOS 11, *)) {
        return context.biometryType == LABiometryTypeFaceID ? @"Face ID" : @"Touch ID";
    }

    return @"Touch ID";
}

@end
