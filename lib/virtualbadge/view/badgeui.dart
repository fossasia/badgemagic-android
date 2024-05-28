import 'package:badgemagic/virtualbadge/view/cell.dart';
import 'package:flutter/material.dart';

class badge extends StatefulWidget {
  const badge({
    super.key,
  });

  @override
  State<badge> createState() => _VirtualBadgeState();
}

class _VirtualBadgeState extends State<badge> {
  List<bool> selectedCells = List.filled(11 * 44, false);

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.symmetric(vertical: 10, horizontal: 10),
      padding: EdgeInsets.all(10),
      width: MediaQuery.of(context).size.width,
      height: MediaQuery.of(context).size.height * 0.127,
      decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(15), color: Colors.black),
      child: GridView.builder(
        physics: NeverScrollableScrollPhysics(),
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 44,
        ),
        itemCount: 11 * 44,
        itemBuilder: (context, index) {
          return Container(
            width: 12,
            height: 12,
            child: GestureDetector(
              onTap: () {
                setState(() {
                  selectedCells[index] = !selectedCells[index];
                });
              },
              child: Cell(
                index: index,
                isSelected: selectedCells[index],
              ),
            ),
          );
        },
      ),
    );
  }
}
