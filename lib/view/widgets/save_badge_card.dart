import 'package:badgemagic/bademagic_module/utils/converters.dart';
import 'package:badgemagic/bademagic_module/utils/file_helper.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class SaveBadgeCard extends StatelessWidget {
  final MapEntry<String, Map<String, dynamic>> badgeData;
  FileHelper file = FileHelper();
  Converters converters = Converters();
  SaveBadgeCard({
    super.key,
    required this.badgeData,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 370.w,
      padding: EdgeInsets.all(6.dg),
      margin: EdgeInsets.all(10.dg),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(6.dg),
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.5),
            spreadRadius: 2,
            blurRadius: 5,
            offset: const Offset(0, 3),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // Wrapping the text with Flexible to ensure it doesn't overflow.
              Flexible(
                child: Padding(
                  padding: EdgeInsets.only(
                      right: 8
                          .w), // Adding some padding to separate text and buttons
                  child: Text(
                    badgeData.key.substring(0, badgeData.key.length - 5),
                    style: const TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 16,
                    ),
                    softWrap: true,
                    overflow: TextOverflow
                        .ellipsis, // Use ellipsis to indicate overflowed text
                    maxLines: 1, // Limit to 1 line for a cleaner look
                  ),
                ),
              ),
              Row(
                mainAxisSize: MainAxisSize.min, // Keep the row compact
                children: [
                  IconButton(
                    icon: Image.asset(
                      "assets/icons/t_play.png",
                      height: 20,
                      color: Colors.black,
                    ),
                    onPressed: () {
                      converters
                          .savedBadgeAnimation(file.jsonToData(badgeData));
                    },
                  ),
                  IconButton(
                    icon: const Icon(
                      Icons.edit,
                      color: Colors.black,
                    ),
                    onPressed: () {},
                  ),
                  IconButton(
                    icon: Image.asset(
                      "assets/icons/t_updown.png",
                      height: 24.h,
                      color: Colors.black,
                    ),
                    onPressed: () {},
                  ),
                  IconButton(
                    icon: const Icon(
                      Icons.share,
                      color: Colors.black,
                    ),
                    onPressed: () {},
                  ),
                  IconButton(
                    icon: const Icon(
                      Icons.delete,
                      color: Colors.black,
                    ),
                    onPressed: () {},
                  ),
                ],
              ),
            ],
          ),
          SizedBox(height: 8.h),
          Row(
            children: [
              GestureDetector(
                  onTap: () {},
                  child: Row(
                    children: [
                      Visibility(
                        visible: file.jsonToData(badgeData).messages[0].flash,
                        child: Container(
                          padding: EdgeInsets.symmetric(
                              horizontal: 10.w, vertical: 4.h),
                          decoration: BoxDecoration(
                            color: Colors.red,
                            borderRadius: BorderRadius.circular(100),
                          ),
                          child: Row(
                            children: [
                              Image.asset(
                                "assets/icons/t_invert.png",
                                color: Colors.white,
                                height: 14.h,
                              )
                            ],
                          ),
                        ),
                      ),
                      SizedBox(
                        width: 8.w,
                      ),
                      Visibility(
                        visible: file.jsonToData(badgeData).messages[0].marquee,
                        child: Container(
                          padding: EdgeInsets.symmetric(
                              horizontal: 12.w, vertical: 4.h),
                          decoration: BoxDecoration(
                            color: Colors.red,
                            borderRadius: BorderRadius.circular(100),
                          ),
                          child: Row(
                            children: [
                              Image.asset(
                                "assets/icons/t_invert.png",
                                color: Colors.white,
                                height: 14.h,
                              )
                            ],
                          ),
                        ),
                      )
                    ],
                  )),
              SizedBox(width: 8.w),
              GestureDetector(
                onTap: () {},
                child: Container(
                  padding:
                      EdgeInsets.symmetric(horizontal: 12.w, vertical: 4.h),
                  decoration: BoxDecoration(
                    color: Colors.red,
                    borderRadius: BorderRadius.circular(100),
                  ),
                  child: Row(
                    children: [
                      Image.asset(
                        "assets/icons/t_double.png",
                        color: Colors.white,
                        height: 14.h,
                      ),
                      const SizedBox(width: 4),
                      Text(
                        file
                            .jsonToData(badgeData)
                            .messages[0]
                            .speed
                            .hexValue
                            .substring(2, 3),
                        style: const TextStyle(color: Colors.white),
                      ),
                    ],
                  ),
                ),
              ),
              SizedBox(width: 8.w),
              GestureDetector(
                onTap: () {},
                child: Container(
                  padding:
                      EdgeInsets.symmetric(horizontal: 12.w, vertical: 4.h),
                  decoration: BoxDecoration(
                    color: Colors.red,
                    borderRadius: BorderRadius.circular(100),
                  ),
                  child: Text(
                    file
                        .jsonToData(badgeData)
                        .messages[0]
                        .mode
                        .toString()
                        .split('.')
                        .last
                        .toUpperCase(),
                    style: const TextStyle(color: Colors.white),
                  ),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
