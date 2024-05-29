import 'package:badgemagic/view/widgets/homescreentabs.dart';
import 'package:badgemagic/virtualbadge/view/badgeui.dart';
import 'package:flutter/material.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _homescreenState();
}

class _homescreenState extends State<HomeScreen> with TickerProviderStateMixin {
  late final TabController _tabController;
  @override
  void initState() {
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
          title:const Text(
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
                margin:const EdgeInsets.all(15),
                child: Material(
                  borderRadius: BorderRadius.circular(10),
                  elevation: 10,
                  child: TextField(
                    decoration: InputDecoration(
                        border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(10)),
                        prefixIcon: const Icon(Icons.tag_faces_outlined),
                        focusedBorder:const  OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.red))),
                  ),
                ),
              ),
              TabBar(
                indicatorSize: TabBarIndicatorSize.label,
                controller: _tabController,
                tabs: const [
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
                  const Animation_Tab(),
                  const Effects_Tab(),
                ],),
              ),
            ],
          ),
        ),
      ),
    );
  }
}


