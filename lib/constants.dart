const homeScreenTitleKey = "bm_hm_title";
const drawBadgeScreen = "bm_db_screen";

//path to all the animation assets used
const String animation = 'assets/animations/ic_anim_animation.gif';
const String aniLeft = 'assets/animations/ic_anim_left.gif';
const String aniDown = 'assets/animations/ic_anim_down.gif';
const String aniFixed = 'assets/animations/ic_anim_fixed.gif';
const String aniLaser = 'assets/animations/ic_anim_laser.gif';
const String aniPicture = 'assets/animations/ic_anim_picture.gif';
const String aniUp = 'assets/animations/ic_anim_up.gif';
const String aniRight = 'assets/animations/ic_anim_right.gif';

//path to all the effects assets used
const String effFlash = 'assets/effects/ic_effect_flash.gif';
const String effInvert = 'assets/effects/ic_effect_invert.gif';
const String effMarque = 'assets/effects/ic_effect_marquee.gif';

//constants for the animation speed
const Duration aniBaseSpeed = Duration(microseconds: 200000); // in uS
const Duration aniMarqueSpeed = Duration(microseconds: 100000); // in uS
const Duration aniFlashSpeed = Duration(microseconds: 500000); // in uS

// Function to calculate animation speed based on speed level
int aniSpeedStrategy(int speedLevel) {
  int speedInMicroseconds = aniBaseSpeed.inMicroseconds -
      (speedLevel * aniBaseSpeed.inMicroseconds ~/ 8);
  return speedInMicroseconds;
}
