package ffWork.domain.resource;

import ffWork.money.Money;

public class Device extends Resource {
    private static final Money DEVICE_BASE_RATE = Money.of("60");
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
        return DEVICE_BASE_RATE;
    }

    @Override
    public String describe() {
        return "Basic device to work with";
    }
}
