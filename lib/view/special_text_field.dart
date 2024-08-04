import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:badgemagic/providers/imageprovider.dart';
import 'package:extended_text_field/extended_text_field.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get_it/get_it.dart';

class InlineImageText extends SpecialText {
  InlineImageProvider textData = GetIt.instance.get<InlineImageProvider>();
  InlineImageText(TextStyle? textStyle, {this.start})
      : super(InlineImageText.flag, '>>', textStyle);
  static const String flag = '<<';
  final int? start;
  CardProvider cardData = GetIt.instance.get<CardProvider>();
  @override
  InlineSpan finishText() {
    final String key = toString();

    // Parse the index from the placeholder text
    final int index = int.parse(key.substring(2, key.length - 2));
    Object vectorIndex = index;
    var keyAt = textData.imageCache.keys.toList()[index];
    if (keyAt is List) {
      vectorIndex = keyAt;
    }

    return ImageSpan(MemoryImage(textData.imageCache[vectorIndex]!),
        imageWidth: 25.w,
        imageHeight: 20.h,
        actualText: key,
        start: start!,
        fit: BoxFit.contain);
  }
}

class MySpecialTextSpanBuilder extends SpecialTextSpanBuilder {
  int pos = 0;
  @override
  SpecialText? createSpecialText(String flag,
      {TextStyle? textStyle,
      SpecialTextGestureTapCallback? onTap,
      int? index,
      int? start}) {
    if (flag.indexOf('<<', pos) != -1) {
      return InlineImageText(
        textStyle,
        start: pos,
      );
    }
    return null;
  }
}
