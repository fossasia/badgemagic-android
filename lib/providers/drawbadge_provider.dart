import 'dart:async';
import 'package:badgemagic/bademagic_module/models/mode.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';

class DrawBadgeProvider extends ChangeNotifier {
  CardProvider cardData = GetIt.instance<CardProvider>();
  Timer? _timer;
  int countFrame = 0;
  int animationIndex = 0;
  int lastFrame = 0;
  //List that contains the state of each cell of the badge
  List<List<bool>> grid =
      List.generate(11, (i) => List.generate(44, (j) => false));

  //function to update the state of the cell
  void updateGrid(int row, int col) {
    grid[row][col] = isDrawing;
    notifyListeners();
  }

  //function to reset the state of the cell
  void resetGrid() {
    grid = List.generate(11, (i) => List.generate(44, (j) => false));
    notifyListeners();
  }

  //function to get the state of the cell
  List<List<bool>> getGrid() => grid;

  //boolean variable to check for isDrawing on Draw badge screen
  bool isDrawing = true;

  //function to toggle the isDrawing variable
  void toggleIsDrawing(bool drawing) {
    isDrawing = drawing;
    notifyListeners();
  }

  //function to get the isDrawing variable
  bool getIsDrawing() => isDrawing;

  void startAnimation(List<List<int>> newGrid, bool isEmpty) {
    _timer?.cancel(); // Cancel any existing timer
    _timer = Timer.periodic(const Duration(milliseconds: 200), (timer) {
      animationIndex++;
      if (isEmpty) {
        newGrid = List.generate(11, (i) => List.generate(44, (j) => 0));
      }
      changeGridValue(newGrid);
    });
  }

  void changeGridValue(List<List<int>> newGrid) {
    int badgeHeight = grid.length;
    int badgeWidth = grid[0].length;
    int newHeight = newGrid.length;
    int newWidth = newGrid[0].length;

    // Process grid
    for (int i = 0; i < badgeHeight; i++) {
      // bool matchFrame = false;

      for (int j = 0; j < badgeWidth; j++) {
        bool flashLEDOn = true;

        if (cardData.getEffectIndex(1) == 1) {
          int aIFlash = animationIndex % 2;
          flashLEDOn = aIFlash > 4;
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
        if (Mode == Mode.fixed) {
          // Offsets to center newGrid in the existing grid
          int verticalOffset = (badgeHeight - newHeight) ~/ 2;
          int horizontalOffset = (badgeWidth - newWidth) ~/ 2;
          // Calculate position in newGrid
          int newGridRow = i - verticalOffset;
          int newGridCol = j - horizontalOffset;

          bool isNewGridCell = newGridRow >= 0 &&
              newGridRow < newHeight &&
              newGridCol >= 0 &&
              newGridCol < newWidth;

          bool fixedCondition = false;
          if (isNewGridCell) {
            // Handle the fixed mode update
            fixedCondition = validMarquee ||
                flashLEDOn && newGrid[newGridRow][newGridCol] == 1;
          }

          grid[i][j] = fixedCondition;
        }
        if (Mode == Mode.right) {
          int animationValue = animationIndex ~/ 1;
          int newGridLength = newGrid[0].length;

          int startPosition = badgeWidth + animationValue;

          int gridIndex = (j - startPosition);
          gridIndex = gridIndex %
              (newGridLength < badgeWidth - 1 ? badgeWidth : newGridLength);

          bool leftCondition = validMarquee ||
              (i < newHeight &&
                  j >= 0 &&
                  j < badgeWidth &&
                  gridIndex >= 0 &&
                  gridIndex < newGridLength &&
                  newGrid[i][gridIndex] == 1);

          grid[i][j] = leftCondition;
        }
        if (Mode == Mode.down) {
          int animationValue = animationIndex ~/ 2;
          int newGridHeight = newGrid.length;
          int verticalOffset = (badgeHeight - newHeight) ~/ 2;
          int horizontalOffset = (badgeWidth - newWidth) ~/ 2;
          int newGridRow = (i - animationValue + newGridHeight) % newGridHeight;
          int newGridCol = j - horizontalOffset;

          bool isNewGridCell = newGridRow >= 0 &&
              newGridRow < newHeight &&
              newGridCol >= 0 &&
              newGridCol < newWidth;

          bool upCondition = validMarquee ||
              (i >= verticalOffset &&
                  isNewGridCell &&
                  newGrid[newGridRow][newGridCol] == 1);

          grid[i][j] = upCondition;
        }
        if (Mode == Mode.up) {
          int animationValue = animationIndex ~/ 2;
          int newGridHeight = newGrid.length;
          int verticalOffset = (badgeHeight - newHeight) ~/ 2;
          int horizontalOffset = (badgeWidth - newWidth) ~/ 2;
          int newGridRow = (i + animationValue + newGridHeight) % newGridHeight;
          int newGridCol = j - horizontalOffset;

          bool isNewGridCell = newGridRow >= 0 &&
              newGridRow < newHeight &&
              newGridCol >= 0 &&
              newGridCol < newWidth;

          bool upCondition = validMarquee ||
              (i >= verticalOffset &&
                  isNewGridCell &&
                  newGrid[newGridRow][newGridCol] == 1);

          grid[i][j] = upCondition;
        }
      }
    }
  }

  notifyListeners();
}
