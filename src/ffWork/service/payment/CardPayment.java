package ffWork.service.payment;

import ffWork.money.Money;

public class CardPayment extends Payment {
    private final String last4Digits; //of the used card

    public CardPayment(Money amount, String paymentId, String last4Digits) {
        super(amount, paymentId);
        this.last4Digits = last4Digits;
    }

    @Override
    public void capture() {
        if (status != PaymentStatus.INITIATED)
            throw new IllegalStateException("Status " + status + " cannot be captured");

        status = PaymentStatus.CAPTURED;
    }

    @Override
    public void refund() {
        if (status != PaymentStatus.CAPTURED) {
            throw new IllegalStateException("Status " + status + " cannot be refunded");
        }

        status = PaymentStatus.REFUNDED;
    }

    @Override
    public String toString() {
        return status.name();
    }
}
