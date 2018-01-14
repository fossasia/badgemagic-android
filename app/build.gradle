apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'

android {
    compileSdkVersion 27
    buildToolsVersion '27.0.3'

    defaultConfig {
        applicationId 'com.nilhcem.blenamebadge'
        minSdkVersion 18
        targetSdkVersion 27
        versionCode 1
        versionName '1.0'
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jre7:$kotlin_version"

    implementation 'com.android.support:appcompat-v7:27.0.2'
    implementation 'com.android.support.constraint:constraint-layout:1.0.2'

    implementation 'com.jakewharton.timber:timber:4.6.0'

    implementation 'no.nordicsemi.android.support.v18:scanner:1.0.0'

    testImplementation 'junit:junit:4.12'
    testImplementation 'com.nhaarman:mockito-kotlin-kt1.1:1.5.0'
    testImplementation 'org.amshove.kluent:kluent:1.31'
}
