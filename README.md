# Gandalf [![Build Status](https://travis-ci.org/btkelly/gandalf.svg?branch=master)](https://travis-ci.org/btkelly/gandalf) <a href="http://www.detroitlabs.com/"><img src="https://img.shields.io/badge/Sponsor-Detroit%20Labs-000000.svg" /></a> [ ![Download](https://api.bintray.com/packages/btkelly/maven/gandalf/images/download.svg) ](https://bintray.com/btkelly/maven/gandalf/_latestVersion) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Gandalf-green.svg?style=true)](https://android-arsenal.com/details/1/3127)

In the lifetime of any application there will come a time where you need to drop support for a feature, end of life a product, notify about maintenance, any number of other reasons, Gandalf is here to help!

Gandalf will easily add a check to a remote file that can notify a user with a simple alert, inform them of an optional update, and in dire situations block the user from accessing older versions of the application completely (**ex:** security vulnerability has been found).

#### Need an iOS version?
You're in luck! Gandalf was built in parallel with its iOS counterpart,
[LaunchGate](http://dtrenz.github.io/LaunchGate/).

## Download

Gandalf is hosted on the jCenter repository and can be downloaded via Gradle:

```groovy
compile 'com.btkelly:gandalf:{latest_version}'
```

## Usage

The goal of Gandalf was to add this basic boiler plate code to any application quickly. You will need to add the following code to your application as well as host a JSON file on a publicly accessible server.

#### Application Class

Extend the Android [`Application`](http://developer.android.com/reference/android/app/Application.html) class and add the following to the `onCreate()`

```java
public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new Gandalf.Installer()
                .setContext(this)
                .setPackageName("com.my.package")
                .setBootstrapUrl("http://www.example.com/bootstrap.json")
                .install();
    }
}
```

#### Splash Activity

Extend `GandalfActivity` for use as your main "Splash" type activity, this is where the magic will happen. Just provide a layout resource id to display while the bootstrap file is being checked and implement the `youShallPass()` method with what should happen after a successful check.

```java
public class SplashActivity extends GandalfActivity {

    @Override
    public void youShallPass() {
        //After a successful bootstrap check we change the content view, you may also load a new activity or do whatever logic you want after the check is complete.
        setContentView(R.layout.activity_splash_finished_loading);
    }

    @Override
    public int contentView() {
        //While the bootstrap check is running we provide a layout to be displayed
        return R.layout.activity_splash_loading;
    }
}
```

#### Manifest Changes

Add the `android:name` attribute to the `application` tag and specify the path to your custom [`Application`](http://developer.android.com/reference/android/app/Application.html) class from above and set your `SplashActivity` as the entry point for your app.

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="io.github.btkelly.gandalf.example">

    <application
        android:name=".CustomApplication"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme">

        <activity
            android:name=".SplashActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

    </application>

</manifest>
```

#### JSON File

You must host a JSON file remotely and set the URL of this file in the the Gandalf installer. The JSON file use the Android `versionCode` not the `versionName` for version information. By default the format must match the file included below, if you would like to use custom JSON you can provide a [custom deserializer](#custom-json-deserializer).

```json
{
  "android": {
    "alert": {
      "message": "We are currently performing server maintenance. Please try again later.",
      "blocking": true
    },
    "optionalUpdate": {
      "optionalVersion": "6",
      "message": "A new version of the application is available, please click below to update to the latest version."
    },
    "requiredUpdate": {
      "minimumVersion": "7",
      "message": "A new version of the application is available and is required to continue, please click below to update to the latest version."
    }
  }
}
```


That's all that's needed to get Gandalf up and running using the basic settings.

If extending `GandalfActivity` doesn't work for you the `Gandalf` class can be used directly by calling `shallIPass(GandalfCallback callback)`. In this case make sure you respond to the callback methods and make a call to `gandalf.save(Alert alert)` and `gandalf.save(OptionalUpdate optionalUpdate)` if not using the `BootstrapDialogUtil` for your UI.

#### Custom titles, buttons and messages

By default, Gandalf provides default title and button text, and gets the message to display to the user from the JSON file.

However, you are able to use your own strings. To do so, you should use the `DialogStringHolder` class when installing Gandalf.

1. If you do not provide a `DialogStringHolder` during installation, a default instance will be used.
2. If you do not provide message strings in the `DialogStringHolder`, the message from the JSON file will be used.
3. If you provide `DialogStringHolder` but do not set some field manually, default values will be used for all unset strings.
4. You could either pass a `String` instance or a string resource id.

**Remember**: you are not forced to set every string : default values will be used for unset string.

```java
DialogStringsHolder dialogStringsHolder = new DialogStringsHolder(this);

// Defines custom dialog titles
dialogStringsHolder.setAlertTitle(R.string.alert_title);
dialogStringsHolder.setUpdateAvailableTitle(R.string.update_available_title);
dialogStringsHolder.setUpdateRequiredTitle(R.string.update_required_title);

// Defines custom button text
dialogStringsHolder.setCloseAppButtonText(R.string.close_app_button);
dialogStringsHolder.setDownloadUpdateButtonText(R.string.download_update_button);
dialogStringsHolder.setOkButtonText(R.string.ok_button);
dialogStringsHolder.setSkipUpdateButtonText(R.string.skip_update_button);

// Defines custom messages
dialogStringsHolder.setUpdateAvailableMessage(R.string.optional_update_message);
dialogStringsHolder.setUpdateRequiredMessage(R.string.required_update_message);
dialogStringsHolder.setAlertMessage(R.string.required_update_message);

new Gandalf.Installer()
        .setContext(this)
        .setPackageName("com.my.package")
        .setBootstrapUrl("http://www.example.com/bootstrap.json")
        .setDialogStringsHolder(dialogStringsHolder) // Set the custom DialogStringsHolder
        .install();
```

#### Custom JSON Deserializer

You may have a different JSON format for the bootstrap file, no problem! To do this you must provide a [`JsonDeserializer<Bootstrap>`](https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/JsonDeserializer.html) during the Gandalf installation.

```java
new Gandalf.Installer()
        .setContext(this)
        .setPackageName("com.my.package")
        .setBootstrapUrl("http://www.example.com/bootstrap.json")
        .setCustomDeserializer(new JsonDeserializer<Bootstrap>() {
             @Override
             public Bootstrap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

                //Inspect the JsonElement object to retrieve the pieces of the Bootstrap file and return using the builder like below
                 return new Bootstrap.Builder()
                         .setAlertBlocking(false)
                         .setAlertMessage("Down for maintenance.")
                         .setOptionalVersion("8")
                         .setOptionalMessage("There is a newer version of the app, please update below.")
                         .setMinimumVersion("6")
                         .setRequiredMessage("You must update to the latest version of the app.")
                         .build();
             }
         })
        .install();
```

## Example App

Included in the source is a simple example application showing four different launch states based on a remote file. You can load and relaunch the app with each scenario by selecting an option in the Android menu.

#### Screenshots
<img src="http://i.imgur.com/WrWQv1r.png" width="30%"> <img src="http://i.imgur.com/435BD4V.png" width="30%"> <img src="http://i.imgur.com/bnM1yNa.png" width="30%">

## License

Copyright 2016 Bryan Kelly

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.

You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations
under the License.
