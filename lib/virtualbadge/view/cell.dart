import 'package:flutter/material.dart';
import 'dart:math' as math;



class Cell extends StatefulWidget {
  final int index;
  final bool isSelected;
  const Cell({Key? key, required this.index, required this.isSelected}) : super(key: key);

  @override
  State<Cell> createState() => _CellState();
}

class _CellState extends State<Cell> {
  @override
  Widget build(BuildContext context) {
    return CustomPaint(
      size: const Size(10, 10), // Set your desired size
      painter: TiltedParallelogramPainter(index: widget.index, isSelected: widget.isSelected),
    );
  }
}

class TiltedParallelogramPainter extends CustomPainter {
  final int index;
  final bool isSelected;
  TiltedParallelogramPainter({Key? key, required this.index, required this.isSelected});  
  @override
  void paint(Canvas canvas, Size size,) {
    final Paint paint = Paint()
      ..color = isSelected?Colors.red:Colors.grey.shade600 
      ..style = PaintingStyle.fill;

    final Path path = Path()
      ..moveTo(0, 0)
      ..lineTo(size.width * 0.4, 0) 
      ..lineTo(size.width*0.655, size.height)
      ..lineTo(size.width * 0.25, size.height)
      ..close();

    const double radians = math.pi / 4;
    canvas.translate(size.width / 2, size.height / 2);
    canvas.rotate(radians);
    canvas.translate(-size.width / 2, -size.height / 2);

    canvas.drawPath(path, paint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    return true;
  }
}
