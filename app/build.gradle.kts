plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.poiesistech.admobads"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.poiesistech.admobads"
        minSdk = 24
        targetSdk = 34
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
        debug {

            resValue("string", "admob_inter_ids", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "admob_rewarded_ids", "ca-app-pub-3940256099942544/5224354917")
            resValue("string", "admob_rewarded_inter_ids", "ca-app-pub-3940256099942544/5354046379")
            resValue("string", "admob_native_ids", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "admob_banner_ids", "ca-app-pub-3940256099942544/2014213617")
            resValue("string", "admob_app_open_ids", "ca-app-pub-3940256099942544/9257395921")
            resValue("string", "admob_app_id", "ca-app-pub-3940256099942544~3347511713")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.process)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //admob
    implementation(libs.android.admob)

    //koin
//    implementation(libs.koin.bom)
//    implementation(libs.koin.core)
    implementation(libs.koin.android)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remote.cfg)

    //sdp
    implementation(libs.dimen.sdp)
}