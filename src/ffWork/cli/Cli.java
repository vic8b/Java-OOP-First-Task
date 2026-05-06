package ffWork.cli;

import ffWork.domain.booking.Booking;
import ffWork.domain.booking.BookingStatus;
import ffWork.domain.resource.Resource;
import ffWork.domain.resource.ResourceType;
import ffWork.domain.user.User;
import ffWork.pricing.PricingPolicy;
import ffWork.pricing.StandardPricing;
import ffWork.repo.*;
import ffWork.service.BookingService;
import ffWork.service.invoice.BillingService;
import ffWork.service.invoice.Invoice;
import ffWork.service.payment.Payment;
import ffWork.service.payment.PaymentService;
import ffWork.time.FFDateTime;

public class Cli {
    private final ConsolePrinter printer;
    private final DataReader reader;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final BookingRepository bookingRepository;
    private PricingPolicy pricing;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final BillingService billingService;

    public Cli() {
        this.printer = new ConsolePrinter();
        this.reader = new DataReader(printer);
        this.userRepository = new InMemoryUserRepository();
        this.resourceRepository = new InMemoryResourceRepository();
        this.bookingRepository = new InMemoryBookingRepository();
        this.pricing = new StandardPricing();
        this.bookingService = new BookingService(
                userRepository,
                resourceRepository,
                bookingRepository,
                pricing
        );
        this.paymentService = new PaymentService(bookingRepository);
        this.billingService = new BillingService();
    }

    public void controlLoop() {
        Option option;

        do {
            printer.printOptions(Option.class);
            option = Option.fromNumber(reader.getOptionIntFromUser());

            if (option == null) {
                printer.printLine("Invalid option");
                continue;
            }

            switch (option) {
                case ADD_USER -> userOptionsMenu();
                case RESOURCES -> resourcesOptionsMenu();
                case BOOKINGS -> bookingsOptionsMenu();
                case PRICING -> pricingOptionsMenu();
                case LIST_OF_USERS -> printListOfUsers();
                case LIST_OF_BOOKINGS -> bookingsListOptionsMenu();
                case LIST_OF_RESOURCES -> printListOfResources();
                case HELP -> printInfo();
                case QUIT -> printer.printLine("End of program");
            }
        } while (option != Option.QUIT);
    }

    private void userOptionsMenu() {
        Option.User option;

        do {
            printer.printOptions(Option.User.class);
            option = Option.User.fromNumber(reader.getOptionIntFromUser());

            if (option == null) {
                printer.printLine("Invalid option");
                continue;
            }

            switch (option) {
                case ADD_USER_INDIVIDUAL -> addIndividualUser();
                case ADD_USER_COMPANY -> addCompanyUser();
            }
        } while (option != Option.User.QUIT);
    }

    private void resourcesOptionsMenu() {
        Option.Resource option;

        do {
            printer.printOptions(Option.Resource.class);
            option = Option.Resource.fromNumber(reader.getOptionIntFromUser());

            if (option == null) {
                printer.printLine("Invalid option");
                continue;
            }

            switch (option) {
                case ADD_ROOM -> addRoom();
                case ADD_DESK -> addDesk();
                case ADD_DEVICE -> addDevice();
            }
        } while (option != Option.Resource.QUIT);
    }

    private void bookingsOptionsMenu() {
        Option.Booking option;

        do {
            printer.printOptions(Option.Booking.class);
            option = Option.Booking.fromNumber(reader.getIntFromUser());

            if (option == null) {
                printer.printLine("Invalid option");
                continue;
            }

            switch (option) {
                case BOOK -> createBooking();
                case CONFIRM -> confirmBooking();
                case CANCEL -> cancelBooking();
            }
        } while (option != Option.Booking.QUIT);
    }

    private void pricingOptionsMenu() {
        Option.Pricing option;

        do {
            printer.printOptions(Option.Pricing.class);
            option = Option.Pricing.fromNumber(reader.getIntFromUser());

            if (option == null) {
                printer.printLine("Invalid option");
                continue;
            }

            switch (option) {
                case SET_PRICING -> setPricing();
                case PAY -> payForTheBooking();
                case INVOICE -> invoiceBooking();
                case REFUND -> refundBooking();
            }
        } while (option != Option.Pricing.QUIT);
    }

    private void bookingsListOptionsMenu() {
        Option.BookingsList option;

        do {
            printer.printOptions(Option.BookingsList.class);
            option = Option.BookingsList.fromNumber(reader.getOptionIntFromUser());

            if (option == null) {
                printer.printLine("Invalid option");
                continue;
            }

            switch (option) {
                case LIST_OF_ALL_BOOKINGS -> printListOfBookings();
                case BOOKINGS_BY_EMAIL -> {
                    User user = reader.chooseUser(userRepository);
                    printListOfBookingsByEmail(user);
                }
                case BOOKINGS_BY_RESOURCE -> {
                    ResourceType resourceType = reader.chooseResourceType();
                    printListOfBookingsByResource(resourceType.getaClass());
                }
                case BOOKINGS_BY_STATUS -> {
                    BookingStatus bookingStatusFromUser = reader.getBookingStatusFromUser();
                    printListOfBookingsByStatus(bookingStatusFromUser);
                }
            }
        } while (option != Option.BookingsList.QUIT);
    }

