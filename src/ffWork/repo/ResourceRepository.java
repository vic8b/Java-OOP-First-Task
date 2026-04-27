package ffWork.repo;

import ffWork.domain.resource.Resource;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository {
    public void add(Resource resource);
    public Optional<Resource> findByName(String name);
    public List<Resource> findAll();
    public List<Resource> findByType(Class<? extends Resource> type);
}
