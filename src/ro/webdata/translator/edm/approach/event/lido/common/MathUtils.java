package ro.webdata.translator.edm.approach.event.lido.common;

import ro.webdata.translator.edm.approach.event.lido.common.constants.Constants;

import java.util.*;

// see: http://math.hws.edu/eck/cs124/javanotes3/c9/ex-9-3-answer.html
public class MathUtils {
    private MathUtils() {}

    private static final TreeMap<Integer, String> arabicMap = new TreeMap<>(Collections.reverseOrder());
    private static final Map<Character, Integer> romanMap = new HashMap<>(7);

    static {
        arabicMap.put(1000, "m");
        arabicMap.put(900, "cm");
        arabicMap.put(500, "d");
        arabicMap.put(400, "cd");
        arabicMap.put(100, "c");
        arabicMap.put(90, "xc");
        arabicMap.put(50, "l");
        arabicMap.put(40, "xl");
        arabicMap.put(10, "x");
        arabicMap.put(9, "ix");
        arabicMap.put(5, "v");
        arabicMap.put(4, "iv");
        arabicMap.put(1, "i");

        romanMap.put('i', 1);
        romanMap.put('v', 5);
        romanMap.put('x', 10);
        romanMap.put('l', 50);
        romanMap.put('c', 100);
        romanMap.put('d', 500);
        romanMap.put('m', 1000);
    }

    // https://rekinyz.wordpress.com/2015/01/27/convert-roman-numerals-to-arabic-numerals-and-vice-versa-with-java/
    public static String intToRoman(int num) {
        StringBuilder roman = new StringBuilder(Constants.EMPTY_VALUE_PLACEHOLDER);

        for (Integer i: arabicMap.keySet()) {
            for (int j = 1; j <= num / i; j++) {
                roman.append(arabicMap.get(i));
            }
            num %= i;
        }

        return roman.toString();
    }

    // https://rekinyz.wordpress.com/2015/01/27/convert-roman-numerals-to-arabic-numerals-and-vice-versa-with-java/
    public static Integer romanToInt(String string) {
        String romanChar = string.toLowerCase();
        int length = romanChar.length() - 1;
        int sum = 0;

        for (int i = 0; i < length; i++) {
            int crrValue = romanMap.get(romanChar.charAt(i));
            int nextValue = romanMap.get(romanChar.charAt(i + 1));

            if (crrValue < nextValue) {
                // The roman numbers are built from left to right, but short roman numbers
                // are build based on "V" and "X" characters are build from right to left.
                // So, for the case of the rest of roman characters, the roman number is
                // INCORRECT if it's build from right to left.
                // E.g.: "XIC" (89) is incorrect. The correct number is "LXXXIX" (89).
                if (nextValue > 10) {
                    return null;
                }
                sum -= crrValue;
            } else {
                sum += crrValue;
            }
        }

        sum += romanMap.get(romanChar.charAt(length));

        return sum;
    }

    // https://stackoverflow.com/questions/6810336/is-there-a-way-in-java-to-convert-an-integer-to-its-ordinal
    public static String getOrdinal(int value) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (value % 100) {
            case 11:
            case 12:
            case 13:
                return value + "th";
            default:
                return value + suffixes[value % 10];
        }
    }
}
