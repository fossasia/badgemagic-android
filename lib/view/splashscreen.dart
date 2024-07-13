import 'dart:async';
import 'package:badgemagic/providers/imageprovider.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';

class SpalshScreen extends StatefulWidget {
  const SpalshScreen({super.key});

  @override
  State<SpalshScreen> createState() => _SpalshScreenState();
}

class _SpalshScreenState extends State<SpalshScreen> {
  InlineImageProvider cacheImageProvider =
      GetIt.instance<InlineImageProvider>();
  @override
  void initState() {
    super.initState();
    _startImageCaching();
  }

  Future<void> _startImageCaching() async {
    await cacheImageProvider.generateImageCache();
    if (mounted) {
      Navigator.of(context).pushReplacementNamed('/homescreen');
    }
  }

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Center(
        child: Image(
          image: AssetImage('assets/icons/splash.png'),
        ),
      ),
    );
  }
}
