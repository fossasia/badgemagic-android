import 'package:badgemagic/badge_effect/badgeeffectabstract.dart';

class BadgeEffectImpl extends BadgeEffect {
  BadgeEffectImpl(super.effectsIndex);

  @override
  void processEffect(int animationIndex, List<List<bool>> canvas,
      List<int> effectsIndex, int badgeHeight, int badgeWidth) {
    bool flashLEDOn = true;
    bool validMarquee = false;
    // Process grid
    for (int i = 0; i < badgeHeight; i++) {
      // bool matchFrame = false;

      for (int j = 0; j < badgeWidth; j++) {
        if (effectsIndex[0] == 1) {
          canvas[i][j] = !canvas[i][j];
        }
        if (effectsIndex[1] == 1) {
          int aIFlash = animationIndex % 2;
          flashLEDOn = aIFlash == 0;
        }

        if (effectsIndex[2] == 1) {
          int aIMarquee = animationIndex ~/ 2;
          validMarquee =
              (i == 0 || j == 0 || i == badgeHeight - 1 || j == badgeWidth - 1);

          if (validMarquee) {
            if ((i == 0 || j == badgeWidth - 1) &&
                !(i == badgeHeight - 1 && j == badgeWidth - 1)) {
              validMarquee = (i + j) % 4 == (aIMarquee % 4);
            } else {
              validMarquee = (i + j - 1) % 4 == (3 - (aIMarquee % 4));
            }
          }
        }
        canvas[i][j] = canvas[i][j] && flashLEDOn || validMarquee;
      }
    }
  }
}
