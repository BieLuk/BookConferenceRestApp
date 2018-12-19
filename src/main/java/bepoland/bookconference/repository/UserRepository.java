package bepoland.bookconference.repository;

import bepoland.bookconference.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Boolean existsByLogin(String username);
    List<User> findAllByAvailable(Boolean available);

}
