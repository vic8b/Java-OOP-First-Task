package ffWork.pricing;

import ffWork.domain.booking.Booking;
import ffWork.money.Money;
import ffWork.time.FFDateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HappyHoursPricing implements PricingPolicy {
    private static final int HAPPY_HOURS_START_HOUR = 14;
    private static final int HAPPY_HOURS_END_HOUR = 16;
    private static final String HOUR_IN_MINUTES_STR = "60";
    private static final BigDecimal DISCOUNT_MULTIPLIER = new BigDecimal("0.7");

    @Override
    public Money price(Booking booking) {
        Money hourlyRate = booking.getResource().hourlyRate();

        int bookingDuration = booking.durationMinutes();
        int happyHoursDuration = happyHoursDuration(booking);
        int notDiscountedDuration = bookingDuration - happyHoursDuration;

        BigDecimal priceOfNotDiscountedDuration = getPriceOfNotDiscountedDuration(hourlyRate, notDiscountedDuration);
        BigDecimal priceOfHappyHoursDuration = getPriceOfHappyHoursDuration(hourlyRate, happyHoursDuration);

        BigDecimal price = priceOfNotDiscountedDuration.add(priceOfHappyHoursDuration);

        return Money.of(price);
    }

    private int happyHoursDuration(Booking booking) {
        FFDateTime start = booking.getStart();
        FFDateTime end = booking.getEnd();

        FFDateTime happyHoursStart = FFDateTime.of(
                start.getYear(),
                start.getMonth(),
                start.getDay(),
                HAPPY_HOURS_START_HOUR,
                0
        );

        FFDateTime happyHoursEnd = FFDateTime.of(
                start.getYear(),
                start.getMonth(),
                start.getDay(),
                HAPPY_HOURS_END_HOUR,
                0
        );

        if (end.compareTo(happyHoursStart) <= 0)
            return 0;

        if (start.compareTo(happyHoursEnd) >= 0)
            return 0;

        FFDateTime countedStart = start.compareTo(happyHoursStart) > 0 ? start : happyHoursStart;
        FFDateTime countedEnd = end.compareTo(happyHoursEnd) < 0 ? end : happyHoursEnd;
        return countedStart.minutesUntil(countedEnd);
    }

    private BigDecimal getPriceOfNotDiscountedDuration(Money hourlyRate, int notDiscountedDuration) {
        return hourlyRate
                .getAmount()
                .multiply(BigDecimal.valueOf(notDiscountedDuration))
                .divide(new BigDecimal(HOUR_IN_MINUTES_STR), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal getPriceOfHappyHoursDuration(Money hourlyRate, int happyHoursDuration) {
        return hourlyRate
                .getAmount()
                .multiply(DISCOUNT_MULTIPLIER)
                .multiply(BigDecimal.valueOf(happyHoursDuration))
                .divide(new BigDecimal(HOUR_IN_MINUTES_STR), 2, RoundingMode.HALF_UP);
    }
}
