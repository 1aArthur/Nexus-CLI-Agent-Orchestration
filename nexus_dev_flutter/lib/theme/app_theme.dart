import 'package:flutter/material.dart';

/// OLED Black & Bright White palette (no neon colors per requirements).
class AppColors {
  // OLED pure black background
  static const Color oledBlack = Color(0xFF000000);
  static const Color oledBlackSurface = Color(0xFF0A0A0A);
  static const Color oledBlackElevated = Color(0xFF101010);
  static const Color oledBlackCard = Color(0xFF141414);

  // Bright white foreground
  static const Color brightWhite = Color(0xFFFFFFFF);
  static const Color whiteSoft = Color(0xFFE8E8E8);
  static const Color whiteMuted = Color(0xFFBDBDBD);
  static const Color whiteDim = Color(0xFF8A8A8A);

  // Borders / dividers (white-based, low opacity)
  static const Color border = Color(0xFF2A2A2A);
  static const Color borderBright = Color(0xFF3D3D3D);

  // Single functional accent: pure white used for emphasis + a neutral grey scale only.
  // Severity scale uses white/grey intensity to stay OLED compliant.
  static const Color severityCritical = Color(0xFFFFFFFF);
  static const Color severityHigh = Color(0xFFD6D6D6);
  static const Color severityMedium = Color(0xFF9A9A9A);
  static const Color severityLow = Color(0xFF6A6A6A);
  static const Color severityClean = Color(0xFF4A4A4A);
}

class AppTheme {
  static ThemeData get lightOled => ThemeData(
        useMaterial3: true,
        brightness: Brightness.dark,
        scaffoldBackgroundColor: AppColors.oledBlack,
        canvasColor: AppColors.oledBlack,
        colorScheme: const ColorScheme(
          brightness: Brightness.dark,
          primary: AppColors.brightWhite,
          onPrimary: AppColors.oledBlack,
          secondary: AppColors.whiteSoft,
          onSecondary: AppColors.oledBlack,
          surface: AppColors.oledBlackSurface,
          onSurface: AppColors.brightWhite,
          surfaceVariant: AppColors.oledBlackElevated,
          onSurfaceVariant: AppColors.whiteMuted,
          outline: AppColors.border,
          error: AppColors.brightWhite,
          onError: AppColors.oledBlack,
          background: AppColors.oledBlack,
          onBackground: AppColors.brightWhite,
        ),
        appBarTheme: const AppBarTheme(
          backgroundColor: AppColors.oledBlack,
          foregroundColor: AppColors.brightWhite,
          elevation: 0,
          scrolledUnderElevation: 0,
        ),
        cardTheme: CardTheme(
          color: AppColors.oledBlackCard,
          surfaceTintColor: AppColors.oledBlackCard,
          elevation: 0,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
            side: const BorderSide(color: AppColors.border, width: 1),
          ),
        ),
        dividerTheme: const DividerThemeData(
          color: AppColors.border,
          thickness: 1,
        ),
        textTheme: const TextTheme(
          bodyLarge: TextStyle(color: AppColors.brightWhite, fontFamily: 'JetBrainsMono'),
          bodyMedium: TextStyle(color: AppColors.whiteSoft, fontFamily: 'JetBrainsMono'),
          bodySmall: TextStyle(color: AppColors.whiteMuted, fontFamily: 'JetBrainsMono'),
          titleLarge: TextStyle(color: AppColors.brightWhite, fontFamily: 'JetBrainsMono', fontWeight: FontWeight.bold),
          titleMedium: TextStyle(color: AppColors.brightWhite, fontFamily: 'JetBrainsMono', fontWeight: FontWeight.bold),
          labelLarge: TextStyle(color: AppColors.brightWhite, fontFamily: 'JetBrainsMono'),
          labelSmall: TextStyle(color: AppColors.whiteDim, fontFamily: 'JetBrainsMono'),
        ),
        iconTheme: const IconThemeData(color: AppColors.brightWhite),
        bottomNavigationBarTheme: const BottomNavigationBarThemeData(
          backgroundColor: AppColors.oledBlack,
          selectedItemColor: AppColors.brightWhite,
          unselectedItemColor: AppColors.whiteDim,
        ),
        inputDecorationTheme: InputDecorationTheme(
          filled: true,
          fillColor: AppColors.oledBlackSurface,
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(10),
            borderSide: const BorderSide(color: AppColors.border),
          ),
          enabledBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(10),
            borderSide: const BorderSide(color: AppColors.border),
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(10),
            borderSide: const BorderSide(color: AppColors.brightWhite),
          ),
          labelStyle: const TextStyle(color: AppColors.whiteMuted),
          hintStyle: const TextStyle(color: AppColors.whiteDim),
        ),
      );
}
