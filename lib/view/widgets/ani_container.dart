import 'package:flutter/material.dart';


//This the container that holds the UI appearence of the animation and effects Tabs
class aniContainer extends StatefulWidget {
  final String animation;
  final String ani_name;
  aniContainer({super.key, required this.animation, required this.ani_name});

  @override
  State<aniContainer> createState() => _aniContainerState();
}

class _aniContainerState extends State<aniContainer> {
  Color tintcolor = Colors.white;
  @override
  Widget build(BuildContext context) {
    double height = MediaQuery.of(context).size.height;
    double width = MediaQuery.of(context).size.width;
    return Container(
      margin: EdgeInsets.all(5),
      height: height * 0.15,
      width: width * 0.307,
      child: GestureDetector(
        onTap: () {
          setState(() {
            if (tintcolor == Colors.white) {
              tintcolor = Colors.red;
            } else {
              tintcolor = Colors.white;
            }
          });
        },
        child: Card(
          surfaceTintColor: Colors.white,
          color: tintcolor,
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
