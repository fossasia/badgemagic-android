import 'dart:async';

import 'package:badgemagic/bademagic_module/utils/byte_array_utils.dart';
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
  int animationIndex = 0;
  int animationSpeed = aniSpeedStrategy(0);
  int counter = 0;
  Timer? timer;
  bool isSavedAnimation = false;
  //List that contains the state of each cell of the badge for home view
  List<List<bool>> homeViewGrid =
      List.generate(11, (i) => List.generate(44, (j) => false));

  //List that contains the state of each cell of the badge for draw view
  List<List<bool>> drawViewGrid =
      List.generate(11, (i) => List.generate(44, (j) => false));

  //List that contains the statet of each cell of the badge for saved view
  List<List<bool>> savedViewGrid =
      List.generate(11, (i) => List.generate(44, (j) => false));

  //getter for the savedViewGrid
  List<List<bool>> getSavedViewGrid() => savedViewGrid;

  //setter for the savedViewGrid
  void setSavedViewGrid(List<List<bool>> grid) {
    //map all the values of the grid to the savedViewGrid
    //and make all other values as false to keep the grid size constant
    for (int i = 0; i < savedViewGrid.length; i++) {
      for (int j = 0; j < savedViewGrid[0].length; j++) {
        if (j < grid[0].length) {
          savedViewGrid[i][j] = grid[i][j];
        } else {
          savedViewGrid[i][j] = false;
        }
      }
    }
    isSavedAnimation = true;
    animationIndex = 0;
    notifyListeners();
  }

  //getter for the drawViewGrid
  List<List<bool>> getDrawViewGrid() => drawViewGrid;

  //setter for the drawViewGrid
  void setDrawViewGrid(int row, int col) {
    drawViewGrid[row][col] = isDrawing;
    notifyListeners();
  }

  BadgeAnimation currentAnimation = LeftAnimation();

  void updateDrawViewGrid(List<List<bool>> badgeData) {
    //copy the badgeData to the drawViewGrid and all the drawViewGrid after badgeData will remain unchanged
    for (int i = 0; i < drawViewGrid.length; i++) {
      for (int j = 0; j < drawViewGrid[0].length; j++) {
        if (j < badgeData[0].length) {
          drawViewGrid[i][j] = badgeData[i][j];
        } else {
          drawViewGrid[i][j] = false;
        }
      }
    }
    notifyListeners();
  }

  BadgeEffect currentEffect = BadgeEffectImpl([0, 0, 0]);
  //function to update the state of the cell
  void updateGrid(int row, int col) {
    homeViewGrid[row][col] = isDrawing;
    notifyListeners();
  }

  //function to reset the state of the cell
  void resetDrawViewGrid() {
    drawViewGrid = List.generate(11, (i) => List.generate(44, (j) => false));
    notifyListeners();
  }

  //function to get the state of the cell
  List<List<bool>> getHomeViewGrid() => homeViewGrid;

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
  void setNewGrid(List<List<bool>> grid, bool isSavedBadge) {
    newGrid = grid;
    animationIndex = 0;
    isSavedAnimation = isSavedBadge;
    notifyListeners();
  }

  void setEffectIndex(List<int> index) {
    currentEffect = BadgeEffectImpl(index);
    notifyListeners();
  }

  void initializeAnimation() {
    startTimer();
  }

  //function to stop timer and reset the animationIndex
  void stopAnimation() {
    logger.d("Timer stopped  ${timer!.tick.toString()}");
    timer?.cancel();

    animationIndex = 0;
  }

  void stopAllAnimations() {
    // Stop any ongoing timer and reset the animation index
    stopAnimation();
    currentAnimation = LeftAnimation();
    currentEffect = BadgeEffectImpl([0, 0, 0]);
    // Reset the grids to all false values
    homeViewGrid = List.generate(11, (i) => List.generate(44, (j) => false));
    savedViewGrid = List.generate(11, (i) => List.generate(44, (j) => false));
    newGrid = List.generate(11, (i) => List.generate(44, (j) => false));
    logger.d("All animations stopped");
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
    int badgeWidth = homeViewGrid[0].length;
    int badgeHeight = homeViewGrid.length;
    var canvas = List.generate(
        badgeHeight, (i) => List.generate(badgeWidth, (j) => false));
    currentAnimation.processAnimation(
        badgeHeight, badgeWidth, animationIndex, newGrid, canvas);
    currentEffect.processEffect(animationIndex, canvas,
        currentEffect.effectsIndex, badgeHeight, badgeWidth);
    isSavedAnimation ? savedViewGrid = canvas : homeViewGrid = canvas;
    notifyListeners();
  }
}
