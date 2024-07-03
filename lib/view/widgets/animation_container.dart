import 'package:flutter/material.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:provider/provider.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class AniContainer extends StatefulWidget {
  final String animation;
  final String aniName;
  final int index;

  const AniContainer({
    super.key,
    required this.animation,
    required this.aniName,
    required this.index,
  });

  @override
  State<AniContainer> createState() => _AniContainerState();
}

class _AniContainerState extends State<AniContainer> {
  @override
  Widget build(BuildContext context) {
    CardProvider animationCardState = Provider.of<CardProvider>(context);

    return Container(
      margin: EdgeInsets.all(5.w),
      height: 60.h,
      width: 110.w,
      child: GestureDetector(
        onTap: () {
          animationCardState.setAnimationIndex(widget.index);
        },
        child: Card(
          surfaceTintColor: Colors.white,
          color: animationCardState.getAnimationIndex() == widget.index
              ? Colors.red
              : Colors.white,
          elevation: 5,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Image.asset(
                widget.animation,
                height: 20.h,
              ),
              Text(widget.aniName),
            ],
          ),
        ),
      ),
    );
  }
}
