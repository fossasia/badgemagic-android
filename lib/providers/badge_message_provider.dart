import 'package:badgemagic/bademagic_module/bluetooth/bluetooth.dart';
import 'package:badgemagic/bademagic_module/models/data.dart';
import 'package:badgemagic/bademagic_module/models/messages.dart';
import 'package:badgemagic/bademagic_module/models/mode.dart';
import 'package:badgemagic/bademagic_module/models/speed.dart';
import 'package:flutter/material.dart';

class BadgeMessageProvider extends ChangeNotifier {

  Map<int, Mode> mode_value_map = {
    0: Mode.LEFT,
    1: Mode.RIGHT,
    2: Mode.UP,
    3: Mode.DOWN,
    4: Mode.FIXED,
    5: Mode.SNOWFLAKE,
    6: Mode.PICTURE,
    7: Mode.ANIMATION,
    8: Mode.LASER
  };

  void generateMessage(String text, bool flash, bool marq, Speed speed, Mode mode) {
    Data data = Data(messages:[Message(text: text, flash: flash, marquee: marq, speed: speed, mode: mode)]);
    dataFormed(data);
    transferData(data);
  }

  void transferData(Data data) {
    scanAndConnect(data);
    print(".......Data is being transferred.......");
  }

  void dataFormed(Data data) => print("${data.messages.length} message : ${data.messages[0].text} Flash : ${data.messages[0].flash} Marquee : ${data.messages[0].marquee} Mode : ${data.messages[0].mode}");
}