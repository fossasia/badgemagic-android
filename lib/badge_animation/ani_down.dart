import 'package:badgemagic/badge_animation/animation_abstract.dart';

class DownAnimation extends BadgeAnimation {
  @override
  void processAnimation(int badgeHeight, int badgeWidth, int animationIndex,
      List<List<bool>> processGrid, List<List<bool>> canvas) {
    int newWidth = processGrid[0].length;
    int newHeight = processGrid.length;
    int animationValue = animationIndex ~/ ((newWidth / badgeHeight));
    for (int i = 0; i < badgeHeight; i++) {
      for (int j = 0; j < badgeWidth; j++) {
        if (j < badgeWidth && i < badgeHeight) {
          bool upCondition = (i >= 0 &&
              i < newHeight &&
              j >= 0 &&
              j < newWidth &&
              processGrid[(i - animationValue + newHeight) % newHeight][j]);

          canvas[i][j] = upCondition;
        }
      }
    }
  }
}
