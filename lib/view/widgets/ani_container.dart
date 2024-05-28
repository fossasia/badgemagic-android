import 'package:flutter/material.dart';

class aniContainer extends StatelessWidget {
  final String animation;
  final String ani_name;
  const aniContainer({super.key, required this.animation, required this.ani_name});

  @override
  Widget build(BuildContext context) {
    Color tintcolor = Colors.white;
    double height =  MediaQuery.of(context).size.height;
    double width  =   MediaQuery.of(context).size.width;
    return GestureDetector(
      onTap: (){
        if(tintcolor == Colors.white)
        {
           tintcolor = Colors.red;
        }
        else
        {
          tintcolor = Colors.white;
        }
      },
      child: Container(
        margin: EdgeInsets.all(5),
        height: height*0.15,
        width:  width*0.307,
        child: Card(
          surfaceTintColor:tintcolor,
          elevation: 10,
          child: Column(children: [
            Image(image: AssetImage(animation),height: height*0.1,),
            Text(ani_name),
          ],),
        ),
      ),
    );
  }
}