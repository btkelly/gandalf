#!/bin/bash
set -ev

if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; then
    ./gradlew clean example:assembleDebug
else
    ./gradlew clean example:assembleDebug -PissueNumber="${TRAVIS_PULL_REQUEST}"
fi
