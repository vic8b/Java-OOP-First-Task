package ffWork.repo;

import ffWork.domain.resource.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryResourceRepository implements ResourceRepository {
    private List<Resource> resources = new ArrayList<>();

    @Override
    public void add(Resource resource) {
        resources.add(resource);
    }

    @Override
    public Optional<Resource> findByName(String name) {
        for (Resource resource : resources) {
            if (resource.getName().equals(name)) {
                return Optional.of(resource);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Resource> findAll() {
        return List.copyOf(resources);
    }

    @Override
    public List<Resource> findByType(Class<? extends Resource> type) {
        List<Resource> foundResources = new ArrayList<>();

        for (Resource resource : resources) {
            if (resource.getClass().equals(type)) {
                foundResources.add(resource);
            }
        }

        return foundResources;
    }
}
