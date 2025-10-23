plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "ceui.lisa.hermes"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.activity.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    api(libs.androidx.appcompat)
    api(libs.material)

    api(libs.androidx.compose.material.icons.core)
    api(libs.androidx.compose.material.icons.extended)


    api(libs.androidx.navigation3.ui)
    api(libs.androidx.navigation3.runtime)
    api(libs.androidx.lifecycle.viewmodel.navigation3)
    api(libs.androidx.material3.adaptive.navigation3)
    api(libs.kotlinx.serialization.core)
    api(libs.androidx.compose.runtime.livedata)


    api(libs.mmkv)
    api(libs.timber)
    api(libs.gson)

    api("com.blankj:utilcodex:1.31.1")

    api(libs.retrofit)
    api(libs.converter.gson)
    api(libs.okhttp)
    api(libs.logging.interceptor)

    api(libs.zoomimage.compose.sketch4.core)
    api(libs.sketch.compose)
    api(libs.sketch.http)

    api(libs.sketch.extensions.compose)


    val room_version = "2.8.1"
    api("androidx.room:room-runtime:$room_version")
    api("androidx.room:room-ktx:${room_version}")
    ksp("androidx.room:room-compiler:$room_version")

    implementation(project(":models"))



    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}