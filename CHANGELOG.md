# Change Log

## v1.4.0

_09-09-2019__

- Updated project dependencies.
- Updated Android Gradle plugin and Gradle version.

## v1.3.5

_03-06-2017__

- Updated project dependencies.
- Updated Android Gradle plugin and Gradle version.

## v1.3.4

_01-19-2017__

- Changed protection level of `downloadFile()` in FileDownloadUpdateListener.java to allow overriding the default behavior.

## v1.3.3

_10-24-2016__

- Added ability to provide a custom OkHttpClient

## v1.3.2

_08-24-2016__

- Changed the priority of Gandalf actions, an alert will now show before an optional update
- Added consumer proguard rules to fix Gson parsing when minify is enabled

## v1.3.1

_08-16-2016__

- Removed application block from lib manifest to avoid merging conflicts.

## v1.3.0

_08-04-2016_

- Added ability to set a custom listener when the user opts to update.
- Included a basic FileDownloadUpdateListener for specifying a custom apk to download.

## v1.2.1

_03-24-2016_

- Network code now handles a null JSON response and fails silently.

## v1.2.0

_03-21-2016_

- Library is a bit more error tolerant and will silently fail on malformed JSON
- New class `DialogStringsHolder` which allows users to customize titles, buttons, and messages. This adds the ability to optionally override the JSON response and display a specific message.

## v1.1.0

_02-15-2016_

- Added the ability for a consumer to set a custom JSON deserializer.

## v1.0.0

_02-02-2016_

Initial release, allows a consumer to use Gandalf with a specific JSON format. This includes an abstract start activity that may be extended for the easiest integration. The consumer can also use the Gandalf class directly to provide a custom UI or integrate outside of an activity's lifecycle.
