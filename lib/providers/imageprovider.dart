import 'dart:convert';
import 'dart:ui' as ui;
import 'package:badgemagic/bademagic_module/utils/byte_array_utils.dart';
import 'package:badgemagic/bademagic_module/utils/image_utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class InlineImageProvider extends ChangeNotifier {
  //list of vectors
  List<String> vectors = [];

  //initializes the list of vectors
  //uses the AssetManifest class to load the list of assets
  Future<void> initVectors() async {
    try {
      final manifestContent = await rootBundle.loadString('AssetManifest.json');
      final Map<String, dynamic> manifestMap = json.decode(manifestContent);

      final imageAssetsList = manifestMap.keys
          .where((String key) => key.startsWith('assets/vectors/'))
          .toList();
      vectors.addAll(imageAssetsList);
      notifyListeners();
    } catch (e) {
      logger.e('Error loading asset manifest: $e');
    }
  }

  //to test the delete operation in TextField
  //used for compairing the length of the current textfield and the prevous
  //if the length of the current controller length is greater than the previous (add operation)
  //else delte operation is performed.
  int controllerLength = 0;

  //Map to store the hex values of the characters
  final Map<int, List<String>> hexStrings = {};

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
    await initVectors();
    for (int x = 0; x < vectors.length; x++) {
      ui.Image image = await imageUtils.generateImageView(vectors[x]);
      ByteData? byteData =
          await image.toByteData(format: ui.ImageByteFormat.png);
      var unit8List = byteData!.buffer.asUint8List();
      imageCache[x] = unit8List;
    }
    notifyListeners();
  }

  void insertInlineImage(int index) {
    String placeholder = index < 10 ? '<<0$index>>' : '<<$index>>';
    int cursorPos =
        message.selection.baseOffset == -1 ? 0 : message.selection.baseOffset;
    String beforeCursor = message.text.substring(0, cursorPos);
    String afterCursor = message.text.substring(cursorPos);
    message.text = beforeCursor + placeholder + afterCursor;
    message.selection = TextSelection.fromPosition(
        TextPosition(offset: cursorPos + placeholder.length));
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
