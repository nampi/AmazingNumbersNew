package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class MyNumber {

    enum Params {
        EVEN, ODD, BUZZ, DUCK, PALINDROMIC, SPY, GAPFUL, SUNNY, SQUARE, JUMPING, HAPPY, SAD;

        final static Set<String> PROPERTIES = Stream.of(values()).map(Params::name).collect(Collectors.toSet());
        final static Set<String> NEGATIVE_PROPERTIES =
                Stream.of(values()).map(Params::getNegative).collect(Collectors.toSet());

        final static String AVAILABLE_PROPERTIES_INFO =
                "Available properties: " + Arrays.stream(values()).collect(Collectors.toList());

        static boolean isCorrect(String property) {
            return PROPERTIES.contains(property) || NEGATIVE_PROPERTIES.contains(property);
        }

        static String getAvailablePropertiesInfo() {
            return AVAILABLE_PROPERTIES_INFO;
        }

        String toLowerCase() {
            return this.name().toLowerCase();
        }

        String getNegative() {
            return "-" + this.name();
        }
    }

    private static final String[][] EXCLUSIVE_PROPERTIES = {
            {Params.EVEN.name(), Params.ODD.name()},
            {Params.EVEN.getNegative(), Params.ODD.getNegative()},
            {Params.DUCK.name(), Params.SPY.name()},
            {Params.SQUARE.name(), Params.SUNNY.name()},
            {Params.HAPPY.name(), Params.SAD.name()},
            {Params.HAPPY.getNegative(), Params.SAD.getNegative()},};

    private final String numStr;
    private final Long num;
    private final String info;
    private final String shortInfo;
    private final Set<Params> params = new HashSet<>();

    public MyNumber(long x) {
        num = x;
        numStr = num.toString();

        params.add((getLastDigit() % 2 == 0) ? Params.EVEN : Params.ODD);

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
        params.add(isHappy() ? Params.HAPPY : Params.SAD);

        info = genInfo();
        shortInfo = genShortInfo();
    }

    public static String[][] getExclusiveProperties() {
        return EXCLUSIVE_PROPERTIES;
    }

    public Set<Params> getParams() {
        return params;
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
        long sum = 0;
        long multi = 1;
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
        return String.format("%nProperties of %1s%n%2s", numStr, toPrint);
    }

    private String genShortInfo() {
        return String.format("%1s is %2s", numStr, String.join(",", params.toString()));
    }

    public void printInfo() {
        System.out.println(info);
    }

    public void printShortInfo() {
        System.out.println(shortInfo);
    }

}

class InputStructure {
    private final Main.Modes mode;
    private final long startNumber;
    private final long amountOfNumbers;
    private final Collection<String> properties;

    static class Builder {
        private Main.Modes mode = Main.Modes.UNKNOWN;
        private long startNumber = 0;
        private long amountOfNumbers = 0;
        private Collection<String> properties = new ArrayList<>();

        public Builder mode(Main.Modes mode) {
            this.mode = mode;
            return this;
        }

        public Builder startNumber(long startNumber) {
            this.startNumber = startNumber;
            return this;
        }

        public Builder amountOfNumbers(long amountOfNumbers) {
            this.amountOfNumbers = amountOfNumbers;
            return this;
        }

        public Builder properties(Collection<String> properties) {
            this.properties = properties;
            return this;
        }

        public InputStructure build() {
            return new InputStructure(this);
        }
    }

    private InputStructure(Builder builder) {
        mode = builder.mode;
        startNumber = builder.startNumber;
        amountOfNumbers = builder.amountOfNumbers;
        properties = List.copyOf(builder.properties);
    }

    public Main.Modes getMode() {
        return mode;
    }

    public long getStartNumber() {
        return startNumber;
    }

    public long getAmountOfNumbers() {
        return amountOfNumbers;
    }

