import 'package:badgemagic/bademagic_module/utils/byte_array_utils.dart';
import 'package:badgemagic/badge_animation/animation_abstract.dart';
import 'package:badgemagic/badge_animation/reference_classes.dart';

class AniAnimation extends BadgeAnimation {
  @override
  void animation(
      List<List<bool>> grid,
      List<List<int>> newGrid,
      int animationIndex,
      bool validMarquee,
      bool flashLEDOn,
      IntReference countFrame,
      int i,
      int j,
      int newHeight,
      int newWidth,
      int badgeHeight,
      int badgeWidth,
      IntReference lastFrame) {
    // Calculate vertical and horizontal offsets to center the newGrid in the badge grid
    int verticalOffset = (badgeHeight - newHeight) ~/ 2;
    int horizontalOffset = (badgeWidth - newWidth) ~/ 2;

    bool lineShow = false;
    bool bitmapShowcenter = false;
    bool bitmapShowOut = false;

    // Calculate the corresponding row and column in the newGrid
    int sourceRow = i - verticalOffset;
    int sourceCol = j - horizontalOffset;

    // Check if the current cell is within the bounds of the newGrid
    bool isWithinNewGrid = sourceRow >= 0 &&
        sourceRow < newHeight &&
        sourceCol >= 0 &&
        sourceCol < newWidth;

    // Calculate center columns
    int leftCenterCol = badgeWidth ~/ 2 - 1;
    int rightCenterCol = badgeWidth ~/ 2;

    // Calculate the maximum distance the lines can travel
    int maxDistance =
        leftCenterCol; // The maximum distance from the center to the edge

    // Adjust the animationIndex to loop back to the center when it reaches maxDistance
    int currentAnimationIndex = animationIndex % (maxDistance + 1);

    // Calculate the current positions of the vertical lines
    int leftColPos = leftCenterCol - currentAnimationIndex;
    int rightColPos = rightCenterCol + currentAnimationIndex;

    // Ensure valid positions
    if (leftColPos < 0) leftColPos += badgeWidth;
    if (rightColPos >= badgeWidth) rightColPos -= badgeWidth;

    // Animation phase control
    // First phase: Only between the two center columns
    if (j == leftColPos || j == rightColPos) {
      lineShow = true; // Draw vertical lines in the center columns
    } else {
      lineShow = false;
    }

    if (countFrame.value == 0) {
      if (isWithinNewGrid && j > leftColPos && j < rightColPos) {
        bitmapShowcenter =
            newGrid[sourceRow][sourceCol] == 1; // Display the inner grid
      }
    }

    // Second phase: Outside the center columns, executed after the first phase is complete
    if (countFrame.value == 1) {
      if (isWithinNewGrid && (j < leftColPos || j > rightColPos)) {
        bitmapShowcenter = newGrid[sourceRow][sourceCol] ==
            1; // Display grid outside the center columns
      }
    }

    grid[i][j] = validMarquee ||
        (flashLEDOn && (lineShow || bitmapShowOut || bitmapShowcenter));

    // Alternate the phase between 0 and 1
    if (i == 0 &&
        j == 0 &&
        leftColPos == leftCenterCol &&
        rightColPos == rightCenterCol) {
      // Toggle only once per full grid cycle
      countFrame.value = (countFrame.value == 0) ? 1 : 0;
      logger.i("countFrame toggled to ${countFrame.value}");
    }
  }
}
