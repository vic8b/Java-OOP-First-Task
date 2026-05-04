package ffWork.repo;

import ffWork.domain.booking.Booking;
import ffWork.domain.booking.BookingStatus;
import ffWork.domain.resource.Resource;
import ffWork.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    public void add(Booking booking);
    public Optional<Booking> findById(String id);
    public List<Booking> findAll();
    public List<Booking> findByResource(Resource resource);
    public List<Booking> findByUser(User user);
    public List<Booking> findByStatus(BookingStatus status);
}
