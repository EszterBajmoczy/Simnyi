package hu.bme.itsec.simnyi.backend.controller;

import hu.bme.itsec.simnyi.backend.model.PasswordDTO;
import hu.bme.itsec.simnyi.backend.model.User;
import hu.bme.itsec.simnyi.backend.model.UserDTO;
import hu.bme.itsec.simnyi.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "User")
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Void> login(@Validated @RequestBody UserDTO dto){
        return ResponseEntity
                .ok()
                .headers(this.userService.login(dto))
                .build();
    }

    @PostMapping
    public ResponseEntity<Void> register(@Validated @RequestBody UserDTO dto){
        return ResponseEntity
                .status(userService.register(dto))
                .build();
    }

    @PatchMapping
    public ResponseEntity<Void> passwordUpdate(@Validated @RequestBody PasswordDTO dto){
        return ResponseEntity
                .ok()
                .header("Authorization", userService.passwordUpdate(dto.getPassword()))
                .build();

    }

    @DeleteMapping
    public ResponseEntity<Void> delete(){
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/logout")
    public ResponseEntity<Void> logout(){
        // WTFa
        return ResponseEntity.ok().header("Authorization", "deleted").build();
    }

}
