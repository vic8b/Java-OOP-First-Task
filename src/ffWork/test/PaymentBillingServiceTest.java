package ffWork.test;

import ffWork.domain.booking.Booking;
import ffWork.domain.resource.Resource;
import ffWork.domain.resource.Room;
import ffWork.domain.user.User;
import ffWork.money.Money;
import ffWork.repo.BookingRepository;
import ffWork.repo.InMemoryBookingRepository;
import ffWork.service.invoice.BillingService;
import ffWork.service.invoice.Invoice;
import ffWork.service.payment.PaymentService;
import ffWork.time.FFDateTime;

import java.util.Set;

class PaymentBillingServiceTest {
    public static void main(String[] args) {
        testPaymentServicePay();
        testBillingServiceToInvoice();
    }

    static Booking createBooking() {
        User user = new User("aaa@abc.com", "John");
        Resource room = new Room("Room A", 8, Set.of("projector"));

        return new Booking(
                "BK-20250915-0",
                user,
                room,
                FFDateTime.parse("2025-09-15T10:00"),
                FFDateTime.parse("2025-09-15T12:00"),
                Money.of("200")
        );
    }

    static void testPaymentServicePay() {
        BookingRepository bookingRepository = new InMemoryBookingRepository();
        PaymentService paymentService = new PaymentService(bookingRepository);
        Booking booking = createBooking();
        bookingRepository.add(booking);

        System.out.println("Booking confirm");
        booking.confirm();

        System.out.println("pay for booking");
        paymentService.pay("BK-20250915-0", "1234");
        System.out.println("ok");
        System.out.println("booking status: " + booking.getStatus());
        System.out.println("booking payment status: " + booking.getPayment().getStatus());

        try {
            paymentService.refund(booking.getId(), "1234");
        } catch (IllegalStateException e) {
            System.out.println("message > " + e.getMessage());
        }
        System.out.println();

        System.out.println("Booking cancel");
        booking.cancel();
        System.out.println("Booking refund");
        paymentService.refund("BK-20250915-0", "1234");
        System.out.println("booking status: " + booking.getStatus());
        System.out.println("booking payment status: " + booking.getPayment().getStatus());
        System.out.println();
    }

    static void testBillingServiceToInvoice() {
        Booking booking = createBooking();

        BillingService billingService = new BillingService();

        Invoice invoice = billingService.toInvoice(booking);
        System.out.println(invoice.getInvoiceInfo());
    }
}
