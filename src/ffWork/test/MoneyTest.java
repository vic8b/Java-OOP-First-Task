package ffWork.test;

import ffWork.money.Money;

class MoneyTest {
    public static void main(String[] args) {
        testCreation();
        testAdd();
        testSubtract();
        testMultiply();
        testCompare();
        testInvalidInput();
        testNegativeResult();
        testRounding();

//        Money.of("abc");
//        Money a = Money.of("5.00");
//        Money b = Money.of("10.00");

//        a.subtract(b);
    }

    static void testCreation() {
        Money m = Money.of("123.456");
        System.out.println("Creation: " + m); // 123.46 PLN
    }

    static void testAdd() {
        Money a = Money.of("10.00");
        Money b = Money.of("5.50");

        Money result = a.add(b);

        if (!result.toString().equals("15.50 PLN")) {
            throw new RuntimeException("Add FAILED");
        }

        System.out.println("Add successful: " + result);
    }

    static void testSubtract() {
        Money a = Money.of("10.00");
        Money b = Money.of("3.25");

        Money result = a.subtract(b);

        if (!result.toString().equals("6.75 PLN")) {
            throw new RuntimeException("Subtract FAILED");
        }

        System.out.println("Subtract successful: " + result);
    }

    static void testMultiply() {
        Money a = Money.of("10.00");

        Money result = a.multiply(2);

        if (!result.toString().equals("20.00 PLN")) {
            throw new RuntimeException("Multiply FAILED");
        }

        System.out.println("Multiply successful: " + result);
    }

    static void testCompare() {
        Money a = Money.of("10.00");
        Money b = Money.of("20.00");

        if (!(a.compareTo(b) < 0)) throw new RuntimeException("Compare FAILED");
        if (!(b.compareTo(a) > 0)) throw new RuntimeException("Compare FAILED");
        if (a.compareTo(Money.of("10.00")) != 0) throw new RuntimeException("Compare FAILED");

        System.out.println(a + " = 10.00 result: " + a.compareTo(Money.of("10.00")));
        System.out.println(a + " < " + b + ": " + (a.compareTo(b) < 0));
        System.out.println("Compare successful");
    }

    static void testInvalidInput() {
        try {
            Money.of("abc");
            throw new RuntimeException("Invalid input FAILED");
        } catch (Exception e) {
            System.out.println("Invalid input OK");
        }
    }

    static void testNegativeResult() {
        try {
            Money a = Money.of("5.00");
            Money b = Money.of("10.00");

            a.subtract(b);

            throw new RuntimeException("Negative result FAILED");
        } catch (IllegalArgumentException e) {
            System.out.println("Negative result OK");
        }
    }

    static void testRounding() {
        Money m = Money.of("10.005");

        if (!m.toString().equals("10.01 PLN")) {
            throw new RuntimeException("Rounding FAILED");
        }

        System.out.println("Rounding OK");
    }
}
