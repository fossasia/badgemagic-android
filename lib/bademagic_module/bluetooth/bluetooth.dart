import 'dart:async';
import 'package:badgemagic/bademagic_module/models/data.dart';
import 'package:badgemagic/bademagic_module/utils/DataToByteArrayConverter.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';

Future<void> writeCharacteristic(
  BluetoothDevice device,
  Guid characteristicId,
  Data data,
) async {
  List<List<int>> dataChunks = convert(data);
  print("Data to write: $dataChunks");

  try {
    List<BluetoothService> services = await device.discoverServices();
    for (BluetoothService service in services) {
      for (BluetoothCharacteristic characteristic in service.characteristics) {
        if (characteristic.uuid == characteristicId && characteristic.properties.write) {
          for (int attempt = 1; attempt <= 3; attempt++) {
            for (List<int> chunk in dataChunks) {
              bool success = false;
              try {
                await characteristic.write(chunk, withoutResponse: false);
                await Future.delayed(Duration(milliseconds: 100)); // Add a delay between writes
                success = true;
              } catch (e) {
                print("Write failed, retrying ($attempt/3): $e");
              }
              if (!success) {
                throw Exception("Failed to write chunk after 3 attempts: $chunk");
              }
            }
          }
          print("Characteristic written successfully");
          return; // Exit once the target characteristic is written
        }
      }
    }
    print("Target characteristic not found");
  } catch (e) {
    print("Failed to write characteristic: $e");
  }
}

Future<void> scanAndConnect(Data data) async {
  ScanResult? foundDevice;

  StreamSubscription<List<ScanResult>>? subscription;

  try {
    subscription = FlutterBluePlus.scanResults.listen(
      (results) async {
        if (results.isNotEmpty) {
          foundDevice = results.firstWhere(
            (result) => result.device.remoteId.toString() == "50:54:7B:63:10:F5",
          );
          if (foundDevice != null) {
            await connectToDevice(foundDevice!, data);
          } else {
            print("Target device not found.");
          }
        }
      },
      onError: (e) {
        print("Scan error: $e");
      },
    );

    await FlutterBluePlus.startScan(
      withServices: [Guid("0000fee0-0000-1000-8000-00805f9b34fb")],
      timeout: Duration(seconds: 10),
    );

    // Wait for the scan to complete before cancelling the subscription
    await Future.delayed(Duration(seconds: 11));
  } finally {
    await subscription?.cancel();
  }
}

Future<void> connectToDevice(ScanResult scanResult, Data data) async {
  const int maxRetries = 3;
  int attempt = 0;
  bool connected = false;

  while (attempt < maxRetries && !connected) {
    try {
      await scanResult.device.connect(autoConnect: false);
      BluetoothConnectionState connectionState = await scanResult.device.connectionState.first;

      if (connectionState == BluetoothConnectionState.connected) {
        print("Device connected");
        await writeCharacteristic(
          scanResult.device,
          Guid("0000fee1-0000-1000-8000-00805f9b34fb"),
          data,
        );
        connected = true;
      } else {
        print("Failed to connect to the device");
      }
    } catch (e) {
      print("Connection error: $e");
      attempt++;
      if (attempt < maxRetries) {
        print("Retrying connection ($attempt/$maxRetries)...");
        await Future.delayed(Duration(seconds: 2)); // Wait before retrying
      } else {
        print("Max retries reached. Connection failed.");
      }
    } finally {
      if (!connected) {
        await scanResult.device.disconnect();
      }
    }
  }
}