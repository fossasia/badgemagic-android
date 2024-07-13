import 'dart:typed_data';
import 'dart:ui' as ui;

import 'package:badgemagic/bademagic_module/utils/image_utils.dart';
import 'package:badgemagic/constants.dart';
import 'package:flutter/material.dart';

class InlineImageProvider extends ChangeNotifier {
  //to test the delete operation in TextField
  //used for compairing the length of the current textfield and the prevous
  //if the length of the current controller length is greater than the previous (add operation)
  //else delte operation is performed.
  int controllerLength = 0;

  //object of ImageUtils class to generate ImageCache
  ImageUtils imageUtils = ImageUtils();

  //controller for the Textfield
  TextEditingController message = TextEditingController();

  //getter for the textfield controller
  TextEditingController getController() => message;

  //selected index of the vector from the list
  late int selectedVector;

  //Map to store the cache of the images generated
  //Image caches are generated at the splash screen
  //The cache generation time acts as a delay in the splash screen
  Map<int, Uint8List?> imageCache = {};

  //function that generates the image cache
  //it fills the map with the Unit8List(byte Array) of the images
  Future<void> generateImageCache() async {
    for (int x = 0; x < vectors.length; x++) {
      ui.Image image = await imageUtils.generateImageView(vectors[x]);
      ByteData? byteData =
          await image.toByteData(format: ui.ImageByteFormat.png);
      var unit8List = byteData!.buffer.asUint8List();
      imageCache[x] = unit8List;
    }
    notifyListeners();
  }

  //updates the controller with the placeholder for the image
  void insertInlineImage(int index) {
    if (index < 10) {
      message.text += '<<0$index>>';
    } else {
      message.text += '<<$index>>';
    }
    print(message.text);
    controllerLength = message.text.length;
    notifyListeners();
  }

  void controllerListener() {
    // Assuming controllerLength is meant to be message.text.length
    int textLength = message.text.length;

    // Check if the text length is sufficient to contain the pattern
    if (textLength >= 4) {
      // Check if the last character is '>' and the character four positions back is '<'
      if (message.text[textLength - 1] == '>' &&
          message.text[textLength - 4] == '<') {
        // Delete the pattern by keeping the text before the pattern
        message.text = message.text.substring(0, textLength - 5);
      }
    }
  }
}
