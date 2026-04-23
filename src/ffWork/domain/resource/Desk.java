package ffWork.domain.resource;

import ffWork.money.Money;

public class Desk extends Resource{
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
            case HOT -> Money.of("50");
            case FIXED -> Money.of("75");
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
