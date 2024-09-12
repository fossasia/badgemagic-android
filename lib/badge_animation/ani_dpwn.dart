import 'package:badgemagic/badge_animation/animation_abstract.dart';

class DownAnimation extends BadgeAnimation {
  @override
  void animation(
      List<List<bool>> grid,
      List<List<int>> newGrid,
      int animationIndex,
      bool validMarquee,
      bool flashLEDOn,
      int currentcountFrame,
      int i,
      int j,
      int newHeight,
      int newWidth,
      int badgeHeight,
      int badgeWidth) {
    if (i < badgeHeight && j < badgeWidth) {
      int newGridRow = (i - animationIndex + newHeight) % newHeight;
      bool upCondition = validMarquee ||
          flashLEDOn &&
              (i >= 0 &&
                  i < newHeight &&
                  j >= 0 &&
                  j < newWidth &&
                  newGrid[newGridRow][j] == 1);

      grid[i][j] = upCondition;
    }
  }
}