    public Collection<String> getProperties() {
        return properties;
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

    private static InputStructure processInput(String inputStr) {
        if ("0".equals(inputStr)) {
            System.out.println("Goodbye!");
            return new InputStructure.Builder()
                    .mode(Modes.EXIT)
                    .build();
        }
        String[] input = inputStr.split(" ");
        long startNumber = parseNum(input[0]);
        if (startNumber < 1) {
            System.out.println("The first parameter should be a natural number or zero.");
            return new InputStructure.Builder()
                    .mode(Modes.INVALID)
                    .build();
        }

        if (input.length == 1) {
            return new InputStructure.Builder()
                    .mode(Modes.ONE_NUMBER)
                    .startNumber(startNumber)
                    .build();
        }

        long amountOfNumbers = parseNum(input[1]);
        if (amountOfNumbers < 1) {
            System.out.println("The second parameter should be a natural number.");
            return new InputStructure.Builder()
                    .mode(Modes.INVALID)
                    .build();
        }

        Collection<String> properties = Arrays.stream(Arrays.copyOfRange(input, 2, input.length))
                .map(String::toUpperCase).collect(Collectors.toSet());
        return new InputStructure.Builder()
                .mode(Modes.MANY_NUMBERS)
                .startNumber(startNumber)
                .amountOfNumbers(amountOfNumbers)
                .properties(properties)
                .build();
    }

    enum Modes {
        ONE_NUMBER,
        MANY_NUMBERS,
        INVALID,
        EXIT,
        UNKNOWN
    }

    public static void main(String[] args) {
        welcome();
        Scanner scanner = new Scanner(System.in);
        boolean isWorking = true;
        while (isWorking) {
            System.out.println("Enter a request:");
            InputStructure input = processInput(scanner.nextLine());

            switch (input.getMode()) {
                case ONE_NUMBER -> {
                    MyNumber num = new MyNumber(input.getStartNumber());
                    num.printInfo();
                }
                case MANY_NUMBERS -> {
                    if (!hasInvalidProperty(input.getProperties()) && !hasExclusiveProperties(input.getProperties())) {
                        processManyArguments(input);
                    }
                }
                case INVALID -> {
                }
                case EXIT -> isWorking = false;
                case UNKNOWN -> {
                    System.out.println("Unknown error");
                    isWorking = false;
                }
            }
        }
    }

    private static void processManyArguments(InputStructure input) {
        var properties = input.getProperties();
        var startNumber = input.getStartNumber();
        var amountOfNumbers = input.getAmountOfNumbers();

        long curNum = startNumber;
        while (amountOfNumbers > 0) {
            MyNumber num = new MyNumber(curNum++);
            boolean print = true;
            if (!properties.isEmpty()) {
                for (var property : properties) {
                    print = property.startsWith("-")
                            ? !num.getParams().contains(MyNumber.Params.valueOf(property.substring(1)))
                            :  num.getParams().contains(MyNumber.Params.valueOf(property));
                    if (!print) {
                        break;
                    }
                }
            }

            if (print) {
                num.printShortInfo();
                amountOfNumbers--;
            }
        }
    }

    private static boolean hasInvalidProperty(Collection<String> properties) {
        if (properties.isEmpty()) {
            return false;
        }

        Collection<String> incorrectProperties = new ArrayList<>();
        for (String property : properties) {
            if (!MyNumber.Params.isCorrect(property)) {
                incorrectProperties.add(property);
            }
        }

        if (incorrectProperties.isEmpty()) {
            return false;
        }

        printIncorrectPropertiesMessage(incorrectProperties);
        return true;
    }

    private static void printIncorrectPropertiesMessage(Collection<String> incorrectProperties) {
        if (incorrectProperties.size() == 1) {
            System.out.println(String.format("The property [%1s] is wrong.", incorrectProperties));
        } else {
            System.out.println(String.format("The properties [%1s] are wrong.",
                    String.join(", ", incorrectProperties)));
        }
        System.out.println(MyNumber.Params.getAvailablePropertiesInfo());
    }

    private static boolean hasExclusiveProperties(Collection<String> properties) {
        if (properties.isEmpty()) {
            return false;
        }

        for (var exclPropertyPair : MyNumber.getExclusiveProperties()) {
            if (properties.contains(exclPropertyPair[0]) && properties.contains(exclPropertyPair[1])) {
                printExclusivePropertiesMessage(exclPropertyPair[0], exclPropertyPair[1]);
                return true;
            }
        }
        for (String property : properties) {
            if (properties.contains("-" + property)) {
                printExclusivePropertiesMessage(property, "-" + property);
                return true;
            }
        }

        return false;
    }

    private static void printExclusivePropertiesMessage(String property1, String property2) {
        System.out.println(String.format("The request contains mutually exclusive properties: [%1s, %2s].",
                property1, property2));
        System.out.println("There are no numbers with these properties.");
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
