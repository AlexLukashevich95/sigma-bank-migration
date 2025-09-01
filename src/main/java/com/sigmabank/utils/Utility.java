package com.sigmabank.utils;

import java.math.BigDecimal;

public class Utility {

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isPositiveDecimal(String s) {
        if (s == null || s.isEmpty()) return false;
        try {
            return new BigDecimal(s).signum() > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String[] splitCsv(String line) {
        return line.split(",");
    }

    public static boolean isValidData(String idStr, String name, String salaryStr, String department) {
        return isInteger(idStr) &&
                !name.isEmpty() &&
                isPositiveDecimal(salaryStr) &&
                !department.isEmpty();
    }
}
