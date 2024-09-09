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
    if (newWidth <= badgeWidth) {
      int verticalOffset = (badgeHeight - newHeight) ~/ 2;
      int horizontalOffset = (badgeWidth - newWidth) ~/ 2;
      int sourceRow = i - verticalOffset;
      int sourceCol = j - horizontalOffset;
      bool isWithinNewGrid = sourceRow >= 0 &&
          sourceRow < newHeight &&
          sourceCol >= 0 &&
          sourceCol < newWidth;
      if (isWithinNewGrid) {
        grid[i][j] =
            validMarquee || (flashLEDOn && newGrid[sourceRow][sourceCol] == 1);
      }
    }
  }
}
