package hu.bme.itsec.simnyi.backend.repository;

import hu.bme.itsec.simnyi.backend.model.Caff;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface CaffRepository extends MongoRepository<Caff, String> {

    List<Caff> findByNameContaining(@NotNull String name);

}
