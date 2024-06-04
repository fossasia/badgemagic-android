enum Mode {
  left('0x00'),
  right('0x01'),
  up('0x02'),
  down('0x03'),
  fixed('0x04'),
  snowflake('0x05'),
  picture('0x06'),
  animation('0x07'),
  laser('0x08');

  final String hexValue;
  const Mode(this.hexValue);
}
