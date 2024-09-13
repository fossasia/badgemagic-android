import 'package:badgemagic/badge_animation/animation_abstract.dart';

class SnowFlakeAnimation extends BadgeAnimation {
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
    // Animation value to determine the current frame
    int animationValue = animationIndex ~/ 1;

    int newGridHeight = newGrid.length;
    int newGridWidth = newGrid[0].length;

    // Calculate the total number of frames that fit the badge width
    int framesCount = (newGridWidth / badgeWidth).ceil();

    // Determine the current frame based on the animation value
    int currentcountFrame = animationValue ~/ badgeWidth % framesCount;

    // Calculate the starting column for the current frame in newGrid
    int startCol = currentcountFrame * badgeWidth;

    bool isNewGridCell = i < newGridHeight && (startCol + j) < newGridWidth;

    // Update the grid based on the current frame's data
    bool snowflakeCondition = validMarquee ||
        flashLEDOn && (isNewGridCell && newGrid[i][startCol + j] == 1);

    grid[i][j] = snowflakeCondition;
  }
}
