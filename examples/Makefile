all: test-ios
install:
	npm ci
clean:
	cd android && ./gradlew clean
test-ios: install build-ios
build-ios:
	cd ios && xcodebuild clean build -scheme examples \
		-workspace examples.xcworkspace \
		-configuration Debug -sdk iphonesimulator \
		CODE_SIGNING_REQUIRED=NO CODE_SIGN_IDENTITY=""
test-android: install build-android
build-android:
	cd android && chmod +x ./gradlew && TERM=dumb ./gradlew assembleDebug --stacktrace
.PHONY: all

