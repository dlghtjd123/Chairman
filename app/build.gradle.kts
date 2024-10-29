plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.chairman"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.chairman"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("androidx.appcompat:appcompat:1.3.1") // 기본 라이브러리
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    add("implementation", "com.squareup.retrofit2:retrofit:2.9.0")
    add("implementation", "com.squareup.retrofit2:converter-gson:2.9.0")
    add("implementation", "com.squareup.okhttp3:okhttp:4.9.1")
}