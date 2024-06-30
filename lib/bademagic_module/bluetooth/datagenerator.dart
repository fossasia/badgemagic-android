import 'package:badgemagic/bademagic_module/models/data.dart';
import 'package:badgemagic/bademagic_module/utils/data_to_bytearray_converter.dart';
import 'package:badgemagic/providers/badge_message_provider.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:get_it/get_it.dart';

class DataTransferManager {
  final BadgeMessageProvider badgeData = BadgeMessageProvider();
  DataToByteArrayConverter converter = DataToByteArrayConverter();
  CardProvider cardData = GetIt.instance<CardProvider>();

  List<List<int>> generateDataChunk() {
    Data data = badgeData.generateData(
      cardData.getController().text,
      cardData.getEffectIndex(1) == 1,
      cardData.getEffectIndex(2) == 1,
      badgeData.speedMap[cardData.getOuterValue()]!,
      badgeData.modeValueMap[cardData.getAnimationIndex()]!,
    );
    return converter.convert(data);
  }
}
