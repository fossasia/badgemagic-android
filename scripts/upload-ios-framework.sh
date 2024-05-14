#!/bin/sh

git config --global user.name "Travis CI"
git config --global user.email "noreply+travis@fossasia.org"

git clone --quiet --branch=ios-framework https://fossasia:$GITHUB_KEY@github.com/fossasia/badgemagic-android ios-framework > /dev/null
cd ios-framework

rm -rf $TRAVIS_BRANCH

mkdir -p $TRAVIS_BRANCH && cp -r ../BadgeMagicModule/build/xcode-frameworks/BadgeMagicModule.framework $TRAVIS_BRANCH/.

git checkout --orphan temporary

git add --all .
git commit -am "[Auto] Update iOS-Framework($(date +%Y-%m-%d.%H:%M:%S))"

git branch -D ios-framework
git branch -m ios-framework

git push origin ios-framework --force --quiet > /dev/null