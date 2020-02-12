#!/bin/sh

FILES_DIFF=$(diff <(find docs/images -type f -name "screen-?.jpg" -exec md5sum {} + | sort -k 2 | sed 's/ .*\// /') <(find fastlane/metadata/android/en-US/images/phoneScreenshots -type f -exec md5sum {} + | sort -k 2 | sed 's/ .*\// /'))
if [[ $FILES_DIFF ]]; then
    echo -e "\033[0;31mScreenshots in docs/images and fastlane/metadata/android/en-US/images/phoneScreenshots are not same\033[0m" >&2
    exit 1;
fi
