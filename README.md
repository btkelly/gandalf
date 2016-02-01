# Gandalf [![Build Status](https://travis-ci.org/btkelly/gandalf.svg?branch=master)](https://travis-ci.org/btkelly/gandalf) <a href="http://www.detroitlabs.com/"><img src="https://img.shields.io/badge/Sponsor-Detroit%20Labs-000000.svg" /></a>

In the lifetime of any application there will come a time where you need to drop support for a feature, end of life a product, notify about maintenance, any number of other reasons, Gandalf is here to help!

Gandalf will easily add a check to a remote file that can notify a user with a simple alert, inform them of an optional update, and in dire situations block the user from accessing older versions of the application completely (**ex:**security vulnerability has been found).

# Usage

The goal of Gandalf was to add this basic boiler plate code you any application quickly. You will need to add the following code to your application as well as host a json file on a publicly accessible server with the format included below.

#### Application Class

Extend the Android [`Application`](http://developer.android.com/reference/android/app/Application.html) class and add the following to the `onCreate()`

```java
@Override
public void onCreate() {
    super.onCreate();
    new Gandalf.Installer()
            .setContext(this)
            .setPackageName("com.my.package")
            .setBootstrapUrl("http://www.example.com/bootstrap.json)
            .install();
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

#### Json File

You must host a json file remotely and set the url of this file in the the Gandalf installer. The Json file must match the following format and use the Android `versionCode` not the `versionName` for version information.

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


That's all that's needed to get Gandalf up and running using the basic settings, if extending `GandalfActivity` doesn't work for you the `Gandalf` class can be used directly by calling `shallIPass(GandalfCallback callback)`. 

## Example App

Included in the source is a simple example application showing four different launch states based on a remote file. You can load and relaunch the app with each scenario by selecting an option in the Android menu.

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
