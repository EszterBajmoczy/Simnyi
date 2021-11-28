package hu.bme.itsec.simnyi.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaffUpdateDTO {

    @NotNull
    private String id;

    @NotBlank
    private String name;

    @NotNull
    private List<CommentDTO> comment;

}
