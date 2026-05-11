package ffWork.test;

import ffWork.domain.booking.Booking;
import ffWork.domain.booking.BookingStatus;
import ffWork.domain.resource.Desk;
import ffWork.domain.resource.Device;
import ffWork.domain.resource.Resource;
import ffWork.domain.resource.Room;
import ffWork.domain.user.User;
import ffWork.money.Money;
import ffWork.pricing.PricingPolicy;
import ffWork.pricing.StandardPricing;
import ffWork.repo.*;
import ffWork.service.BookingService;
import ffWork.time.FFDateTime;

import java.util.List;
import java.util.Optional;
import java.util.Set;

class BookingServiceTest {
    public static void main(String[] args) {
        exampleTestsForBooking();

//        deviceQuantityTest(); //should fail -> fails
//        roomBookingsCollisionTest(); //should fail -> fails
//        cancelledBookingDoesNotBlockResource();
//        endPriorToStart(); //should fail -> fails
    }

    private static void endPriorToStart() {
        FFDateTime end = FFDateTime.parse("2025-09-15T10:00");
        FFDateTime start = FFDateTime.parse("2025-09-15T12:30");

        UserRepository userRepo = createUserRepo();
        ResourceRepository resourceRepo = createResourceRepo();
        BookingRepository bookingRepoDeviceTest = createBookingRepo();
        PricingPolicy pricingPolicy = createPricingPolicy();

        System.out.println("Creation of booking service");
        BookingService bookingService = createBookingService(userRepo, resourceRepo, bookingRepoDeviceTest, pricingPolicy);

        User user1 = getUserFromRepo(userRepo, "abc@abc.com");

        Set<String> roomEquipment = Set.of(
                "Rubber",
                "Desks",
                "Array"
        );
        Room room = new Room("New room", 10, roomEquipment);

        System.out.println("Creation of first booking");
        bookingService.book(
                user1,
                room,
                start,
                end
        );

    }

    private static void cancelledBookingDoesNotBlockResource() {
        FFDateTime start1 = FFDateTime.parse("2025-09-15T10:00");
        FFDateTime end1 = FFDateTime.parse("2025-09-15T12:30");

        FFDateTime start2 = FFDateTime.parse("2025-09-15T10:20");
        FFDateTime end2 = FFDateTime.parse("2025-09-15T12:50");

        UserRepository userRepo = createUserRepo();
        ResourceRepository resourceRepo = createResourceRepo();
        BookingRepository bookingRepoDeviceTest = createBookingRepo();
        PricingPolicy pricingPolicy = createPricingPolicy();

        Set<String> roomEquipment = Set.of(
                "Projector",
                "Desk",
                "Monitor"
        );
        Room room = new Room("New room", 10, roomEquipment);

        System.out.println("Creation of booking service");
        BookingService bookingService = createBookingService(userRepo, resourceRepo, bookingRepoDeviceTest, pricingPolicy);

        User user1 = getUserFromRepo(userRepo, "abc@abc.com");
        User user2 = getUserFromRepo(userRepo, "aaa@abc.com");

        System.out.println("Creation of first booking");
        bookingService.book(
                user1,
                room,
                start1,
                end1
        );

        System.out.println("Cancellation of first booking (BK-<20250915>-<0>)");
        bookingService.cancel("BK-<20250915>-<0>");
        for (Booking booking : bookingService.listByStatus(BookingStatus.CANCELLED)) {
            System.out.println(booking.getId() + " status: " + booking.getStatus());
        }

        System.out.println("Creation of second booking");
        bookingService.book(
                user2,
                room,
                start2,
                end2
        );

//        System.out.println("Creation of third booking");
//        bookingService.book(
//                user1,
//                room,
//                start1,
//                end1
//        ); //Exception
    }

    private static void roomBookingsCollisionTest() {
        FFDateTime start1 = FFDateTime.parse("2025-09-15T10:00");
        FFDateTime end1 = FFDateTime.parse("2025-09-15T12:30");

        FFDateTime start2 = FFDateTime.parse("2025-09-15T10:20");
        FFDateTime end2 = FFDateTime.parse("2025-09-15T12:50");

        UserRepository userRepo = createUserRepo();
        ResourceRepository resourceRepo = createResourceRepo();
        BookingRepository bookingRepoDeviceTest = createBookingRepo();
        PricingPolicy pricingPolicy = createPricingPolicy();

        Set<String> roomEquipment = Set.of(
                "Rubber",
                "Desks",
                "Array"
        );
        Room room = new Room("New room", 10, roomEquipment);

        System.out.println("Creation of booking service");
        BookingService bookingService = createBookingService(userRepo, resourceRepo, bookingRepoDeviceTest, pricingPolicy);

        User user1 = getUserFromRepo(userRepo, "abc@abc.com");
        User user2 = getUserFromRepo(userRepo, "aaa@abc.com");

        System.out.println("Creation of first booking");
        bookingService.book(
                user1,
                room,
                start1,
                end1
        );

        System.out.println("Creation of second booking");
        bookingService.book(
                user2,
                room,
                start2,
                end2
        );
    }

