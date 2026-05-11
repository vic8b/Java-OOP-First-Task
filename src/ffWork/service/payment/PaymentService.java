package ffWork.service.payment;

import ffWork.domain.booking.Booking;
import ffWork.domain.booking.BookingStatus;
import ffWork.repo.BookingRepository;

public class PaymentService {
    private final BookingRepository bookingRepository;

    public PaymentService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Payment pay(String bookingId, String cardLast4) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking does not exist"));

        CardPayment cardPayment = new CardPayment(booking.getCalculatedPrice(), bookingId, cardLast4);
        cardPayment.capture();
        booking.setPayment(cardPayment);

        return cardPayment;
    }

    public Payment pay(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking does not exist"));

        WalletPayment walletPayment = new WalletPayment(booking.getCalculatedPrice(), bookingId);
        walletPayment.capture();
        booking.setPayment(walletPayment);

        return walletPayment;
    }

    public void refund(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking does not exist"));

        if (booking.getStatus() != BookingStatus.CANCELLED) {
            throw new IllegalStateException("Cancel booking first");
        }

        Payment payment = booking.getPayment();

        if (payment == null) {
            throw new IllegalStateException("Booking has no payment");
        }

        if (payment instanceof CardPayment cardPayment) {
            cardPayment.refund();
            return;
        }

        if (payment instanceof WalletPayment walletPayment) {
            walletPayment.refund();
            return;
        }

        throw new IllegalStateException("Unsupported payment type");
    }
}
