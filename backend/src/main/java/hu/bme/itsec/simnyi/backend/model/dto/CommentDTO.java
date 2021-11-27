package hu.bme.itsec.simnyi.backend.model.dto;

import hu.bme.itsec.simnyi.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    @NotBlank
    private String id;

    @NotBlank
    private String nameOfUser;

    @NotNull
    private String caffId;

    @NotNull
    private String content;

}
