import 'dart:math' as math;

import 'package:flutter/material.dart';

class BadgePainter extends CustomPainter {
  final List<List<bool>> grid;

  BadgePainter({required this.grid});

  @override
  void paint(Canvas canvas, Size size) {
    double cellWidth = size.width / grid[0].length;
    double cellHeight = size.height / grid.length;

    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        final Paint paint = Paint()
          ..color = grid[row][col]
              ? const Color.fromARGB(255, 255, 0, 0)
              : Colors.grey.shade900
          ..style = PaintingStyle.fill;

        final Path path = Path()
          ..moveTo(col * cellWidth, row * cellHeight)
          ..lineTo(col * cellWidth + cellWidth * 0.4, row * cellHeight)
          ..lineTo(col * cellWidth + cellWidth * 0.655,
              row * cellHeight + cellHeight)
          ..lineTo(
              col * cellWidth + cellWidth * 0.25, row * cellHeight + cellHeight)
          ..close();

        const double radians = math.pi / 4;
        canvas.save();
        canvas.translate((col + 0.5) * cellWidth, (row + 0.5) * cellHeight);
        canvas.rotate(radians);
        canvas.translate(-(col + 0.5) * cellWidth, -(row + 0.5) * cellHeight);

        canvas.drawPath(path, paint);
        canvas.restore();
      }
    }
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    return true;
  }
}
