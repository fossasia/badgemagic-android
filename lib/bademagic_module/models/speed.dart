enum Speed {
  one('0x00'),
  two('0x10'),
  three('0x20'),
  four('0x30'),
  five('0x40'),
  six('0x50'),
  seven('0x60'),
  eight('0x70');

  final String hexValue;
  const Speed(this.hexValue);

  // Static method to get int value of speed from the Enum Speed
  static int getIntValue(Speed speed) {
    return int.parse(speed.hexValue.substring(2), radix: 16);
  }

  // Static method to get Speed from hex value
  static Speed fromHex(String hexValue) {
    return Speed.values.firstWhere(
      (speed) => speed.hexValue == hexValue,
      orElse: () => Speed.one, // Default to Speed.one if no match
    );
  }
}
