package ro.webdata.lido.convert.edm.common;

import java.util.*;

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
        StringBuilder roman = new StringBuilder("");

        for (Integer i: arabicMap.keySet()) {
            for (int j = 1; j <= num / i; j++) {
                roman.append(arabicMap.get(i));
            }
            num %= i;
        }

        return roman.toString();
    }

    // https://rekinyz.wordpress.com/2015/01/27/convert-roman-numerals-to-arabic-numerals-and-vice-versa-with-java/
    public static int romanToInt(String s) {
        int sum = 0;
        int len = s.length() - 1;

        for (int i = 0; i < len; i++) {
            if (romanMap.get(s.charAt(i)) < romanMap.get(s.charAt(i + 1))) {
                sum -= romanMap.get(s.charAt(i));
            } else {
                sum += romanMap.get(s.charAt(i));
            }
        }

        sum += romanMap.get(s.charAt(len));

        return sum;
    }
}
