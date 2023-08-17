package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;
// TODO I see no libraries found for javafx
// import javafx.util.Pair;

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

    private final String numStr;
    private final Long num;
    private final String info;
    private final String shortInfo;

    private final Set<Params> params = new HashSet<>();
    public Collection<Params> getParams() {
        return params;
    }

    public MyNumber(String x) {
        numStr = x;
        num = Long.parseLong(numStr);

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
        int divis = getFirstDigit() * 10 + getLastDigit();
        return num % divis == 0;
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
        double epsilon = 0.000001d;

        return Math.abs(sqrt - Math.floor(sqrt)) < epsilon;
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
            toPrint.append(param.toLowerCase() + ": " + params.contains(param) + "/n");
        }
        return "\nProperties of " + numStr + toPrint;
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
    private interface Checkable {
        boolean check(MyNumber num);
    }

    private static boolean check(Collection<String> users, String param, Checkable check, MyNumber num) {
        return users.contains(param) && check.check(num) || users.contains("-" + param) && !check.check(num);
    }

    private static long parseNum(String x) {
        long num = 0;
        try {
            num = Long.parseLong(x);
        } catch (Exception e) {
        }
        return num;
    }

    public static void main(String[] args) {
        welcome();
        Scanner scanner = new Scanner(System.in);
        // I suggest splitting this code in small static methods
        // TODO how to split them in methods
        // if first method parse input, then I need to return startNumber, count and users, in this case do I need to
        // create a structure for it

        while (true) {
            System.out.println("Enter a request:");
            String inputStr = scanner.nextLine();

            if ("0".equals(inputStr)) {
                System.out.println("Goodbye!");
                break;
            }
            String[] input = inputStr.split(" ");
            long startNumber = parseNum(input[0]);
            if (startNumber < 1) {
                System.out.println("The first parameter should be a natural number or zero.");
                continue;
            }

            if (input.length == 1) {
                MyNumber num = new MyNumber(inputStr);
                num.printInfo();
                continue;
            }

            long count = parseNum(input[1]);
            if (count < 1) {
                System.out.println("The second parameter should be a natural number.");
                continue;
            }

            // TODO how to do it?
            Collection<String> users = new
                    HashSet<>(Arrays.asList(input).subList(2, input.length).stream().map((x) -> x.toUpperCase()).toList());

            if (hasInvalidProperty(users)) {
                continue;
            }

            if (hasExclusiveProperty(users)) {
                continue;
            }

            processManyArguments(users, startNumber, count);
        }
    }

    private static void processManyArguments(Collection<String> users, long startNumber, long count) {
        System.out.println();
        long cur = startNumber;
        while (count > 0) {
            MyNumber num = new MyNumber(Long.toString(cur++));
            if (users.size() > 0) {
                boolean toSkip = false;
                // TODO use var or proper name
                for (var param : MyNumber.Params.values()) {
                    //toSkip |= check(users, param.name(), (x) -> !(x.getParams().contains(param)), num);
                    // I don't understand why the lines below are incorrect
                    //toSkip |= users.contains(param) && !num.params.contains(param);
                    //toSkip |= users.contains("-" + param) && num.params.contains(param);
                }
                if (toSkip) {
                    continue;
                }
            }

            num.printShortInfo();
            count--;
        }
    }

    private static boolean hasInvalidProperty(Collection<String> users) {
        if (users.isEmpty()) {
            return false;
        }
        // TODO how to create these?
        Collection<String> allCases =  Stream.of(MyNumber.Params.values()).map(MyNumber.Params::name).toList();
        Collection<String> allCasesNegative = Stream.of(MyNumber.Params.values()).map(MyNumber.Params::name).map((a) -> "-" + a).toList();
        ArrayList<String> incorrect = new ArrayList<>();
        for (String user: users) {
            boolean correct = false;
            if (allCases.contains(user) || allCasesNegative.contains(user)) {
                correct = true;
            }
            if (!correct) {
                incorrect.add(user);
            }
        }

        if (incorrect.size() > 0) {
            if (incorrect.size() == 1) {
                System.out.println("The property [" + incorrect.get(0).toUpperCase() + "] is wrong.");
            } else {
                StringBuilder incor = new StringBuilder();
                for (String p : incorrect) {
                    incor.append(p.toUpperCase()).append(", ");
                }
                System.out.println("The properties [" +
                        incor.substring(0, incor.length() - 2) + "] are wrong.");
            }

            System.out.println("Available properties: [" +
                    Arrays.stream(MyNumber.Params.values()).toList());
            return true;
        }
        return false;
    }

    private static boolean hasExclusiveProperty(Collection<String> users) {
        if (users.isEmpty()) {
            return false;
        }
        // TODO how to import pair
        String[][] names = {
                {"EVEN", "ODD"},
                {"-EVEN", "-ODD"},
                {"DUCK", "SPY"},
                {"SQUARE", "SUNNY"},
                {"HAPPY", "SAD"},
                {"-HAPPY", "-SAD"},

        };

        if (users.size() > 0) {
            boolean flag = false;
            for (var name : names) {
                if (users.contains(name[0]) && users.contains(name[1])) {
                    System.out.println("The request contains mutually exclusive properties: [" + name[0] + ", " + name[1] + "].");
                    flag = true;
                }
            }
            for (String user : users) {
                if (users.contains("-" + user)) {
                    System.out.println("The request contains mutually exclusive properties: [" +
                            user + ", -" + user + "].");
                    flag = true;
                    break;
                }
            }
            if (flag) {
                System.out.println("There are no numbers with these properties.");
                return true;
            }
        }
        return false;
    }



    private static void welcome() {
        // TODO is it good to use text?
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