    private static void deviceQuantityTest() {
        FFDateTime start = FFDateTime.parse("2025-09-15T10:00");
        FFDateTime end = FFDateTime.parse("2025-09-15T12:30");

        UserRepository userRepo = createUserRepo();
        ResourceRepository resourceRepo = createResourceRepo();
        BookingRepository bookingRepoDeviceTest = createBookingRepo();
        PricingPolicy pricingPolicy = createPricingPolicy();

        Device laptop = new Device("laptop", 2);


        System.out.println("Creation of booking service");
        BookingService bookingService = createBookingService(userRepo, resourceRepo, bookingRepoDeviceTest, pricingPolicy);

        User user1 = getUserFromRepo(userRepo, "abc@abc.com");
        User user2 = getUserFromRepo(userRepo, "aaa@abc.com");

        System.out.println("Creation of first booking");
        bookingService.book(
                user1,
                laptop,
                start,
                end
        );

        System.out.println("Creation of second booking");
        bookingService.book(
                user2,
                laptop,
                start,
                end
        );

        System.out.println("Creation of third booking");
        bookingService.book(
                user1,
                laptop,
                start,
                end
        ); // IllegalArgumentException - fully booked
    }

    static void exampleTestsForBooking() {
        FFDateTime start = FFDateTime.parse("2025-09-15T10:00");
        FFDateTime end = FFDateTime.parse("2025-09-15T12:30");

        UserRepository userRepo = createUserRepo();
        ResourceRepository resourceRepo = createResourceRepo();
        BookingRepository bookingRepo = createBookingRepo();
        PricingPolicy pricingPolicy = createPricingPolicy();

        User user1 = getUserFromRepo(userRepo, "aaa@abc.com");


        Resource laptop = getResourceFromRepo(resourceRepo, "laptop");

        System.out.println("Creation of booking service");
        BookingService bookingService = createBookingService(userRepo, resourceRepo, bookingRepo, pricingPolicy);

        System.out.println("Creation of booking1 (and adding to repo)");
        bookingService.book(user1, laptop, start, end);

        Booking latestBooking = null;

        List<Booking> byStatus = bookingRepo.findByStatus(BookingStatus.PENDING);
        for (Booking booking : byStatus) {
            latestBooking = booking;
        }

        String idOfLatestBooking = latestBooking.getId();
        System.out.println("ID of latest booking: " + idOfLatestBooking);

//        System.out.println("Booking complete");
//        bookingService.complete(idOfLatestBooking);
//        throws IllegalStateException: Status PENDING cannot be changed to COMPLETED

//        System.out.println("Booking cancel");
//        bookingService.cancel(idOfLatestBooking);
//        System.out.println("Booking status after cancelling: " + latestBooking.getStatus()); //CANCELLED

        System.out.println("Booking confirm");
        bookingService.confirm(idOfLatestBooking);
        System.out.println("Booking status after confirmation: " + latestBooking.getStatus());

        System.out.println("Booking complete");
        bookingService.complete(idOfLatestBooking);
        System.out.println("Booking status after completion: " + latestBooking.getStatus());

        Resource superDesk = new Desk("Desk 1", Desk.DeskType.HOT);
        Resource newRoom = new Room("Room1", Money.of("200"), 10, Set.of("projectors"));
        bookingService.book(user1, newRoom, start, end);
        bookingService.book(user1, superDesk, start, end);

        System.out.println("List of rooms:");
        List<Booking> bookingsRooms = bookingService.listByResource(newRoom);
        for (Booking booking : bookingsRooms) {
            System.out.println(booking.getId());
            System.out.println(booking.getResource().describe());
            System.out.println(booking.getCalculatedPrice());
        }
        System.out.println();

        System.out.println("List of desks");
        List<Booking> bookingsDesk = bookingService.listByResource(superDesk);
        for (Booking booking : bookingsDesk) {
            System.out.println(booking.getId());
            System.out.println(booking.getResource().describe());
            System.out.println(booking.getCalculatedPrice());
        }
    }

    private static Resource getResourceFromRepo(ResourceRepository resourceRepo, String name) {
        Optional<Resource> resourceOpt = resourceRepo.findByName(name);
        Resource resource = resourceOpt.get();

        return resource;
    }

    private static User getUserFromRepo(UserRepository userRepository, String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        User user = userOpt.get();

        return user;
    }

    private static PricingPolicy createPricingPolicy() {
        PricingPolicy pricingPolicy = new StandardPricing();

        return pricingPolicy;
    }

    private static BookingRepository createBookingRepo() {
        BookingRepository bookingRepository = new InMemoryBookingRepository();

        return bookingRepository;
    }

    private static ResourceRepository createResourceRepo() {
        Set<String> roomEquipment = Set.of(
                "Rubber",
                "Desks",
                "Array"
        );

        ResourceRepository resourceRepository = new InMemoryResourceRepository();
        resourceRepository.add(new Desk("SuperDesk", Desk.DeskType.HOT));
        resourceRepository.add(new Room("New room", 10, roomEquipment));
        resourceRepository.add(new Device("laptop", 3));

        return resourceRepository;
    }

    private static UserRepository createUserRepo() {
        UserRepository userRepository = new InMemoryUserRepository();
        userRepository.add(new User("aaa@abc.com", "John"));
        userRepository.add(new User("abc@abc.com", "Andrew"));
        userRepository.add(new User("bbb@abc.com", "Mark"));

        return userRepository;
    }

    private static BookingService createBookingService(UserRepository userRepo, ResourceRepository resourceRepo, BookingRepository bookingRepo, PricingPolicy pricingPolicy) {
        BookingService bookingService = new BookingService(userRepo, resourceRepo, bookingRepo, pricingPolicy);
        return bookingService;
    }
}
