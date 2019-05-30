<img height="200px" src="./docs/images/app_icon.png" align="right" />

# Badge Magic [![Build Status](https://travis-ci.org/fossasia/badge-magic-android.svg?branch=development)](https://travis-ci.org/fossasia/badge-magic-android) [![Join the chat at https://gitter.im/fossasia/badge-magic](https://badges.gitter.im/fossasia/badge-magic.svg)](https://gitter.im/fossasia/badge-magic?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
> Magically Create Symbols and Text on LED Name Badges using Bluetooth

The Badge Magic Android app lets you create scrolling symbols and text on LED name badges through Bluetooth. The app provides options to portray names, clipart and simple animations on LED badges. For the data transfer from the smartphone to the LED badge we use Bluetooth. The project is based on the work of [Nilhcem](https://github.com/Nilhcem).

<table align='right'>
    <tr>
        <a href='https://play.google.com/store/apps/details?id=org.fossasia.badgemagic'><img align='center' height='55' src='./docs/images/google_play_badge.png'></a>
    </tr>
    <tr>
        <a href='https://f-droid.org/en/packages/org.fossasia.badgemagic/'><img align='center' alt='Get it on F-Droid' src='https://f-droid.org/badge/get-it-on.png' height="83"/></a>
    </tr>
</table>

## Download

* Download **[Badge Magic Dev Release here](https://github.com/fossasia/badge-magic-android/blob/apk/badge-magic-dev-release.apk)**
* Download Tested [LED Badge Version here](https://github.com/fossasia/badge-magic-android/blob/apk/LED-badge-dev.apk)

## Permissions
* Bluetooth: For sending data to the badge.
* GPS Location: This has been the standard set by Android for use with Bluetooth Low Energy (BLE) devices. For more information, please read the notes on [Android website](https://source.android.com/devices/bluetooth/ble).

## Communication

Please talk to us on the badge-magic [Gitter channel here](https://gitter.im/fossasia/badge-magic).

## Reverse-Engineering Bluetooth LE Devices

Security in Bluetooth LE devices is optional, and many cheap products you can find on the market are not secured at all. This applies to our Bluetooth LED Badge. While this could lead to some privacy issues, this can also be a source of fun, especially when you want to use an LED Badge in a different way. It also makes it easy for us to get started with the development of a Free and Open Source Android app. 

As we understand how the Bluetooth LED badge works, converting a text to multiple byte arrays, we can send using the Bluetooth LE APIs. An indepth blog post about reverse-engineering the Bluetooth community [is here](http://nilhcem.com/iot/reverse-engineering-bluetooth-led-name-badge). 

The implementation in the Android app consists of manipulating bits. That may be tricky. A single bit error and nothing will work, plus it will be hard to debug. For those reasons, and since the specs are perfectly clear the reverse engineer Gautier Mechling strongly recommends to start writing unit tests before the code implementation. 

## Available Devices

There are a number of devices with Bluetooth on the market. As far as we can tell they are mostly from the same manufacturer. When you get a device ensure it comes with Bluetooth. There are devices that don't support Bluetooth. These are not supported in the app currently.
* Get one from [here](https://sg.pslab.io/product/led-badge/)

## Screenshots

| <!-- -->    | <!-- -->    | <!-- -->    |
|-------------|-------------|-------------|
| <img src="./docs/images/screen-1.png" width="288" /> <img src="./docs/images/screen-1-hard.png" width="288" /> | <img src="./docs/images/screen-2.png" width="288" /> <img src="./docs/images/screen-2-hard.png" width="288" /> | <img src="./docs/images/screen-3.png" width="288" /> <img src="./docs/images/screen-3-hard.png" width="288" /> |
| <!-- -->    | <!-- -->    | <!-- -->    |
| <img src="./docs/images/screen-4.png" width="288" /> <img src="./docs/images/screen-4-hard.png" width="288" /> | <img src="./docs/images/screen-5.png" width="288" /> <img src="./docs/images/screen-5-hard.png" width="288" /> | <img src="./docs/images/screen-6.png" width="288" /> <img src="./docs/images/screen-6-hard.png" width="288" /> |

## Contributions Best Practices

### For first time Contributor

First time contributors can read [CONTRIBUTING.md](CONTRIBUTING.md) file for help regarding creating issues and sending pull requests.

### Branch Policy

We have the following branches

 * **development** All development goes on in this branch. If you're making a contribution, you are supposed to make a pull request to _development_. PRs to development branch must pass a build check on Travis CI.
 * **master** This contains shipped code. After significant features/bugfixes are accumulated on development, we make a version update and make a release.
 * **apk** This branch contains many apk files, that are automatically generated on the merged pull request a) debug apk b) release apk
    - There are multiple files in the apk branch of the project, this branch consists of all the APK files and other files that are relevant when an APK is generated.
    - Once a pull request is merged, the previous APK branch is deleted and a new APK branch is created.
    - If a PR is merged in development branch then the new APKs for the development branch are generated whereas the APKs corresponding to the master branch are not regenerated and simply the previously generated files are added.

### Code practices

Please help us follow the best practice to make it easy for the reviewer as well as the contributor. We want to focus on the code quality more than on managing pull request ethics.

 * Single commit per pull request
 * For writing commit messages please read the [CommitStyle.md](docs/commitStyle.md) carefully. Kindly adhere to the guidelines.
 * Follow uniform design practices. The design language must be consistent throughout the app.
 * The pull request will not get merged until and unless the commits are squashed. In case there are multiple commits on the PR, the commit author needs to squash them and not the maintainers cherrypicking and merging squashes.
 * If the PR is related to any front end change, please attach relevant screenshots in the pull request description.

### Join the development

* Before you join development, please set up the project on your local machine, run it and go through the application completely. Press on any button you can find and see where it leads to. Explore. (Don't worry ... Nothing will happen to the app or to you due to the exploring :wink: Only thing that will happen is, you'll be more familiar with what is where and might even get some cool ideas on how to improve various aspects of the app.)
* If you would like to work on an issue, drop in a comment at the issue. If it is already assigned to someone, but there is no sign of any work being done, please free to drop in a comment so that the issue can be assigned to you if the previous assignee has dropped it entirely.
