import 'package:badgemagic/badge_animation/animation_abstract.dart';
import 'package:badgemagic/badge_animation/reference_classes.dart';

class UpAnimation extends BadgeAnimation {
  @override
  void animation(
      List<List<bool>> grid,
      List<List<int>> newGrid,
      int animationIndex,
      bool validMarquee,
      bool flashLEDOn,
      IntReference currentcountFrame,
      int i,
      int j,
      int newHeight,
      int newWidth,
      int badgeHeight,
      int badgeWidth,
      IntReference lastFrame) {
    if (j < badgeWidth && i < badgeHeight) {
      int newGridRow = (i + animationIndex + newHeight) % newHeight;

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
