import 'package:badgemagic/bademagic_module/bluetooth/writestate.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:logger/logger.dart';
import 'ble_state_interface.dart';
import 'bletoast.dart';

class ConnectState implements BleState {
  final ScanResult scanResult;
  final Logger logger = Logger();
  final int maxRetries = 3;
  BleStateToast toast = BleStateToast();

  ConnectState({required this.scanResult});

  @override
  Future<BleState?> isFailed(String message) async {
    toast.failureToast(message);
    return null;
  }

  @override
  Future<BleState?> isSuccess(String message) async {
    toast.successToast(message);
    return WriteState(device: scanResult.device).processState();
  }

  @override
  Future<BleState?> processState() async {
    int attempt = 0;
    bool connected = false;

    while (attempt < maxRetries && !connected) {
      try {
        await scanResult.device.connect(autoConnect: false);
        BluetoothConnectionState connectionState =
            await scanResult.device.connectionState.first;

        if (connectionState == BluetoothConnectionState.connected) {
          logger.d("Device connected");
          connected = true;
          return isSuccess('Device connected successfully.');
        } else {
          logger.e("Failed to connect to the device");
        }
      } catch (e) {
        logger.e("Connection error: $e");
        attempt++;
        if (attempt < maxRetries) {
          logger.d("Retrying connection ($attempt/$maxRetries)...");
          await Future.delayed(
              const Duration(seconds: 2)); // Wait before retrying
        } else {
          logger.e("Max retries reached. Connection failed.");
          return isFailed('Failed to connect retry');
        }
      } finally {
        if (!connected) {
          await scanResult.device.disconnect();
        }
      }
    }
    return null;
  }
}
