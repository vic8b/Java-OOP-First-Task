package ffWork.cli;

import ffWork.domain.booking.Booking;
import ffWork.domain.booking.BookingStatus;
import ffWork.domain.resource.Resource;
import ffWork.domain.user.User;
import ffWork.repo.BookingRepository;
import ffWork.repo.ResourceRepository;
import ffWork.repo.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class ConsolePrinter {
    //void methods to print (e.g. from collection)

    public void printLine(String text) {
        System.out.println(text);
    }

    public void printUsers(UserRepository userRepository) {
        StringBuilder stringBuilder = new StringBuilder();
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            stringBuilder.append(user.toString());
            stringBuilder.append(System.lineSeparator());
        }

        System.out.println(stringBuilder);
        stringBuilder.setLength(0);
    }

    public void printBookings(BookingRepository bookingRepository) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Booking> allBookings = bookingRepository.findAll();

        for (Booking booking : allBookings) {
            stringBuilder.append(booking.toString())
                    .append(System.lineSeparator());
        }

        System.out.println(stringBuilder);
        stringBuilder.setLength(0);
    }

    public void printBookingsByEmail(BookingRepository bookingRepository, User user) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Booking> listOfBookingByUser = bookingRepository.findByUser(user);

        if (listOfBookingByUser.isEmpty()) {
            System.out.println("No bookings found");
        } else {
            for (Booking booking : listOfBookingByUser) {
                stringBuilder.append(booking)
                        .append(System.lineSeparator());
            }

            System.out.println(stringBuilder);
            stringBuilder.setLength(0);
        }
    }

    public void printBookingsByResource(ResourceRepository resourceRepository, BookingRepository bookingRepository, Class<? extends Resource> resourceType) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Resource> resources = resourceRepository.findByType(resourceType);
        List<Booking> bookingsByResource = new ArrayList<>();

        for (Resource resource : resources) {
            bookingsByResource.addAll(bookingRepository.findByResource(resource));
        }

        if (bookingsByResource.isEmpty()) {
            printLine("No bookings found");
        } else {
            printLine("Bookings for resource type: ");

            for (Booking booking : bookingsByResource) {
                stringBuilder.append(booking)
                        .append(System.lineSeparator());
            }

            System.out.println(stringBuilder);
            stringBuilder.setLength(0);
        }
    }

    public void printBookingsByStatus(BookingRepository bookingRepository, BookingStatus status) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Booking> bookingsByStatus = bookingRepository.findByStatus(status);

        if (bookingsByStatus.isEmpty()) {
            System.out.println("No bookings with status " + status + " exist");
        } else {
            for (Booking booking : bookingsByStatus) {
                stringBuilder.append(booking)
                        .append(System.lineSeparator());
            }

            System.out.println(stringBuilder);
            stringBuilder.setLength(0);
        }
    }

    public void printResources(ResourceRepository resourceRepository) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Resource> allResources = resourceRepository.findAll();

        for (Resource resource : allResources) {
            stringBuilder.append(resource)
                    .append(System.lineSeparator());
        }

        System.out.println(stringBuilder);
        stringBuilder.setLength(0);
    }

    public <E extends Enum<E> & CliOption> void printOptions(Class<E> enumType) {
        StringBuilder stringBuilder = new StringBuilder();

        for (E option : enumType.getEnumConstants()) {
                stringBuilder.append(option.getOptionNumber())
                        .append(" - ")
                        .append(option.name())
                        .append(" - ")
                        .append(option.getDescription())
                        .append(System.lineSeparator());


        }

        System.out.println(stringBuilder);
    }
}
