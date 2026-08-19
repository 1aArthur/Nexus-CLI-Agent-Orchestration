import 'package:flutter/material.dart';

// OLED True Black + Pure White Theme
class NexusTheme {
  // Pure OLED Black - #000000
  static const Color oledBlack = Color(0xFF000000);
  
  // Pure White - #FFFFFF
  static const Color pureWhite = Color(0xFFFFFFFF);
  
  // White with opacity for subtle elements
  static const Color white10 = Color(0x1AFFFFFF);   // 10%
  static const Color white20 = Color(0x33FFFFFF);   // 20%
  static const Color white30 = Color(0x4DFFFFFF);   // 30%
  static const Color white40 = Color(0x66FFFFFF);   // 40%
  static const Color white50 = Color(0x80FFFFFF);   // 50%
  static const Color white60 = Color(0x99FFFFFF);   // 60%
  static const Color white70 = Color(0xB3FFFFFF);   // 70%
  static const Color white80 = Color(0xCCFFFFFF);   // 80%
  static const Color white90 = Color(0xE6FFFFFF);   // 90%
  
  // OLED Black with opacity
  static const Color black10 = Color(0x1A000000);
  static const Color black20 = Color(0x33000000);
  static const Color black30 = Color(0x4D000000);
  static const Color black40 = Color(0x66000000);
  static const Color black50 = Color(0x80000000);
  static const Color black60 = Color(0x99000000);
  static const Color black70 = Color(0xB3000000);
  static const Color black80 = Color(0xCC000000);
  static const Color black90 = Color(0xE6000000);

  // Accent colors (minimal use for status indicators only)
  static const Color cyan = Color(0xFF00FFFF);      // Electric cyan
  static const Color emerald = Color(0xFF00FF88);   // Success green
  static const Color amber = Color(0xFFFFB800);     // Warning amber
  static const Color red = Color(0xFFFF3366);       // Error red
  static const Color purple = Color(0xFFBB86FC);    // Purple accent

