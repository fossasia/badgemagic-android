import 'package:badgemagic/constants.dart';
import 'package:badgemagic/providers/badge_message_provider.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:badgemagic/view/widgets/homescreentabs.dart';
import 'package:badgemagic/view/widgets/speedial.dart';
import 'package:badgemagic/virtualbadge/view/badgeui.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> with TickerProviderStateMixin {
  late final TabController _tabController;
  BadgeMessageProvider badgeData = BadgeMessageProvider();

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 3, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    double height = MediaQuery.of(context).size.height;
    double width = MediaQuery.of(context).size.width;
    CardProvider cardData = Provider.of<CardProvider>(context);
    //seting the context to be used by the scaffold messenger
    WidgetsBinding.instance.addPostFrameCallback((_) {
      cardData.setContext(context);
    });
    return DefaultTabController(
      length: 3,
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.red,
          title: const Text(
            key: Key(homeScreenTitleKey),
            'Badge Magic',
            style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
          ),
          centerTitle: true,
        ),
        body: SafeArea(
          child: SizedBox(
              height: height,
              width: width,
              child: SingleChildScrollView(
                child: Column(
                  children: [
                    const BMBadge(),
                    Container(
                      margin: const EdgeInsets.all(15),
                      child: Material(
                        borderRadius: BorderRadius.circular(10),
                        elevation: 10,
                        child: TextField(
                          controller: cardData.getController(),
                          decoration: InputDecoration(
                              border: OutlineInputBorder(
                                  borderRadius: BorderRadius.circular(10)),
                              prefixIcon: const Icon(Icons.tag_faces_outlined),
                              focusedBorder: const OutlineInputBorder(
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
                    SingleChildScrollView(
                      child: Column(
                        children: [
                          AspectRatio(
                            aspectRatio: 1.5,
                            child: TabBarView(
                              physics: const NeverScrollableScrollPhysics(),
                              controller: _tabController,
                              children: const [
                                RadialDial(),
                                AnimationTab(),
                                EffectTab(),
                              ],
                            ),
                          ),
                          Container(
                            padding: EdgeInsets.only(
                                bottom: height * 0.2,
                                top: height *
                                    0.02), // Adjust the value as needed
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                GestureDetector(
                                  onTap: () {
                                    badgeData.checkAndTransffer();
                                  },
                                  child: Container(
                                    padding: const EdgeInsets.symmetric(
                                        horizontal: 20, vertical: 8),
                                    decoration: BoxDecoration(
                                      borderRadius: BorderRadius.circular(10),
                                      color: Colors.grey.shade400,
                                    ),
                                    child: const Text('Transfer'),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              )),
        ),
      ),
    );
  }
}
