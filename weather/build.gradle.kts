plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.weather"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String","API_KEY", "\"2446e3823cfb46b7bc8d950370133f9c\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.ui)
    implementation(libs.androidx.material3.v100)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.coroutines.android.v152)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.drawerlayout)
    implementation(libs.androidx.activity.compose.v160)
    implementation(libs.gson)
    implementation(libs.androidx.material3.v100alpha01)
    implementation(libs.androidx.drawerlayout.v111)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.ui.tooling.preview.v150)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.coil.compose)
    implementation(libs.androidx.webkit)
    implementation(libs.jsoup)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx.v287)
    implementation(libs.androidx.datastore.preferences.v100)
    implementation(libs.coil.compose.v210)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.foundation)
    implementation (libs.glide)

}