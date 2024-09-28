import 'messages.dart';

class Data {
  final List<Message> messages;
  Data({required this.messages});

    // Convert Data object to JSON
  Map<String, dynamic> toJson() => {
        'messages': messages.map((message) => message.toJson()).toList(),
      };

  // Convert JSON to Data object
  factory Data.fromJson(Map<String, dynamic> json) {
    var messagesFromJson = json['messages'] as List;
    List<Message> messageList =
        messagesFromJson.map((message) => Message.fromJson(message)).toList();
    return Data(messages: messageList);
  }
}
