import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';


//This the container that holds the UI appearence of the animation and effects Tabs
class aniContainer extends StatefulWidget {
  final String animation;
  final String ani_name;
  final int index;
  aniContainer({super.key, required this.animation, required this.ani_name, required this.index});

  @override
  State<aniContainer> createState() => _aniContainerState();
}

class _aniContainerState extends State<aniContainer> {
  Color tintcolor = Colors.white;
  @override
  Widget build(BuildContext context) {
    CardProvider animationcardstate = Provider.of<CardProvider>(context);
    double height = MediaQuery.of(context).size.height;
    double width = MediaQuery.of(context).size.width;
    return Container(
      margin: EdgeInsets.all(5),
      height: height * 0.15,
      width: width * 0.307,
      child: GestureDetector(
        onTap: () {
          animationcardstate.setAnimationIndex(widget.index);
        },
        child: Card(
          surfaceTintColor: Colors.white,
          color: animationcardstate.getAnimationIndex() == widget.index ? Colors.red : Colors.white,
          elevation: 10,
          child: Column(
            children: [
              Image(
                image: AssetImage(widget.animation),
                height: height * 0.1,
              ),
              Text(widget.ani_name),
            ],
          ),
        ),
      ),
    );
  }
}
