abstract class BleState {
  Future<BleState?> isSuccess(String message);

  Future<BleState?> isFailed(String message);

  Future<BleState?> processState();
}
