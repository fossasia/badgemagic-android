abstract class BadgeEffect {
  List<int> effectsIndex = [0, 0, 0];
  BadgeEffect(this.effectsIndex);
  void processEffect(int animationIndex, List<List<bool>> canvas,
      List<int> effectsIndex, int badgeHeight, int badgeWidth);
}
