package ee.smit.homework.classfinder;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("File nor pattern arguments not found");
        }
        
        ClassFinder classFinder = new ClassFinder();
        List<String> matches = classFinder.getMatches(args[0], args[1]);

        for (String string : matches) {
            System.out.println(string);
        }
    }
}