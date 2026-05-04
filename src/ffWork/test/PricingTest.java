package ffWork.test;

import ffWork.domain.booking.Booking;
import ffWork.domain.booking.BookingStatus;
import ffWork.domain.resource.Desk;
import ffWork.domain.resource.Device;
import ffWork.domain.resource.Resource;
import ffWork.domain.resource.Room;
import ffWork.domain.user.User;
import ffWork.money.Money;
import ffWork.pricing.HappyHoursPricing;
import ffWork.pricing.PricingPolicy;
import ffWork.pricing.StandardPricing;
import ffWork.time.FFDateTime;

import java.util.Set;

class PricingTest {
    public static void main(String[] args) {
        System.out.println("Standard price for Room, Desk, Device (2hrs)");
        testStandardPricingForRoom();
        testStandardPricingForDesk();
        testStandardPricingForDevice();
        System.out.println();

        System.out.println("Happy hours price for Room, Desk, Device (2hrs)");
        testHappyHoursFullyInsideForRoom();
        testHappyHoursFullyInsideForDesk();
        testHappyHoursFullyInsideForDevice();
        System.out.println();

        System.out.println("Happy hours (partially, start) price for Room, Desk, Device (2hrs)");
        testHappyHoursPartiallyAtStartForRoom();
        testHappyHoursPartiallyAtStartForDesk();
        testHappyHoursPartiallyAtStartForDevice();
        System.out.println();


        System.out.println("Happy hours (partially, end) price for Room (2hrs)");
        testHappyHoursPartiallyAtEnd();
        System.out.println();

        System.out.println("Happy hours outside hh price for Room, Desk, Device (2hrs)");
        testHappyHoursOutsideForRoom();
        testHappyHoursOutsideForDesk();
        testHappyHoursOutsideForDevice();
    }

    static Booking createBookingForRoom(String startText, String endText) {
        User user = new User("test@example.com", "Test User");
        Resource room = new Room("Room A", 10, Set.of("projector"));

        return new Booking(
                "BK-TEST",
                user,
                room,
                FFDateTime.parse(startText),
                FFDateTime.parse(endText),
                Money.of("0")
        );
    }

    static Booking createBookingForDesk(String startText, String endText) {
        User user = new User("test@example.com", "Test User");
        Desk desk = new Desk("Desk a", Desk.DeskType.HOT);

        return new Booking(
                "BK-TEST",
                user,
                desk,
                FFDateTime.parse(startText),
                FFDateTime.parse(endText),
                Money.of("0")
        );
    }

    static Booking createBookingForDevice(String startText, String endText) {
        User user = new User("test@example.com", "Test User");
        Device device = new Device("Device", 1);

        return new Booking(
                "BK-TEST",
                user,
                device,
                FFDateTime.parse(startText),
                FFDateTime.parse(endText),
                Money.of("0")
        );
    }


    private static void testStandardPricingForRoom() {
        PricingPolicy pricing = new StandardPricing();

        Booking booking = createBookingForRoom(
                "2025-09-15T10:00",
                "2025-09-15T12:00"
        );

        System.out.println("Hourly rate: " + booking.getResource().hourlyRate());
        System.out.println("Value of standard pricing: " + pricing.price(booking));
        // 2hrs -> 200
    }

    private static void testStandardPricingForDesk() {
        PricingPolicy pricing = new StandardPricing();

        Booking booking = createBookingForDesk(
                "2025-09-15T10:00",
                "2025-09-15T12:00"
        );

        System.out.println("Hourly rate: " + booking.getResource().hourlyRate());
        System.out.println("Value of standard pricing: " + pricing.price(booking));
    }

    private static void testStandardPricingForDevice() {
        PricingPolicy pricing = new StandardPricing();

        Booking booking = createBookingForDevice(
                "2025-09-15T10:00",
                "2025-09-15T12:00"
        );

        System.out.println("Hourly rate: " + booking.getResource().hourlyRate());
        System.out.println("Value of standard pricing: " + pricing.price(booking));
        // 2hrs -> 200
    }

