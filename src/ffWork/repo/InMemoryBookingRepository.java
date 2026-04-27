package ffWork.repo;

import ffWork.domain.booking.Booking;
import ffWork.domain.booking.BookingStatus;
import ffWork.domain.resource.Resource;
import ffWork.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryBookingRepository implements BookingRepository {
    private List<Booking> bookings = new ArrayList<>();

    @Override
    public void add(Booking booking) {
        bookings.add(booking);
    }

    @Override
    public Optional<Booking> findById(String id) {
        for (Booking booking : bookings) {
            if (booking.getId().equals(id)) {
                return Optional.of(booking);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Booking> findAll() {
        return List.copyOf(bookings);
    }

    @Override
    public List<Booking> findByResource(Resource resource) {
        List<Booking> foundBookings = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getResource().equals(resource)) {
                foundBookings.add(booking);
            }
        }

        return foundBookings;
    }

    @Override
    public List<Booking> findByUser(User user) {
        List<Booking> foundBookings = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getUser().equals(user)) {
                foundBookings.add(booking);
            }
        }

        return foundBookings;
    }

    @Override
    public List<Booking> findByStatus(BookingStatus status) {
        ArrayList<Booking> foundBoookings = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getStatus().equals(status)) {
                foundBoookings.add(booking);
            }
        }

        return foundBoookings;
    }
}
