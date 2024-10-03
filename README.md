# Dana SDK Integration Guide

Welcome to the Dana SDK Integration Guide. Follow the steps below to integrate the SDK into your Android project.

## Prerequisites

Ensure you have the latest version of Android Studio and your project is set up with Kotlin support.

## Step 1: Add the SDK to Your Project

1. Copy the `danasdk-release.aar` file into your project's `app/libs` directory.
2. Open your module's `build.gradle.kts` file and add the following lines to the `dependencies`
   block:

    ```kotlin
    implementation(files("libs/danasdk-release.aar"))
    ```

## Step 2: Add Required Dependencies

The Liveliness SDK requires the following dependencies. Add them to your `build.gradle.kts` file
inside the `dependencies` block:

   ```kotlin
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.29")
    implementation("com.vanniktech:android-image-cropper:4.5.0")
   ```

## Step 3: Sync Your Project

After adding the SDK and its dependencies, sync your project with Gradle files by clicking
on `File > Sync Project with Gradle Files` in Android Studio.

## Step 4: Add Permissions

If you haven't already, add the following permissions to your `AndroidManifest.xml` file:

   ```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_SMS" />
   ```

## Step 5: Define a Kotlin Helper Class

Create a Kotlin helper class with a companion object that contains a static function to execute the Dana SDK.
This is not required to create a separate class to execute Dana SDK. you can make it in your own way.

   ```kotlin
   import android.app.Activity
   import com.danafintech.danasdk.config.DanaConfig
   import com.danafintech.danasdk.config.DanaEnvironment
   import com.danafintech.danasdk.config.DanaSdk
   import com.danafintech.danasdk.config.User
   
   class DanaSdkHelper {
      companion object {
         @JvmStatic
         fun launchDanaSdk(phone:String, activity: Activity) {
            val config = DanaConfig(
               companyKey = "", // TODO: Replace
               user = User(
                  phone = phone,
                  firebaseToken = ""
               ),
               userData = mapOf(),
               environment = DanaEnvironment.DEVELOPMENT,
               showActionBar = false,
               showDanaDialog = false,
               onLogOut = {} // TODO: Implement your logout logic
            )
            DanaSdk(config).launchSdk(activity)
         }
      }
   }
   ```

## Step 6: Launch Dana SDK

Now you can use the launchDanaSdk function from both Java and Kotlin to execute the Dana SDK.
DanaSdkHelper.launchDanaSdk(application, this)

   ```kotlin
   DanaSdkHelper.launchDanaSdk("USER_PHONE_NUMBER", this)
   ```

## Step 6: ProGuard Rules

If you are using ProGuard, add the necessary rules for the Dana SDK and its dependencies to
your `proguard-rules.pro` file. Refer to the respective libraries' documentation for specific rules.

## Step 7: Run Your Project

After completing the integration steps, run your project to test the Dana SDK.

## Dana SDK Configuration
### companyKey
Replace the `companyKey` field with your company's specific identifier provided by Dana.

### User
Provide the following information within the `User` object:

- `phone`: The user's phone number.
- `firebaseToken`: The Firebase Cloud Messaging (FCM) token for the user's device, if you want to notify users via Dana.

### language
Specify the desired language for the Dana SDK (e.g., `DanaLanguage.ENGLISH`).

### environment
Set the environment variable (`DANA_ENVIRONMENT`) based on your application's deployment environment (e.g., `DanaEnvironment.DEVELOPMENT` or `DanaEnvironment.PRODUCTION`).

### userData (Optional)
If you choose not to use Dana eKYC services and need to provide user profile data manually, replace placeholder values in the `userData.profile` map with actual user data:

- `name`: User's full name.
- `name_bn`: User's name in Bangla (if applicable).
- `email`: User's email address.
- `nid`: User's National ID (if applicable).
- `father_name`: User's father's name.
- `father_name_bn`: User's father's name in Bangla (if applicable).
- `mother_name`: User's mother's name.
- `mother_name_bn`: User's mother's name in Bangla (if applicable).
- `pre_addr`: User's present address.
- `pre_addr_bn`: User's present address in Bangla (if applicable).
- `per_addr`: User's permanent address.
- `per_addr_bn`: User's permanent address in Bangla (if applicable).
- `dob`: User's date of birth (formatted as `YYYY/MM/DD`).
- `profession`: User's profession or occupation.
- `photo`: URL to the user's profile photo.

#### Example
```kotlin
userData = mapOf(
    "variable" to mapOf<String, Any>(),
    "profile" to mapOf(
        "name" to "USER_NAME",
        "name_bn" to "USER_NAME_BANGLA",
        "email" to "USER_EMAIL",
        "nid" to "USER_NID",
        "father_name" to "USER_FATHER_NAME",
        "father_name_bn" to "USER_FATHER_NAME_BANGLA",
        "mother_name" to "USER_MOTHER_NAME",
        "mother_name_bn" to "USER_MOTHER_NAME_BANGLA",
        "pre_addr" to "USER_PRESENT_ADDRESS",
        "pre_addr_bn" to "USER_PRESENT_ADDRESS_BANGLA",
        "per_addr" to "USER_PERMANENT_ADDRESS",
        "per_addr_bn" to "USER_PERMANENT_ADDRESS_BANGLA",
        "dob" to "YYYY/MM/DD (USER_DATE_OF_BIRTH)",
        "profession" to "USER_PROFESSION",
        "photo" to "USER_PHOTO_URL"
    )
)
```

### onLogOut
Implement the `onLogOut` function to handle user logout events. This function will be called when the user logs out from the Dana SDK.

### Dana SMS SDK
Dana SMS Sdk is integrated with Dana SDK. If you want to use Dana SMS SDK Separately, you can use the following code to run Dana SMS SDK.

   ```kotlin
   @JvmStatic
   fun runSmsSdk(activity: Activity) {
      (activity as? LifecycleOwner)?.lifecycleScope?.launch {
          val isSend =
              DanaCreditScoreConfig(
                  "DANA_ACCESS_TOKEN",
                  "DANA_REFRESH_TOKEN",
                  DanaSMSEnvironment.DEVELOPMENT,
              ).initializeSmsParsing(activity)
          if (isSend) {
              Log.d("CreditScoreHelper", "Successfully submitted transaction data")
          } else {
              Log.d("CreditScoreHelper", "Failed to submit transaction data")
          }
      }
   }
   ```

## Support

For any issues or additional support, please contact our developer support team.

Thank you for choosing Dana SDK for your project!
