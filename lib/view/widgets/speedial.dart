import 'package:flutter/material.dart';
import 'package:badgemagic/providers/cardsprovider.dart';
import 'package:provider/provider.dart';
import 'dart:math';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class InnerDialPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final radius = min(size.width / 2, size.height / 2) * 0.9;

    final paint = Paint()
      ..color = Colors.grey.shade300
      ..style = PaintingStyle.stroke
      ..strokeWidth = 12.w;

    canvas.drawCircle(center, radius, paint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    return false;
  }
}

class RadialDialPainter extends CustomPainter {
  final double value;
  final double max;
  final Color color;

  RadialDialPainter({
    required this.value,
    required this.max,
    required this.color,
  });

  @override
  void paint(Canvas canvas, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final radius = min(size.width / 2, size.height / 2) * 0.8;

    final paint = Paint()
      ..color = Colors.grey.shade300
      ..style = PaintingStyle.stroke
      ..strokeCap = StrokeCap.round
      ..strokeWidth = 12.w;

    const startAngle = 3 * pi / 4;

    canvas.drawArc(
      Rect.fromCircle(center: center, radius: radius),
      startAngle,
      6 * pi / 4,
      false,
      paint,
    );

    final progressPaint = Paint()
      ..color = Colors.red
      ..style = PaintingStyle.stroke
      ..strokeCap = StrokeCap.round
      ..strokeWidth = 12.w;

    canvas.drawArc(
      Rect.fromCircle(center: center, radius: radius),
      startAngle,
      6 * pi / 4 * (value / max),
      false,
      progressPaint,
    );
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    return true;
  }
}

class InnerPointerPainter extends CustomPainter {
  final double value;
  final double max;
  final Color color;

  InnerPointerPainter({
    required this.value,
    required this.max,
    required this.color,
  });

  @override
  void paint(Canvas canvas, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final radius = min(size.width / 2, size.height / 2) * 0.4;

    final pointerAngle = 3 * pi / 4 + 6 * pi / 4 * (value / max);
    final pointerLength = radius + 25.w;

    final pointerPaint = Paint()
      ..color = color
      ..strokeCap = StrokeCap.round
      ..strokeWidth = 4.w;

    final pointerStart = Offset(
      center.dx + radius * cos(pointerAngle),
      center.dy + radius * sin(pointerAngle),
    );
    final pointerEnd = Offset(
      center.dx + pointerLength * cos(pointerAngle),
      center.dy + pointerLength * sin(pointerAngle),
    );

    canvas.drawLine(pointerStart, pointerEnd, pointerPaint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    return true;
  }
}

class RadialDial extends StatefulWidget {
  const RadialDial({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _RadialDialState createState() => _RadialDialState();
}

class _RadialDialState extends State<RadialDial> {
  double outerValue = 0.0;
  final double maxValue = 8.0;

  final double initialAngle = 155 * pi / 180;
  double previousAngle = 0.0;
  bool isDragging = true;

  @override
  void initState() {
    super.initState();
    previousAngle = initialAngle;
  }

  @override
  Widget build(BuildContext context) {
    CardProvider outerValueProvider = Provider.of<CardProvider>(context);

    void updateOuterValue(double angle) {
      const startAngle = 155 * pi / 270;
      const endAngle = 360 * pi / 180;

      const totalAngle = endAngle - startAngle;

      final numSections = maxValue;

      final anglePerSection = totalAngle / numSections;

      final section = ((angle - startAngle) / anglePerSection).round();

      final clampedSection = section.clamp(1, numSections);

      setState(() {
        outerValueProvider.setOuterValue(clampedSection.toInt());
      });
    }

    void updateAngle(Offset position, Size size) {
      if (!isDragging) return;

      final center = Offset(size.width / 2, size.height / 2);
      final dx = position.dx - center.dx;
      final dy = position.dy - center.dy;
      final distanceFromCenter = sqrt(dx * dx + dy * dy);

      if (distanceFromCenter > size.width / 2) return;

      var angle = atan2(dy, dx);

      if (angle < 0) {
        angle += 2 * pi;
      }

      const startAngle = 155 * pi / 270;
      const endAngle = 360 * pi / 180;

      if (angle >= startAngle && angle <= endAngle) {
        if ((angle >= previousAngle && angle <= endAngle) ||
            (angle < startAngle && previousAngle < startAngle) ||
            (angle - previousAngle).abs() < pi) {
          setState(() {
            updateOuterValue(angle);
          });
        }
        previousAngle = angle;
      }
    }

    return Stack(
      alignment: Alignment.center,
      children: [
        CustomPaint(
          painter: RadialDialPainter(
            value: outerValueProvider.getOuterValue().toDouble(),
            max: maxValue,
            color: Colors.red,
          ),
          child: SizedBox(
            width: 230.w,
            height: 250.h,
          ),
        ),
        CustomPaint(
          painter: InnerDialPainter(),
          child: Container(
            color: Colors.transparent,
            width: 150.w,
          ),
        ),
        GestureDetector(
          onPanUpdate: (details) {
            if (isDragging) {
              RenderBox renderBox = context.findRenderObject() as RenderBox;
              Offset localPosition =
                  renderBox.globalToLocal(details.globalPosition);
              updateAngle(localPosition, renderBox.size);
            }
          },
          child: CustomPaint(
            painter: InnerPointerPainter(
              value: outerValueProvider.getOuterValue().toDouble(),
              max: maxValue,
              color: Colors.red,
            ),
            child: SizedBox(
              width: 140.w,
              height: 140.h,
            ),
          ),
        ),
        Positioned(
          child: Text(
            (outerValueProvider.getOuterValue()).toString(),
            style: TextStyle(
              fontSize: 60.sp,
              fontWeight: FontWeight.w600,
              color: const Color.fromRGBO(113, 113, 113, 1),
            ),
          ),
        ),
      ],
    );
  }
}
