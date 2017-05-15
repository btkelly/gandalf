#!/bin/bash
set -ev

if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; then
    ./gradlew clean gandalf:gnagCheck example:assembleDebug
else
    ./gradlew clean gandalf:gnagReport example:assembleDebug -PauthToken="${GNAG_AUTH_TOKEN}" -PissueNumber="${TRAVIS_PULL_REQUEST}"
fi
