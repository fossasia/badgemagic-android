import 'package:badgemagic/providers/badge_message_provider.dart';
import 'package:badgemagic/view/homescreen.dart';
import 'package:badgemagic/view/splashscreen.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:badgemagic/providers/cardsprovider.dart';

void main() {
  runApp(MultiProvider(
    providers: [
      ChangeNotifierProvider(create: (context) => CardProvider()),
      ChangeNotifierProvider(create: (context) => BadgeMessageProvider()),
    ],
    child: const MyApp(),));
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
          debugShowCheckedModeBanner: false,
          theme: ThemeData(
            colorScheme: ColorScheme.fromSeed(seedColor: Colors.red),
            useMaterial3: true,
          ),
          initialRoute: '/',
          routes: {
            '/': (context) => const SpalshScreen(),
            '/homescreen': (context) => const HomeScreen(),
          },
        );
      }
  }
