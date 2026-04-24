package ffWork.pricing;

import ffWork.domain.booking.Booking;
import ffWork.money.Money;

public interface PricingPolicy {
    Money price(Booking booking);
}
