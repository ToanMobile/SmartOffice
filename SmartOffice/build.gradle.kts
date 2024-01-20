plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.artifex"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.zxing:core:3.5.2")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("com.vungle:publisher-sdk-android:6.12.1")
    implementation("com.google.cloud:google-cloud-logging:3.15.14")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout-core:1.0.4")
    // implementation("com.bytedance.applog:RangersAppLog-Lite-cn:6.14.2")

    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0-RC2")
    implementation("com.yandex.android:mobmetricapushlib:1.5.0")
    implementation("androidx.concurrent:concurrent-futures:1.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.kochava.tracker:tracker:5.2.0") // Required
    implementation("com.kochava.tracker:events:5.2.0") // Optional
    implementation("com.kochava.tracker:engagement:5.2.0") // Optional
    implementation("com.kochava.tracker:datapointnetwork:5.2.0") // Optional
    implementation("com.kochava.tracker:legacyreferrer:5.2.0")
    implementation(files("libs/bcpg-jdk15on-1.58.0.0.jar"))
    implementation(files("libs/bcpkix-jdk15on-1.58.0.0.jar"))
    implementation(files("libs/core-1.58.0.0.jar"))
    implementation(files("libs/prov-1.58.0.0.jar"))
    implementation(files("libs/core-1.58.0.0-sources.jar"))
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Optional
}
