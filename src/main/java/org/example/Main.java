package org.example;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class MyNumber {

    public enum Params {
        EVEN,
        ODD,
        BUZZ,
        DUCK,
        PALINDROMIC,
        SPY,
        GAPFUL,
        SUNNY,
        SQUARE,
        JUMPING,
        HAPPY,
        SAD;

        public String toLowerCase() {
            return this.name().toLowerCase();
        }
    }

    private static final String[][] exclusiveProperties = {
            {"EVEN", "ODD"},
            {"-EVEN", "-ODD"},
            {"DUCK", "SPY"},
            {"SQUARE", "SUNNY"},
            {"HAPPY", "SAD"},
            {"-HAPPY", "-SAD"},
    };

    public static String[][] getExclusiveProperties() {
        return exclusiveProperties;
    }

    private final String numStr;
    private final Long num;
    private final String info;
    private final String shortInfo;

    private final Set<Params> params = new HashSet<>();

    public Set<Params> getParams() {
        return params;
    }

    public MyNumber(long x) {
        num = x;
        numStr = num.toString();

        if (getLastDigit() % 2 == 0) {
            params.add(Params.EVEN);
        } else {
            params.add(Params.ODD);
        }
        if (num % 7 == 0 || getLastDigit() == 7) {
            params.add(Params.BUZZ);
        }
        if (numStr.contains("0")) {
            params.add(Params.DUCK);
        }
        if (isPalindromic()) {
            params.add(Params.PALINDROMIC);
        }
        if (isGapful()) {
            params.add(Params.GAPFUL);
        }
        if (isSpy()) {
            params.add(Params.SPY);
        }
        if (isSquare(num + 1)) {
            params.add(Params.SUNNY);
        }
        if (isSquare(num)) {
            params.add(Params.SQUARE);
        }
        if (isJumping()) {
            params.add(Params.JUMPING);
        }
        if (isHappy()) {
            params.add(Params.HAPPY);
        } else {
            params.add(Params.SAD);
        }
        info = genInfo();
        shortInfo = genShortInfo();

    }

    private int getLastDigit() {
        return Character.getNumericValue(numStr.charAt(numStr.length() - 1));
    }

    private int getFirstDigit() {
        return Character.getNumericValue(numStr.charAt(0));
    }

    private boolean isPalindromic() {
        for (int i = 0, j = numStr.length() - 1; i < j; i++, j--) {
            if (numStr.charAt(i) != numStr.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    private boolean isGapful() {
        if (numStr.length() < 3) {
            return false;
        }
        int divs = getFirstDigit() * 10 + getLastDigit();
        return num % divs == 0;
    }

    private boolean isSpy() {
        int sum = 0;
        int multi = 1;
        long cur = num;
        while (cur > 0) {
            long digit = cur % 10;
            sum += digit;
            multi *= digit;
            cur /= 10;
        }
        return sum == multi;
    }

    private boolean isSquare(long t) {
        double sqrt = Math.sqrt(t);
        double EPSILON = 0.000001d;

        return Math.abs(sqrt - Math.floor(sqrt)) < EPSILON;
    }

    private boolean isJumping() {
        if (numStr.length() == 1) {
            return true;
        }
        for (int i = 0; i < numStr.length() - 1; i++) {
            int cur = Character.getNumericValue(numStr.charAt(i));
            int next = Character.getNumericValue(numStr.charAt(i + 1));
            if (Math.abs(cur - next) != 1) {
                return false;
            }
        }
        return true;
    }

    private long getNextNum(long n) {
        long nextNum = 0;
        while (n > 0) {
            nextNum += (n % 10) * (n % 10);
            n /= 10;
        }
        return nextNum;
    }

    private boolean isHappy() {
        long slow = num;
        long fast = getNextNum(num);
        while (fast != 1 && slow != fast) {
            slow = getNextNum(slow);
            fast = getNextNum(getNextNum(fast));
        }
        return fast == 1;

    }

    private String genInfo() {
        StringBuilder toPrint = new StringBuilder();
        for (var param : Params.values()) {
            toPrint.append(param.toLowerCase()).append(": ").append(params.contains(param)).append("\n");
        }
        return "\nProperties of " + numStr + "\n" + toPrint;
    }

    private String genShortInfo() {
        return numStr + " is " + String.join(",", params.toString());
    }

    public void printInfo() {
        System.out.println(info);
    }

    public void printShortInfo() {
        System.out.println(shortInfo);
    }

}

public class Main {
    private static long parseNum(String x) {
        long num = 0;
        try {
            num = Long.parseLong(x);
        } catch (Exception e) {
            System.out.println("Error: invalid input");
        }
        return num;
    }


    public static class InputStructure {
        private Collection<String> properties = new ArrayList<>();
        private long startNumber = 0;
        private long amountOfNumbers = 0;
        private Modes mode = Modes.UNKNOWN;

        public Collection<String> getProperties() {
            return properties;
        }

        public long getStartNumber() {
            return startNumber;
        }

        public long getAmountOfNumbers() {
            return amountOfNumbers;
        }

        public Modes getMode() {
            return mode;
        }

        public void setProperties(Collection<String> properties) {
            this.properties = properties;
        }

        public void setStartNumber(long startNumber) {
            this.startNumber = startNumber;
        }

        public void setAmountOfNumbers(long amountOfNumbers) {
            this.amountOfNumbers = amountOfNumbers;
        }

        public void setMode(Modes mode) {
            this.mode = mode;
        }
    }

    private static InputStructure processInput(String inputStr) {
        InputStructure inputStructure = new InputStructure();
        if (inputStr.equals("0")) {
            System.out.println("Goodbye!");
            inputStructure.setMode(Modes.EXIT);
            return inputStructure;
        }
        String[] input = inputStr.split(" ");
        long startNumber = parseNum(input[0]);
        if (startNumber < 1) {
            System.out.println("The first parameter should be a natural number or zero.");
            inputStructure.setMode(Modes.INVALID);
            return inputStructure;
        }

        inputStructure.setStartNumber(startNumber);
        if (input.length == 1) {
            inputStructure.setMode(Modes.ONE_NUMBER);
            return inputStructure;
        }

        long amountOfNumbers = parseNum(input[1]);
        if (amountOfNumbers < 1) {
            System.out.println("The second parameter should be a natural number.");
            inputStructure.setMode(Modes.INVALID);
            return inputStructure;
        }
        inputStructure.setAmountOfNumbers(amountOfNumbers);

        Collection<String> properties = Arrays.stream(Arrays.copyOfRange(input, 2, input.length)).
                map(String::toUpperCase).collect(Collectors.toSet());
        inputStructure.setProperties(properties);
        inputStructure.setMode(Modes.MANY_NUMBERS);
        return inputStructure;
    }

    public enum Modes {
        EXIT,
        INVALID,
        ONE_NUMBER,
        MANY_NUMBERS,
        UNKNOWN
    }

    public static void main(String[] args) {
        welcome();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter a request:");

            var input = processInput(scanner.nextLine());
            if (Modes.UNKNOWN.equals(input.getMode())) {
                System.out.println("Unknown error");
                break;
            }
            if (Modes.EXIT.equals(input.getMode())) {
                break;
            }
            if (Modes.INVALID.equals(input.getMode())) {
                continue;
            }
            if (Modes.ONE_NUMBER.equals(input.getMode())) {
                MyNumber num = new MyNumber(input.getStartNumber());
                num.printInfo();
                continue;
            }

            if (hasInvalidProperty(input.getProperties())) {
                continue;
            }

            if (hasExclusiveProperty(input.getProperties())) {
                continue;
            }

            processManyArguments(input);
        }
    }

    private static void processManyArguments(InputStructure input) {
        var properties = input.getProperties();
        var startNumber = input.getStartNumber();
        var amountOfNumbers = input.getAmountOfNumbers();

        System.out.println();
        long curNum = startNumber;
        while (amountOfNumbers > 0) {
            MyNumber num = new MyNumber(curNum++);
            if (properties.size() > 0) {
                boolean needSkip = false;
                for (var property : properties) {
                    if (property.startsWith("-")) {
                        needSkip |= num.getParams().contains(MyNumber.Params.valueOf(property.substring(1)));
                    } else {
                        needSkip |= !num.getParams().contains(MyNumber.Params.valueOf(property));
                    }
                }
                if (needSkip) {
                    continue;
                }
            }

            num.printShortInfo();
            amountOfNumbers--;
        }
    }

    private static boolean hasInvalidProperty(Collection<String> properties) {
        if (properties.isEmpty()) {
            return false;
        }

        Set<String> allProperties =
                Stream.of(MyNumber.Params.values()).map(MyNumber.Params::name).collect(Collectors.toSet());
        Set<String> allNegativeProperties =
                Stream.of(MyNumber.Params.values()).map(MyNumber.Params::name).map(x -> "-" + x).collect(Collectors.toSet());
        Collection<String> incorrectProperties = new ArrayList<>();
        for (String property : properties) {
            if (!(allProperties.contains(property) || allNegativeProperties.contains(property))) {
                incorrectProperties.add(property);
            }
        }

        if (incorrectProperties.isEmpty()) {
            return false;
        }
        if (incorrectProperties.size() == 1) {
            System.out.println(MessageFormat.format("The property [{0}] is wrong.", incorrectProperties));
        } else {
            System.out.println(MessageFormat.format("The properties [{0}] are wrong.",
                    String.join(", ", incorrectProperties)));
        }

        System.out.println("Available properties: " + Arrays.stream(MyNumber.Params.values()).toList());
        return true;
    }

    private static boolean hasExclusiveProperty(Collection<String> properties) {
        if (properties.isEmpty()) {
            return false;
        }

        boolean hasExclProperty = false;
        for (var exclPropertyPair : MyNumber.getExclusiveProperties()) {
            if (properties.contains(exclPropertyPair[0]) && properties.contains(exclPropertyPair[1])) {
                System.out.println(MessageFormat.format("The request contains mutually exclusive properties: [{0}, {1}].",
                        exclPropertyPair[0], exclPropertyPair[1]));
                hasExclProperty = true;
            }
        }
        for (String property : properties) {
            if (properties.contains("-" + property)) {
                System.out.println(MessageFormat.format("The request contains mutually exclusive properties: [{0}, {1}].",
                        property, "-" + property));
                hasExclProperty = true;
            }
        }
        if (hasExclProperty) {
            System.out.println("There are no numbers with these properties.");
            return true;
        }
        return false;
    }

    private static void welcome() {
        String text = """
                Welcome to Amazing Numbers!

                Supported requests:
                - enter a natural number to know its properties;
                - enter two natural numbers to obtain the properties of the list:
                  * the first parameter represents a starting number;
                  * the second parameter shows how many consecutive numbers are to be processed;
                - two natural numbers and two properties to search for;
                - a property preceded by minus must not be present in numbers;
                - separate the parameters with one space;
                - enter 0 to exit.""";
        System.out.println(text);
    }
}
