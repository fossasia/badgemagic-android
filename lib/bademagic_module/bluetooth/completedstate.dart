import 'package:badgemagic/bademagic_module/bluetooth/ble_state_interface.dart';
import 'package:badgemagic/bademagic_module/bluetooth/bletoast.dart';

class CompletedState implements BleState {
  final bool isSuccess;
  final String message;
  BleStateToast toast = BleStateToast();

  CompletedState({required this.isSuccess, required this.message});

  @override
  Future<BleState?> processState() async {
    if (isSuccess) {
      toast.successToast(message);
    }
    toast.failureToast(message);
    return null;
  }
}
