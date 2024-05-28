import 'dart:async';

import 'package:flutter/material.dart';

class SpalshScreen extends StatefulWidget {
  const SpalshScreen({super.key});

  @override
  State<SpalshScreen> createState() => _SpalshScreenState();
}

class _SpalshScreenState extends State<SpalshScreen> {

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
  }
  @override
  Widget build(BuildContext context) {
    Timer(Duration(seconds: 3),
                () =>
            Navigator.of(context).pushReplacementNamed('/homescreen'));
    return Scaffold(
      body: Center(
        child: Image(image: AssetImage('assets/icons/splash.png'),),
      ),
    );
  }
}