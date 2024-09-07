import 'package:badgemagic/virtualbadge/view/badge_painter.dart';
import 'package:flutter/material.dart';

class BadgeWidget extends StatefulWidget {
  static const int rows = 11;
  static const int cols = 44;
  final List<List<bool>> grid;

  const BadgeWidget({super.key, required this.grid});

  @override
  State<BadgeWidget> createState() => _BadgeWidgetState();
}

class _BadgeWidgetState extends State<BadgeWidget> {
  @override
  Widget build(BuildContext context) {
    return CustomPaint(
      size: const Size(400, 480),
      painter: BadgePainter(grid: widget.grid),
    );
  }
}
