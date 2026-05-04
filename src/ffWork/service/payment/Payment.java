package ffWork.service.payment;

import ffWork.money.Money;

public abstract class Payment {
    private final Money amount;
    private final String paymentId;
    protected PaymentStatus status;

    protected Payment(Money amount, String paymentId) {
        this.amount = amount;
        this.paymentId = paymentId;
        this.status = PaymentStatus.INITIATED;
    }

    public abstract void capture();

    public abstract void refund();

    public PaymentStatus getStatus() {
        return status;
    }
}
