import 'package:flutter/material.dart';

class CardProvider extends ChangeNotifier {
  int animationIndex = 0;
  List effectsindex = [0, 0, 0];

  int getAnimationIndex() => animationIndex;

  int getEffectIndex(int index) => effectsindex[index];

  void setAnimationIndex(int index) {
    animationIndex = index;
    notifyListeners();
  }

  void setEffectIndex(int index) {
    effectsindex[index] == 1 ? effectsindex[index] = 0 : effectsindex[index] = 1;
    notifyListeners();
  }
}
