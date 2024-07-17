import 'package:badgemagic/bademagic_module/models/data.dart';
import 'package:badgemagic/bademagic_module/models/messages.dart';
import 'package:badgemagic/bademagic_module/models/mode.dart';
import 'package:badgemagic/bademagic_module/models/speed.dart';
import 'package:badgemagic/bademagic_module/utils/byte_array_utils.dart';
import 'package:badgemagic/bademagic_module/utils/converters.dart';
import 'package:badgemagic/bademagic_module/utils/data_to_bytearray_converter.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  Converters converters = Converters();
  DataToByteArrayConverter converter = DataToByteArrayConverter();
  test('result should start with 77616E670000', () {
    var data = Data(messages: [
      Message(text: ['A'])
    ]);

    List<List<int>> result = converter.convert(data);

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
      () async {
    var data = Data(messages: [
      Message(text: await converters.messageTohex('Hii'), flash: true),
      Message(text: await converters.messageTohex('Hii'), flash: true),
      Message(text: await converters.messageTohex('Hii'), flash: false),
      Message(text: await converters.messageTohex('Hii'), flash: false),
      Message(text: await converters.messageTohex('Hii'), flash: true),
      Message(text: await converters.messageTohex('Hii'), flash: false),
      Message(text: await converters.messageTohex('Hii'), flash: true),
      Message(text: await converters.messageTohex('Hii'), flash: false)
    ]);

    var result = converter.convert(data);

    expect(result[0].sublist(6, 7), [0x53]); //binary is 01010011
  });

  test('marquee should be 0x00 when no messages have marquee option enabled',
      () async {
    var data = Data(messages: [
      Message(text: await converters.messageTohex('Hii'), marquee: false)
    ]);

    var result = converter.convert(data);

    expect(result[0].sublist(7, 8), [0x00]);
  });

  test(
      'marquee should contain 8 bits, each bit representing the marquee value of each message, 1 when marquee is enabled, 0 otherwise',
      () async {
    var data = Data(messages: [
      Message(text: await converters.messageTohex('Hii'), marquee: true),
      Message(text: await converters.messageTohex('Hii'), marquee: true),
      Message(text: await converters.messageTohex('Hii'), marquee: false),
      Message(text: await converters.messageTohex('Hii'), marquee: false),
      Message(text: await converters.messageTohex('Hii'), marquee: true),
      Message(text: await converters.messageTohex('Hii'), marquee: false),
      Message(text: await converters.messageTohex('Hii'), marquee: true),
      Message(text: await converters.messageTohex('Hii'), marquee: false)
    ]);

    var result = converter.convert(data);

    expect(result[0].sublist(7, 8), [0x53]);
  });

  test(
      'option should be a single byte containing the speed and the mode, repeated for all 8 messages',
      () async {
    Data data = Data(messages: [
      Message(
          text: await converters.messageTohex('Hii'),
          speed: Speed.one,
          mode: Mode.right),
      Message(
          text: await converters.messageTohex('Hii'),
          speed: Speed.two,
          mode: Mode.left),
      Message(
          text: await converters.messageTohex('Hii'),
          speed: Speed.three,
          mode: Mode.up),
      Message(
          text: await converters.messageTohex('Hii'),
          speed: Speed.four,
          mode: Mode.fixed),
      Message(
          text: await converters.messageTohex('Hii'),
          speed: Speed.six,
          mode: Mode.laser),
      Message(
          text: await converters.messageTohex('Hii'),
          speed: Speed.seven,
          mode: Mode.snowflake),
      Message(
          text: await converters.messageTohex('Hii'),
          speed: Speed.eight,
          mode: Mode.picture),
    ]);

    var result = converter.convert(data);

    expect(result[0].sublist(8, 16),
        [0x01, 0x10, 0x22, 0x34, 0x58, 0x65, 0x76, 0x00]);
  });

  test(
      'size should contain the 2 bytes hexadecimal value for each message, skipping invalid characters if any',
      () async {
    Data data = Data(messages: [
      Message(text: await converters.messageTohex('A')),
      Message(text: await converters.messageTohex('...')),
      Message(
          text: await converters.messageTohex('abcdefghijklmnopqrstuvwxyz')),
      Message(text: await converters.messageTohex('_' * 500)),
      Message(text: await converters.messageTohex('É')),
      Message(text: await converters.messageTohex('ÇÇÇÇÇabc')),
      Message(text: await converters.messageTohex('')),
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

  test('the 6 next bytes after the size should all be equal to 0x00', () async {
    var data =
        Data(messages: [Message(text: await converters.messageTohex('A'))]);

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
      () async {
    Data data = Data(messages: [
      Message(text: await converters.messageTohex('AB')),
      Message(text: await converters.messageTohex('ÈC')),
    ]);

    List<List<int>> result = converter.convert(data);

    expect([
      toHex(result[3].sublist(14, 16) + result[4] + result[5].sublist(0, 15))
    ], [
      "00386CC6C6FEC6C6C6C60000FC6666667C666666FC00007CC6C6C0C0C0C6C67C00"
    ]);
  });

  test('each packet should contain 16 bytes', () async {
    // Given
    final data1 =
        Data(messages: [Message(text: await converters.messageTohex('A'))]);
    final data2 = Data(messages: [
      Message(text: await converters.messageTohex('B')),
      Message(text: await converters.messageTohex('BBB'))
    ]);
    final data3 = Data(messages: [
      Message(text: await converters.messageTohex('C')),
      Message(text: await converters.messageTohex('CCC')),
      Message(text: await converters.messageTohex('CCCCC')),
      Message(text: await converters.messageTohex('CCCCCCCC'))
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
