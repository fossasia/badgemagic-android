import 'package:badgemagic/badge_animation/animation_abstract.dart';

class FixedAnimation extends BadgeAnimation {
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
    if (newWidth <= badgeWidth + 4) {
      grid[i][j] = validMarquee ||
          i >= 0 &&
              i < newHeight &&
              j >= 0 &&
              j < newWidth &&
              (flashLEDOn && newGrid[i][j] == 1);
    }
  }
}
