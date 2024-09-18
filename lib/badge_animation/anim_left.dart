import 'package:badgemagic/badge_animation/animation_abstract.dart';
import 'package:badgemagic/badge_animation/reference_classes.dart';

class LeftAnimation extends BadgeAnimation {
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
    // Calculate how much of the new grid is currently visible in the grid
    int scrollOffset = animationIndex % (newWidth + badgeWidth);

    // Get the corresponding column in the new grid based on the scroll position
    int sourceCol = j + scrollOffset - badgeWidth;

    // If sourceCol is negative, display blank space (off-screen part of the grid)
    if (sourceCol >= 0 && sourceCol < newWidth) {
      // Ensure flashLEDOn and validMarquee effects are applied
      grid[i][j] =
          validMarquee || flashLEDOn && newGrid[i % newHeight][sourceCol] == 1;
    } else {
      validMarquee ? grid[i][j] = true : grid[i][j] = false;
    }
  }
}
