enum Speed {
    ONE('0x00'),
    TWO('0x10'),
    THREE('0x20'),
    FOUR('0x30'),
    FIVE('0x40'),
    SIX('0x50'),
    SEVEN('0x60'),
    EIGHT('0x70');

  final String hexValue;

  const Speed(this.hexValue);
}