    private void addIndividualUser() {
        User user = reader.createIndividualUser();
        userRepository.add(user);
        printer.printLine("Individual user created successfully");
    }

    private void addCompanyUser() {
        User user = reader.createCompanyUser();
        userRepository.add(user);
        printer.printLine("Company user created successfully");
    }

    private void addRoom() {
        Resource room = reader.createRoom();
        resourceRepository.add(room);
        printer.printLine("Room added successfully");
    }

    private void addDesk() {
        Resource desk = reader.createDesk();
        resourceRepository.add(desk);
        printer.printLine("Desk added successfully");
    }

    private void createBooking() {
        User user;
        Resource resource;

        user = reader.chooseUser(userRepository);
        resource = reader.chooseResource(resourceRepository);

        if (user == null || resource == null) {
            printer.printLine("Booking couldn't be created");
        } else {
            try {
                printer.printLine("Enter start date (format: \"yyyy-MM-dd'T'HH:mm\" e.g. \"2025-09-15T10:00\": ");
                FFDateTime start = FFDateTime.parse(reader.getStringFromUser());
                printer.printLine("Enter end date (format: \"yyyy-MM-dd'T'HH:mm\" e.g. \"2025-09-15T10:00\": ");
                FFDateTime end = FFDateTime.parse(reader.getStringFromUser());
                Booking booking = bookingService.book(user, resource, start, end);
                printer.printLine("Booking created: ");
                printer.printLine(booking.toString());
            } catch (IllegalArgumentException e) {
                printer.printLine(e.getMessage());
            }
        }
    }

    private void confirmBooking() {
        try {
            Booking booking = reader.chooseBooking(bookingRepository);

            if (booking == null) {
                return;
            }

            booking.confirm();

            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                printer.printLine("Booking confirmed successfully");
            }
        } catch (IllegalStateException e) {
            printer.printLine(e.getMessage());
        }
    }

    private void cancelBooking() {
        try {
            Booking booking = reader.chooseBooking(bookingRepository);

            if (booking == null) {
                return;
            }

            booking.cancel();

            if (booking.getStatus() == BookingStatus.CANCELLED) {
                printer.printLine("Booking cancelled successfully");
            }
        } catch (IllegalStateException e) {
            printer.printLine(e.getMessage());
        }
    }

    private void addDevice() {
        Resource device = reader.createDevice();
        resourceRepository.add(device);
        printer.printLine("Device added successfully");
    }

    private void setPricing() {
        this.pricing = reader.setPricingPolicy();
        bookingService.setPricing(this.pricing);

        printer.printLine("Pricing policy has been set to " + pricing.getClass().getSimpleName());
    }

    private void payForTheBooking() {
        Payment payment;

        Booking booking = reader.chooseBooking(bookingRepository);

        if (booking == null) {
            return;
        }

        printer.printLine("Enter last 4 digits (if payment by Wallet, leave blank):");
        String last4 = reader.getStringFromUser();

        try {
            if (last4.isEmpty()) {
                payment = paymentService.pay(booking.getId());
                printer.printLine("Payment captured");
                printer.printLine("method: " + payment.getClass().getSimpleName());
            } else {
                payment = paymentService.pay(booking.getId(), last4);
                printer.printLine("Payment captured");
                printer.printLine("method: " + payment.getClass().getSimpleName() + ", last 4 digits: " + last4);
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            printer.printLine(e.getMessage());
        }
    }

    private void invoiceBooking() {
        Booking booking = reader.chooseBooking(bookingRepository);

        if (booking == null) {
            return;
        }

        Invoice invoice = billingService.toInvoice(booking);
        System.out.println(invoice.getInvoiceInfo());
    }

    private void refundBooking() {
        Booking booking = reader.chooseBooking(bookingRepository);

        if (booking == null) {
            return;
        }

        printer.printLine("Enter last 4 digits (if payment by Wallet, leave blank):");
        String last4 = reader.getStringFromUser();

        try {
            if (last4.isEmpty()) {
                paymentService.refund(booking.getId());
                printer.printLine("Payment refunded");
            } else {
                paymentService.refund(booking.getId(), last4);
                printer.printLine("Payment refunded");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            printer.printLine(e.getMessage());
        }
    }

    private void printListOfUsers() {
        printer.printUsers(userRepository);
    }

    private void printListOfBookings() {
        printer.printBookings(bookingRepository);
    }

    private void printListOfBookingsByEmail(User user) {
        printer.printBookingsByEmail(bookingRepository, user);
    }

    private void printListOfBookingsByResource(Class<? extends Resource> resourceType) {
        printer.printBookingsByResource(resourceRepository, bookingRepository, resourceType);
    }

    private void printListOfBookingsByStatus(BookingStatus bookingStatus) {
        printer.printBookingsByStatus(bookingRepository, bookingStatus);
    }

    private void printListOfResources() {
        printer.printResources(resourceRepository);
    }

    private void printInfo() {
        printer.printLine("Current pricelist setting:");
        printer.printLine(pricing.getClass().getSimpleName());
    }
}