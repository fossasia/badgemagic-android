import 'package:badgemagic/badge_animation/animation_abstract.dart';

class UpAnimation extends BadgeAnimation {
  @override
  void processAnimation(int badgeHeight, int badgeWidth, int animationIndex,
      List<List<bool>> processGrid, List<List<bool>> canvas) {
    int newWidth = processGrid[0].length;
    int newHeight = processGrid.length;
    for (int i = 0; i < badgeHeight; i++) {
      for (int j = 0; j < badgeWidth; j++) {
        if (j < badgeWidth && i < badgeHeight) {
          int newGridRow = (i + animationIndex + newHeight) % newHeight;

          bool upCondition = (i >= 0 &&
              i < newHeight &&
              j >= 0 &&
              j < newWidth &&
              processGrid[newGridRow][j]);

          canvas[i][j] = upCondition;
        }
      }
    }
  }
}