  // Gradients
  static const LinearGradient orbGradient = LinearGradient(
    colors: [pureWhite, Color(0xFFCCCCCC), oledBlack],
    stops: [0.0, 0.5, 1.0],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  static const RadialGradient glowGradient = RadialGradient(
    colors: [pureWhite, Color(0xFF888888), Colors.transparent],
    stops: [0.0, 0.5, 1.0],
  );

  // Text styles - Monospace only (JetBrains Mono)
  static const TextStyle monoLarge = TextStyle(
    fontFamily: 'JetBrainsMono',
    fontSize: 24,
    fontWeight: FontWeight.w700,
    color: pureWhite,
    height: 1.2,
    letterSpacing: 0,
  );

  static const TextStyle monoTitle = TextStyle(
    fontFamily: 'JetBrainsMono',
    fontSize: 18,
    fontWeight: FontWeight.w600,
    color: pureWhite,
    height: 1.3,
    letterSpacing: 0.15,
  );

  static const TextStyle monoBody = TextStyle(
    fontFamily: 'JetBrainsMono',
    fontSize: 14,
    fontWeight: FontWeight.w400,
    color: pureWhite,
    height: 1.4,
    letterSpacing: 0.25,
  );

  static const TextStyle monoSmall = TextStyle(
    fontFamily: 'JetBrainsMono',
    fontSize: 12,
    fontWeight: FontWeight.w400,
    color: white70,
    height: 1.5,
    letterSpacing: 0.25,
  );

  static const TextStyle monoTiny = TextStyle(
    fontFamily: 'JetBrainsMono',
    fontSize: 10,
    fontWeight: FontWeight.w500,
    color: white50,
    height: 1.4,
    letterSpacing: 0.5,
  );

  static const TextStyle monoLabel = TextStyle(
    fontFamily: 'JetBrainsMono',
    fontSize: 11,
    fontWeight: FontWeight.w600,
    color: white40,
    height: 1.3,
    letterSpacing: 1.0,
  );

  // Border radius
  static const BorderRadius radiusSmall = BorderRadius.all(Radius.circular(8));
  static const BorderRadius radiusMedium = BorderRadius.all(Radius.circular(12));
  static const BorderRadius radiusLarge = BorderRadius.all(Radius.circular(16));
  static const BorderRadius radiusXLarge = BorderRadius.all(Radius.circular(24));
  static const BorderRadius radiusCircle = BorderRadius.all(Radius.circular(999));

  // Shadows
  static List<BoxShadow> get glowShadow => [
    BoxShadow(
      color: cyan.withValues(alpha: 0.3),
      blurRadius: 20,
      spreadRadius: 2,
    ),
    BoxShadow(
      color: pureWhite.withValues(alpha: 0.1),
      blurRadius: 40,
      spreadRadius: 5,
    ),
  ];

  static List<BoxShadow> get cardShadow => [
    BoxShadow(
      color: pureWhite.withValues(alpha: 0.05),
      blurRadius: 10,
      offset: const Offset(0, 4),
    ),
  ];

  // ThemeData
  static ThemeData get themeData => ThemeData(
    useMaterial3: true,
    brightness: Brightness.dark,
    scaffoldBackgroundColor: oledBlack,
    canvasColor: oledBlack,
    fontFamily: 'JetBrainsMono',
    colorScheme: const ColorScheme.dark(
      primary: pureWhite,
      onPrimary: oledBlack,
      secondary: cyan,
      onSecondary: oledBlack,
      tertiary: emerald,
      onTertiary: oledBlack,
      surface: Color(0xFF0A0A0A),
      onSurface: pureWhite,
      surfaceContainerHighest: Color(0xFF111111),
      outline: white20,
      outlineVariant: white10,
      error: red,
      onError: pureWhite,
    ),
    textTheme: const TextTheme(
      displayLarge: monoLarge,
      displayMedium: monoTitle,
      displaySmall: monoTitle,
      headlineLarge: monoTitle,
      headlineMedium: monoTitle,
      headlineSmall: monoBody,
      titleLarge: monoBody,
      titleMedium: monoBody,
      titleSmall: monoSmall,
      bodyLarge: monoBody,
      bodyMedium: monoBody,
      bodySmall: monoSmall,
      labelLarge: monoSmall,
      labelMedium: monoTiny,
      labelSmall: monoTiny,
    ),
    cardTheme: CardThemeData(
      color: const Color(0xFF0A0A0A),
      surfaceTintColor: Colors.transparent,
      shadowColor: pureWhite.withValues(alpha: 0.05),
      elevation: 0,
      shape: RoundedRectangleBorder(
        borderRadius: radiusMedium,
        side: const BorderSide(color: white10, width: 1),
      ),
      margin: EdgeInsets.zero,
    ),
    elevatedButtonTheme: ElevatedButtonThemeData(
      style: ElevatedButton.styleFrom(
        backgroundColor: pureWhite,
        foregroundColor: oledBlack,
        elevation: 0,
        shadowColor: Colors.transparent,
        shape: RoundedRectangleBorder(borderRadius: radiusMedium),
        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 14),
        textStyle: const TextStyle(
          fontFamily: 'JetBrainsMono',
          fontSize: 13,
          fontWeight: FontWeight.w600,
          letterSpacing: 0.5,
        ),
      ),
    ),
    outlinedButtonTheme: OutlinedButtonThemeData(
      style: OutlinedButton.styleFrom(
        foregroundColor: pureWhite,
        side: const BorderSide(color: white30, width: 1),
        shape: RoundedRectangleBorder(borderRadius: radiusMedium),
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
        textStyle: const TextStyle(
          fontFamily: 'JetBrainsMono',
          fontSize: 12,
          fontWeight: FontWeight.w500,
        ),
      ),
    ),
    inputDecorationTheme: InputDecorationTheme(
      filled: true,
      fillColor: const Color(0xFF050505),
      border: OutlineInputBorder(
        borderRadius: radiusMedium,
        borderSide: const BorderSide(color: white10),
      ),
      enabledBorder: OutlineInputBorder(
        borderRadius: radiusMedium,
        borderSide: const BorderSide(color: white10),
      ),
      focusedBorder: OutlineInputBorder(
        borderRadius: radiusMedium,
        borderSide: const BorderSide(color: cyan, width: 2),
      ),
      errorBorder: OutlineInputBorder(
        borderRadius: radiusMedium,
        borderSide: const BorderSide(color: red),
      ),
      contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
      hintStyle: monoSmall.copyWith(color: white30),
      labelStyle: monoSmall.copyWith(color: white50),
    ),
    dividerTheme: const DividerThemeData(
      color: white10,
      thickness: 1,
      space: 1,
    ),
    navigationBarTheme: NavigationBarThemeData(
      backgroundColor: oledBlack,
      surfaceTintColor: Colors.transparent,
      indicatorColor: white10,
      labelTextStyle: WidgetStateProperty.all(monoTiny),
      iconTheme: WidgetStateProperty.resolveWith((states) {
        if (states.contains(WidgetState.selected)) {
          return const IconThemeData(color: pureWhite, size: 24);
        }
        return const IconThemeData(color: white40, size: 24);
      }),
    ),
    bottomSheetTheme: const BottomSheetThemeData(
      backgroundColor: oledBlack,
      surfaceTintColor: Colors.transparent,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      modalBackgroundColor: oledBlack,
    ),
    dialogTheme: DialogThemeData(
      backgroundColor: const Color(0xFF0A0A0A),
      surfaceTintColor: Colors.transparent,
      shape: RoundedRectangleBorder(
        borderRadius: radiusLarge,
        side: const BorderSide(color: white10),
      ),
      titleTextStyle: monoTitle,
      contentTextStyle: monoBody,
    ),
  );
}