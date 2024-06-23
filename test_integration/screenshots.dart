import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';
import 'package:badgemagic/main.dart' as app;
import 'package:badgemagic/constants.dart';
import 'utils.dart';

void main() async {
  final binding = IntegrationTestWidgetsFlutterBinding.ensureInitialized();
  setUpAll(() {
    return Future(() async {
      WidgetsApp.debugAllowBannerOverride = false; // Hide the debug banner
      if (Platform.isAndroid) {
        await binding.convertFlutterSurfaceToImage();
      }
    });
  });

  group('E2E Group', () {
    testWidgets('Take Screenshots', (tester) async {
      app.main();

      final homeScreenTitle = find.byKey(const ValueKey(homeScreenTitleKey));

      await pumpUntilFound(tester, homeScreenTitle);
      await tester.pump(const Duration(seconds: 10));
      await binding.takeScreenshot('01');
    });
  });
}
