import 'package:badgemagic/providers/badge_message_provider.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class SaveBadgeDialog extends StatelessWidget {
  const SaveBadgeDialog({
    super.key,
    required this.textController,
  });

  final TextEditingController textController;

  @override
  Widget build(BuildContext context) {
    BadgeMessageProvider badgeMessageProvider = BadgeMessageProvider();
    return Dialog(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(5.r),
      ),
      child: Container(
        height: 150.h, // Increase height for TextField space
        width: 300.w, // Increased width
        padding: const EdgeInsets.all(10), // Added padding for better layout
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            const Text(
              'Save Badge',
              style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 18,
              ),
            ),
            const SizedBox(
                height: 10), // Space between title and file name text
            const Text(
              'File Name',
              style: TextStyle(
                fontWeight: FontWeight.w400,
                color: Colors.red,
              ),
            ),
            const SizedBox(
                height: 10), // Space between file name and text field
            TextField(
              controller: textController,
              autofocus: true,
              onTap: () {
                // Select all text when the TextField is tapped
                textController.selection = TextSelection(
                  baseOffset: 0,
                  extentOffset: textController.text.length,
                );
              },
              decoration: const InputDecoration(
                enabledBorder: UnderlineInputBorder(
                  borderSide: BorderSide(color: Colors.red),
                ),
                focusedBorder: UnderlineInputBorder(
                  borderSide: BorderSide(
                      color: Colors.red,
                      width: 2), // Thicker border when focused
                ),
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                TextButton(
                    onPressed: () {
                      Navigator.pop(context);
                    },
                    child: const Text(
                      'Cancel',
                      style: TextStyle(color: Colors.red),
                    )),
                TextButton(
                    onPressed: () {
                      badgeMessageProvider.saveBadgeData(textController.text);
                    },
                    child: const Text(
                      'Save',
                      style: TextStyle(color: Colors.red),
                    )),
              ],
            )
          ],
        ),
      ),
    );
  }
}
