package hu.bme.itsec.simnyi.backend.repository;

import hu.bme.itsec.simnyi.backend.model.Caff;
import org.springframework.content.commons.repository.ContentStore;
import org.springframework.stereotype.Component;

@Component
public interface FileContentStore extends ContentStore<Caff, String> {

}
