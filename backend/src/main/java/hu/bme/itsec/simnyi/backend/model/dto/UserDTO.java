package hu.bme.itsec.simnyi.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  @NotBlank
  @Pattern(regexp = "^\\S*$")
  private String username;

  @NotBlank
  private String password;

}
