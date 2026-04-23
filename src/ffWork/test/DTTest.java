package ffWork.test;

import ffWork.time.FFDateTime;

class DTTest {
    public static void main(String[] args) {
        FFDateTime dt1 = FFDateTime.parse("2025-09-15T10:00");
        System.out.println(dt1); // 2025-09-15T10:00

        FFDateTime dt2 = dt1.plusMinutes(90);
        System.out.println(dt2); // 2025-09-15T11:30

        System.out.println(dt1.minutesUntil(dt2)); // 90
        System.out.println(dt1.compareTo(dt2) < 0); // true

        try {
            FFDateTime.parse("2025/09/15 10:00");
            System.out.println("ERROR");
        } catch (IllegalArgumentException e) {
            System.out.println("OK");
        }

//        System.out.println(FFDateTime.parse("2025/09/15 10:00"));
    }
}
