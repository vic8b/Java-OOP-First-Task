package ffWork.domain.resource;

import ffWork.money.Money;

import java.util.Set;

public class Room extends Resource {
    private static final Money ROOM_BASE_RATE = Money.of("100");
    private final int seats;
    private final Set<String> equipment;

    public Room(String name, Money customHourlyRate, int seats, Set<String> equipment) {
        super(name, customHourlyRate);
        this.seats = seats;
        this.equipment = equipment;
    }

    public Room(String name, int seats, Set<String> equipment) {
        this(name, null, seats, equipment);
    }

    public int getSeats() {
        return seats;
    }

    public Set<String> getEquipment() {
        return Set.copyOf(equipment);
    }

    @Override
    protected Money baseRatePerHour() {
        return Money.of("100");
    }

    @Override
    public String describe() {
        return "Coworking private room";
    }
}
