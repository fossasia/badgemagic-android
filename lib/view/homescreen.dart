import 'package:badgemagic/constants.dart';
import 'package:badgemagic/view/widgets/ani_container.dart';
import 'package:badgemagic/virtualbadge/view/badgeui.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class homescreen extends StatefulWidget {
  const homescreen({super.key});

  @override
  State<homescreen> createState() => _homescreenState();
}

class _homescreenState extends State<homescreen> with TickerProviderStateMixin {
  late final TabController _tabController;
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    _tabController = TabController(length: 3, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 3,
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.red,
          title: Text(
            'Badge Magic',
            style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
          ),
          centerTitle: true,
        ),
        body: SafeArea(
          child: Column(
            children: [
              badge(),
              Container(
                margin: EdgeInsets.all(15),
                child: Material(
                  borderRadius: BorderRadius.circular(10),
                  elevation: 10,
                  child: TextField(
                    decoration: InputDecoration(
                        border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(10)),
                        prefixIcon: Icon(Icons.tag_faces_outlined),
                        focusedBorder: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.red))),
                  ),
                ),
              ),
              TabBar(
                indicatorSize: TabBarIndicatorSize.label,
                controller: _tabController,
                tabs: [
                  Tab(text: 'Speed'),
                  Tab(text: 'Animation'),
                  Tab(text: 'Effects'),
                ],
              ),
              Expanded(
                child: TabBarView(
                  controller: _tabController,
                  children: [
                  Container(),
                  Container(
                    child: const Column(
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
                    ),
                  ),
                  Container(),
                ],),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
