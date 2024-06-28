import 'package:badgemagic/bademagic_module/bluetooth/bluetooth.dart';
import 'package:badgemagic/bademagic_module/models/data.dart';
import 'package:badgemagic/bademagic_module/models/messages.dart';
import 'package:badgemagic/bademagic_module/models/mode.dart';
import 'package:badgemagic/bademagic_module/models/speed.dart';
import 'package:badgemagic/bademagic_module/utils/converters.dart';
import 'package:flutter/material.dart';
import 'package:logger/logger.dart';

class BadgeMessageProvider extends ChangeNotifier {
  BadgeMagicBluetooth badgeMagicBluetooth = BadgeMagicBluetooth();
  static final Logger logger = Logger();

  Map<int, Mode> modeValueMap = {
    0: Mode.left,
    1: Mode.right,
    2: Mode.up,
    3: Mode.down,
    4: Mode.fixed,
    5: Mode.snowflake,
    6: Mode.picture,
    7: Mode.animation,
    8: Mode.laser
  };

  Map<int, Speed> speedMap = {
    1: Speed.one,
    2: Speed.two,
    3: Speed.three,
    4: Speed.four,
    5: Speed.five,
    6: Speed.six,
    7: Speed.seven,
    8: Speed.eight,
  };

  void generateMessage(
      String text, bool flash, bool marq, Speed speed, Mode mode) {
    Data data = Data(messages: [
      Message(text: Converters.messageTohex('AB')),
      Message(text: Converters.messageTohex('ÈC')),
    ]);
    dataFormed(data);
    transferData(data);
  }

  void transferData(Data data) {
    BadgeMagicBluetooth.scanAndConnect(data);
    logger.d(".......Data is being transferred.......");
  }

  void dataFormed(Data data) => logger.d(
      "${data.messages.length} message : ${data.messages[0].text} Flash : ${data.messages[0].flash} Marquee : ${data.messages[0].marquee} Mode : ${data.messages[0].mode}");
}
