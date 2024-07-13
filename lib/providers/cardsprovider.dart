import 'package:badgemagic/bademagic_module/utils/image_utils.dart';
import 'package:flutter/material.dart';

class CardProvider extends ChangeNotifier {
  ImageUtils imageUtils = ImageUtils();
  int outerValue = 1;

  int getOuterValue() => outerValue;

  //context for snackbar
  BuildContext? context;

  void setContext(BuildContext context) {
    this.context = context;
    notifyListeners();
  }

  BuildContext? getContext() => context;

  //outer value for the speed dial
  void setOuterValue(int value) {
    outerValue = value;
    notifyListeners();
  }

  int animationIndex = 0;
  List<int> effectsIndex = [0, 0, 0];

  int getAnimationIndex() => animationIndex;

  int getEffectIndex(int index) => effectsIndex[index];

  void setAnimationIndex(int index) {
    animationIndex = index;
    notifyListeners();
  }

  void setEffectIndex(int index) {
    effectsIndex[index] = effectsIndex[index] == 1 ? 0 : 1;
    notifyListeners();
  }
}
