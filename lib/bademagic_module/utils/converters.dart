import 'package:badgemagic/bademagic_module/utils/data_to_bytearray_converter.dart';

class Converters {
  //this function converts the user entered message to hex
  //compares the message to the map of characters and returns the hex value of the character
  //then adds the hexstring to the list
  //thus generating the hex value of the message
  static List<String> messageTohex(String message) {
    List<String> messages = [];
    int i = 0;
    while (i < message.length) {
      var ch = message[i];
      logger.d("ch = $ch");
      if (charCodes.containsKey(ch)) {
        messages.add(charCodes[ch]!);
      }
      i++;
    }
    logger.d("message to hex = $message");
    return messages;
  }
}
