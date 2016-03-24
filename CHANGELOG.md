# Change Log

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