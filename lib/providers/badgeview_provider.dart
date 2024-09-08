import 'dart:async';

import 'package:badgemagic/bademagic_module/utils/byte_array_utils.dart';
import 'package:badgemagic/badge_animation/anim_left.dart';
import 'package:badgemagic/badge_animation/animation_abstract.dart';
import 'package:badgemagic/constants.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';

class DrawBadgeProvider extends ChangeNotifier {
  CardProvider cardData = GetIt.instance<CardProvider>();
  int countFrame = 0;
  int animationIndex = 0;
  int lastFrame = 0;
  AnimationController? _controller;
  int animationSpeed = aniBaseSpeed.inMilliseconds;
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
  void setDrawViewGrid(List<List<bool>> newGrid) {
    drawViewGrid = newGrid;
    notifyListeners();
  }

  BadgeAnimation? currentAnimation;

  //function to update the state of the cell
  void updateGrid(int row, int col) {
    homeViewGrid[row][col] = isDrawing;
    notifyListeners();
  }

  //function to reset the state of the cell
  void resetGrid() {
    homeViewGrid = List.generate(11, (i) => List.generate(44, (j) => false));
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
  void calculateDuration() {
    int newSpeed = aniSpeedStrategy(cardData.getOuterValue());
    if (newSpeed != animationSpeed) {
      animationSpeed = newSpeed;
      timer?.cancel();
      startTimer();
    }
  }

  //function to get the isDrawing variable
  bool getIsDrawing() => isDrawing;

  List<List<int>> newGrid =
      List.generate(11, (i) => List.generate(44, (j) => 0));

  //getter for newGrid
  List<List<int>> getNewGrid() => newGrid;

  //setter for newGrid
  void setNewGrid(List<List<int>> newGrid) {
    this.newGrid = newGrid;
    notifyListeners();
  }

  void initializeAnimation(TickerProvider vsync) {
    _controller = AnimationController(
      vsync: vsync,
      duration: const Duration(days: 1000),
    )..addListener(() {
        setAnimationMode();
        changeGridValue(newGrid);
        calculateDuration();
      });
    startTimer();
    _controller!.repeat();
  }

  void startTimer() {
    logger.i("Timer started");
    logger.i("Animation speed: $animationSpeed");
    timer =
        Timer.periodic(Duration(microseconds: animationSpeed), (Timer timer) {
      animationIndex++;
    });
  }

  void startAnimation() {
    animationIndex = 0;
    _controller!.forward();
  }

  void setAnimationMode() {
    switch (cardData.getAnimationIndex()) {
      //add cases from 0 to 8
      case 0:
        currentAnimation = LeftAnimation();
        break;
      case 1:
        // currentAnimation = RightAnimation();
        break;
      case 2:
        // currentAnimation = UpAnimation();
        break;
      case 3:
        // currentAnimation = DownAnimation();
        break;
      case 4:
        // currentAnimation = FixedAnimation();
        break;
      case 5:
        // currentAnimation = SnowFlakeAnimation();
        break;
      case 6:
        currentAnimation = null;
        break;
      case 7:
        currentAnimation = null;
        break;
      case 8:
        currentAnimation = null;
        break;
      default:
        currentAnimation = LeftAnimation();
        break;
    }
  }

  void changeGridValue(List<List<int>> newGrid) {
    int badgeWidth = homeViewGrid[0].length;
    int badgeHeight = homeViewGrid.length;
    int newHeight = newGrid.length;
    int newWidth = newGrid[0].length;

    // Process grid
    for (int i = 0; i < badgeHeight; i++) {
      // bool matchFrame = false;

      for (int j = 0; j < badgeWidth; j++) {
        bool flashLEDOn = true;

        if (cardData.getEffectIndex(1) == 1) {
          int aIFlash = animationIndex % 2;
          flashLEDOn = aIFlash == 0;
        }

        bool validMarquee = false;

        if (cardData.getEffectIndex(2) == 1) {
          int aIMarquee = animationIndex ~/ 2;
          validMarquee =
              (i == 0 || j == 0 || i == badgeHeight - 1 || j == badgeWidth - 1);

          if (validMarquee) {
            if ((i == 0 || j == badgeWidth - 1) &&
                !(i == badgeHeight - 1 && j == badgeWidth - 1)) {
              validMarquee = (i + j) % 4 == (aIMarquee % 4);
            } else {
              validMarquee = (i + j - 1) % 4 == (3 - (aIMarquee % 4));
            }
          }
        }
        if (currentAnimation != null) {
          currentAnimation!.animation(
              homeViewGrid,
              newGrid,
              animationIndex,
              validMarquee,
              flashLEDOn,
              countFrame,
              i,
              j,
              newHeight,
              newWidth,
              badgeHeight,
              badgeWidth);
        }
      }
      notifyListeners();
    }
  }
}
