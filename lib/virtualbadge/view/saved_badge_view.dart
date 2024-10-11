import 'package:badgemagic/providers/badgeview_provider.dart';
import 'package:badgemagic/virtualbadge/view/saved_badge_painter.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:provider/provider.dart';

class SavedBadgeView extends StatefulWidget {
  const SavedBadgeView({super.key});

  @override
  State<SavedBadgeView> createState() => _SavedBadgeViewState();
}

class _SavedBadgeViewState extends State<SavedBadgeView> {
  @override
  Widget build(BuildContext context) {
    final grid = Provider.of<DrawBadgeProvider>(context).getSavedViewGrid();
    return Container(
        margin: EdgeInsets.only(top: 8.h, left: 15.w, right: 15.w),
        padding: EdgeInsets.all(8.dg),
        height: 100.h,
        width: 500.w,
        decoration: BoxDecoration(
          color: Colors.black,
          border: Border.all(color: Colors.black),
          borderRadius: BorderRadius.circular(10),
        ),
        child: CustomPaint(
          size: const Size(400, 480),
          painter: SavedBadgePainter(grid: grid),
        ));
  }
}
