package hu.bme.itsec.simnyi.backend.repository;

import hu.bme.itsec.simnyi.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Long> {

    Optional<User> findUserByUsernameAndPassword(String username, String password);

    boolean existsByUsernameAndPassword(String username, String password);

    Optional<User> findUserByUsername(String username);
}
