enum Mode {
  LEFT('0x00'),
  RIGHT('0x01'),
  UP('0x02'),
  DOWN('0x03'),
  FIXED('0x04'),
  SNOWFLAKE('0x05'),
  PICTURE('0x06'),
  ANIMATION('0x07'),
  LASER('0x08');
  
  
  final String hexValue;
  const Mode(this.hexValue);
}
