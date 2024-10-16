import 'dart:async';

import 'package:badgemagic/badge_animation/ani_animation.dart';
import 'package:badgemagic/badge_animation/ani_down.dart';
import 'package:badgemagic/badge_animation/ani_fixed.dart';
import 'package:badgemagic/badge_animation/ani_laser.dart';
import 'package:badgemagic/badge_animation/ani_left.dart';
import 'package:badgemagic/badge_animation/ani_picture.dart';
import 'package:badgemagic/badge_animation/ani_right.dart';
import 'package:badgemagic/badge_animation/ani_snowflake.dart';
import 'package:badgemagic/badge_animation/ani_up.dart';
import 'package:badgemagic/badge_animation/animation_abstract.dart';
import 'package:badgemagic/badge_effect/badge_effect_impl.dart';
import 'package:badgemagic/badge_effect/badgeeffectabstract.dart';
import 'package:badgemagic/constants.dart';
import 'package:flutter/material.dart';

class DrawBadgeProvider extends ChangeNotifier {
  bool _isDisposed = false;

  @override
  void dispose() {
    _isDisposed = true;
    timer?.cancel();
    super.dispose();
  }

  int animationIndex = 0;
  int animationSpeed = aniSpeedStrategy(0);
  int counter = 0;
  Timer? timer;
  //List that contains the state of each cell of the badge for home view
  List<List<bool>> homeViewGrid =
      List.generate(11, (i) => List.generate(44, (j) => false));

  //List that contains the state of each cell of the badge for draw view
  List<List<bool>> drawViewGrid =
      List.generate(11, (i) => List.generate(44, (j) => false));

  //getter for the drawViewGrid
  List<List<bool>> getDrawViewGrid() => drawViewGrid;

  //setter for the drawViewGrid
  void setDrawViewGrid(int row, int col) {
    drawViewGrid[row][col] = isDrawing;
    notifyListeners();
  }

  BadgeAnimation currentAnimation = LeftAnimation();

  BadgeEffect currentEffect = BadgeEffectImpl([0, 0, 0]);
  //function to update the state of the cell
  void updateGrid(int row, int col) {
    homeViewGrid[row][col] = isDrawing;
    notifyListeners();
  }

  //function to reset the state of the cell
  void resetGrid() {
    drawViewGrid = List.generate(11, (i) => List.generate(44, (j) => false));
    notifyListeners();
  }

  //function to get the state of the cell
  List<List<bool>> getGrid() => homeViewGrid;

  //boolean variable to check for isDrawing on Draw badge screen
  bool isDrawing = true;

  //function to toggle the isDrawing variable
  void toggleIsDrawing(bool drawing) {
    isDrawing = drawing;
    notifyListeners();
  }

  //function to calculate duration for the animation
  void calculateDuration(int speed) {
    int newSpeed = aniSpeedStrategy(speed - 1);
    if (newSpeed != animationSpeed) {
      animationSpeed = newSpeed;
      timer?.cancel();
      startTimer();
    }
  }

  //function to get the isDrawing variable
  bool getIsDrawing() => isDrawing;

  List<List<bool>> newGrid =
      List.generate(11, (i) => List.generate(44, (j) => false));

  //getter for newGrid
  List<List<bool>> getNewGrid() => newGrid;

  //setter for newGrid
  void setNewGrid(List<List<bool>> grid) {
    newGrid = grid;
    animationIndex = 0;
    notifyListeners();
  }

  void setEffectIndex(List<int> index) {
    currentEffect = BadgeEffectImpl(index);
    notifyListeners();
  }

  void initializeAnimation() {
    startTimer();
  }

  void startTimer() {
    timer =
        Timer.periodic(Duration(microseconds: animationSpeed), (Timer timer) {
      renderGrid(newGrid);
      animationIndex++;
    });
  }

  Map<int, BadgeAnimation?> animationMap = {
    0: LeftAnimation(),
    1: RightAnimation(),
    2: UpAnimation(),
    3: DownAnimation(),
    4: FixedAnimation(),
    5: SnowFlakeAnimation(),
    6: PictureAnimation(),
    7: AniAnimation(),
    8: LaserAnimation(),
  };

  void setAnimationMode(int animationCard) {
    animationIndex = 0;
    currentAnimation = animationMap[animationCard] ?? LeftAnimation();
  }

  void renderGrid(List<List<bool>> newGrid) {
    if (_isDisposed) return;
    int badgeWidth = homeViewGrid[0].length;
    int badgeHeight = homeViewGrid.length;
    var canvas = List.generate(
        badgeHeight, (i) => List.generate(badgeWidth, (j) => false));
    currentAnimation.processAnimation(
        badgeHeight, badgeWidth, animationIndex, newGrid, canvas);
    currentEffect.processEffect(animationIndex, canvas,
        currentEffect.effectsIndex, badgeHeight, badgeWidth);
    homeViewGrid = canvas;
    notifyListeners();
  }
}
