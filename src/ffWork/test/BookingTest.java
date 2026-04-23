package ffWork.test;

import ffWork.domain.booking.Booking;
import ffWork.domain.booking.BookingStatus;
import ffWork.domain.resource.Resource;
import ffWork.domain.resource.Room;
import ffWork.domain.user.User;
import ffWork.money.Money;
import ffWork.time.FFDateTime;

import java.util.Set;

class BookingTest {
    public static void main(String[] args) {
        testBookingCreation();
        testDurationMinutes();
        testConfirm();
        testCancelFromPending();
        testCompleteFromConfirmed();
        testInvalidStatusTransition();
        testInvalidTimeRange();
    }

    static Booking createSampleBooking() {
        User user = new User("john@example.com", "John");
        Resource room = new Room("Room A", Money.of("120.00"), 8, Set.of("projector"));

        FFDateTime start = FFDateTime.parse("2025-09-15T10:00");
        FFDateTime end = FFDateTime.parse("2025-09-15T12:30");

        return new Booking(
                "BK-20250915-1",
                user,
                room,
                start,
                end,
                BookingStatus.PENDING,
                Money.of("300.00")
        );
    }

    static void testBookingCreation() {
        Booking booking = createSampleBooking();
        System.out.println("Booking creation OK");
        System.out.println("Duration: " + booking.durationMinutes() + " minutes");
        System.out.println();
    }

    static void testDurationMinutes() {
        Booking booking = createSampleBooking();

        if (booking.durationMinutes() != 150) {
            throw new RuntimeException("durationMinutes FAILED");
        }

        System.out.println("durationMinutes OK");
    }

    static void testConfirm() {
        Booking booking = createSampleBooking();
        booking.confirm();

        System.out.println("confirm() OK");
    }

    static void testCancelFromPending() {
        Booking booking = createSampleBooking();
        booking.cancel();

        System.out.println("cancel() from PENDING OK");
    }

    static void testCompleteFromConfirmed() {
        Booking booking = createSampleBooking();
        booking.confirm();
        booking.complete();

        System.out.println("complete() from CONFIRMED OK");
    }

    static void testInvalidStatusTransition() {
        try {
            Booking booking = createSampleBooking();
            booking.complete(); // PENDING -> COMPLETED should fail
            throw new RuntimeException("Invalid status transition FAILED");
        } catch (IllegalStateException e) {
            System.out.println("Invalid status transition OK");
        }
    }

    static void testInvalidTimeRange() {
        try {
            User user = new User("john@example.com", "John");
            Resource room = new Room("Room A", Money.of("120.00"), 8, Set.of("projector"));

            FFDateTime start = FFDateTime.parse("2025-09-15T12:30");
            FFDateTime end = FFDateTime.parse("2025-09-15T10:00");

            new Booking(
                    "BK-20260423-1",
                    user,
                    room,
                    start,
                    end,
                    BookingStatus.PENDING,
                    Money.of("300.00")
            );

            throw new RuntimeException("Invalid time range FAILED");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid time range OK");
        }
    }
}
