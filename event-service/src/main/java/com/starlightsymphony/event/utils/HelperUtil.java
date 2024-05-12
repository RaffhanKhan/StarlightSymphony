package com.starlightsymphony.event.utils;

public interface HelperUtil {

    public static boolean checkNull(String input) {
        if(null == input) {
            return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }

    public static boolean isNotDefaultInt(int value) {
        int defaultValue = 0; // or any other value that you consider as the default
        return value != defaultValue;
    }

    public static boolean isNotDefaultDouble(double value) {
        double defaultValue = 0.0; // or any other value that you consider as the default
        return value != defaultValue;
    }



}
