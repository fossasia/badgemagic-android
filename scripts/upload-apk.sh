#!/bin/sh
set -e

git config --global user.name "Travis CI"
git config --global user.email "noreply+travis@fossasia.org"

export DEPLOY_BRANCH=${DEPLOY_BRANCH:-development}
export PUBLISH_BRANCH=${PUBLISH_BRANCH:-master}

if [ "$TRAVIS_PULL_REQUEST" != "false" -o "$TRAVIS_REPO_SLUG" != "fossasia/badge-magic-android" ] || ! [ "$TRAVIS_BRANCH" == "$DEPLOY_BRANCH" -o "$TRAVIS_BRANCH" == "$PUBLISH_BRANCH" ]; then
    echo "We upload apk only for changes in development or master, and not PRs. So, let's skip this shall we ? :)"
    exit 0
fi

./gradlew bundleRelease

git clone --quiet --branch=apk https://fossasia:$GITHUB_KEY@github.com/fossasia/badge-magic-android apk > /dev/null
cd apk

if [[ "$TRAVIS_BRANCH" == "$PUBLISH_BRANCH" ]]; then
	/bin/rm -f *
else
	/bin/rm -f badge-magic-dev-*.apk
fi

find ../app/build/outputs -type f \( -name '*.apk' -o -name '*.aab' \) -exec cp -v {} . \;

#removing unused apps
for file in app*; do
    if [[ ${file} =~ "unsigned" || ${file} =~ "unaligned" ]]; then
        rm ${file}
    else
        if [[ "$TRAVIS_BRANCH" == "$PUBLISH_BRANCH" ]]; then
            if [[ ${file} =~ ".aab" ]]; then
                mv ${file} badge-magic-master-${file}
            else
                mv ${file} badge-magic-master-${file:4}
            fi
        elif [[ "$TRAVIS_BRANCH" == "$DEPLOY_BRANCH" ]]; then
            if [[ ${file} =~ ".aab" ]]; then
                mv ${file} badge-magic-dev-${file}
            else
                mv ${file} badge-magic-dev-${file:4}
            fi
        fi
    fi
done

# Create a new branch that will contains only latest apk
git checkout --orphan temporary

# Add generated APK
git add --all .
git commit -am "[Auto] Update Test Apk ($(date +%Y-%m-%d.%H:%M:%S))"

# Delete current apk branch
git branch -D apk
# Rename current branch to apk
git branch -m apk

# Force push to origin since histories are unrelated
git push origin apk --force --quiet > /dev/null

# Publish App to Play Store
if [[ "$TRAVIS_BRANCH" != "$PUBLISH_BRANCH" ]]; then
    echo "We publish apk only for changes in master branch. So, let's skip this shall we ? :)"
    exit 0
fi

gem install fastlane
fastlane supply --aab badge-magic-master-app.aab --track alpha --json_key fastlane.json --package_name $PACKAGE_NAME
