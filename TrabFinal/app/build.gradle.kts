plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.emerson.trabfinal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.emerson.trabfinal"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.firebase:firebase-analytics:22.0.2")
    implementation(libs.play.services.maps)
    implementation(libs.viewbinding)
    testImplementation("junit:junit:4.13.2")


    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    implementation ("com.hbb20:ccp:2.5.0")

    /*AUTHENTICATION FIREBASE*/
    implementation("com.google.firebase:firebase-auth:23.0.0")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))

//    DattaBase firebase
    implementation("com.google.firebase:firebase-database:21.0.0")

    implementation ("com.firebaseui:firebase-ui-database:8.0.0")

    implementation("com.google.android.gms:play-services-maps:18.2.0")
   implementation("com.google.firebase:firebase-analytics")


}