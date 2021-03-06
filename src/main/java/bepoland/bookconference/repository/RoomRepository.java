package bepoland.bookconference.repository;

import bepoland.bookconference.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByName(String name);
    Boolean existsByName(String name);

}
