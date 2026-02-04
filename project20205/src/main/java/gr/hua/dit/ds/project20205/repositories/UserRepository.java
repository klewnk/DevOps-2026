package gr.hua.dit.ds.project20205.repositories;
import gr.hua.dit.ds.project20205.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}


