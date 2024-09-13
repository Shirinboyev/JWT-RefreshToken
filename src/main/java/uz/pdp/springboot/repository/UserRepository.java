package uz.pdp.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springboot.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findFirstByUsername(String username);
    @Query("SELECT au.id FROM User au WHERE au.username = :username")
    Long getIdWithUsername(@Param("username") String username);

}
