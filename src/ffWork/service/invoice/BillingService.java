package ffWork.service.invoice;

import ffWork.domain.booking.Booking;
import ffWork.time.FFDateTime;

public class BillingService implements Billable {
    private static int counter = 0;

    @Override
    public Invoice toInvoice(Booking booking) {
        FFDateTime currentDay = FFDateTime.getCurrentDay();

        String invoiceNumber = "INV-<" + currentDay.getDate() + ">-<" + counter + ">";
        Invoice invoice = new Invoice(invoiceNumber, currentDay, booking.getUser(), booking.getCalculatedPrice(), booking);
        counter++;

        return invoice;
    }
}
