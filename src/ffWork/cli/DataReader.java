package ffWork.cli;

import ffWork.domain.booking.Booking;
import ffWork.domain.booking.BookingStatus;
import ffWork.domain.resource.*;
import ffWork.domain.user.CompanyUser;
import ffWork.domain.user.IndividualUser;
import ffWork.domain.user.User;
import ffWork.pricing.HappyHoursPricing;
import ffWork.pricing.PricingPolicy;
import ffWork.pricing.StandardPricing;
import ffWork.repo.BookingRepository;
import ffWork.repo.ResourceRepository;
import ffWork.repo.UserRepository;

import java.util.*;

public class DataReader {
    private final Scanner sc = new Scanner(System.in);
    private final ConsolePrinter printer;

    public DataReader(ConsolePrinter printer) {
        this.printer = printer;
    }

    public int getOptionIntFromUser() {
        while (true) {
            try {
                printer.printLine("Enter option number: ");
                return sc.nextInt();
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.err.println("Enter NUMBER");
            } finally {
                sc.nextLine();
            }
        }
    }

    public int getIntFromUser() {
        while (true) {
            try {
                int userChoice = sc.nextInt();
                sc.nextLine();
                return userChoice;
            } catch (InputMismatchException e) {
                System.err.println("Enter NUMBER");
                sc.nextLine();
            }
        }
    }

    public String getStringFromUser() {
        return sc.nextLine();
    }

    public IndividualUser createIndividualUser() {
        printer.printLine("Enter user e-mail: ");
        String email = getStringFromUser();
        printer.printLine("Enter user display name: ");
        String displayName = getStringFromUser();

        return new IndividualUser(email, displayName);
    }

    public CompanyUser createCompanyUser() {
        printer.printLine("Enter user e-mail: ");
        String email = getStringFromUser();
        printer.printLine("Enter user display name: ");
        String displayName = getStringFromUser();
        printer.printLine("Enter company name: ");
        String companyName = getStringFromUser();
        printer.printLine("Enter tax ID: ");
        String taxId = getStringFromUser();

        return new CompanyUser(email, displayName, companyName, taxId);
    }

    public User chooseUser(UserRepository userRepository) {
        List<User> allUsers = userRepository.findAll();

        if (allUsers.isEmpty()) {
            printer.printLine("No users added. Please create users first.");
            return null;
        } else {
            printer.printLine("All added users (emails):");
            for (User user : allUsers) {
                printer.printLine(user.getEmail());
            }

            while (true) {
                printer.printLine("Enter User email");
                String stringFromUser = getStringFromUser();

                for (User user : allUsers) {
                    if (stringFromUser.equals(user.getEmail())) {
                        return user;
                    }
                }
                printer.printLine("User does not exist");
            }
        }
    }

    public Resource createRoom() {
        Set<String> equipment = new HashSet<>();

        printer.printLine("Enter room name: ");
        String name = getStringFromUser();
        printer.printLine("Enter number of seats: ");
        int seats = getIntFromUser();
        printer.printLine("Enter number of equipment:");
        int numberOfEquipment = getIntFromUser();

        for (int i = 0; i < numberOfEquipment; i++) {
            printer.printLine("Enter item " + (i + 1) + "/" + numberOfEquipment);
            equipment.add(getStringFromUser());
        }

        return new Room(name, seats, equipment);
    }

    public Resource createDesk() {
        printer.printLine("Enter desk name: ");
        String name = getStringFromUser();
        printer.printLine("Choose desk possibility:");
        for (Desk.DeskType type : Desk.DeskType.values()) {
            printer.printLine(String.valueOf(type));
        }
        try {
            Desk.DeskType type = Desk.DeskType.valueOf(getStringFromUser().toUpperCase());
            return new Desk(name, type);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid possibility input");
        }

        return null;
    }

    public Resource createDevice() {
        printer.printLine("Enter device name: ");
        String name = getStringFromUser();
        printer.printLine("Enter quantity of device: ");
        int quantity = getIntFromUser();

        return new Device(name, quantity);
    }

    public Resource chooseResource(ResourceRepository resourceRepository) {
        List<Resource> allResources = resourceRepository.findAll();

        if (allResources.isEmpty()) {
            printer.printLine("No resources added. Please create resources first.");
            return null;
        } else {
            printer.printLine("All resources:");
            for (Resource resource : allResources) {
                printer.printLine(resource.getName());
            }

            while (true) {
                printer.printLine("Enter Resource name");
                String stringFromUser = getStringFromUser();
                for (Resource resource : allResources) {
                    if (stringFromUser.equals(resource.getName())) {
                        return resource;
                    }
                }

                printer.printLine("Resource does not exist");
            }
        }
    }

    public ResourceType chooseResourceType() {
        printer.printLine("Available resources:");

        for (ResourceType type : ResourceType.values()) {
            printer.printLine(type.name());
        }

        while (true) {
            printer.printLine("Enter resource type name:");
            String stringFromUser = getStringFromUser().toUpperCase();

            try {
                return ResourceType.valueOf(stringFromUser);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid resource type name");
            }
        }
    }

    public Booking chooseBooking(BookingRepository bookingRepository) {
        List<Booking> allBookings = bookingRepository.findAll();

        if (allBookings.isEmpty()) {
            printer.printLine("No booking is created");
            return null;
        }

        printer.printLine("Bookings:");
        for (Booking booking : allBookings) {
            printer.printLine(booking.toString());
        }

        while (true) {
            printer.printLine("Choose booking ID: ");
            String id = getStringFromUser();

            Optional<Booking> bookingOpt = bookingRepository.findById(id);
            if (bookingOpt.isPresent()) {
                return bookingOpt.get();
            }
            System.err.println("Booking id is incorrect");
        }
    }

    public PricingPolicy setPricingPolicy() {
        int standardPricing = 1;
        int happyHoursPricing = 2;

        printer.printLine("Available pricing policies: ");
        printer.printLine(StandardPricing.class.getSimpleName() + " - " + standardPricing);
        printer.printLine(HappyHoursPricing.class.getSimpleName() + " - " + happyHoursPricing);

        while (true) {
            printer.printLine("Choose option:");
            int chosenOption = getIntFromUser();

            if (chosenOption == standardPricing) {
                return new StandardPricing();
            } else if (chosenOption == happyHoursPricing) {
                return new HappyHoursPricing();
            } else {
                printer.printLine("Incorrect choice");
            }
        }
    }

    public BookingStatus getBookingStatusFromUser() {
        printer.printLine("Choose booking status:");
        for (BookingStatus status : BookingStatus.values()) {
            printer.printLine(String.valueOf(status));
        }
        try {
            return BookingStatus.valueOf(getStringFromUser().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid status");
        }

        return null;
    }
}
