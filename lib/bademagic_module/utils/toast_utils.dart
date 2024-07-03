import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:get_it/get_it.dart';

class ToastUtils {
  CardProvider contextProvider = GetIt.instance<CardProvider>();

  // Create a toast message
  void showToast(String message) {
    ScaffoldMessenger.of(contextProvider.getContext()!).showSnackBar(
      SnackBar(
        margin: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
        elevation: 10,
        duration: const Duration(seconds: 1),
        content: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Image(
              image: AssetImage('assets/icons/icon.png'),
              height: 20,
            ),
            const SizedBox(
              width: 10,
            ),
            Flexible(
              child: Text(
                message,
                style: const TextStyle(color: Colors.black),
              ),
            )
          ],
        ),
        backgroundColor: Colors.white,
        behavior: SnackBarBehavior.floating,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10),
        ),
        dismissDirection: DismissDirection.startToEnd,
      ),
    );
  }

  // Create a error toast
  void showErrorToast(String message) {
    showToast('Error: $message');
  }
}
