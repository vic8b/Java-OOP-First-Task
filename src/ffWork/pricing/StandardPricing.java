package ffWork.pricing;

import ffWork.domain.booking.Booking;
import ffWork.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StandardPricing implements PricingPolicy {
    private static final String HOUR_IN_MINUTES_STR = "60";

    @Override
    public Money price(Booking booking) {
        Money hourlyRate = booking.getResource().hourlyRate();
        int bookingDuration = booking.durationMinutes();

        BigDecimal price = hourlyRate
                .getAmount()
                .multiply(BigDecimal.valueOf(bookingDuration))
                .divide(new BigDecimal(HOUR_IN_MINUTES_STR), 2, RoundingMode.HALF_UP);

        return Money.of(price);
    }
}
