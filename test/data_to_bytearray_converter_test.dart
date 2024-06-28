import 'package:badgemagic/bademagic_module/models/data.dart';
import 'package:badgemagic/bademagic_module/models/messages.dart';
import 'package:badgemagic/bademagic_module/models/mode.dart';
import 'package:badgemagic/bademagic_module/models/speed.dart';
import 'package:badgemagic/bademagic_module/utils/byte_array_utils.dart';
import 'package:badgemagic/bademagic_module/utils/converters.dart';
import 'package:badgemagic/bademagic_module/utils/data_to_bytearray_converter.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  DataToByteArrayConverter converter = DataToByteArrayConverter();
  test('result should start with 77616E670000', () {
    var data = Data(messages: [
      Message(text: ['A'])
    ]);

    List<List<int>> result = converter.convert(data);
    print(result);

    expect(result[0].sublist(0, 6), [0x77, 0x61, 0x6E, 0x67, 0x00, 0x00]);
  });

  test('flash should be 0x00 when no messages have flash option enabled', () {
    var data = Data(messages: [
      Message(text: ['A'])
    ]);

    var result = converter.convert(data);
    expect(result[0].sublist(6, 7), [0x00]);
  });

  test(
      "flash should contain 8 bits, each bit representing the flash value of each message, 1 when flash is enabled, 0 otherwise",
      () {
    var data = Data(messages: [
      Message(text: Converters.messageTohex('Hii'), flash: true),
      Message(text: Converters.messageTohex('Hii'), flash: true),
      Message(text: Converters.messageTohex('Hii'), flash: false),
      Message(text: Converters.messageTohex('Hii'), flash: false),
      Message(text: Converters.messageTohex('Hii'), flash: true),
      Message(text: Converters.messageTohex('Hii'), flash: false),
      Message(text: Converters.messageTohex('Hii'), flash: true),
      Message(text: Converters.messageTohex('Hii'), flash: false)
    ]);

    var result = converter.convert(data);

    expect(result[0].sublist(6, 7), [0x53]); //binary is 01010011
  });

  test('marquee should be 0x00 when no messages have marquee option enabled',
      () {
    var data = Data(messages: [
      Message(text: Converters.messageTohex('Hii'), marquee: false)
    ]);

    var result = converter.convert(data);

    expect(result[0].sublist(7, 8), [0x00]);
  });

  test(
      'marquee should contain 8 bits, each bit representing the marquee value of each message, 1 when marquee is enabled, 0 otherwise',
      () {
    var data = Data(messages: [
      Message(text: Converters.messageTohex('Hii'), marquee: true),
      Message(text: Converters.messageTohex('Hii'), marquee: true),
      Message(text: Converters.messageTohex('Hii'), marquee: false),
      Message(text: Converters.messageTohex('Hii'), marquee: false),
      Message(text: Converters.messageTohex('Hii'), marquee: true),
      Message(text: Converters.messageTohex('Hii'), marquee: false),
      Message(text: Converters.messageTohex('Hii'), marquee: true),
      Message(text: Converters.messageTohex('Hii'), marquee: false)
    ]);

    var result = converter.convert(data);

    expect(result[0].sublist(7, 8), [0x53]);
  });

  test(
      'option should be a single byte containing the speed and the mode, repeated for all 8 messages',
      () {
    Data data = Data(messages: [
      Message(
          text: Converters.messageTohex('Hii'),
          speed: Speed.one,
          mode: Mode.right),
      Message(
          text: Converters.messageTohex('Hii'),
          speed: Speed.two,
          mode: Mode.left),
      Message(
          text: Converters.messageTohex('Hii'),
          speed: Speed.three,
          mode: Mode.up),
      Message(
          text: Converters.messageTohex('Hii'),
          speed: Speed.four,
          mode: Mode.fixed),
      Message(
          text: Converters.messageTohex('Hii'),
          speed: Speed.six,
          mode: Mode.laser),
      Message(
          text: Converters.messageTohex('Hii'),
          speed: Speed.seven,
          mode: Mode.snowflake),
      Message(
          text: Converters.messageTohex('Hii'),
          speed: Speed.eight,
          mode: Mode.picture),
    ]);

    var result = converter.convert(data);

    expect(result[0].sublist(8, 16),
        [0x01, 0x10, 0x22, 0x34, 0x58, 0x65, 0x76, 0x00]);
  });

  test(
      'size should contain the 2 bytes hexadecimal value for each message, skipping invalid characters if any',
      () {
    Data data = Data(messages: [
      Message(text: Converters.messageTohex('A')),
      Message(text: Converters.messageTohex('...')),
      Message(text: Converters.messageTohex('abcdefghijklmnopqrstuvwxyz')),
      Message(text: Converters.messageTohex('_' * 500)),
      Message(text: Converters.messageTohex('É')),
      Message(text: Converters.messageTohex('ÇÇÇÇÇabc')),
      Message(text: Converters.messageTohex('')),
    ]);

    List<List<int>> result = converter.convert(data);

    expect(result[1].sublist(0, 16), [
      0x00,
      0x01,
      0x00,
      0x03,
      0x00,
      0x1A,
      0x01,
      0xF4,
      0x00,
      0x00,
      0x00,
      0x03,
      0x00,
      0x00,
      0x00,
      0x00
    ]);
  });

  test('the 6 next bytes after the size should all be equal to 0x00', () {
    var data = Data(messages: [Message(text: Converters.messageTohex('A'))]);

    var result = converter.convert(data);
    expect(result[2].sublist(0, 6), [0x00, 0x00, 0x00, 0x00, 0x00, 0x00]);
  });

  test('the 20 next bytes after the timestamp should all be equal to 0x00', () {
    var data = Data(messages: [
      Message(text: ['A'])
    ]);

    var result = converter.convert(data);

    expect(result[2].sublist(12, 16) + result[3].sublist(0, 16),
        List.filled(20, 0x00));
  });

  test(
      '`message should be located at the end and containing hex code for each character, skipping invalid characters',
      () {
    Data data = Data(messages: [
      Message(text: Converters.messageTohex('AB')),
      Message(text: Converters.messageTohex('ÈC')),
    ]);

    List<List<int>> result = converter.convert(data);

    expect([
      toHex(result[3].sublist(14, 16) + result[4] + result[5].sublist(0, 15))
    ], [
      "00386CC6C6FEC6C6C6C60000FC6666667C666666FC00007CC6C6C0C0C0C6C67C00"
    ]);
  });

  test('each packet should contain 16 bytes', () {
    // Given
    final data1 = Data(messages: [Message(text: Converters.messageTohex('A'))]);
    final data2 = Data(messages: [
      Message(text: Converters.messageTohex('B')),
      Message(text: Converters.messageTohex('BBB'))
    ]);
    final data3 = Data(messages: [
      Message(text: Converters.messageTohex('C')),
      Message(text: Converters.messageTohex('CCC')),
      Message(text: Converters.messageTohex('CCCCC')),
      Message(text: Converters.messageTohex('CCCCCCCC'))
    ]);

    // When
    final result1 = converter.convert(data1);
    final result2 = converter.convert(data2);
    final result3 = converter.convert(data3);

    // Then
    for (var packet in result1) {
      expect(packet.length, 16);
    }
    for (var packet in result2) {
      expect(packet.length, 16);
    }
    for (var packet in result3) {
      expect(packet.length, 16);
    }
  });
}
