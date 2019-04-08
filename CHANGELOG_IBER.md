# iBer对本库的修改

## 1. 源代码出处

[GitHub:react-native-fingerprint-scanner](https://github.com/hieuvp/react-native-fingerprint-scanner.git)
版本: `"version": "2.5.0"`

## 2. 第一次修改

### 2.1 仅修改android的`build.gradle`

修改后:


```groove
def safeExtGet(prop, fallback) {
    rootProject.ext.has(prop) ? rootProject.ext.get(prop) : fallback
}

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:1.3.1'
    }
}

apply plugin: 'com.android.library'

android {

    compileSdkVersion safeExtGet('compileSdkVersion', 25)
    buildToolsVersion safeExtGet('buildToolsVersion', '25.0.3')

    defaultConfig {
        minSdkVersion safeExtGet('minSdkVersion', 16)
        targetSdkVersion safeExtGet('targetSdkVersion', 25)
        versionCode 1
        versionName "1.0"
    }
    lintOptions {
        abortOnError false
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compile 'com.facebook.react:react-native:+'
    compile "com.wei.android.lib:fingerprintidentify:${safeExtGet("fingerprintidentify", "1.2.1")}"
}


```

## 2.2 iBer第一次发版

`react-native-fingerprint-iber-scanner@1.0.0`