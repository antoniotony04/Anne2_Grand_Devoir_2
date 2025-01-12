package com.example.demo;

import javafx.scene.paint.Color;

public class GlobalSettings {
    private static Color backgroundColor = Color.WHITE;
    private static double volume = 50.0;

    public static Color getBackgroundColor() {
        return backgroundColor;
    }

    public static void setBackgroundColor(Color color) {
        backgroundColor = color;
    }

    public static double getVolume() {
        return volume;
    }

    public static void setVolume(double value) {
        volume = value;
    }

    public static String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
