package ffWork.service.invoice;

import ffWork.domain.booking.Booking;

public interface Billable {
    Invoice toInvoice(Booking booking);
}