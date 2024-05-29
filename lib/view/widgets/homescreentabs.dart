import 'package:badgemagic/constants.dart';
import 'package:badgemagic/view/widgets/ani_container.dart';
import 'package:flutter/material.dart';


//effects tab to show effects that the user can select
class Effects_Tab extends StatefulWidget {
  const Effects_Tab({
    super.key,
  });

  @override
  State<Effects_Tab> createState() => _Effects_TabState();
}

class _Effects_TabState extends State<Effects_Tab> {
  @override
  Widget build(BuildContext context) {
    return Container(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          aniContainer(animation: eff_invert, ani_name: 'Invert'),
          aniContainer(animation: eff_flash, ani_name: 'Effect'),
          aniContainer(animation: eff_marque, ani_name: 'Marquee'),
        ],
      ),
    );
  }
}

//Animation tab to show animation choices for the user
class Animation_Tab extends StatefulWidget {
  const Animation_Tab({
    super.key,
  });

  @override
  State<Animation_Tab> createState() => _Animation_TabState();
}

class _Animation_TabState extends State<Animation_Tab> {
  @override
  Widget build(BuildContext context) {
    return  Column(
      children: [
        Row(
          children: [
            aniContainer(animation: ani_left, ani_name: 'Left'),
            aniContainer(animation: ani_right, ani_name: 'Right'),
            aniContainer(animation: ani_up, ani_name: 'Up'),
          ],
        ),
        Row(children: [
          aniContainer(animation: ani_down, ani_name: 'Down'),
          aniContainer(animation: ani_fixed, ani_name: 'Fixed'),
          aniContainer(animation: ani_fixed, ani_name: 'Snowflake'),
        ],),
        Row(children: [
          aniContainer(animation: ani_picture, ani_name: 'Picture'),
          aniContainer(animation: animation, ani_name: 'Animation'),
          aniContainer(animation: ani_laser, ani_name: 'Laser'),
        ],),
      ],
    );
  }
}
