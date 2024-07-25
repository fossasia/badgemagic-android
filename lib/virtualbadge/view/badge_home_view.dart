import 'package:badgemagic/virtualbadge/widgets/badgeWidget.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class BMBadgeHome extends StatefulWidget {
  const BMBadgeHome({super.key});

  @override
  State<BMBadgeHome> createState() => _BMBadgeHomeState();
}

class _BMBadgeHomeState extends State<BMBadgeHome> {
  @override
  Widget build(BuildContext context) {
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
      child: BadgeWidget(),
    );
  }
}
