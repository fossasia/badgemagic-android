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
  State<EffectTab> createState() => _EffectsTabState();
}

class _EffectsTabState extends State<EffectTab> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return const Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        EffectContainer(
          effect: effInvert,
          effectName: 'Invert',
          index: 0,
        ),
        EffectContainer(
          effect: effFlash,
          effectName: 'Effect',
          index: 1,
        ),
        EffectContainer(
          effect: effMarque,
          effectName: 'Marquee',
          index: 2,
        ),
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
    return const Column(
      children: [
        Row(
          children: [
            AniContainer(
              animation: aniLeft,
              aniName: 'Left',
              index: 0,
            ),
            AniContainer(
              animation: aniRight,
              aniName: 'Right',
              index: 1,
            ),
            AniContainer(
              animation: aniUp,
              aniName: 'Up',
              index: 2,
            ),
          ],
        ),
        Row(
          children: [
            AniContainer(
              animation: aniDown,
              aniName: 'Down',
              index: 3,
            ),
            AniContainer(
              animation: aniFixed,
              aniName: 'Fixed',
              index: 4,
            ),
            AniContainer(
              animation: aniFixed,
              aniName: 'Snowflake',
              index: 5,
            ),
          ],
        ),
        Row(
          children: [
            AniContainer(
              animation: aniPicture,
              aniName: 'Picture',
              index: 6,
            ),
            AniContainer(
              animation: animation,
              aniName: 'Animation',
              index: 7,
            ),
            AniContainer(
              animation: aniLaser,
              aniName: 'Laser',
              index: 8,
            ),
          ],
        ),
      ],
    );
  }
}
