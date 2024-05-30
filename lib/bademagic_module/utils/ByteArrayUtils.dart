String toHex(List<int> bytes) {
  StringBuffer buffer = StringBuffer();
  for (int byte in bytes) {
    buffer.write('${byte < 16 ? '0' : ''}${byte.toRadixString(16)}');
  }
  return buffer.toString().toUpperCase();
}

List<int> hexStringToByteArray(String hexString) {
  if (hexString.length % 2 != 0) {
    throw ArgumentError("Invalid hex string: $hexString");
  }

  List<int> data = [];
  for (int i = 0; i < hexString.length; i += 2) {
    int firstDigit = int.parse(hexString[i], radix: 16);
    int secondDigit = int.parse(hexString[i + 1], radix: 16);
    data.add((firstDigit << 4) + secondDigit);
  }
  print(data.length);
  return data;
}
