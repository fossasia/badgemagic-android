import 'dart:io';
import 'package:badgemagic/bademagic_module/bluetooth/ble_state_interface.dart';
import 'package:badgemagic/bademagic_module/bluetooth/bletoast.dart';
import 'package:badgemagic/bademagic_module/bluetooth/completedstate.dart';
import 'package:badgemagic/bademagic_module/bluetooth/scanstate.dart';
import 'package:badgemagic/bademagic_module/models/data.dart';
import 'package:badgemagic/bademagic_module/models/messages.dart';
import 'package:badgemagic/bademagic_module/models/mode.dart';
import 'package:badgemagic/bademagic_module/models/speed.dart';
import 'package:badgemagic/bademagic_module/utils/converters.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:get_it/get_it.dart';
import 'package:logger/logger.dart';

class BadgeMessageProvider {
  static final Logger logger = Logger();
  CardProvider cardData = GetIt.instance<CardProvider>();
  BleStateToast toast = BleStateToast();

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

  Future<void> transferData() async {
    DateTime now = DateTime.now();
    BleState state = ScanState();
    while (state is! CompletedState) {
      BleState? nextState = await state.processState();
      if (nextState != null) {
        state = nextState;
      } else {
        break;
      }
    }
    if (state is CompletedState) {
      await state.processState(); // Ensure the toast is shown
    }
    logger.d("Time to transfer data is = ${DateTime.now().difference(now)}");
    logger.d(".......Data transfer completed.......");
  }

  Future<void> checkAndTransfer() async {
    if (await FlutterBluePlus.isSupported == false) {
      toast.failureToast('Bluetooth is not supported by the device');
      return;
    }

    if (cardData.getController().text.isEmpty) {
      toast.failureToast("Please enter a message");
      return;
    }

    final adapterState = await FlutterBluePlus.adapterState.first;
    if (adapterState == BluetoothAdapterState.on) {
      await transferData();
    } else {
      if (Platform.isAndroid) {
        toast.successToast('Turning on Bluetooth...');
        await FlutterBluePlus.turnOn();
      } else if (Platform.isIOS) {
        toast.successToast('Please turn on Bluetooth');
      }
    }
  }
}
