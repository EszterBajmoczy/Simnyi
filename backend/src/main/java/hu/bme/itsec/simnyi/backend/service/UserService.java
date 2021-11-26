package hu.bme.itsec.simnyi.backend.service;

import hu.bme.itsec.simnyi.backend.exception.CustomHttpException;
import hu.bme.itsec.simnyi.backend.model.Role;
import hu.bme.itsec.simnyi.backend.model.User;
import hu.bme.itsec.simnyi.backend.model.dto.UserDTO;
import hu.bme.itsec.simnyi.backend.repository.UserRepository;
import hu.bme.itsec.simnyi.backend.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger("Service");

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public HttpStatus register(UserDTO dto, Role role) {
        try {
            var user = new User();
            user.setUsername(dto.getUsername());
            user.setPassword(getEncodedPassword(dto.getPassword()));
            user.setGrantedAuthority(role);
            userRepository.save(user);
        } catch (Exception e) {
            logger.error("reserved username", e);
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.OK;
    }

    /**
     * @return JWT token
     */
    public HttpHeaders login(UserDTO dto) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), getDecodedPassword(dto.getPassword())));
        var result = (User) authenticate.getPrincipal();
        return jwtTokenUtil.createAuthorizationHeader(result);
    }

    public boolean checkUsernamePassword(@NotBlank String username, @NotBlank String password) {
        return userRepository.findUserByUsernameAndPassword(username, getEncodedPassword(password)).isPresent();
    }

    private String getEncodedPassword(@NotBlank String password){
        return passwordEncoder.encode(getDecodedPassword(password));
    }

    private String getDecodedPassword(@NotBlank String password){
        return new String(Base64.getDecoder().decode(password));
    }

    public String passwordUpdate(String password) {
        var encodedNewPassword = getEncodedPassword(password);
        var samePassword = userRepository.existsByUsernameAndPassword("currentUsername", encodedNewPassword);
        if(samePassword){
            throw new CustomHttpException(HttpStatus.CONFLICT, "Ez nem új jelszó");
        }
        //FIXME getCurrentUser();
        return "token";
    }

    public Optional<User> getCurrentUser(){
        return userRepository.findUserByUsername(
                jwtTokenUtil.getCurrentUsername()
        );
    }

    public void delete() {
        getCurrentUser().ifPresent(userRepository::delete);
    }

    public void delete(String username) {
        userRepository.findUserByUsername(username).ifPresent(userRepository::delete);
    }
}
