package hu.bme.itsec.simnyi.backend.repository;

import hu.bme.itsec.simnyi.backend.model.Caff;
import hu.bme.itsec.simnyi.backend.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
