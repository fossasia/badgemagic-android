import 'package:badgemagic/providers/imageprovider.dart';
import 'package:badgemagic/view/widgets/navigation_drawer.dart';
import 'package:badgemagic/virtualbadge/view/draw_badge_UI.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:provider/provider.dart';

class DrawBadge extends StatefulWidget {
  const DrawBadge({super.key});

  @override
  State<DrawBadge> createState() => _DrawBadgeState();
}

class _DrawBadgeState extends State<DrawBadge> {
  @override
  void dispose() {
    SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    InlineImageProvider drawToggle = Provider.of<InlineImageProvider>(context);
    SystemChrome.setEnabledSystemUIMode(SystemUiMode.immersiveSticky);
    SystemChrome.setPreferredOrientations([
      DeviceOrientation.landscapeLeft,
      DeviceOrientation.landscapeRight,
    ]);
    return Scaffold(
      appBar: AppBar(
        leading: Builder(builder: (context) {
          return IconButton(
              onPressed: () {
                Scaffold.of(context).openDrawer();
              },
              icon: const Icon(
                Icons.menu,
                color: Colors.white,
              ));
        }),
        backgroundColor: Colors.red,
        title: const Text(
          'Badge Magic',
          style: TextStyle(color: Colors.white),
        ),
      ),
      drawer: const BMDrawer(),
      body: Column(
        children: [
          Container(
            margin: EdgeInsets.symmetric(vertical: 20.h, horizontal: 20.w),
            padding: EdgeInsets.all(10.dg),
            height: 400.h,
            width: 500.w,
            decoration: BoxDecoration(
              color: Colors.black,
              border: Border.all(color: Colors.black),
              borderRadius: BorderRadius.circular(10),
            ),
            child: const BMBadge(),
          ),
          SizedBox(
            height: 55.h,
          ),
          Row(
            children: [
              TextButton(
                onPressed: () {
                  setState(() {
                    drawToggle.toggleIsDrawing(true);
                  });
                },
                child: Column(
                  children: [
                    Icon(
                      Icons.edit,
                      color:
                          drawToggle.getIsDrawing() ? Colors.red : Colors.black,
                    ),
                    Text(
                      'Draw',
                      style: TextStyle(
                        color: drawToggle.isDrawing ? Colors.red : Colors.black,
                      ),
                    )
                  ],
                ),
              ),
              TextButton(
                onPressed: () {
                  setState(() {
                    drawToggle.toggleIsDrawing(false);
                  });
                },
                child: Column(
                  children: [
                    Icon(
                      Icons.delete,
                      color: drawToggle.isDrawing ? Colors.black : Colors.red,
                    ),
                    Text(
                      'Erase',
                      style: TextStyle(
                        color: drawToggle.isDrawing ? Colors.black : Colors.red,
                      ),
                    )
                  ],
                ),
              ),
              TextButton(
                onPressed: () {
                  setState(() {
                    drawToggle.resetGrid();
                  });
                },
                child: const Column(
                  children: [
                    Icon(
                      Icons.refresh,
                      color: Colors.black,
                    ),
                    Text(
                      'Reset',
                      style: TextStyle(color: Colors.black),
                    )
                  ],
                ),
              ),
              TextButton(
                onPressed: () {},
                child: const Column(
                  children: [
                    Icon(
                      Icons.save,
                      color: Colors.black,
                    ),
                    Text('Save', style: TextStyle(color: Colors.black))
                  ],
                ),
              ),
            ],
          )
        ],
      ),
    );
  }
}
