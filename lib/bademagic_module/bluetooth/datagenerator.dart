import 'package:badgemagic/bademagic_module/models/data.dart';
import 'package:badgemagic/bademagic_module/utils/data_to_bytearray_converter.dart';
import 'package:badgemagic/bademagic_module/utils/file_helper.dart';
import 'package:badgemagic/providers/badge_message_provider.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:badgemagic/providers/imageprovider.dart';
import 'package:get_it/get_it.dart';

class DataTransferManager {
  final BadgeMessageProvider badgeData = BadgeMessageProvider();
  DataToByteArrayConverter converter = DataToByteArrayConverter();
  CardProvider cardData = GetIt.instance<CardProvider>();
  FileHelper fileHelper = FileHelper();
  InlineImageProvider controllerData = GetIt.instance<InlineImageProvider>();

  Future<List<List<int>>> generateDataChunk() async {
    if (!cardData.getIsSavedBadgeData()) {
      Data data = await badgeData.generateData(
        controllerData.getController().text,
        cardData.getEffectIndex(1) == 1,
        cardData.getEffectIndex(2) == 1,
        badgeData.speedMap[cardData.getOuterValue()]!,
        badgeData.modeValueMap[cardData.getAnimationIndex()]!,
      );
      return converter.convert(data);
    } else {
      Map<String, dynamic> jsonData = cardData.getSavedBadgeDataMap();
      Data data = fileHelper.jsonToData(jsonData);
      return converter.convert(data);
    }
  }
}
