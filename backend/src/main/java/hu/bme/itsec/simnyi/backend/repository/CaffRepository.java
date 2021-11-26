package hu.bme.itsec.simnyi.backend.repository;

import hu.bme.itsec.simnyi.backend.model.Caff;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface CaffRepository extends MongoRepository<Caff, String> {

    Optional<Caff> findCaffByName(@NotNull String name);

}
