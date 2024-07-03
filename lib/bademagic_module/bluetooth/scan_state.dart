import 'dart:async';
import 'package:badgemagic/bademagic_module/bluetooth/connect_state.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'base_ble_state.dart';

class ScanState extends NormalBleState {
  @override
  Future<BleState?> processState() async {
    StreamSubscription<List<ScanResult>>? subscription;
    toast.showToast("Searching for device...");

    Completer<BleState?> nextStateCompleter = Completer();

    ScanResult? foundDevice;

    try {
      subscription = FlutterBluePlus.scanResults.listen(
        (results) async {
          if (results.isNotEmpty) {
            foundDevice = results.firstWhere(
              (result) => result.advertisementData.serviceUuids
                  .contains(Guid("0000fee0-0000-1000-8000-00805f9b34fb")),
            );
            if (foundDevice != null) {
              toast.showToast('Device found. Connecting...');
              nextStateCompleter
                  .complete(ConnectState(scanResult: foundDevice!));
            } else {
              nextStateCompleter
                  .completeError(Exception('BLE LED Device not found.'));
            }
          } else {
            nextStateCompleter
                .completeError(Exception('No BLE Devices not found.'));
          }
        },
        onError: (e) async {
          logger.e("Scan error: $e");
          toast.showErrorToast('Scan error occurred.');
          nextStateCompleter.completeError(e);
        },
      );

      await FlutterBluePlus.startScan(
        withServices: [Guid("0000fee0-0000-1000-8000-00805f9b34fb")],
        timeout: const Duration(seconds: 5), // Reduced scan timeout
      );

      await Future.delayed(const Duration(seconds: 6));

      return await nextStateCompleter.future;
    } catch (e) {
      logger.e("Exception during scanning: $e");
      throw Exception("Exception during scanning: $e");
    } finally {
      await subscription?.cancel();
    }
  }
}
