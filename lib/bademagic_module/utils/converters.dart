import 'package:badgemagic/bademagic_module/utils/data_to_bytearray_converter.dart';

class Converters {
  //this function converts the user entered message to hex
  //compares the message to the map of characters and returns the hex value of the character
  //then adds the hexstring to the list
  //thus generating the hex value of the message
  static List<String> messageTohex(String message) {
    DataToByteArrayConverter converter = DataToByteArrayConverter();
    List<String> messages = [];
    int i = 0;
    while (i < message.length) {
      var ch = message[i];
      if (converter.charCodes.containsKey(ch)) {
        messages.add(converter.charCodes[ch]!);
      }
      i++;
    }
    return messages;
  }

  //function to convert the bitmap to the LED hex format
  //it takes the 2D list of pixels and converts it to the LED hex format
  static List<String> convertBitmapToLEDHex(List<List<int>> image) {
    // Determine the height and width of the image
    int height = image.length;
    int width = image.isNotEmpty ? image[0].length : 0;

    // Initialize variables to calculate padding and offsets
    int finalSum = 0;

    // Calculate and adjust for right-side padding
    for (int j = 0; j < width; j++) {
      int sum = 0;
      for (int i = 0; i < height; i++) {
        sum += image[i][j]; // Sum up pixel values in each column
      }
      if (sum == 0) {
        // If column sum is zero, mark all pixels in that column as -1
        for (int i = 0; i < height; i++) {
          image[i][j] = -1;
        }
      } else {
        // Otherwise, update finalSum and exit loop
        finalSum += j;
        break;
      }
    }

    // Calculate and adjust for left-side padding
    for (int j = width - 1; j >= 0; j--) {
      int sum = 0;
      for (int i = 0; i < height; i++) {
        sum += image[i]
            [j]; // Sum up pixel values in each column (from right to left)
      }
      if (sum == 0) {
        // If column sum is zero, mark all pixels in that column as -1
        for (int i = 0; i < height; i++) {
          image[i][j] = -1;
        }
      } else {
        // Otherwise, update finalSum and exit loop
        finalSum += (height - j - 1);
        break;
      }
    }

    // Calculate padding difference to align height to a multiple of 8
    int diff = 0;
    if ((height - finalSum) % 8 > 0) {
      diff = 8 - (height - finalSum) % 8;
    }

    // Calculate left and right offsets for padding
    int rOff = (diff / 2).floor();
    int lOff = (diff / 2).ceil();

    // Initialize a new list to accommodate the padded image
    List<List<int>> list =
        List.generate(height, (i) => List.filled(width + rOff + lOff, 0));

    // Fill the new list with the padded image data
    for (int i = 0; i < height; i++) {
      int k = 0;
      for (int j = 0; j < rOff; j++) {
        list[i][k++] = 0; // Fill right-side padding
      }
      for (int j = 0; j < width; j++) {
        if (image[i][j] != -1) {
          list[i][k++] = image[i][j]; // Copy non-padded pixels
        }
      }
      for (int j = 0; j < lOff; j++) {
        list[i][k++] = 0; // Fill left-side padding
      }
    }

    // Convert each 8-bit segment into hexadecimal strings
    List<String> allHexs = [];
    for (int i = 0; i < list[0].length ~/ 8; i++) {
      StringBuffer lineHex = StringBuffer();

      for (int k = 0; k < height; k++) {
        StringBuffer stBuilder = StringBuffer();

        // Construct 8-bit segments for each row
        for (int j = i * 8; j < i * 8 + 8; j++) {
          stBuilder.write(list[k][j]);
        }

        // Convert binary string to hexadecimal
        String hex = int.parse(stBuilder.toString(), radix: 2)
            .toRadixString(16)
            .padLeft(2, '0');
        lineHex.write(hex); // Append hexadecimal to line
      }

      allHexs.add(lineHex.toString()); // Store completed hexadecimal line
    }

    return allHexs; // Return list of hexadecimal strings
  }
}
