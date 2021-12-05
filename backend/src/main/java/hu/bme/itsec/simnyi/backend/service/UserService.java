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
import java.util.regex.Pattern;

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
            var psw = checkPsw(dto.getPassword());

            if(!psw){
                throw new CustomHttpException(HttpStatus.BAD_REQUEST, "Not a secure enough password!");
            }

            var user = new User();
            user.setUsername(dto.getUsername());
            user.setPassword(getEncodedPassword(dto.getPassword()));
            user.setGrantedAuthority(role);
            if(userRepository.findUserByUsername(user.getUsername()).isEmpty()) {
                userRepository.save(user);
            } else {
                throw new CustomHttpException(HttpStatus.CONFLICT, "Username is already taken.");
            }
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new CustomHttpException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private boolean checkPsw(String psw){
        Pattern upperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        return psw.length() >= 10 &&
                upperCasePatten.matcher(psw).find() &&
                lowerCasePatten.matcher(psw).find() &&
                digitCasePatten.matcher(psw).find();

    }

    /**
     * @return JWT token
     */
    public HttpHeaders login(UserDTO dto) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), getDecodedPassword(dto.getPassword())));
            var result = (User) authenticate.getPrincipal();
            return jwtTokenUtil.createAuthorizationHeader(result);
        } catch (Exception e){
            throw new CustomHttpException(HttpStatus.CONFLICT, "Login failed: " + e.getMessage());
        }
    }

    private String getEncodedPassword(@NotBlank String password){
        return passwordEncoder.encode(getDecodedPassword(password));
    }

    private String getDecodedPassword(@NotBlank String password){
        return new String(Base64.getDecoder().decode(password));
    }

    public HttpStatus userDataUpdate(UserDTO dto) {
        var user = userRepository.findUserByUsername(jwtTokenUtil.getCurrentUsername()).get();

        if(dto.getUsername() != null && !dto.getUsername().equals(user.getUsername()) && userRepository.findUserByUsername(dto.getUsername()).isEmpty()){
            user.setUsername(dto.getUsername());
        }

        if(dto.getPassword() != null && !dto.getPassword().equals(user.getPassword())){
            user.setPassword(getEncodedPassword(dto.getPassword()));
        }
        userRepository.save(user);
        return HttpStatus.OK;
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
