package ffWork.domain.resource;

import ffWork.money.Money;

public class Desk extends Resource {
    private static final Money HOT_DESK_BASE_RATE = Money.of("50");
    private static final Money FIXED_DESK_BASE_RATE = Money.of("75");
    private final DeskType deskType;

    public Desk(String name, Money customHourlyRate, DeskType type) {
        super(name, customHourlyRate);
        this.deskType = type;
    }

    public Desk(String name, DeskType type) {
        this(name, null, type);
    }

    @Override
    protected Money baseRatePerHour() {
        return switch (this.deskType) {
            case HOT -> HOT_DESK_BASE_RATE;
            case FIXED -> FIXED_DESK_BASE_RATE;
        };
    }

    @Override
    public String describe() {
        return switch (this.deskType) {
            case HOT -> "HOT - desk is not permanently assigned";
            case FIXED -> "FIXED - dedicated, private desk";
        };
    }

    public enum DeskType {HOT, FIXED}
}
