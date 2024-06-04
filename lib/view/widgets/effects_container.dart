import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class EffectContainer extends StatefulWidget {
  final String effect;
  final String effectName;
  final int index;
  const EffectContainer(
      {super.key,
      required this.effect,
      required this.effectName,
      required this.index});

  @override
  State<EffectContainer> createState() => _EffectContainerState();
}

class _EffectContainerState extends State<EffectContainer> {
  @override
  Widget build(BuildContext context) {
    CardProvider effectcardstate = Provider.of<CardProvider>(context);
    double height = MediaQuery.of(context).size.height;
    double width = MediaQuery.of(context).size.width;
    return Container(
      margin: const EdgeInsets.all(5),
      height: height * 0.15,
      width: width * 0.307,
      child: GestureDetector(
        onTap: () {
          effectcardstate.setEffectIndex(widget.index);
        },
        child: Card(
          surfaceTintColor: Colors.white,
          color: effectcardstate.getEffectIndex(widget.index) == 1
              ? Colors.red
              : Colors.white,
          elevation: 10,
          child: Column(
            children: [
              Image(
                image: AssetImage(widget.effect),
                height: height * 0.1,
              ),
              Text(widget.effectName),
            ],
          ),
        ),
      ),
    );
  }
}
