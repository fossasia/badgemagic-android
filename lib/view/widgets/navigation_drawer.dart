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
            title: const Text(
              'Create Badges',
              style:
                  TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
            ),
            onTap: () {
              Navigator.pushNamed(context, '/');
            },
          ),
          ListTile(
            leading: Image.asset(
              "assets/icons/r_signature.png",
              height: 24,
              color: Colors.black,
            ),
            title: const Text(
              'Draw Clipart',
              style:
                  TextStyle(fontWeight: FontWeight.bold, color: Colors.black),
            ),
            onTap: () {
              Navigator.pushNamed(context, '/drawBadge');
            },
          ),
          ListTile(
            leading: Image.asset(
              "assets/icons/r_save.png",
              height: 24,
              color: Colors.black,
            ),
            title: const Text(
              'Saved Badges',
              style: TextStyle(
                color: Colors.black,
                fontWeight: FontWeight.bold,
              ),
            ),
            onTap: () {
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Image.asset(
              "assets/icons/r_save.png",
              height: 24,
              color: Colors.black,
            ),
            title: const Text(
              'Saved Cliparts',
              style: TextStyle(
                color: Colors.black,
                fontWeight: FontWeight.bold,
              ),
            ),
            onTap: () {
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Image.asset(
              "assets/icons/r_setting.png",
              height: 24,
              color: Colors.black,
            ),
            title: const Text(
              'Settings',
              style: TextStyle(
                color: Colors.black,
                fontWeight: FontWeight.bold,
              ),
            ),
            onTap: () {
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Image.asset(
              "assets/icons/r_team.png",
              height: 24,
              color: Colors.black,
            ),
            title: const Text(
              'About Us',
              style: TextStyle(
                color: Colors.black,
                fontWeight: FontWeight.bold,
              ),
            ),
            onTap: () {
              Navigator.pop(context);
            },
          ),
          const Divider(),
          const Row(
            children: [
              Padding(
                padding: EdgeInsets.symmetric(horizontal: 20.0, vertical: 10),
                child: Text(
                  'Other',
                  style: TextStyle(
                    color: Colors.black54,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ],
          ),
          ListTile(
            leading: Image.asset(
              "assets/icons/r_price.png",
              height: 24,
              color: Colors.black,
            ),
            title: const Text(
              'Buy Badge',
              style: TextStyle(
                color: Colors.black,
                fontWeight: FontWeight.bold,
              ),
            ),
            onTap: () {
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: const Icon(
              Icons.share,
              color: Colors.black,
            ),
            title: const Text(
              'Share',
              style: TextStyle(
                color: Colors.black,
                fontWeight: FontWeight.bold,
              ),
            ),
            onTap: () {
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: const Icon(
              Icons.star,
              color: Colors.black,
            ),
            title: const Text(
              'Rate Us',
              style: TextStyle(
                color: Colors.black,
                fontWeight: FontWeight.bold,
              ),
            ),
            onTap: () {
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Image.asset(
              "assets/icons/r_virus.png",
              height: 24,
              color: Colors.black,
            ),
            title: const Text(
              'Feedback/Bug Reports',
              style: TextStyle(
                color: Colors.black,
                fontWeight: FontWeight.bold,
              ),
            ),
            onTap: () {
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Image.asset(
              "assets/icons/r_insurance.png",
              height: 24,
              color: Colors.black,
            ),
            title: const Text(
              'Privacy Policy',
              style: TextStyle(
                color: Colors.black,
                fontWeight: FontWeight.bold,
              ),
            ),
            onTap: () {
              Navigator.pop(context);
            },
          ),
        ],
      ),
    );
  }
}
