import 'package:badgemagic/view/homescreen.dart';
import 'package:badgemagic/view/splashscreen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:provider/provider.dart';
import 'package:badgemagic/providers/cardsprovider.dart';

void main() {
  runApp(ChangeNotifierProvider(
    create: (context) => CardProvider(),
    child: const MyApp(),));
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return ScreenUtilInit(
      designSize: const Size(430, 932),
      builder: (context, child) {
        return MaterialApp(
          debugShowCheckedModeBanner: false,
          title: 'Flutter Demo',
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
      },
    );
  }
}
