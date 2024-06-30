import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';

class BleStateToast {
  CardProvider contextProvider = GetIt.instance<CardProvider>();

  //create a toast message for success state of BLE
  void successToast(String message) {
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
            Text(
              message,
              style: const TextStyle(color: Colors.black),
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

  //show toast for failure state of BLE
  void failureToast(String message) {
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
            Text(
              message,
              style: const TextStyle(color: Colors.black),
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
}
