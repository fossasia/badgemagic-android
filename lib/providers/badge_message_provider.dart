import 'package:badgemagic/bademagic_module/bluetooth/bluetooth.dart';
import 'package:badgemagic/bademagic_module/models/data.dart';
import 'package:badgemagic/bademagic_module/models/messages.dart';
import 'package:badgemagic/bademagic_module/models/mode.dart';
import 'package:badgemagic/bademagic_module/models/speed.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:flutter/foundation.dart';

class BadgeMessageProvider extends ChangeNotifier{


  //Data object to add the number of messages as per the user requirement
  Data data = new Data(messages: []);

  //Map to get the mode according to the value selected at the UI
  Map mode_value_map = {
  0 : Mode.LEFT,
  1 : Mode.RIGHT,
  2 : Mode.UP,
  3 : Mode.DOWN,
  4 : Mode.FIXED,
  5 : Mode.SNOWFLAKE,
  6 : Mode.PICTURE,
  7 : Mode.ANIMATION,
  8 : Mode.LASER
};

void generateMessage(String text)
{
  //obj of card provider to get the data at the time of transffer
  CardProvider cardData = new CardProvider();

  bool flash = cardData.getEffectIndex(1) == 1? true : false;
  bool marquee = cardData.getEffectIndex(2) == 1? true : false;
  Speed speed = Speed.THREE;
  Mode mode = mode_value_map[cardData.getAnimationIndex()];
  Message msg = new Message(text: text,flash: flash,marquee: marquee,speed: speed,mode: mode);
  addMessageToData(msg);

}

//adding the created Message object to the data object
void addMessageToData(Message message) => data.messages.add(message);

//function to transffer the generated message data to the badge
void transferData() => scanAndConnect(data);

// method to check whether the data is formed correctly or not
void dataFormed() => print(data.toString());

}