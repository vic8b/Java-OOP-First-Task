package ffWork.test;

import ffWork.domain.resource.Desk;
import ffWork.domain.resource.Device;
import ffWork.domain.resource.Resource;
import ffWork.domain.resource.Room;
import ffWork.money.Money;

import java.util.Set;

class ResourceTest {
    public static void main(String[] args) {
        testRoom();
        testDesk();
        testDevice();
        testCustomHourlyRate();
        testPolymorphism();
        testRoomConstructorWithoutCustomRate();
        testDeskConstructorWithoutCustomRate();
    }

    static void testRoom() {
        Room room = new Room(
                "Room A",
                null,
                8,
                Set.of("projector", "whiteboard")
        );

        System.out.println("Room test:");
        System.out.println("Name: " + room.getName());
        System.out.println("Seats: " + room.getSeats());
        System.out.println("Equipment: " + room.getEquipment());
        System.out.println("Description: " + room.describe());
        System.out.println("Hourly rate: " + room.hourlyRate());
        System.out.println();
    }

    static void testDesk() {
        Desk desk = new Desk(
                "Desk 1",
                null,
                Desk.DeskType.HOT
        );

        System.out.println("Desk test:");
        System.out.println("Name: " + desk.getName());
        System.out.println("Description: " + desk.describe());
        System.out.println("Hourly rate: " + desk.hourlyRate());
        System.out.println();
    }

    static void testDevice() {
        Device device = new Device(
                "Monitor",
                null,
                5
        );

        System.out.println("Device test:");
        System.out.println("Name: " + device.getName());
        System.out.println("Quantity: " + device.getQuantity());
        System.out.println("Description: " + device.describe());
        System.out.println("Hourly rate: " + device.hourlyRate());
        System.out.println();
    }

    static void testCustomHourlyRate() {
        Room premiumRoom = new Room(
                "Premium Room",
                Money.of("250.00"),
                12,
                Set.of("projector", "tv", "conference phone")
        );

        System.out.println("Custom hourly rate test:");
        System.out.println("Name: " + premiumRoom.getName());
        System.out.println("Base description: " + premiumRoom.describe());
        System.out.println("Custom hourly rate: " + premiumRoom.hourlyRate());
        System.out.println();
    }

    static void testPolymorphism() {
        Resource[] resources = {
                new Room("Room B", null, 6, Set.of("whiteboard")),
                new Desk("Desk 2", null, Desk.DeskType.FIXED),
                new Device("Laptop", Money.of("80.00"), 3)
        };

        System.out.println("Polymorphism test:");
        for (Resource resource : resources) {
            System.out.println("Name: " + resource.getName());
            System.out.println("Description: " + resource.describe());
            System.out.println("Hourly rate: " + resource.hourlyRate());
            System.out.println("---");
        }
    }

    static void testRoomConstructorWithoutCustomRate() {
        Room room = new Room(
                "Room C",
                10,
                Set.of("projector")
        );

        System.out.println("Room (no custom rate) test:");
        System.out.println("Name: " + room.getName());
        System.out.println("Hourly rate (should be base 100 PLN): " + room.hourlyRate());
        System.out.println();
    }

    static void testDeskConstructorWithoutCustomRate() {
        Desk desk = new Desk(
                "Desk 3",
                Desk.DeskType.FIXED
        );

        System.out.println("Desk (no custom rate) test:");
        System.out.println("Name: " + desk.getName());
        System.out.println("Type: " + desk.describe());
        System.out.println("Hourly rate (should be 75 PLN): " + desk.hourlyRate());
        System.out.println();
    }
}
