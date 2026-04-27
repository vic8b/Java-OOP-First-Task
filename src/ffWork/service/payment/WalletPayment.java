package ffWork.service.payment;

import ffWork.money.Money;

public class WalletPayment extends Payment {
    public WalletPayment(Money amount, String paymentId) {
        super(amount, paymentId);
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
}
