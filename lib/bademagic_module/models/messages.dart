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
    this.speed = Speed.one, // Default speed
    this.mode = Mode.left, // Default mode
  });

  // Convert Message object to JSON
  Map<String, dynamic> toJson() => {
        'text': text,
        'flash': flash,
        'marquee': marquee,
        'speed': speed.hexValue, // Use hexValue for serialization
        'mode': mode.hexValue, // Use hexValue for serialization
      };

  // Convert JSON to Message object
  factory Message.fromJson(Map<String, dynamic> json) {
    return Message(
      text: List<String>.from(json['text']),
      flash: json['flash'] as bool,
      marquee: json['marquee'] as bool,
      speed: Speed.fromHex(
          json['speed'] as String), // Using helper method for safety
      mode: Mode.fromHex(
          json['mode'] as String), // Using helper method for safety
    );
  }
}
