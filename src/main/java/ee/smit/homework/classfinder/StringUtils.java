package ee.smit.homework.classfinder;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static List<Character> stringToCharList(String string) {
        List<Character> result = new ArrayList<>();
        for (char ch : string.toCharArray()) {
            result.add(ch);
        }
        return result;
    }

    public static boolean containsUpperCase(String line) {
        for (char ch : line.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> splitByLastCapitalLetter(String string) {
        List<String> result = new ArrayList<>();
        int lastCapitalLetterIndex = -1;
        for (int i = 0; i < string.length(); i++) {
            if (Character.isUpperCase(string.charAt(i))) {
                lastCapitalLetterIndex = i;
            }
        }
        if (lastCapitalLetterIndex == 0) {
            result.add(string);
        } else if (lastCapitalLetterIndex > 0) {
            result.add(string.substring(0, lastCapitalLetterIndex));
            result.add(string.substring(lastCapitalLetterIndex));
        }
        return result;
    }
}