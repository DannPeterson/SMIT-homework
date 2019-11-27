package ee.smit.homework.classfinder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.nio.file.Files.readAllLines;

public class ClassFinder {
    private List<String> fileStrings;
    private String pattern;
    private List<String> patternMatches;

    public List<String> getMatches(String fileName, String pattern) {
        patternMatches = new ArrayList<>();
        if (isInputDataCorrect(fileName, pattern)) {
            setData(fileName, pattern);
            chooseMatchesFindingMethod();
            sortMatches();
        }
        return patternMatches;
    }

    private boolean isInputDataCorrect(String fileName, String pattern) {
        if (isFileCorrect(fileName) && isPatternCorrect(pattern)) {
            return true;
        }
        return false;
    }

    private boolean isFileCorrect(String fileName) {
        File classes = new File(fileName);
        if (!classes.exists()) {
            throw new IllegalArgumentException("File not found");
        }
        if (classes.length() == 0) {
            throw new IllegalArgumentException("File is empty");
        }
        return true;
    }

    private boolean isPatternCorrect(String pattern) {
        if (pattern.trim().length() > 0) {
            return true;
        }
        return false;
    }

    private void setData(String fileName, String userPattern) {
        try {
            fileStrings = readAllLines(Paths.get(fileName));
            fileStrings.replaceAll(String::trim);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading lines from file");
        }
        pattern = userPattern;
    }

    private void chooseMatchesFindingMethod() {
        patternMatches = new ArrayList<>();

        if (pattern.endsWith(" ")) {
            if (StringUtils.containsUpperCase(pattern)) {
                findMatchesEndsWithSpaceContainsUpperCase();
            } else {
                findMatchesEndsWithSpaceNoUpperCase(fileStrings, pattern.trim());
            }
        } else {
            if (StringUtils.containsUpperCase(pattern)) {
                findMatchesNoSpaceCaseSensitive(fileStrings, pattern);
            } else {
                findMatchesNoSpaceCaseInsensitive();
            }
        }
    }

    private void findMatchesEndsWithSpaceContainsUpperCase() {
        List<String> patternWords = StringUtils.splitByLastCapitalLetter(pattern.trim());
        if (patternWords.size() == 2) {
            findMatchesNoSpaceCaseSensitive(fileStrings, patternWords.get(0));
            List<String> firstWordMatches = new ArrayList<>(patternMatches);
            findMatchesEndsWithSpaceNoUpperCase(firstWordMatches, patternWords.get(1));
        } else if (patternWords.size() == 1) {
            findMatchesEndsWithSpaceNoUpperCase(fileStrings, patternWords.get(0));
        }
    }

    private void findMatchesEndsWithSpaceNoUpperCase(List<String> lines, String pattern) {
        List<String> matches = new ArrayList<>();
        List<String> fileStringsLowCase = new ArrayList<>(lines);
        fileStringsLowCase.replaceAll(String::toLowerCase);

        for (int i = 0; i < fileStringsLowCase.size(); i++) {
            if (fileStringsLowCase.get(i).endsWith(pattern.toLowerCase())) {
                matches.add(lines.get(i));
            }
        }
        patternMatches = matches;
    }

    private void findMatchesNoSpaceCaseSensitive(List<String> lines, String pattern) {
        for (int i = 0; i < lines.size(); i++) {

            StringBuilder patternMatch = new StringBuilder();
            List<Character> patternChars = StringUtils.stringToCharList(pattern);

            for (char ch : lines.get(i).toCharArray()) {
                if (patternChars.size() > 0 && patternChars.get(0) == '*') {
                    patternMatch.append('*');
                    patternChars.remove(0);
                } else if (patternChars.size() > 0 && patternChars.get(0) == ch) {
                    patternMatch.append(ch);
                    patternChars.remove(0);
                }
            }
            if (patternMatch.toString().equals(pattern)) {
                patternMatches.add(fileStrings.get(i));
            }
        }
    }

    private void findMatchesNoSpaceCaseInsensitive() {
        List<String> fileStringsLowCase = new ArrayList<>(fileStrings);
        fileStringsLowCase.replaceAll(String::toLowerCase);
        findMatchesNoSpaceCaseSensitive(fileStringsLowCase, pattern);
    }

    private void sortMatches() {
        Map<String, String> classNameMap = getClassNameTreeMap();
        List<String> sortedStrings = new ArrayList<>();
        for (Map.Entry<String, String> entry : classNameMap.entrySet()) {
            sortedStrings.add(entry.getValue());
        }
        patternMatches = sortedStrings;
    }

    private Map<String, String> getClassNameTreeMap() {
        Map<String, String> result = new TreeMap<>();
        for (String line : patternMatches) {
            String[] packages = line.split("\\.");
            if (packages.length > 0) {
                String className = packages[packages.length - 1];
                result.put(className, line);
            } else {
                result.put(line, line);
            }
        }
        return result;
    }
}