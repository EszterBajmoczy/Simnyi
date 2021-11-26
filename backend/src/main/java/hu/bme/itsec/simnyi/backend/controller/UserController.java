package hu.bme.itsec.simnyi.backend.controller;

import hu.bme.itsec.simnyi.backend.model.Role;
import hu.bme.itsec.simnyi.backend.model.dto.PasswordDTO;
import hu.bme.itsec.simnyi.backend.model.dto.UserDTO;
import hu.bme.itsec.simnyi.backend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RestController
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/public/user/login")
    public ResponseEntity<Void> login(@Validated @RequestBody UserDTO dto){
        return ResponseEntity
                .ok()
                .headers(this.userService.login(dto))
                .build();
    }

    @PostMapping(path = "/public/user/register")
    public ResponseEntity<Void> register(@Validated @RequestBody UserDTO dto){
        return ResponseEntity
                .status(userService.register(dto, Role.USER))
                .build();
    }

    @SecurityRequirement(name = "Authorization")
    @PatchMapping(path = "/user/password-update")
    public ResponseEntity<Void> passwordUpdate(@Validated @RequestBody PasswordDTO dto){
        return ResponseEntity
                .ok()
                .header("Authorization", userService.passwordUpdate(dto.getPassword()))
                .build();

    }


    @SecurityRequirement(name = "Authorization")
    @DeleteMapping(path = "/user/delete")
    public ResponseEntity<Void> delete(){
        userService.delete();
        return ResponseEntity
                .ok()
                .build();
    }

    @SecurityRequirement(name = "Authorization")
    @DeleteMapping(path = "/admin/delete/{username}")
    public ResponseEntity<Void> delete(@Validated @NotBlank @PathVariable("username") String username){
        userService.delete(username);
        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping(path = "/admin/register")
    public ResponseEntity<Void> registerAdmin(@Validated @RequestBody UserDTO dto){
        return ResponseEntity
                .status(userService.register(dto, Role.ADMIN))
                .build();
    }

}
