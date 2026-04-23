package ffWork.domain.resource;

import ffWork.money.Money;

public class Device extends Resource {
    private final int quantity;

    public Device(String name, Money customHourlyRate, int quantity) {
        super(name, customHourlyRate);
        this.quantity = quantity;
    }

    public Device(String name, int quantity) {
        this(name, null, quantity);
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    protected Money baseRatePerHour() {
        return Money.of("60");
    }

    @Override
    public String describe() {
        return "Basic device to work with";
    }
}
