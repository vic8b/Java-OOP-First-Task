package ffWork.repo;

import ffWork.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public void add(User user);
    public Optional<User> findByEmail(String email);
    public List<User> findAll();
}
