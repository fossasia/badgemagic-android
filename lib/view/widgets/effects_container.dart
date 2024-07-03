import 'package:flutter/material.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:provider/provider.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class EffectContainer extends StatefulWidget {
  final String effect;
  final String effectName;
  final int index;

  const EffectContainer({
    super.key,
    required this.effect,
    required this.effectName,
    required this.index,
  });

  @override
  State<EffectContainer> createState() => _EffectContainerState();
}

class _EffectContainerState extends State<EffectContainer> {
  @override
  Widget build(BuildContext context) {
    CardProvider effectCardState = Provider.of<CardProvider>(context);

    return Container(
      margin: EdgeInsets.all(5.w),
      height: 90.h,
      width: 110.w,
      child: GestureDetector(
        onTap: () {
          effectCardState.setEffectIndex(widget.index);
        },
        child: Card(
          surfaceTintColor: Colors.white,
          color: effectCardState.getEffectIndex(widget.index) == 1
              ? Colors.red
              : Colors.white,
          elevation: 5,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Image.asset(
                widget.effect,
                height: 60.h,
              ),
              Text(widget.effectName),
            ],
          ),
        ),
      ),
    );
  }
}
