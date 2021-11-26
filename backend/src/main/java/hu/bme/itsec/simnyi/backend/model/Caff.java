package hu.bme.itsec.simnyi.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "caff")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Caff {

    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    private List<Comment> comment = new ArrayList<>();

    @ContentId
    @Column(nullable = false)
    private String contentId;

    @ContentLength
    @Column(nullable = false)
    private long contentLength;

    @Column(nullable = false)
    private String mimeType;

}
