import 'package:badgemagic/view/widgets/navigation_drawer.dart';
import 'package:flutter/material.dart';

class CommonScaffold extends StatelessWidget {
  final String title;
  final Widget body;
  final Key? scaffoldKey;

  const CommonScaffold(
      {super.key, required this.body, required this.title, this.scaffoldKey});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: Builder(builder: (context) {
          return IconButton(
            onPressed: () {
              Scaffold.of(context).openDrawer();
            },
            icon: const Icon(
              Icons.menu,
              color: Colors.white,
            ),
          );
        }),
        backgroundColor: Colors.red,
        title: Text(
          key: scaffoldKey,
          title,
          style: const TextStyle(color: Colors.white),
        ),
      ),
      drawer: const BMDrawer(),
      body: body,
    );
  }
}
