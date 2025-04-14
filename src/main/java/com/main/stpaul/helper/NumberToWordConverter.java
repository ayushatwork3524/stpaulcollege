package com.main.stpaul.helper;

public class NumberToWordConverter {
    private static final String[] units = { "", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE",
            "TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN",
            "NINETEEN" };

    private static final String[] tens = { "", "", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY",
            "EIGHTY", "NINETY" };

    private static final String[] thousands = { "", "THOUSAND", "MILLION", "BILLION", "TRILLION" };

    public static String convert(double n) {
        if (n == 0) {
            return "ZERO";
        }

        long wholePart = (long) n;
        int decimalPart = (int) Math.round((n - wholePart) * 100); // Get 2 decimal places

        String words = convertToWords(wholePart).trim();
        
        if (decimalPart > 0) {
            words += " POINT " + convertToWords(decimalPart).trim();
        }
        
        return words;
    }

    private static String convertToWords(long number) {
        if (number < 20) {
            return units[(int) number];
        } else if (number < 100) {
            return tens[(int) number / 10] + (number % 10 != 0 ? " " + units[(int) number % 10] : "");
        } else if (number < 1000) {
            return units[(int) number / 100] + " HUNDRED" + (number % 100 != 0 ? " " + convertToWords(number % 100) : "");
        } else {
            for (int i = 0; i < thousands.length; i++) {
                long unitValue = (long) Math.pow(1000, i + 1);
                if (number < unitValue) {
                    return convertToWords(number / (unitValue / 1000)) + " " + thousands[i] +
                           (number % (unitValue / 1000) != 0 ? " " + convertToWords(number % (unitValue / 1000)) : "");
                }
            }
        }
        return "NUMBER OUT OF RANGE";
    }
}
