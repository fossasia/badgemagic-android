import 'package:badgemagic/bademagic_module/bluetooth/ble_state_interface.dart';
import 'package:badgemagic/bademagic_module/bluetooth/scanstate.dart';
import 'package:badgemagic/bademagic_module/models/data.dart';
import 'package:badgemagic/bademagic_module/models/messages.dart';
import 'package:badgemagic/bademagic_module/models/mode.dart';
import 'package:badgemagic/bademagic_module/models/speed.dart';
import 'package:badgemagic/bademagic_module/utils/converters.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:logger/logger.dart';

class BadgeMessageProvider {
  static final Logger logger = Logger();
  BleState state = ScanState();
  CardProvider cardData = GetIt.instance<CardProvider>();

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

  Data generateData(
      String text, bool flash, bool marq, Speed speed, Mode mode) {
    Data data = Data(messages: [
      Message(
        text: Converters.messageTohex(text),
        flash: flash,
        marquee: marq,
        speed: speed,
        mode: mode,
      )
    ]);
    logger.d(
        "${data.messages.length} message : ${data.messages[0].text} Flash : ${data.messages[0].flash} Marquee : ${data.messages[0].marquee} Mode : ${data.messages[0].mode}");
    return data;
  }

  void transferData() {
    state.processState();
    logger.d(".......Data is being transferred.......");
  }

  void checkAndTransffer() {
    if (cardData.getController().text.isEmpty) {
      ScaffoldMessenger.of(cardData.getContext()!).showSnackBar(
        SnackBar(
            margin: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
            elevation: 10,
            duration: const Duration(seconds: 1),
            content: const Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Image(
                  image: AssetImage('assets/icons/icon.png'),
                  height: 20,
                ),
                SizedBox(
                  width: 10,
                ),
                Text(
                  'Please enter a message to send',
                  style: TextStyle(color: Colors.black),
                )
              ],
            ),
            backgroundColor: Colors.white,
            behavior: SnackBarBehavior.floating,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10),
            )),
      );
      return;
    }

    ScaffoldMessenger.of(cardData.getContext()!).showSnackBar(
      SnackBar(
        margin: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
        elevation: 10,
        duration: const Duration(seconds: 1),
        content: const Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Image(
              image: AssetImage('assets/icons/icon.png'),
              height: 20,
            ),
            SizedBox(
              width: 10,
            ),
            Text(
              'Searching for device...',
              style: TextStyle(color: Colors.black),
            )
          ],
        ),
        backgroundColor: Colors.white,
        behavior: SnackBarBehavior.floating,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10),
        ),
        dismissDirection: DismissDirection.startToEnd,
      ),
    );

    transferData();
  }
}
