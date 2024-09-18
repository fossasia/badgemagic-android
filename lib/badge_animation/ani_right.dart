import 'package:badgemagic/badge_animation/animation_abstract.dart';
import 'package:badgemagic/badge_animation/reference_classes.dart';

class RightAnimation extends BadgeAnimation {
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
    // Calculate the scroll offset to move from left to right
    int scrollOffset = animationIndex % (newWidth + badgeWidth);

    // Get the corresponding column in the new grid based on the reversed scroll position
    int sourceCol = newWidth - scrollOffset + j;

    // If sourceCol is within bounds of the new grid, display it, else blank space
    if (sourceCol >= 0 && sourceCol < newWidth) {
      grid[i][j] =
          validMarquee || flashLEDOn && newGrid[i % newHeight][sourceCol] == 1;
    } else {
      validMarquee ? grid[i][j] = true : grid[i][j] = false;
    }
  }
}
