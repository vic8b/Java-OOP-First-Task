package ffWork.cli;

import ffWork.domain.booking.Booking;
import ffWork.domain.resource.Resource;
import ffWork.domain.user.User;
import ffWork.repo.BookingRepository;
import ffWork.repo.ResourceRepository;
import ffWork.repo.UserRepository;

import java.util.List;

public class ConsolePrinter {
    //void methods to print (e.g. from collection)
    private final StringBuilder stringBuilder = new StringBuilder();

    public void printLine(String text) {
        System.out.println(text);
    }

    public void printUsers(UserRepository userRepository) {
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            stringBuilder.append(user.toString());
            stringBuilder.append(System.lineSeparator());
        }

        System.out.println(stringBuilder);
        stringBuilder.setLength(0);
    }

    public void printBookings(BookingRepository bookingRepository) {
        List<Booking> allBookings = bookingRepository.findAll();

        for (Booking booking : allBookings) {
            stringBuilder.append(booking.toString())
                    .append(System.lineSeparator());
        }

        System.out.println(stringBuilder);
        stringBuilder.setLength(0);
    }

    public void printResources(ResourceRepository resourceRepository) {
        List<Resource> allResources = resourceRepository.findAll();

        for (Resource resource : allResources) {
            stringBuilder.append(resource)
                    .append(System.lineSeparator());
        }

        System.out.println(stringBuilder);
        stringBuilder.setLength(0);
    }

    public <E extends Enum<E> & CliOption> void printOptions(Class<E> enumType) {
        StringBuilder sb = new StringBuilder();

        for (E option : enumType.getEnumConstants()) {
                sb.append(option.getOptionNumber())
                        .append(" - ")
                        .append(option.name())
                        .append(" - ")
                        .append(option.getDescription())
                        .append(System.lineSeparator());


        }

        System.out.println(sb);
    }
}
