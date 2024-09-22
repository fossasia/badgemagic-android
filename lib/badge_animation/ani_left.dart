import 'package:badgemagic/badge_animation/animation_abstract.dart';

class LeftAnimation extends BadgeAnimation {
  @override
  void processAnimation(int badgeHeight, int badgeWidth, int animationIndex,
      List<List<bool>> processGrid, List<List<bool>> canvas) {
    int newWidth = processGrid[0].length;
    int newHeight = processGrid.length;
    for (int i = 0; i < badgeHeight; i++) {
      for (int j = 0; j < badgeWidth; j++) {
        // Calculate how much of the new grid is currently visible in the grid
        int scrollOffset = animationIndex % (newWidth + badgeWidth);

        // Get the corresponding column in the new grid based on the scroll position
        int sourceCol = j + scrollOffset - badgeWidth;

        // If sourceCol is negative, display blank space (off-screen part of the grid)
        if (sourceCol >= 0 && sourceCol < newWidth) {
          // Ensure flashLEDOn and validMarquee effects are applied
          canvas[i][j] = processGrid[i % newHeight][sourceCol];
        }
      }
    }
  }
}
