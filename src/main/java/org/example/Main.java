package org.example;


import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

class MyNumber {
    String x;
    Long num;
    boolean even;
    boolean odd;
    boolean buzz;
    boolean duck;
    boolean palindromic;
    boolean gapful;
    boolean spy;
    boolean sunny;
    boolean square;
    boolean jumping;
    boolean happy;
    boolean sad;

    public MyNumber(String x) {
        this.x = x;
        this.num = Long.parseLong(x);
        this.even = getLastDigit() % 2 == 0;
        this.odd = !this.even;
        this.buzz = num % 7 == 0 || getLastDigit() == 7;
        this.duck = x.contains("0");
        this.palindromic = isPalindromic();
        this.gapful = isGapful();
        this.spy = isSpy();
        this.sunny = isSquare(num + 1);
        this.square = isSquare(num);
        this.jumping = isJumping();
        this.happy = isHappy();
        this.sad = !this.happy;
    }

    public int getLastDigit() {
        return Character.getNumericValue(x.charAt(x.length() - 1));
    }

    public int getFirstDigit() {
        return Character.getNumericValue(x.charAt(0));
    }

    public boolean isPalindromic() {
        for (int i = 0, j = x.length() - 1; i < j; i++, j--) {
            if (x.charAt(i) != x.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    public boolean isGapful() {
        if (x.length() < 3) {
            return false;
        }
        int divis = getFirstDigit() * 10 + getLastDigit();
        return Long.parseLong(x) % divis == 0;
    }

    public boolean isSpy() {
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

    public boolean isSquare(long t) {
        double sq = Math.sqrt(t);
        return ((sq - Math.floor(sq)) == 0);
    }

    public boolean isJumping() {
        if (x.length() == 1) {
            return true;
        }
        for (int i = 0; i < x.length() - 1; i++) {
            int cur = Character.getNumericValue(x.charAt(i));
            int next = Character.getNumericValue(x.charAt(i + 1));
            if (Math.abs(cur - next) != 1) {
                return false;
            }
        }
        return true;
    }

    public long getNextNum(long n) {
        long nextNum = 0;
        while (n > 0) {
            nextNum += (n % 10) * (n % 10);
            n /= 10;
        }
        return nextNum;
    }
    public boolean isHappy() {
        long slow = num;
        long fast = getNextNum(num);
        while (fast != 1 && slow != fast) {
            slow = getNextNum(slow);
            fast = getNextNum(getNextNum(fast));
        }
        return fast == 1;

    }

    public void printInfo() {
        System.out.println("Properties of " + this.x);
        System.out.println("even: " + this.even);
        System.out.println("odd: " + this.odd);
        System.out.println("buzz: " + this.buzz);
        System.out.println("duck: " + this.duck);
        System.out.println("palindromic: " + this.palindromic);
        System.out.println("gapful: " + this.gapful);
        System.out.println("spy: " + this.spy);
        System.out.println("sunny: " + this.sunny);
        System.out.println("square: " + this.square);
        System.out.println("jumping: " + this.jumping);
        System.out.println("happy: " + this.happy);
        System.out.println("sad: " + this.sad);
    }

    public void printShortInfo() {
        System.out.print(x + " is ");
        if (this.even) {
            System.out.print("even");
        } else {
            System.out.print("odd");
        }
        if (this.buzz) {
            System.out.print(", buzz");
        }
        if (this.duck) {
            System.out.print(", duck");
        }
        if (this.palindromic) {
            System.out.print(", palindromic");
        }
        if (this.gapful) {
            System.out.print(", gapful");
        }
        if (this.spy) {
            System.out.print(", spy");
        }
        if (this.sunny) {
            System.out.print(", sunny");
        }
        if (this.square) {
            System.out.print(", square");
        }
        if (this.jumping) {
            System.out.print(", jumping");
        }
        if (this.happy) {
            System.out.print(", happy");
        }
        if (this.sad) {
            System.out.print(", sad");
        }
        System.out.println();
    }

}

public class Main {
    public static void main(String[] args) {
        welcome();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a request:");
            String x = scanner.nextLine();
            String[] result = x.split(" ");
            if (result.length == 1) {
                if (x.equals("0")) {
                    System.out.println("Goodbye!");
                    break;
                } else if (Long.parseLong(x) >= 1) {
                    MyNumber num = new MyNumber(x);
                    num.printInfo();
                } else {
                    System.out.println("The first parameter should be a natural number or zero.");
                }
            } else {
                long startNumber = Long.parseLong(result[0]);
                int count = Integer.parseInt(result[1]);
                if (count <= 0) {
                    System.out.println("The second parameter should be a natural number.");
                    continue;
                }
                ArrayList<String> users = new ArrayList<String>();
                for (int i = 2; i < result.length; i++) {
                    if (!users.contains(result[i].toUpperCase())) {
                        users.add(result[i].toUpperCase());
                    }
                }


                String[] properties = {"buzz", "duck", "palindromic",
                        "gapful", "spy", "even", "odd", "sunny", "square", "jumping",
                        "happy", "sad"};
                ArrayList<String> incorrect = new ArrayList<String>();
                for (String user: users) {
                    boolean correct = false;
                    for (String p : properties) {
                        if (user.toLowerCase().equals(p)) {
                            correct = true;
                            break;
                        }
                        if (user.toLowerCase().equals("-" + p)) {
                            correct = true;
                            break;
                        }
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
                    System.out.println("Available properties: [BUZZ, DUCK, PALINDROMIC, " +
                            "GAPFUL, SPY, EVEN, ODD, SUNNY, SQUARE, JUMPING, HAPPY, SAD]");
                    continue;
                }

                if (users.size() > 0) {
                    boolean flag = false;
                    if (users.contains("EVEN") && users.contains("ODD")) {
                        System.out.println("The request contains mutually exclusive properties: [EVEN, ODD].");
                        flag = true;
                    }
                    if (users.contains("-EVEN") && users.contains("-ODD")) {
                        System.out.println("The request contains mutually exclusive properties: [-EVEN, -ODD].");
                        flag = true;
                    }
                    if (users.contains("DUCK") && users.contains("SPY")) {
                        System.out.println("The request contains mutually exclusive properties: [DUCK, SPY].");
                        flag = true;
                    }
                    if (users.contains("SQUARE") && users.contains("SUNNY")) {
                        System.out.println("The request contains mutually exclusive properties: [SQUARE, SUNNY].");
                        flag = true;
                    }
                    if (users.contains("HAPPY") && users.contains("SAD")) {
                        System.out.println("The request contains mutually exclusive properties: [HAPPY, SAD].");
                        flag = true;
                    }
                    if (users.contains("-HAPPY") && users.contains("-SAD")) {
                        System.out.println("The request contains mutually exclusive properties: [-HAPPY, -SAD].");
                        flag = true;
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
                        continue;
                    }
                }

                System.out.println();
                if (users.size() == 0) {
                    for (int i = 0; i < count; i++) {
                        MyNumber num = new MyNumber(Long.toString(startNumber + i));
                        num.printShortInfo();
                    }
                } else {
                    int i = 0;
                    while (count > 0) {
                        MyNumber num = new MyNumber(Long.toString(startNumber + i));
                        i++;
                        if (users.contains("EVEN") && !num.even) {
                            continue;
                        }
                        if (users.contains("ODD") && !num.odd) {
                            continue;
                        }
                        if (users.contains("BUZZ") && !num.buzz) {
                            continue;
                        }
                        if (users.contains("DUCK") && !num.duck) {
                            continue;
                        }
                        if (users.contains("PALINDROMIC") && !num.palindromic) {
                            continue;
                        }
                        if (users.contains("SPY") && !num.spy) {
                            continue;
                        }
                        if (users.contains("GAPFUL") && !num.gapful) {
                            continue;
                        }
                        if (users.contains("SUNNY") && !num.sunny) {
                            continue;
                        }
                        if (users.contains("SQUARE") && !num.square) {
                            continue;
                        }
                        if (users.contains("JUMPING") && !num.jumping) {
                            continue;
                        }
                        if (users.contains("HAPPY") && !num.happy) {
                            continue;
                        }
                        if (users.contains("SAD") && !num.sad) {
                            continue;
                        }
                        if (users.contains("-EVEN") && num.even) {
                            continue;
                        }
                        if (users.contains("-ODD") && num.odd) {
                            continue;
                        }
                        if (users.contains("-BUZZ") && num.buzz) {
                            continue;
                        }
                        if (users.contains("-DUCK") && num.duck) {
                            continue;
                        }
                        if (users.contains("-PALINDROMIC") && num.palindromic) {
                            continue;
                        }
                        if (users.contains("-SPY") && num.spy) {
                            continue;
                        }
                        if (users.contains("-GAPFUL") && num.gapful) {
                            continue;
                        }
                        if (users.contains("-SUNNY") && num.sunny) {
                            continue;
                        }
                        if (users.contains("-SQUARE") && num.square) {
                            continue;
                        }
                        if (users.contains("-JUMPING") && num.jumping) {
                            continue;
                        }
                        if (users.contains("-HAPPY") && num.happy) {
                            continue;
                        }
                        if (users.contains("-SAD") && num.sad) {
                            continue;
                        }
                        num.printShortInfo();
                        count--;
                    }
                }
            }
        }
    }

    public static void welcome() {
        System.out.println("Welcome to Amazing Numbers!");
        System.out.println("");
        System.out.println("Supported requests:");
        System.out.println("- enter a natural number to know its properties;");
        System.out.println("- enter two natural numbers to obtain the properties of the list:");
        System.out.println("  * the first parameter represents a starting number;");
        System.out.println("  * the second parameter shows how many consecutive numbers are to be processed;");
        System.out.println("- two natural numbers and two properties to search for;");
        System.out.println("- a property preceded by minus must not be present in numbers;");
        System.out.println("- separate the parameters with one space;");
        System.out.println("- enter 0 to exit.");
    }
}
