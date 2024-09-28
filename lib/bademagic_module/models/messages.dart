import 'package:badgemagic/bademagic_module/models/mode.dart';
import 'package:badgemagic/bademagic_module/models/speed.dart';

class Message {
  final List<String> text;
  final bool flash;
  final bool marquee;
  final Speed speed;
  final Mode mode;

  Message({
    required this.text,
    this.flash = false,
    this.marquee = false,
    this.speed = Speed.one,
    this.mode = Mode.left,
  });

   // Convert Message object to JSON
  Map<String, dynamic> toJson() => {
        'text': text,
        'flash': flash,
        'marquee': marquee,
        'speed': speed.hexValue, // Use hexValue for serialization
        'mode': mode.hexValue,   // Use hexValue for serialization
      };

   // Convert JSON to Message object
  factory Message.fromJson(Map<String, dynamic> json) {
    return Message(
      text: List<String>.from(json['text']),
      flash: json['flash'],
      marquee: json['marquee'],
      speed: Speed.values.firstWhere((speed) => speed.hexValue == json['speed']),
      mode: Mode.values.firstWhere((mode) => mode.hexValue == json['mode']),
    );
  }
}
