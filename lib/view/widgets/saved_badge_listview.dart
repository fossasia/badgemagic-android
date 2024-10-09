import 'package:badgemagic/bademagic_module/models/data.dart';
import 'package:badgemagic/view/widgets/save_badge_card.dart';
import 'package:flutter/material.dart';

class BadgeListView extends StatelessWidget {
  final Future<List<MapEntry<String, Map<String, dynamic>>>> futureBadges;

  const BadgeListView({super.key, required this.futureBadges});

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<MapEntry<String, Map<String, dynamic>>>>(
      future: futureBadges, // Pass the future
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Center(child: CircularProgressIndicator());
        } else if (snapshot.hasError) {
          return const Center(child: Text('Error loading badges'));
        } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
          return const Center(child: Text('No badges available'));
        } else {
          List<MapEntry<String, Map<String, dynamic>>> savedBadges =
              snapshot.data!;
          return Expanded(
            child: ListView.builder(
              itemCount: savedBadges.length,
              itemBuilder: (context, index) {
                return SaveBadgeCard(
                  badgeData: savedBadges[index], // Pass Data object to card
                );
              },
            ),
          );
        }
      },
    );
  }
}
