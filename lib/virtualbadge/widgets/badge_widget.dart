import 'package:badgemagic/providers/drawbadge_provider.dart';
import 'package:badgemagic/providers/imageprovider.dart';
import 'package:badgemagic/view/draw_badge_screen.dart';
import 'package:badgemagic/virtualbadge/view/cell.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class BadgeWidget extends StatefulWidget {
  static const int rows = 11;
  static const int cols = 44;

  const BadgeWidget({super.key});

  @override
  State<BadgeWidget> createState() => _BadgeWidgetState();
}

class _BadgeWidgetState extends State<BadgeWidget> {
  @override
  Widget build(BuildContext context) {
    DrawBadgeProvider cellStateToggle = Provider.of<DrawBadgeProvider>(context);
    return CustomPaint(
      size: const Size(400, 480),
      painter: BadgePainter(grid: cellStateToggle.getGrid()),
    );
  }
}
