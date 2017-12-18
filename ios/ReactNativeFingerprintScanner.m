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

    if ([context canEvaluatePolicy:LAPolicyDeviceOwnerAuthentication error:&error]) {
        callback(@[[NSNull null], @true]);
    } else {
        // Device does not support FingerprintScanner
        callback(@[RCTMakeError(@"FingerprintScannerNotSupported", nil, nil)]);
        return;
    }
}

RCT_EXPORT_METHOD(authenticate: (NSString *)reason
                  fallback: (BOOL)fallbackEnabled
				  fallbackText: (NSString *)fallbackTitle
                  callback: (RCTResponseSenderBlock)callback)
{
    LAContext *context = [[LAContext alloc] init];
    NSError *error;

    // Toggle fallback button
    if (!fallbackEnabled) {
        context.localizedFallbackTitle = @"";
    } else {
		// Use the user defined fallbackTitle if provided
		if (fallbackTitle.length != 0) {
			context.localizedFallbackTitle = fallbackTitle;
		}
	}

    // Device has FingerprintScanner
    if ([context canEvaluatePolicy:LAPolicyDeviceOwnerAuthentication error:&error]) {
        context.interactionNotAllowed = false;
        // Attempt Authentication
        [context evaluatePolicy:LAPolicyDeviceOwnerAuthentication
                localizedReason:reason
                          reply:^(BOOL success, NSError *error)
         {
             // Failed Authentication
             if (error) {
                 NSString *errorReason;
                 NSOperatingSystemVersion ios11_0_0 = (NSOperatingSystemVersion){11, 0, 0};
                if ([[NSProcessInfo processInfo] isOperatingSystemAtLeastVersion:ios11_0_0]) {
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
                            
                        case LAErrorInvalidContext:
                            errorReason = @"InvalidContext";
                            break;
                            
                        case LAErrorNotInteractive:
                            errorReason = @"NotInteractive";
                            break;
                            
                        case LAErrorBiometryLockout:
                            errorReason = @"FingerprintScannerLockout";
                            break;
                            
                        default:
                            errorReason = @"FingerprintScannerUnknownError";
                            break;
                    }
                } else {
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
                            
                        case LAErrorInvalidContext:
                            errorReason = @"InvalidContext";
                            break;
                            
                        case LAErrorNotInteractive:
                            errorReason = @"NotInteractive";
                            break;
                            
                        case LAErrorTouchIDLockout:
                            errorReason = @"FingerprintScannerLockout";
                            break;
                            
                        default:
                            errorReason = @"FingerprintScannerUnknownError";
                            break;
                    }
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
        callback(@[RCTMakeError(@"FingerprintScannerNotSupported", nil, nil)]);
        [context invalidate];
        return;
    }
}

@end
