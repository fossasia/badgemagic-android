import 'package:badgemagic/bademagic_module/models/speed.dart';
import 'package:badgemagic/providers/badge_message_provider.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:badgemagic/view/widgets/homescreentabs.dart';
import 'package:flutter/material.dart';
import 'package:logger/logger.dart';
import 'package:provider/provider.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> with TickerProviderStateMixin {
  static final Logger logger = Logger();

  late final TabController _tabController;
  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 3, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    BadgeMessageProvider badgeData = Provider.of<BadgeMessageProvider>(context);
    CardProvider cardData = Provider.of<CardProvider>(context);
    return DefaultTabController(
      length: 3,
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.red,
          title: const Text(
            'Badge Magic',
            style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
          ),
          centerTitle: true,
        ),
        body: SafeArea(
          child: Column(
            children: [
              const Badge(),
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
              Expanded(
                child: TabBarView(
                  controller: _tabController,
                  children: [
                    Container(),
                    const AnimationTab(),
                    const EffectTab(),
                  ],
                ),
              ),
              TextButton(
                  onPressed: () async {
                    logger.d(
                        "${cardData.getAnimationIndex()} : ${cardData.getController().text} : ${cardData.getEffectIndex(2)}");
                    badgeData.generateMessage(
                        cardData.getController().text,
                        cardData.getEffectIndex(1) == 1,
                        cardData.getEffectIndex(2) == 1,
                        Speed.eight,
                        badgeData.modeValueMap[cardData.getAnimationIndex()]!);
                  },
                  child: const Text('Transffer'))
            ],
          ),
        ),
      ),
    );
  }
}
