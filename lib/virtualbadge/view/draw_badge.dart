import 'package:badgemagic/providers/badgeview_provider.dart';
import 'package:badgemagic/virtualbadge/view/draw_badge_paint.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:provider/provider.dart';

class BMBadge extends StatefulWidget {
  const BMBadge({super.key});

  @override
  State<BMBadge> createState() => _BMBadgeState();
}

class _BMBadgeState extends State<BMBadge> {
  DrawBadgeProvider cellStateToggle = GetIt.instance<DrawBadgeProvider>();
  static const int rows = 11;
  static const int cols = 44;

  void _handlePanUpdate(DragUpdateDetails details) {
    RenderBox renderBox = context.findRenderObject() as RenderBox;
    Offset localPosition = renderBox.globalToLocal(details.globalPosition);
    double cellWidth = renderBox.size.width / cols;
    double cellHeight = renderBox.size.height / rows;

    int col = (localPosition.dx / cellWidth).clamp(0, cols - 1).toInt();
    int row = (localPosition.dy / cellHeight).clamp(0, rows - 1).toInt();

    setState(() {
      cellStateToggle.setDrawViewGrid(row, col);
    });
  }

  @override
  Widget build(BuildContext context) {
    final grid = Provider.of<DrawBadgeProvider>(context).getDrawViewGrid();
    return GestureDetector(
        onPanUpdate: _handlePanUpdate,
        child: CustomPaint(
          size: const Size(400, 480),
          painter: DrawBadgePaint(grid: grid),
        ));
  }
}
