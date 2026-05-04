package ffWork.service;

import ffWork.domain.booking.Booking;
import ffWork.domain.booking.BookingStatus;
import ffWork.domain.resource.Desk;
import ffWork.domain.resource.Device;
import ffWork.domain.resource.Resource;
import ffWork.domain.resource.Room;
import ffWork.domain.user.User;
import ffWork.money.Money;
import ffWork.pricing.PricingPolicy;
import ffWork.repo.BookingRepository;
import ffWork.repo.ResourceRepository;
import ffWork.repo.UserRepository;
import ffWork.time.FFDateTime;

import java.util.List;
import java.util.Optional;

public class BookingService {
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final BookingRepository bookingRepository;
    private PricingPolicy pricing;
    private int counter = 0;

    public BookingService(UserRepository userRepository, ResourceRepository resourceRepository, BookingRepository bookingRepository, PricingPolicy pricing) {
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
        this.bookingRepository = bookingRepository;
        this.pricing = pricing;
    }

    public Booking book(User user, Resource resource, FFDateTime start, FFDateTime end) {
        String id = "BK-<" + start.getDate() + ">-<" + counter + ">";

        List<Booking> bookingListByResource = bookingRepository.findByResource(resource);
        int overlappingBookings = 0;

        if (!bookingListByResource.isEmpty()) {
            for (Booking booking : bookingListByResource) {
                if ((resource instanceof Room) || (resource instanceof Desk)) {
                    if (isPendingOrConfirmed(booking)) {
                        if (isOverlapping(start, end, booking)) {
                            throw new IllegalArgumentException("Resource not available (bookings cannot overlap)");
                        }
                    }
                }
                if (resource instanceof Device device) {
                    if (isNotCancelledAndCompleted(booking)) {
                        if (isOverlapping(start, end, booking)) {
                            overlappingBookings++;
                        }
                        if (overlappingBookings >= device.getQuantity()) {
                            throw new IllegalArgumentException("Device is not available (fully booked)");
                        }
                    }
                }
            }
        }

        Booking newBooking = new Booking(id, user, resource, start, end, null);
        counter++;

        Money bookingCalculatedPrice = pricing.price(newBooking);
        newBooking.setCalculatedPrice(bookingCalculatedPrice);

        bookingRepository.add(newBooking);
        return newBooking;
    }

    public Booking book(User user, Resource resource, FFDateTime start, int durationMinutes) {
        return book(user, resource, start, start.plusMinutes(durationMinutes));
    }

    public void confirm(String bookingId) {
        Optional<Booking> foundById = bookingRepository.findById(bookingId);

        if (foundById.isPresent()) {
            foundById.ifPresent(Booking::confirm);
        } else
            System.out.println("Booking doesn't exist");
    }

    public void cancel(String bookingId) {
        Optional<Booking> foundById = bookingRepository.findById(bookingId);

        if (foundById.isPresent()) {
            foundById.ifPresent(Booking::cancel);
        } else
            System.out.println("Booking doesn't exist");
    }

    public void complete(String bookingId) {
        Optional<Booking> foundById = bookingRepository.findById(bookingId);

        if (foundById.isPresent()) {
            foundById.ifPresent(Booking::complete);
        } else
            System.out.println("Booking doesn't exist");
    }

    public List<Booking> listByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public List<Booking> listByResource(Resource resource) {
        return bookingRepository.findByResource(resource);
    }

    public List<Booking> listByStatus(BookingStatus bookingStatus) {
        return bookingRepository.findByStatus(bookingStatus);
    }

    private static boolean isNotCancelledAndCompleted(Booking booking) {
        return booking.getStatus() != BookingStatus.CANCELLED && booking.getStatus() != BookingStatus.COMPLETED;
    }

    private static boolean isPendingOrConfirmed(Booking booking) {
        return booking.getStatus() == BookingStatus.PENDING || booking.getStatus() == BookingStatus.CONFIRMED;
    }

    private static boolean isOverlapping(FFDateTime start, FFDateTime end, Booking booking) {
        return start.toEpochMinutes() < (booking.getEnd().toEpochMinutes()) && booking.getStart().toEpochMinutes() < (end.toEpochMinutes());
    }

    public void setPricing(PricingPolicy pricing) {
        this.pricing = pricing;
    }
}
