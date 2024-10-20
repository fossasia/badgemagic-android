import 'package:badgemagic/bademagic_module/utils/byte_array_utils.dart';
import 'package:badgemagic/bademagic_module/utils/file_helper.dart';
import 'package:badgemagic/bademagic_module/utils/toast_utils.dart';
import 'package:badgemagic/constants.dart';
import 'package:badgemagic/providers/badgeview_provider.dart';
import 'package:badgemagic/view/widgets/common_scaffold_widget.dart';
import 'package:badgemagic/view/widgets/saved_badge_listview.dart';
import 'package:badgemagic/virtualbadge/view/saved_badge_view.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:get_it/get_it.dart';

class SaveBadgeScreen extends StatefulWidget {
  const SaveBadgeScreen({super.key});

  @override
  State<SaveBadgeScreen> createState() => _SaveBadgeScreenState();
}

class _SaveBadgeScreenState extends State<SaveBadgeScreen> {
  List<MapEntry<String, Map<String, dynamic>>> badgeData = [];
  DrawBadgeProvider drawBadgeProvider = GetIt.instance<DrawBadgeProvider>();
  ToastUtils toastUtils = ToastUtils();
  FileHelper fileHelper = FileHelper();

  @override
  void initState() {
    super.initState();
    loadSavedBadges();
    SystemChrome.setPreferredOrientations([
      DeviceOrientation.portraitUp,
      DeviceOrientation.portraitDown,
      DeviceOrientation.landscapeLeft,
      DeviceOrientation.landscapeRight,
    ]);
    // Set a new 2D array to store the badge data with all false
    drawBadgeProvider.setNewGrid(
        List.generate(11, (index) => List.generate(44, (index) => false)),
        true);
  }

  // Method to load saved badges and refresh the list
  Future<void> loadSavedBadges() async {
    var data = await fileHelper.getBadgeDataFiles();
    setState(() {
      badgeData = data;
    });
  }

  @override
  Widget build(BuildContext context) {
    return CommonScaffold(
      actions: [
        TextButton(
            onPressed: () {
              fileHelper.importBadgeData(context).then((value) {
                if (value) {
                  logger.d('value: $value');
                  toastUtils.showToast('Badge imported successfully');
                  loadSavedBadges();
                }
              });
            },
            child: const Text(
              'Import',
              style: TextStyle(color: Colors.white),
            ))
      ],
      body: badgeData.isEmpty
          ? Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Padding(
                    padding: EdgeInsets.only(left: 50.0.w),
                    child: SvgPicture.asset(
                      'assets/icons/empty_badge.svg',
                      height: 200.h,
                    ),
                  ),
                  SizedBox(
                    height: 20.h,
                  ),
                  Text(
                    'No saved badges !',
                    style: TextStyle(
                      color: Colors.black,
                      fontSize: 20.sp,
                    ),
                  ),
                  Text(
                    'Looks like there are no saved badges yet.',
                    style: TextStyle(
                      color: Colors.black,
                      fontSize: 14.sp,
                    ),
                  ),
                ],
              ),
            )
          : Column(
              children: [
                SavedBadgeView(),
                BadgeListView(
                  futureBadges: Future.value(badgeData),
                  refreshBadgesCallback: loadSavedBadges, // Pass the callback
                ),
              ],
            ),
      title: 'Badge Magic',
      key: const Key(drawBadgeScreen),
    );
  }
}
