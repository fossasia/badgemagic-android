import 'dart:ui' as ui;
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/flutter_svg.dart';

class ImageUtils {
  // Define target width and height for the bitmap
  static double targetHeight = 11.0;
  static double targetWidth = 44.0;

  //function to load and scale the svg according to the badge size
  Future<ui.Image> scaleSVG(String svg) async {
    //loading the Svg from the assets
    String svgString = await rootBundle.loadString(svg);

    // Load SVG picture and information
    final SvgStringLoader svgStringLoader = SvgStringLoader(svgString);
    final PictureInfo pictureInfo = await vg.loadPicture(svgStringLoader, null);
    final ui.Picture picture = pictureInfo.picture;

    //creating canvas to draw the svg on
    final ui.PictureRecorder recorder = ui.PictureRecorder();
    final ui.Canvas canvas = Canvas(recorder,
        Rect.fromPoints(Offset.zero, Offset(targetWidth, targetHeight)));

    //scaling the svg to the badge size
    canvas.scale(targetWidth / pictureInfo.size.width,
        targetHeight / pictureInfo.size.height);

    //drawing the svg on the canvas
    canvas.drawPicture(picture);

    //converting the canvas to the ui.Image object
    final ui.Image imgByteData = await recorder
        .endRecording()
        .toImage(targetWidth.ceil(), targetHeight.ceil());

    return imgByteData;
  }

  //function to convert the ui.Image to byte array
  Future<Uint8List?> convertImageToByteArray(ui.Image image) async {
    final ByteData? byteData =
        await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    return byteData?.buffer.asUint8List();
  }

  //function to convert the byte array to 2D list of pixels
  List<List<int>> convertUint8ListTo2DList(
      Uint8List byteArray, int width, int height) {
    //initialize the 2D list of pixels
    List<List<int>> pixelArray =
        List.generate(height, (i) => List<int>.filled(width, 0));
    int bytesPerPixel = 4; // RGBA format (4 bytes per pixel)
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int index = (y * width + x) * bytesPerPixel;
        if (index + bytesPerPixel <= byteArray.length) {
          int r = byteArray[index];
          int g = byteArray[index + 1];
          int b = byteArray[index + 2];
          int a = byteArray[index + 3];
          int color = (a << 24) | (r << 16) | (g << 8) | b;
          pixelArray[y][x] = color;
        } else {
          // Handle out-of-bounds case gracefully, e.g., fill with a default color
          pixelArray[y][x] = Colors.transparent.value;
        }
      }
    }
    return pixelArray;
  }

  //function to trim the bitmap with the transparent pixels
  //it iterates throught the 2D list of pixels and finds the bounding box of the non-transparent pixels
  //and crops the bitmap to that bounding box
  //it returns the cropped bitmap
  List<List<int>> trimedBitmap(List<List<int>> source) {
    int width = source[0].length;
    int height = source.length;

    int firstX = 0;
    int firstY = 0;
    int lastX = width;
    int lastY = height;

    bool found = false;

    // Find firstX
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (source[y][x] != 0) {
          firstX = x > 1 ? x - 1 : x;
          found = true;
          break;
        }
      }
      if (found) break;
    }

    found = false;

    // Find firstY
    for (int y = 0; y < height; y++) {
      for (int x = firstX; x < width; x++) {
        if (source[y][x] != 0) {
          firstY = y > 1 ? y - 1 : y;
          found = true;
          break;
        }
      }
      if (found) break;
    }

    found = false;

    // Find lastX
    for (int x = width - 1; x >= firstX; x--) {
      for (int y = height - 1; y >= firstY; y--) {
        if (source[y][x] != 0) {
          lastX = x < width - 2 ? x + 2 : x + 1;
          found = true;
          break;
        }
      }
      if (found) break;
    }

    found = false;

    // Find lastY
    for (int y = height - 1; y >= firstY; y--) {
      for (int x = width - 1; x >= firstX; x--) {
        if (source[y][x] != 0) {
          lastY = y < height - 2 ? y + 2 : y + 1;
          found = true;
          break;
        }
      }
      if (found) break;
    }

    // Create trimmed bitmap
    List<List<int>> trimmedBitmap = List.generate(
        lastY - firstY, (_) => List<int>.filled(lastX - firstX, 0));
    for (int y = firstY; y < lastY; y++) {
      for (int x = firstX; x < lastX; x++) {
        trimmedBitmap[y - firstY][x - firstX] = source[y][x];
      }
    }

    // Scale the bitmap to the desired dimension
    return trimmedBitmap;
  }
}