    private static void testHappyHoursFullyInsideForRoom() {
        PricingPolicy pricing = new HappyHoursPricing();

        Booking booking = createBookingForRoom(
                "2025-09-15T14:00",
                "2025-09-15T16:00"
        );

        System.out.println("Hourly rate: " + booking.getResource().hourlyRate());
        System.out.println("Value of hh pricing: " + pricing.price(booking));
    }

    private static void testHappyHoursFullyInsideForDesk() {
        PricingPolicy pricing = new HappyHoursPricing();

        Booking booking = createBookingForDesk(
                "2025-09-15T14:00",
                "2025-09-15T16:00"
        );

        System.out.println("Hourly rate: " + booking.getResource().hourlyRate());
        System.out.println("Value of hh pricing: " + pricing.price(booking));
    }

    private static void testHappyHoursFullyInsideForDevice() {
        PricingPolicy pricing = new HappyHoursPricing();

        Booking booking = createBookingForDevice(
                "2025-09-15T14:00",
                "2025-09-15T16:00"
        );

        System.out.println("Hourly rate: " + booking.getResource().hourlyRate());
        System.out.println("Value of hh pricing: " + pricing.price(booking));
    }

    private static void testHappyHoursPartiallyAtStartForRoom() {
        PricingPolicy pricing = new HappyHoursPricing();

        Booking booking = createBookingForRoom(
                "2025-09-15T15:00",
                "2025-09-15T17:00"
        );

        System.out.println("Hourly rate: " + booking.getResource().hourlyRate());
        System.out.println("Value of hh pricing partially in hh hours: " + pricing.price(booking));
        //1hrs in hh + 1h -> 70 + 100 = 170
    }

    private static void testHappyHoursPartiallyAtStartForDesk() {
        PricingPolicy pricing = new HappyHoursPricing();

        Booking booking = createBookingForDesk(
                "2025-09-15T15:00",
                "2025-09-15T17:00"
        );

        System.out.println("Hourly rate: " + booking.getResource().hourlyRate());
        System.out.println("Value of hh pricing partially in hh hours: " + pricing.price(booking));
    }

    private static void testHappyHoursPartiallyAtStartForDevice() {
        PricingPolicy pricing = new HappyHoursPricing();

        Booking booking = createBookingForDevice(
                "2025-09-15T15:00",
                "2025-09-15T17:00"
        );

        System.out.println("Hourly rate: " + booking.getResource().hourlyRate());
        System.out.println("Value of hh pricing partially in hh hours: " + pricing.price(booking));
    }

    private static void testHappyHoursPartiallyAtEnd() {
        PricingPolicy pricing = new HappyHoursPricing();

        Booking booking = createBookingForRoom(
                "2025-09-15T13:00",
                "2025-09-15T15:00"
        );

        System.out.println("Hourly rate: " + booking.getResource().hourlyRate());
        System.out.println("Value of hh pricing partially in hh hours: " + pricing.price(booking));
        //1hrs in hh + 1h -> 70 + 100 = 170
    }

    private static void testHappyHoursOutsideForRoom() {
        PricingPolicy pricing = new HappyHoursPricing();

        Booking booking = createBookingForRoom(
                "2025-09-15T12:00",
                "2025-09-15T14:00"
        );

        System.out.println("Hourly rate: " + booking.getResource().hourlyRate());
        System.out.println("Value of hh pricing outside hh hours: " + pricing.price(booking));
        //2hrs -> 200
    }

    private static void testHappyHoursOutsideForDesk() {
        PricingPolicy pricing = new HappyHoursPricing();

        Booking booking = createBookingForDesk(
                "2025-09-15T12:00",
                "2025-09-15T14:00"
        );

        System.out.println("Hourly rate: " + booking.getResource().hourlyRate());
        System.out.println("Value of hh pricing outside hh hours: " + pricing.price(booking));
    }

    private static void testHappyHoursOutsideForDevice() {
        PricingPolicy pricing = new HappyHoursPricing();

        Booking booking = createBookingForDevice(
                "2025-09-15T12:00",
                "2025-09-15T14:00"
        );

        System.out.println("Hourly rate: " + booking.getResource().hourlyRate());
        System.out.println("Value of hh pricing outside hh hours: " + pricing.price(booking));
    }
}
