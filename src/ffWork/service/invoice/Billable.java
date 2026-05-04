package ffWork.service.invoice;

import ffWork.domain.booking.Booking;

public interface Billable {
    public Invoice toInvoice(Booking booking);
}