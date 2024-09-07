import 'package:badgemagic/bademagic_module/utils/byte_array_utils.dart';
import 'package:cancellation_token/cancellation_token.dart';

class TokenGenerator{
  //make the class singleton
  static final TokenGenerator _instance = TokenGenerator._internal();
  factory TokenGenerator() => _instance;
  TokenGenerator._internal();

  CancellationToken token = CancellationToken();

  //getter for the token
  CancellationToken get getToken => token;

  void refreshToken(){
    token.cancel();
    logger.i("Token status: ${token.isCancelled}");
    token = CancellationToken();
  }
}