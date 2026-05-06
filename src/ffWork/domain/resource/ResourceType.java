package ffWork.domain.resource;

public enum ResourceType {
    ROOM(Room.class),
    DESK(Desk.class),
    DEVICE(Device.class);

    private final Class<? extends Resource> aClass;

    ResourceType(Class<? extends Resource> aClass) {
        this.aClass = aClass;
    }

    public Class<? extends Resource> getaClass() {
        return aClass;
    }
}
