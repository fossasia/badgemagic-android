import 'dart:async';
import 'package:badgemagic/bademagic_module/bluetooth/connectstate.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:logger/logger.dart';
import 'ble_state_interface.dart';
import 'toast.dart';

class ScanState implements BleState {
  ScanResult? foundDevice;
  final Logger logger = Logger();
  BleStateToast toast = BleStateToast();

  @override
  Future<BleState?> isFailed(String message) async {
    toast.failureToast(message);
    return null;
  }

  @override
  Future<BleState?> isSuccess(String message) async {
    toast.successToast(message);
    return ConnectState(scanResult: foundDevice!).processState();
  }

  @override
  Future<BleState?> processState() async {
    StreamSubscription<List<ScanResult>>? subscription;

    try {
      subscription = FlutterBluePlus.scanResults.listen(
        (results) async {
          if (results.isNotEmpty) {
            foundDevice = results.firstWhere(
              (result) => result.advertisementData.serviceUuids
                  .contains(Guid("0000fee0-0000-1000-8000-00805f9b34fb")),
            );
            if (foundDevice != null) {
              await isSuccess('Device found. Connecting...');
            } else {
              await isFailed('Target device not found.');
              logger.e("Target device not found.");
            }
          }
        },
        onError: (e) async {
          logger.e("Scan error: $e");
          await isFailed("Scan error");
        },
      );

      await FlutterBluePlus.startScan(
        withServices: [Guid("0000fee0-0000-1000-8000-00805f9b34fb")],
        timeout: const Duration(seconds: 10),
      );

      await Future.delayed(const Duration(seconds: 11));
    } finally {
      await subscription?.cancel();
    }
    return null;
  }
}
