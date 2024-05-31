import 'package:badgemagic/constants.dart';
import 'package:badgemagic/view/widgets/animation_container.dart';
import 'package:badgemagic/view/widgets/effects_container.dart';
import 'package:flutter/material.dart';


//effects tab to show effects that the user can select
class EffectTab extends StatefulWidget {
  const EffectTab({
    super.key,
  });

  @override
  State<EffectTab> createState() => _Effects_TabState();
}

class _Effects_TabState extends State<EffectTab> {

  @override
  void initState() {
    super.initState();
  }
  @override
  Widget build(BuildContext context) {
    return const Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        EffectContainer(effect: eff_invert, effect_name: 'Invert', index: 0,),
        EffectContainer(effect: eff_flash, effect_name: 'Effect', index: 1,),
        EffectContainer(effect: eff_marque, effect_name: 'Marquee', index: 2,),
      ],
    );
  }
}

//Animation tab to show animation choices for the user
class AnimationTab extends StatefulWidget {
  const AnimationTab({
    super.key,
  });

  @override
  State<AnimationTab> createState() => _AnimationTabState();
}

class _AnimationTabState extends State<AnimationTab> {
  @override
  Widget build(BuildContext context) {
    return  Column(
      children: [
        Row(
          children: [
            aniContainer(animation: ani_left, ani_name: 'Left', index: 0,),
            aniContainer(animation: ani_right, ani_name: 'Right', index: 1,),
            aniContainer(animation: ani_up, ani_name: 'Up', index: 2,),
          ],
        ),
        Row(children: [
          aniContainer(animation: ani_down, ani_name: 'Down', index: 3,),
          aniContainer(animation: ani_fixed, ani_name: 'Fixed', index: 4,),
          aniContainer(animation: ani_fixed, ani_name: 'Snowflake', index: 5,),
        ],),
        Row(children: [
          aniContainer(animation: ani_picture, ani_name: 'Picture', index: 6,),
          aniContainer(animation: animation, ani_name: 'Animation', index: 7,),
          aniContainer(animation: ani_laser, ani_name: 'Laser', index: 8,),
        ],),
      ],
    );
  }
}
