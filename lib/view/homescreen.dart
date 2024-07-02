import 'package:badgemagic/constants.dart';
import 'package:badgemagic/providers/badge_message_provider.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:badgemagic/view/widgets/homescreentabs.dart';
import 'package:badgemagic/view/widgets/speedial.dart';
import 'package:badgemagic/virtualbadge/view/badgeui.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

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
    CardProvider cardData = Provider.of<CardProvider>(context);
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
          child: SingleChildScrollView(
            child: Column(
              children: [
                const BMBadge(),
                Container(
                  margin: EdgeInsets.all(15.w),
                  child: Material(
                    borderRadius: BorderRadius.circular(10.r),
                    elevation: 10,
                    child: TextField(
                      controller: cardData.getController(),
                      decoration: InputDecoration(
                          border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(10.r)),
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
                SizedBox(
                  height: 230.h, // Adjust the height dynamically
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
                  padding: EdgeInsets.symmetric(vertical: 20.h),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      GestureDetector(
                        onTap: () {
                          badgeData.checkAndTransfer();
                        },
                        child: Container(
                          padding: EdgeInsets.symmetric(
                              horizontal: 20.w, vertical: 8.h),
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(10.r),
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
        ),
      ),
    );
  }
}
