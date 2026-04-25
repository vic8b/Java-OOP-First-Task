package ffWork.service.invoice;

import ffWork.domain.booking.Booking;
import ffWork.domain.user.User;
import ffWork.money.Money;
import ffWork.time.FFDateTime;

public class Invoice {
    private final String invoiceNumber;
    private final FFDateTime issueDate;
    private final User buyer;
    private final Money total;
    private final String itemDescription;
    private final Booking booking;

    public Invoice(String invoiceNumber, FFDateTime issueDate, User buyer, Money total, Booking booking) {
        this.invoiceNumber = invoiceNumber;
        this.issueDate = issueDate;
        this.buyer = buyer;
        this.total = total;
        this.itemDescription = "Reservation for \"" + booking.getResource().describe()
                + " (start: \"" + booking.getStart() + ", end: " + booking.getEnd() + ")";
        this.booking = booking;
    }
}
