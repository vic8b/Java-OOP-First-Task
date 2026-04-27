package ffWork.domain.booking;

import ffWork.domain.resource.Resource;
import ffWork.domain.user.User;
import ffWork.money.Money;
import ffWork.service.payment.Payment;
import ffWork.time.FFDateTime;

public class Booking {
    private final String id;
    private final User user;
    private final Resource resource;
    private final FFDateTime start;
    private final FFDateTime end;
    private BookingStatus status;
    private Money calculatedPrice;
    private Payment payment;

    public Booking(String id, User user, Resource resource, FFDateTime start, FFDateTime end, Money calculatedPrice) {
        this.id = id;
        this.user = user;
        this.resource = resource;
        this.start = start;
        this.end = end;
        this.status = BookingStatus.PENDING;
        this.calculatedPrice = calculatedPrice;

        if (end.compareTo(start) <= 0) {
            throw new IllegalArgumentException("start must be prior to end");
        }
    }

    public void confirm() {
        if (status == BookingStatus.PENDING) {
            this.status = BookingStatus.CONFIRMED;
        } else {
            throw new IllegalStateException("Status " + status + " cannot be changed to: " + BookingStatus.CONFIRMED);
        }
    }

    public void cancel() {
        if (status == BookingStatus.PENDING || status == BookingStatus.CONFIRMED) {
            this.status = BookingStatus.CANCELLED;
        } else {
            throw new IllegalStateException("Status " + status + " cannot be changed to: " + BookingStatus.CANCELLED);
        }
    }

    public void complete() {
        if (status == BookingStatus.CONFIRMED) {
            this.status = BookingStatus.COMPLETED;
        } else {
            throw new IllegalStateException("Status " + status + " cannot be changed to " + BookingStatus.COMPLETED);
        }
    }

    public int durationMinutes() {
        return start.minutesUntil(end);
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Resource getResource() {
        return resource;
    }

    public FFDateTime getStart() {
        return start;
    }

    public FFDateTime getEnd() {
        return end;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setCalculatedPrice(Money calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public Money getCalculatedPrice() {
        return calculatedPrice;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
