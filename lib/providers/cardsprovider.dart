import 'package:flutter/material.dart';

class CardProvider extends ChangeNotifier {
  TextEditingController message = TextEditingController();
  int animationIndex = 0;
  List<int> effectsIndex = [0, 0, 0];

  int getAnimationIndex() => animationIndex;

  TextEditingController getController() => message;

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
