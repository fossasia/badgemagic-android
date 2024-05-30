import 'package:badgemagic/bademagic_module/models/data.dart';
import 'package:badgemagic/bademagic_module/utils/DataToByteArrayConverter.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';

List<BluetoothDevice> devices = [];
BluetoothDevice targetdevice =
    BluetoothDevice.fromId("0000fee1-0000-1000-8000-00805f9b34fb");

void startScanning() async {
  await FlutterBluePlus.startScan();
  FlutterBluePlus.scanResults.listen((results) {
    for (ScanResult result in results) {
      if (!devices.contains(result.device)) {
        devices.add(result.device);
      }
    }
  });
}

void writeCharacteristic(
  BluetoothDevice device, Guid characteristicId, Data data,BluetoothDevice bledevice) async {
  List<List<int>> ans = convert(data);

  print("ans ${ans.toString()}");
  List<BluetoothService> services = await device.discoverServices();
  for (BluetoothService service in services) {
    // Reads all characteristics
    var characteristics = service.characteristics;
    for (BluetoothCharacteristic c in characteristics) {
      if (c.properties.write) {
        for (int x = 0; x < ans.length; x++) {
          await c.write(ans[x],
              withoutResponse: false, timeout: 70, allowLongWrite: true);
        }
        print("Characteristic is written");
      }
    }
  }
}

Future scanDevice(Data data) async {

  ScanResult? r ;

  var subscription =  await FlutterBluePlus.onScanResults.listen(
    (results) async {

      
      if (results.isNotEmpty) {

         r = results.last;
      
       await r!.device.connect(autoConnect: false, mtu: null);
        await r!.device.connectionState
            .where((val) => val == BluetoothConnectionState.connected)
            .first;
        if (r!.device.isConnected) {
          print("device is connected");
          writeCharacteristic(
              r!.device, Guid("0000fee1-0000-1000-8000-00805f9b34fb"), data,r!.device);
        } else {
          print("Device is not connected");
        }
        // the most recently found device
        print('${r!.device.remoteId}: "${r!.advertisementData.advName}" found!');
        
      }
    },
    onError: (e) => print(e),
  );


  // r!.device.cancelWhenDisconnected(subscription);

  await FlutterBluePlus.startScan(
    withServices: [Guid("0000fee0-0000-1000-8000-00805f9b34fb")],
    timeout: Duration(seconds: 10),
  );
  


}
