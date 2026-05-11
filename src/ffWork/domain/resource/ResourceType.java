package ffWork.domain.resource;

public enum ResourceType {
    ROOM(Room.class),
    DESK(Desk.class),
    DEVICE(Device.class);

    private final Class<? extends Resource> resourceClass;

    ResourceType(Class<? extends Resource> aClass) {
        this.resourceClass = aClass;
    }

    public Class<? extends Resource> getResourceClass() {
        return resourceClass;
    }
}
