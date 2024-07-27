import 'package:flutter/material.dart';

class DrawBadgeProvider extends ChangeNotifier{
  
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
}