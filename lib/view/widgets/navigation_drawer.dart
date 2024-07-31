import 'package:flutter/material.dart';

class BMDrawer extends StatelessWidget {
  const BMDrawer({super.key});

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: <Widget>[
          const DrawerHeader(
            decoration: BoxDecoration(
              color: Colors.red,
            ),
            child: Center(
              child: Text(
                'Badge Magic',
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 24,
                ),
              ),
            ),
          ),
          ListTile(
            leading: const Icon(Icons.edit),
            title: const Text('Create Badges'),
            onTap: () {
              Navigator.pushNamed(context, '/');
            },
          ),
          ListTile(
            leading: const Icon(Icons.draw_outlined),
            title: const Text('Draw Clipart'),
            onTap: () {
              Navigator.pushNamed(context, '/drawBadge');
            },
          ),
        ],
      ),
    );
  }
}
