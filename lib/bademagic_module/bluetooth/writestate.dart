import 'package:badgemagic/bademagic_module/bluetooth/datagenerator.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:get_it/get_it.dart';
import 'package:logger/logger.dart';
import 'ble_state_interface.dart';
import 'bletoast.dart';
import 'completedstate.dart';

class WriteState implements BleState {
  final BluetoothDevice device;
  final Logger logger = Logger();

  GetIt getIt = GetIt.instance;
  final CardProvider cardData = GetIt.instance<CardProvider>();
  DataTransferManager manager = DataTransferManager();
  BleStateToast toast = BleStateToast();

  WriteState({required this.device});

  @override
  Future<BleState?> processState() async {
    List<List<int>> dataChunks = manager.generateDataChunk();
    logger.d("Data to write: $dataChunks");

    try {
      List<BluetoothService> services = await device.discoverServices();
      for (BluetoothService service in services) {
        for (BluetoothCharacteristic characteristic
            in service.characteristics) {
          if (characteristic.uuid ==
                  Guid("0000fee1-0000-1000-8000-00805f9b34fb") &&
              characteristic.properties.write) {
            for (List<int> chunk in dataChunks) {
              bool success = false;
              for (int attempt = 1; attempt <= 3; attempt++) {
                try {
                  await characteristic.write(chunk, withoutResponse: false);
                  success = true;
                  break;
                } catch (e) {
                  logger.e("Write failed, retrying ($attempt/3): $e");
                }
              }
              if (!success) {
                throw Exception(
                    "Failed to write chunk after 3 attempts: $chunk");
              }
            }
            logger.d("Characteristic written successfully");
            return CompletedState(
                isSuccess: true, message: "Data transferred successfully");
          }
        }
      }
      return CompletedState(
          isSuccess: false, message: "Please use the correct Badge");
    } catch (e) {
      logger.e("Failed to write characteristic: $e");
    }
    return null;
  }
}
