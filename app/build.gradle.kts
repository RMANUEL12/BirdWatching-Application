import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

android {
    namespace = "com.example.bobolinkbirdwatching"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bobolinkbirdwatching"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "EBIRD_API_KEY", "\"${localProperties["EBIRD_API_KEY"]}\"")
        buildConfigField ("String", "GoogleMapsDirectionsApiKey", "\"${localProperties["GoogleMapsDirectionsApiKey"]}\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Importing the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))

    // Adding the dependency for the Firebase Authentication library
    implementation("com.google.firebase:firebase-auth")

    implementation("com.google.firebase:firebase-analytics")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation ("com.google.maps:google-maps-services:0.15.0")
    implementation ("com.google.maps.android:android-maps-utils:0.5")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")// replace x.x with the latest version
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.android.gms:play-services-location:21.0.1") // for JSON converter
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.1.0")

//    For the Fragments
    implementation ("androidx.navigation:navigation-fragment-ktx:2.4.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.4.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")



    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
