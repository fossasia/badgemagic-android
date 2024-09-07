import 'dart:math' as math;

import 'package:flutter/material.dart';

class BadgePainter extends CustomPainter {
  final List<List<bool>> grid;

  BadgePainter({required this.grid});

  List<List<bool>>? previousGrid;

  final Paint redPaint = Paint()
    ..color = Colors.red
    ..style = PaintingStyle.fill;

  final Paint greyPaint = Paint()
    ..color = Colors.grey.shade800
    ..style = PaintingStyle.fill;

  double radians = math.pi / 4;

  @override
  void paint(Canvas canvas, Size size) {
    double cellWidth = size.width / grid[0].length;
    double cellHeight = size.height / grid.length;

    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        final Paint paint = grid[row][col] ? redPaint : greyPaint;

        final Path path = Path()
          ..moveTo(col * cellWidth, row * cellHeight)
          ..lineTo(col * cellWidth + cellWidth * 0.4, row * cellHeight)
          ..lineTo(col * cellWidth + cellWidth * 0.655,
              row * cellHeight + cellHeight)
          ..lineTo(
              col * cellWidth + cellWidth * 0.25, row * cellHeight + cellHeight)
          ..close();

        canvas.save();
        canvas.translate((col + 0.5) * cellWidth, (row + 0.5) * cellHeight);
        canvas.rotate(radians);
        canvas.translate(-(col + 0.5) * cellWidth, -(row + 0.5) * cellHeight);
        canvas.drawPath(path, paint);
        canvas.restore();
      }
    }
    previousGrid = grid;
  }

  @override
  bool shouldRepaint(covariant BadgePainter oldDelegate) {
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        if (grid[row][col] != previousGrid?[row][col]) {
          return true;
        }
      }
    }
    return false;
  }
}
