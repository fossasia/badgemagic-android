import 'dart:io';
import 'dart:convert';
import 'dart:typed_data';
import 'package:badgemagic/bademagic_module/utils/byte_array_utils.dart';
import 'package:badgemagic/bademagic_module/utils/image_utils.dart';
import 'package:badgemagic/providers/imageprovider.dart';
import 'package:flutter/services.dart';
import 'package:get_it/get_it.dart';
import 'package:path_provider/path_provider.dart';
import 'package:uuid/uuid.dart';

class FileHelper {
  final InlineImageProvider imageCacheProvider =
      GetIt.instance<InlineImageProvider>();
  ImageUtils imageUtils = ImageUtils();
  static const Uuid uuid = Uuid();

  static Future<String> _getFilePath(String filename) async {
    final directory = await getApplicationDocumentsDirectory();
    return '${directory.path}/$filename';
  }

  static Future<File> _writeToFile(String filename, String data) async {
    final path = await _getFilePath(filename);

    logger.d('Writing to file: $path');

    return File(path).writeAsString(data);
  }

  // static Future<String> _readFromFile(String filename) async {
  //   try {
  //     final path = await _getFilePath(filename);
  //     return await File(path).readAsString();
  //   } catch (e) {
  //     return ''; // Return an empty string if there's an error
  //   }
  // }

  static String _generateUniqueFilename() {
    final String uniqueId = uuid.v4();
    final String timestamp = DateTime.now().millisecondsSinceEpoch.toString();
    return 'data_${timestamp}_$uniqueId.json';
  }

  // Add a new image to the cache
  void addToCache(Uint8List imageData) {
    int key;
    if (imageCacheProvider.availableKeys.isNotEmpty) {
      // Reuse the lowest available key
      key = imageCacheProvider.availableKeys.first;
      imageCacheProvider.availableKeys.remove(key);
    } else {
      // Assign a new key
      key = imageCacheProvider.imageCache.length;
      while (imageCacheProvider.imageCache.containsKey(key)) {
        key++;
      }
    }
    imageCacheProvider.imageCache[key] = imageData;
  }

  // Remove an image from the cache
  void removeFromCache(int key) {
    if (imageCacheProvider.imageCache.containsKey(key)) {
      imageCacheProvider.imageCache.remove(key);
      imageCacheProvider.availableKeys
          .add(key); // Add key to the pool of available keys
    }
  }

  // Generate a Uint8List from a 2D list (image data) and add it to the cache
  Future<void> _addImageDataToCache(List<List<dynamic>> imageData) async {
    // Convert List<List<dynamic>> to List<List<int>>
    List<List<int>> intImageData =
        imageData.map((list) => list.cast<int>()).toList();
    Uint8List imageBytes =
        await imageUtils.convert2DListToUint8List(intImageData);
    addToCache(imageBytes);
  }

  // Read all files, parse the 2D lists, and add to cache
  Future<void> loadImageCacheFromFiles() async {
    final directory = await getApplicationDocumentsDirectory();
    final List<FileSystemEntity> files = directory.listSync();

    for (var file in files) {
      if (file is File && file.path.endsWith('.json')) {
        final String content = await file.readAsString();
        if (content.isNotEmpty) {
          // Ensure correct type casting
          final List<dynamic> decodedData = jsonDecode(content);
          final List<List<dynamic>> imageData =
              decodedData.cast<List<dynamic>>();
          await _addImageDataToCache(imageData);
        }
      }
    }
  }

  // Save a 2D list to a file with a unique name
  Future<void> saveImage(List<List<bool>> imageData) async {
    List<List<int>> image = List.generate(
        imageData.length, (i) => List<int>.filled(imageData[i].length, 0));

    //convert the 2D list of bool into 2D list of int
    for (int i = 0; i < imageData.length; i++) {
      for (int j = 0; j < imageData[i].length; j++) {
        image[i][j] = imageData[i][j] ? 1 : 0;
      }
    }

    // Generate a unique filename
    String filename = _generateUniqueFilename();

    logger.d('Saving image to file: $filename');

    // Convert the 2D list to JSON string
    String jsonData = jsonEncode(image);

    logger.d('JSON data: $jsonData');

    // Write the JSON string to a file
    await _writeToFile(filename, jsonData);

    logger.d('Image saved to file: $filename');

    //Add the image to the image cache after saving it to a file
    await _addImageDataToCache(image);
  }
}
