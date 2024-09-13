package uz.pdp.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springboot.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findFirstByUsernameAndPassword(String username, String password);

    Optional<User> findFirstByUsername(String username);
}
