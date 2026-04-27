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
                + " (start: \"" + booking.getStart() + ", end: " + booking.getEnd() + ")\"";
        this.booking = booking;
    }

    public String getInvoiceInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Invoice number: ")
                .append(invoiceNumber)
                .append("\n")
                .append("Issue date: ")
                .append(issueDate)
                .append("\n")
                .append("Buyer: ")
                .append(buyer.getDisplayName())
                .append("\n")
                .append("Total: ")
                .append(total)
                .append("\n")
                .append("Item description: ")
                .append(itemDescription)
                .append("\n");
        return sb.toString();
    }
}
