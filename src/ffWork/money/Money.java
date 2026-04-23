package ffWork.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money implements Comparable<Money> {
    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        BigDecimal scaledAmount = amount.setScale(2, RoundingMode.HALF_UP);
        if (scaledAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }
        this.amount = scaledAmount;
    }

    public static Money of(String amountAsString) {
        return new Money(new BigDecimal(amountAsString));
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        BigDecimal subtractResult = this.amount.subtract(other.amount);
        if (subtractResult.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Not enough money");
        }
        return new Money(subtractResult);
    }

    public Money multiply(BigDecimal multiplier) {
        return new Money(amount.multiply(multiplier));
    }

    public Money multiply(double multiplier) {
        return new Money(amount.multiply(BigDecimal.valueOf(multiplier)));
    }

    @Override
    public int compareTo(Money other) {
        return this.amount.compareTo(other.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }

    @Override
    public String toString() {
        return amount + " PLN";
    }
}
