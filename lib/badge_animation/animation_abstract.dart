import 'package:badgemagic/badge_animation/reference_classes.dart';

abstract class BadgeAnimation {
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
      IntReference lastFrame);
}
