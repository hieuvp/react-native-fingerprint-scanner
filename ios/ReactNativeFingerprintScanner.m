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
        NSString *type = @"touch";
        if(@available(iOS 11.0, *)) {
            if(context.biometryType == LABiometryTypeFaceID) {
                type = @"face";
            }
        }
        callback(@[[NSNull null], type]);
    } else {
        // Device does not support FingerprintScanner
        callback(@[RCTMakeError(@"FingerprintScannerNotSupported", nil, nil)]);
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
    
    void (^fallbackBlock)() = ^(){
        NSError *error;
        LAContext *context = [[LAContext alloc] init];
        if ([context canEvaluatePolicy:LAPolicyDeviceOwnerAuthentication error:&error]) {
            [context evaluatePolicy:LAPolicyDeviceOwnerAuthentication localizedReason:reason reply:^(BOOL success, NSError * _Nullable error) {
                if(error || !success) {
                    NSString *errorReason = @"AuthenticationFailed";
                    NSLog(@"Authentication failed: %@", errorReason);
                    callback(@[RCTMakeError(errorReason, nil, nil)]);
                } else {
                    callback(@[[NSNull null], @"Authenticated with Fingerprint Scanner."]);
                }
            }];
        } else {
            NSString *errorReason = @"AuthenticationFailed";
            NSLog(@"Authentication failed: %@", errorReason);
            callback(@[RCTMakeError(errorReason, nil, nil)]);
        }
    };

    // Device has FingerprintScanner
    if ([context canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:&error]) {
        // Attempt Authentication
        [context evaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics
                localizedReason:reason
                          reply:^(BOOL success, NSError *error)
         {
             // Failed Authentication
             if (error) {
                 
                 if(error.code == LAErrorUserFallback) {
                     fallbackBlock();
                     return;
                 }
                 
                 NSString *errorReason;
                 switch (error.code) {
                     case LAErrorAuthenticationFailed:
                         errorReason = @"AuthenticationFailed";
                         break;

                     case LAErrorUserCancel:
                         errorReason = @"UserCancel";
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
        // This clause is reached if the user has attempted an invalid fingerprint too many times,
        // or if Touch ID is not enabled
        fallbackBlock();
    }
}

@end
